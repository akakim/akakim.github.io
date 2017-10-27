
/*
 *  GetMusicList.java 1.0.0 2017/10/23
 *
 * Copyright (c) 2017 RyoRyeong KIM
 * All rights reserved
 *
 * this software is the confidential and proprietary information to Ryo Ryeong KIM
 * you shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with RyoRyeong KiM
 */
package com.tripath.muse.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.squareup.picasso.Picasso;
import com.tripath.muse.R;
import com.tripath.muse.network.model.MusicItemModel;
import com.tripath.muse.utils.NetworkUtil;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * 백그라운드에서 음악을 재생할 수 있는 서비스
 */

public class MusicService extends Service implements MediaPlayer.OnErrorListener,
        MediaPlayer.OnPreparedListener,
        MediaPlayer.OnCompletionListener,
        MediaPlayer.OnSeekCompleteListener{

    /** 음악을 재생시키는 모듈 */
    MediaPlayer mediaPlayer;

    /** mediaplayer의 상태가 변화할 때 사용하는 변수 구글 개발자 문서를 참고 */
    public static final String IDLE_STATUS          =   "idle";
    public static final String INITIALIZE_STATUS    =   "init";
    public static final String PREPARE_STATUS       =   "prepare";
    public static final String PREPARING_STATUS     =   "preparing";
    public static final String START_STATUS         =   "start";
    public static final String PAUSE_STATUS         =   "PAUSE";
    public static final String STOP_STATUS          =   "LAST_STOP";
    public static final String END_STATUS           =   "end";
    public static final String ERROR_STATUS         =   "error";

    /** activity에서 service로 올때, Message의 종류를 확인하는 상수 */
    public static final int STOP_AND_PLAY           = 1;
    public static final int PAUSE                   = 2;
    public static final int PLAY                    = 3;
    public static final int PLAY_AND_PAUSE          = 4;
    public static final int NEXTWIND                = 6;
    public static final int LAST_STOP               = 7;
    public static final int RESET                   = 8;
    public static final int UPDATE_NETWORK_WIND = 11;




    /** service to activity actions */
    public static final String PLAY_AND_PAUSE_ACTION = "com.tripath.muse.service.MusicService.playAndPauseIntent";
    public static final String NEXT_WIND_ACTION      = "com.tripath.muse.service.MusicService.nextWindIntent";
    public static final String NETWORK_ERROOR_ACTION = "com.tripath.muse.service.MusicService.netWorkErrorIntent";

    /** notification 관련 */
    public static final int REQUEST_CODE = 1000;                                                    /** pending intent 코드 값 */
    final int NOTIFICATION_ID= 4001;                                                                /** noti ID */
    private  NotificationManagerCompat notificationManagerCompat;                                   /** noti 매니저 */

    String currentStatus = IDLE_STATUS;                                                             /** 음악의 상태 값  */

    final String TAG = getClass().getSimpleName();



    boolean isInit = true;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new Messenger(new RemoteHandler()).getBinder();                                      /** 전역변수로 두고, handler를 이용하려한다면 무한 루프가 걸린다.*/
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mediaPlayer =new MediaPlayer();
        notificationManagerCompat = NotificationManagerCompat.from(this);

        mediaPlayer.setOnPreparedListener( this );
        mediaPlayer.setOnSeekCompleteListener( this );
        mediaPlayer.setOnCompletionListener( this );
        mediaPlayer.setOnErrorListener( this );
        mediaPlayer.setVolume(100,100);

        currentStatus = IDLE_STATUS;     // 초기화시 idle;

        // Cancel all notifications to handle the case where the Service was killed and
        // restarted by the system.
        notificationManagerCompat.cancelAll();


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {

//        Log.d(TAG,"onDestroy()");
        if( mediaPlayer != null){
            mediaPlayer.release();
            mediaPlayer = null;
        }
        super.onDestroy();
    }

    /**
     * mediaPlayer의 재생이 끝난 콜백
     * 끝나고 다음곡을 재생해야 하므로 BroadCastReceiver로 Activity에 상태가 변경됨을 알린다.
     * @param mp
     */
    @Override
    public void onCompletion(MediaPlayer mp) {

        mp.stop();
        currentStatus= STOP_STATUS;

        mp.reset();
        currentStatus= IDLE_STATUS;
//        Log.d(TAG,"onCompletion : sendIntent");
        if( NetworkUtil.isNetworkOn(this)){
            Intent nextWindIntent = new Intent(NEXT_WIND_ACTION);
            sendBroadcast(nextWindIntent);

        }else {
            Intent networkErrorAction = new Intent(NETWORK_ERROOR_ACTION);
            sendBroadcast(networkErrorAction);

        }
    }

    /**
     * MediaPlayer의 에러가 발생할 경우 오류내용을 볼 수 있도록 생성
     * @param mp
     * @param what
     * @param extra
     * @return
     */
    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        /** 로그 출력을 위한 builder*/
        StringBuilder builder = new StringBuilder();

        currentStatus = ERROR_STATUS;

        if( START_STATUS.equals(currentStatus)){
            mp.stop();
        }

        mp.reset();
        currentStatus = IDLE_STATUS;


        builder.append("오류 : " );

        switch (what){
            case MediaPlayer.MEDIA_ERROR_UNKNOWN:
                builder.append("알수없는 에러");
                break;
            case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
                builder.append("미디어 플레이어가 다운됬습니다. ");
                break;
            default:
                builder.append("default 알수없는 에러 ");
                break;
        }

        builder.append("\n세부내용 : " + extra + " ");

        switch(extra){
            case MediaPlayer.MEDIA_ERROR_IO:
                builder.append("파일을 찾지 못했습니다. 네트워크 상태를 확인해주세요");
                break;
            case MediaPlayer.MEDIA_ERROR_MALFORMED:
                builder.append("파일을 읽을 수 없습니다. ");
                break;

            case MediaPlayer.MEDIA_ERROR_UNSUPPORTED:
                builder.append("지원하지않는 파일 형식입니다.  ");
                break;

            case MediaPlayer.MEDIA_ERROR_TIMED_OUT:
                builder.append("응답이 없습니다.");
                break;
            case -2147483648:
                builder.append("시스템 로우 레벨 ");

                if( !NetworkUtil.isNetworkOn(this)){
                    Intent networkErrorAction = new Intent(NETWORK_ERROOR_ACTION);
                    sendBroadcast(networkErrorAction);
                }
                break;
            default:
                builder.append("알수없는 에러 ");

                break;
        }

        Log.e(TAG,"OnError " + builder.toString());
        return false;
    }

    /**
     * asynchStart로 실행하면, 준비가 완료되면 이쪽 콜백으로 오게된다.
     * @param mp
     */
    @Override
    public void onPrepared(MediaPlayer mp) {

        mp.start();
        currentStatus= START_STATUS;
    }

    /**
     * 재생이 완료된시점
     * @param mp
     */
    @Override
    public void onSeekComplete(MediaPlayer mp) {

    }


    /**
     * Activity에서 서비스로 오는 메세지를 다루는 Handler
     */
    private class RemoteHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {

            final MusicItemModel musicModel;
            switch (msg.what) {
                case STOP_AND_PLAY:

                    musicModel = (MusicItemModel) msg.obj;

                    StopAndStart(musicModel);

                    if(isInit){
                        Notification notification = createNotification(musicModel.getTitle(), musicModel.getArtist(),musicModel.getImgPath());
                        startForeground(NOTIFICATION_ID, notification);
                        isInit = false;
                    }else if (START_STATUS.equals(currentStatus)){
                        Notification notification = createNotification(musicModel.getTitle(), musicModel.getArtist(), musicModel.getImgPath());
                        notificationManagerCompat.notify(NOTIFICATION_ID,notification);
                    }
                    break;

                case PLAY_AND_PAUSE:
                    musicModel = (MusicItemModel) msg.obj;

                    boolean currentStatusIsPause = false;

                    if(START_STATUS.equals(currentStatus)){
                        currentStatusIsPause = false;
                    }else {
                        currentStatusIsPause = true;
                    }
                    Notification playAndPauseNotification =
                            createNotification(musicModel.getTitle(), musicModel.getArtist(),currentStatusIsPause,musicModel.getImgPath());

                    startOrPause();
                    notificationManagerCompat.notify(NOTIFICATION_ID,playAndPauseNotification);
                    break;

                case NEXTWIND:
                    musicModel = (MusicItemModel) msg.obj;

                    StopAndStart(musicModel);
                    Notification nextWindNotification =
                            createNotification(musicModel.getTitle(), musicModel.getArtist() ,musicModel.getImgPath());

                    notificationManagerCompat.notify(NOTIFICATION_ID,nextWindNotification);

                    break;

                case UPDATE_NETWORK_WIND:
                    musicModel = (MusicItemModel) msg.obj;

                    StopAndStart(musicModel);
                    break;

                case LAST_STOP:
                    currentStatus = IDLE_STATUS;
                    mediaPlayer.reset();
                    break;

                case RESET:
                    if(currentStatus.equals(START_STATUS)) {
                        mediaPlayer.stop();
                    }else if( currentStatus.equals(PAUSE_STATUS)) {
                        mediaPlayer.stop();
                    }
                    mediaPlayer.reset();
                    currentStatus= IDLE_STATUS;
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 현재 status를 확인하여 음악을 재생시키는 메소드
     * @param musicModel 상태가 변경되어야할 MusicItem
     * @return
     */
    public String StopAndStart(MusicItemModel musicModel ){

        try {
            String url = musicModel.getSite() +"?name=" + URLEncoder.encode(musicModel.getSource(), "UTF-8"); /** 한글이 들어가서 인코딩 */
//            Log.d(TAG,"current State : " + currentStatus);
//            Log.d(TAG,"url : " + url);
            if (mediaPlayer == null ) {
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setOnPreparedListener(this);
                mediaPlayer.setOnSeekCompleteListener(this);
                mediaPlayer.setOnCompletionListener(this);
                mediaPlayer.setOnErrorListener(this);
                currentStatus = IDLE_STATUS;

            }else if (ERROR_STATUS.equals(currentStatus)){
                mediaPlayer.reset();
                currentStatus = IDLE_STATUS;

            }
            else if (IDLE_STATUS.equals(currentStatus)) {
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaPlayer.setDataSource(url);
//                Log.d("setDataSource","mediaPlayer is start");
                mediaPlayer.prepareAsync();

            }else if (PAUSE_STATUS.equals(currentStatus)){
                mediaPlayer.stop();
                currentStatus = STOP_STATUS;
                mediaPlayer.reset();
                currentStatus = IDLE_STATUS;
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaPlayer.setDataSource( url) ;
                mediaPlayer.prepareAsync();
                currentStatus = START_STATUS;

            } else if (mediaPlayer.isPlaying()) {
//                Log.d("setDataSource","mediaPlayer is Playing");

                mediaPlayer.stop();                                                                 /** 얼핏 생각하면 reset만한다고 되지않는다. stop 후 reset 한다. */
                currentStatus = STOP_STATUS;                                                        /** stop을 하지않으면 다음 곡을 재생할 수가 없다. */
                mediaPlayer.reset();
                currentStatus = IDLE_STATUS;
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaPlayer.setDataSource( url) ;
                mediaPlayer.prepareAsync();
                currentStatus = START_STATUS;

            } else if(START_STATUS.equals(currentStatus)){
                mediaPlayer.stop();                                                                 /** 얼핏 생각하면 reset만한다고 되지않는다. stop 후 reset 한다. */
                currentStatus = STOP_STATUS;                                                        /** stop을 하지않으면 다음 곡을 재생할 수가 없다. */
                mediaPlayer.reset();
                currentStatus = IDLE_STATUS;
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaPlayer.setDataSource( url) ;
                mediaPlayer.prepareAsync();
                currentStatus = START_STATUS;

            } else if(STOP_STATUS.equals(currentStatus)){
                mediaPlayer.reset();
                currentStatus = IDLE_STATUS;
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaPlayer.setDataSource( url) ;
                mediaPlayer.prepareAsync();
                currentStatus = START_STATUS;

            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "UnsupportedEncodingException" + e.getMessage();

        } catch (IOException e){
            e.printStackTrace();
            return "IOException" + e.getMessage();

        } catch (IllegalStateException e){
            return "IllegalStateException "+ e.getMessage();

        } catch ( Exception e){
            e.printStackTrace();
            return "Exception" +  e.getMessage();

        }

        return "success";
    }

    public void startOrPause(){

        if(mediaPlayer!= null){
            switch ( currentStatus ){
                case START_STATUS:
                    mediaPlayer.pause();
                    currentStatus = PAUSE_STATUS;
                    break;

                case PAUSE_STATUS:
                    mediaPlayer.start();
                    currentStatus = START_STATUS;
                    break;
                default:
                    break;
            }

        }
    }

    /**
     *
     * @param title 타이틀
     * @param subTitle 부재목
     * @param url Album URL
     * @return
     */
    public Notification createNotification(final String title,final String subTitle,String url){
        return  createNotification(title,subTitle,true , url );
    }

    /**
     * 뮤직 컨트롤러를 생성하는 메소드
     * 재생 일시정지 다음 재생 시에도 늘 새로생성하여 Notification을 갱신한다.
     *
     * serivce에서 상태가 변경되면, activity에 상태가 변경됨을 알려야되기 때문에
     * PendingIntent 구현시 broadcastreceiver를 사용한다.
     * @param title 타이틀
     * @param subTitle 부재목
     * @param isNextStatusPause 다음 상태가 pause를 보여줘야되는지 아닌지 여부
     * @param url 갱신할 앨범 사진 URL
     * @return
     */
    public Notification createNotification(final String title,final String subTitle ,boolean isNextStatusPause,String url){
//        Log.d(TAG,"createNotification : is next status PAUSE? " + isNextStatusPause);
        NotificationCompat.Builder notiBuilder      = new NotificationCompat.Builder(this);
        RemoteViews smallViews                      = new RemoteViews(getPackageName(),R.layout.small_music_controller);
        PendingIntent playAndPausePendingIntent     = null;
        Intent playAndPauseIntent                   = null;
        Intent nextWindIntent                       = new Intent (NEXT_WIND_ACTION);

        if( isNextStatusPause ){

            smallViews.setViewVisibility( R.id.layoutPlay   , View.INVISIBLE);
            smallViews.setViewVisibility( R.id.layoutPause  , View.VISIBLE);

            playAndPauseIntent          = new Intent( PLAY_AND_PAUSE_ACTION );
            playAndPauseIntent.setPackage( this.getPackageName() );

            playAndPausePendingIntent   =
                    PendingIntent.getBroadcast( this , REQUEST_CODE, playAndPauseIntent ,PendingIntent.FLAG_CANCEL_CURRENT) ;

            smallViews.setOnClickPendingIntent( R.id.pause,playAndPausePendingIntent );
            smallViews.setOnClickPendingIntent( R.id.layoutPlay,playAndPausePendingIntent );


        }else {

            smallViews.setViewVisibility( R.id.layoutPlay   , View.VISIBLE);
            smallViews.setViewVisibility( R.id.layoutPause  , View.INVISIBLE);

            playAndPauseIntent          = new Intent( PLAY_AND_PAUSE_ACTION );
            playAndPauseIntent.setPackage( this.getPackageName() );

            playAndPausePendingIntent   =
                    PendingIntent.getBroadcast( this , REQUEST_CODE, playAndPauseIntent ,PendingIntent.FLAG_CANCEL_CURRENT) ;

            smallViews.setOnClickPendingIntent( R.id.play,playAndPausePendingIntent );
            smallViews.setOnClickPendingIntent( R.id.layoutPause,playAndPausePendingIntent );

        }

        nextWindIntent.setPackage( this.getPackageName() );
        PendingIntent pendingNextWindIntent =
                PendingIntent.getBroadcast( this , REQUEST_CODE, nextWindIntent ,PendingIntent.FLAG_CANCEL_CURRENT) ;

        smallViews.setOnClickPendingIntent( R.id.layoutFastWind,pendingNextWindIntent );
        smallViews.setOnClickPendingIntent( R.id.fastWind,pendingNextWindIntent );

        smallViews.setTextViewText( R.id.textTitle, title);
        smallViews.setTextViewText( R.id.textSubTitle, subTitle);

        notiBuilder.setSmallIcon(R.drawable.ic_notification)
                   .setVisibility(android.support.v7.app.NotificationCompat.VISIBILITY_PUBLIC)
                   .setUsesChronometer(true)
                   .setCustomContentView(smallViews);

        Notification notification = notiBuilder.build();
        Picasso.with(this).load(url).error(R.drawable.no_image).into( smallViews, R.id.albumArt, NOTIFICATION_ID,notification);

        return notification;

    }
}
