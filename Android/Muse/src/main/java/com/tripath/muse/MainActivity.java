
/*
 *  MainActivity.java 1.0.0 2017/10/23
 *
 * Copyright (c) 2017 RyoRyeong KIM
 * All rights reserved
 *
 * this software is the confidential and proprietary information to Ryo Ryeong KIM
 * you shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with RyoRyeong KiM
 */

package com.tripath.muse;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Spinner;
import android.widget.Toast;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;
import com.tripath.muse.activity.BaseActivity;
import com.tripath.muse.common.Constants;
import com.tripath.muse.network.model.MusicItemModel;
import com.tripath.muse.network.model.MusicModel;
import com.tripath.muse.network.proto.GetMusicList;
import com.tripath.muse.service.MusicService;

import com.tripath.muse.fragments.MusicListFragment;
import com.tripath.muse.fragments.PlaybackControlsFragment;
import com.tripath.muse.utils.NetworkUtil;
import com.tripath.muse.widget.DotsProgressBarDialog;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 첫 진입 화면 음악 항목들과 컨트롤러가 있다.
 */
public class MainActivity extends BaseActivity implements
        MusicListFragment.OnFragmentInteractionListener,
        PlaybackControlsFragment.OnFragmentInteractionListener,
        View.OnClickListener,
        DialogInterface.OnClickListener {

    final String TAG = getClass().getSimpleName();
    final String initGenreCd = "P";
    final int initItemCount = 10;
    final int initStartIndex = 0;


    /** 외부 네트워크 모듈 */
    WebView                     adWebView;                                                          /** 광고 영역  */

    /** 앱 내부 통신 */
    BroadcastReceiver           broadcastReceiver;                                                  /** global broadcastReciever */
    ActivityRemoteHandler       activityRemoteHandler;                                              /** activity-> service 를 연결하는 핸들러 */
    Messenger                   remoteMessenger;                                                    /** activity-> service 를 연결하는 메신저 */
    ServiceConnection           serviceConnection;                                                  /** activity-> service를 연결하는 객체 */
    IntentFilter                intentFilter;                                                       /** global broadcastReciever로 부터 Intent를 거르는 객체 */

    /** 데이터 클래스 */
    GetMusicList                getMusicList;
    MusicModel                  musicModel;

    /** UI 관련 */
    DotsProgressBarDialog       dotsProgressBarDialog;                                              /** 공통 다이얼로그 */
    MusicListFragment           musicListFragment;                                                  /** 음악 목록*/
    PlaybackControlsFragment    playbackControlsFragment;                                           /** 음악 컨트롤러*/
    List<MusicItemModel>        musicModels = new ArrayList<>();                                    /** 서버로 부터 받은 데이터, 서비스로 값을 전달한다. */

    int currentMusicPlayIndex = -1;                                                                 /** 현재 재생중인 index; */
    int startIndex = initStartIndex;                                                                /** 서버에서 데이터를 불러올때, 시작하는 index. */
    boolean isFinish = false;                                                                       /**  back Button 2번 눌린 유 무 */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activityRemoteHandler = new ActivityRemoteHandler();

        findViewById(R.id.refreshBtn).setOnClickListener( this );

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                Message msg =Message.obtain();
//                Log.d(TAG,"broacastReceiver action " + action + " current Index  =" + currentMusicPlayIndex);
                switch (action){
                    case MusicService.NEXT_WIND_ACTION:

                        try {

                            if( currentMusicPlayIndex >= -1 || currentMusicPlayIndex < musicModels.size()){

                                msg.what=MusicService.NEXTWIND;
                                currentMusicPlayIndex++;

                                if( currentMusicPlayIndex > musicModels.size()) {                   /** index out of range 방지 */
                                    currentMusicPlayIndex = musicModels.size();

                                }else if ( currentMusicPlayIndex == musicModels.size() ){

                                    if("Y".equals(musicModel.getNextYn())){
                                        requestGetMusicList(false,true,Constants.SERVER_URL + Constants.GET_MUSIC_LIST, initGenreCd, initItemCount, startIndex,true);

                                    }else if ("N".equals(musicModel.getNextYn())){
                                        Toast.makeText(MainActivity.this,Constants.NO_MORE_LIST_ITEM_MESSAGE,Toast.LENGTH_SHORT).show();

                                    }

                                } else if (currentMusicPlayIndex >= -1 || currentMusicPlayIndex < musicModels.size()){
                                    msg.obj = musicModels.get(currentMusicPlayIndex);
                                    musicListFragment.invalidateNextItem(currentMusicPlayIndex);
                                    playbackControlsFragment.setStatus(true);
                                    playbackControlsFragment.setItemInfo(
                                            musicModels.get(currentMusicPlayIndex).getTitle(),
                                            musicModels.get(currentMusicPlayIndex).getArtist());

                                    playbackControlsFragment.setAlbumArt( musicModels.get(currentMusicPlayIndex).getImgPath());
                                    remoteMessage(msg);

                                } else {
                                    Log.e(TAG,"error..");
                                }
                            }
                        }catch (RemoteException e) {
                            e.printStackTrace();
                        }
                        break;

                    case MusicService.NETWORK_ERROOR_ACTION:
                            showAlertNetWorkError();
                        break;

                    case MusicService.PLAY_AND_PAUSE_ACTION:
                        if(currentMusicPlayIndex >= musicModels.size()  ){
                            currentMusicPlayIndex = musicModels.size() -1;

                            Message messagePlay = Message.obtain();
                            messagePlay.what = MusicService.PLAY_AND_PAUSE;
                            messagePlay.obj = musicModels.get(currentMusicPlayIndex);

                            playbackControlsFragment.setStatus(!playbackControlsFragment.getStatus());


                            try {
                                remoteMessage(messagePlay);
                            } catch (RemoteException e) {
                                Log.e(TAG, "PlayAndPauseNotification RemoteException");
                            }
                        }else if (currentMusicPlayIndex >= -1 || currentMusicPlayIndex < musicModels.size()) {

                            Message messagePlay = Message.obtain();
                            messagePlay.what = MusicService.PLAY_AND_PAUSE;
                            messagePlay.obj = musicModels.get(currentMusicPlayIndex);
                            playbackControlsFragment.setStatus(!playbackControlsFragment.getStatus());


                            try {
                                remoteMessage(messagePlay);
                            } catch (RemoteException e) {
                                Log.e(TAG, "PlayAndPauseNotification RemoteException");
                            }
                        }
                        break;
                    default:
                        break;
                }
            }
        };



        dotsProgressBarDialog       = new DotsProgressBarDialog(this);
        musicListFragment           = (MusicListFragment)getSupportFragmentManager().findFragmentByTag( "MusicListFragment" );
        playbackControlsFragment    = (PlaybackControlsFragment) getSupportFragmentManager().findFragmentByTag( "PlaybackControlsFragment" );

        intentFilter                = new IntentFilter();
        intentFilter.addAction(MusicService.NEXT_WIND_ACTION );
        intentFilter.addAction(MusicService.NETWORK_ERROOR_ACTION );
        intentFilter.addAction(MusicService.PLAY_AND_PAUSE_ACTION );

        adWebView                   = (WebView)findViewById( R.id.adWebView);

        initWebView(adWebView);

        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                remoteMessenger     = new Messenger(service);

                if (remoteMessenger != null) {
                    Message msg     = new Message();
                    msg.what        = 0;
                    msg.obj         = new Messenger(activityRemoteHandler);
                    try {
                        remoteMessenger.send(msg);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                remoteMessenger = null;
            }
        };

        Intent serviceIntent = new Intent(this, MusicService.class);
        bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE);

        requestInitGetMusicList();

    }

    @Override
    protected void onResume() {
        super.onResume();

        registerReceiver( broadcastReceiver , intentFilter );

    }

    @Override
    protected void onPause() {
        super.onPause();

        if (dotsProgressBarDialog.isShowing()){
            dotsProgressBarDialog.dismiss();
        }
    }

    public void initWebView ( WebView webView){

        WebSettings settings = webView.getSettings();
        WebViewClient webViewClient = new WebViewClient();
        WebChromeClient webChromeClient = new WebChromeClient();

        settings.setLoadWithOverviewMode( true );
        settings.setUseWideViewPort( true );

        webView.setWebViewClient( webViewClient );
        webView.setWebChromeClient( webChromeClient );

    }

    public void showDialog() {
        dotsProgressBarDialog.show();
    }


    public void hideDialog() {

        if (dotsProgressBarDialog.isShowing()){
            dotsProgressBarDialog.dismiss();
        }
    }

    /**
     * 음악리스트를 초기화한다.
     * @param itemList 서버로 부터 받아온 리스트
     */
    public void invalidateUI(List itemList){
        this.invalidateUI(itemList,false);
    }

    /**
     * @param itemList 서버로 부터 받아온 리스트
     * @param isAddable 리스트를 더 하는건지 초기화할 지 설정
     */
    public void invalidateUI(List itemList,boolean isAddable) {

        if(isAddable){
            musicModels.addAll( itemList );
            musicListFragment.addListItem( itemList );

        }else {
            musicModels.clear();
            musicModels.addAll( itemList );
            musicListFragment.setListItem(itemList);

        }
    }

    public void onFragmentErrorOccured(String msg) {
        showAlertDialog("에러", msg);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        Log.d(TAG,"onDestroy()");
        unregisterReceiver(broadcastReceiver);

        // service 연결 해제 activity와 lifecycle을 맞춘다.
        if( serviceConnection != null)
           unbindService(serviceConnection);

        if(activityRemoteHandler != null)
            activityRemoteHandler = null;
    }

    @Override
    public void onClick(View v) {

        Message msg = Message.obtain();
        switch (v.getId()){

            case R.id.refreshBtn:
                //초기화
                msg.what = MusicService.RESET;

                requestInitGetMusicList();

                playbackControlsFragment.setStatus( false ) ;
                playbackControlsFragment.setItemInfo( "", "");
                playbackControlsFragment.setAlbumArt( getResources().getDrawable(R.drawable.no_image,null));


                if(remoteMessenger !=null){
                    try {
                        remoteMessenger.send(msg);
                    }catch (RemoteException e) {
                        e.printStackTrace();
                    }

                }else {
                    Log.e(TAG,"remote object is null");

                }
                musicListFragment.initializeSelection();

                break;
            default:
                break;
        }
    }


    public void showAlertDialog(String title,String msg){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(msg);

        AlertDialog dialog = builder.create();
        dialog.show();

    }


    public void showAlertNetWorkError(){

        showAlertDialogOnClick("네트워크","네트워크 연결을 확인해주세요",MainActivity.this);
    }

    public void showAlertDialogOnClick(String title, String msg, DialogInterface.OnClickListener listener){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setPositiveButton("확인",listener);

        AlertDialog dialog = builder.create();
        dialog.show();

    }


    public void showToast(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }

    /***
     * 리스트 초기화
     */
    public void requestInitGetMusicList(){

        requestGetMusicList(true,true,Constants.SERVER_URL + Constants.GET_MUSIC_LIST,initGenreCd,initItemCount,initStartIndex,false);
    }

    /**
     *  swipe이벤트로 리스트를 추가하는 경우 ... 다이얼로그를 보여주지 않는다.
     * @param url 요청 목록
     */
    public void requestGetMusicListSwipeEvent(final String url, final String genreCd, final int countPerPage, final int pageNum){

        requestGetMusicList(false,false,url,genreCd,countPerPage,pageNum,false);
    }


    /**
     * 음악 항목을 서버로 요청한다.
     * @param url           요청 URL
     * @param genreCd       장르 코드
     * @param countPerPage  페이스 숫자 .
     * @param pageNum
     */
    public void requestGetMusicList(final boolean isInit,final boolean showLoadingOption, final String url, final String genreCd, final int countPerPage, final int pageNum,final boolean nextWindTriggeringMusic){

        getMusicList = new GetMusicList();
        musicModel = new MusicModel();
//        Log.d(TAG,"getURL : " + url );
        new AsyncTask<Void,Void,String>(){

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                if(showLoadingOption)
                   showDialog();
            }

            @Override
            protected String doInBackground(Void... params) {

                try {
                    Response response= getMusicList.requestData(MainActivity.this,url,genreCd,countPerPage,pageNum);

//                    Log.d(TAG,"response Code " + response.code() );
                    if( response.code() == 200 ) {

                        ResponseBody responseBody = response.body();
//                        Log.d(TAG,"responseBody " + response.body() );
                        musicModel.setParameter(responseBody.string());

                    }else {
                        Log.d(TAG," IO Exception occured  "  + "["+Constants.HTTP_ERROR_MESSAGE+ ": 0"+response.code()+"]");
                        IOException ioException = new IOException( "["+Constants.HTTP_ERROR_MESSAGE+ ": 0"+response.code()+"]"+"");
                        throw ioException;

                    }

                } catch (IOException e ){

                    if( e == null){
                        return "["+Constants.CODE_UNEXPECTED_ERROR+ "]";
                    }else if( e.getMessage().contains("ECONNREFUSED")){
                        return "["+Constants.CODE_CONNECTION_REFUSE_ERROR+"]";
                    }else {
                        return "["+Constants.CODE_IO_EXCEPTION_ERROR+ "]";

                    }

                } catch (JSONException e ){
                    return "["+Constants.CODE_JSON_PARSING_ERROR+ "]";
                } catch (Exception e ){
                    return "["+Constants.CODE_UNEXPECTED_ERROR+ "]";
                }
                return "success";
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);

                if(showLoadingOption)
                    hideDialog();

                musicListFragment.onRefreshComplete();

                if(result != null ){
                    if( result.equals("success")){

                        if( "0000".equals(musicModel.getResultCode())) {

                            Log.d(TAG,"musicMode URL : " + musicModel.getMainADURL());

                            adWebView.loadUrl( musicModel.getMainADURL());
                            startIndex = Integer.parseInt(musicModel.getEndIndex());

                            if (isInit) {
                                invalidateUI(musicModel.getMusicModels());
                            } else {
                                startIndex = Integer.parseInt(musicModel.getEndIndex());
                                invalidateUI(musicModel.getMusicModels(), true);
                                musicListFragment.scrollSelf(pageNum);
                            }

                            if (nextWindTriggeringMusic) {

                                Message message = Message.obtain();
                                message.what = MusicService.NEXTWIND;
                                message.obj = musicModels.get(currentMusicPlayIndex);
                                musicListFragment.invalidateNextItem(currentMusicPlayIndex);
                                playbackControlsFragment.setStatus(true);
                                playbackControlsFragment.setItemInfo(
                                        musicModels.get(currentMusicPlayIndex).getTitle(),
                                        musicModels.get(currentMusicPlayIndex).getArtist());

                                playbackControlsFragment.setAlbumArt( musicModels.get(currentMusicPlayIndex).getImgPath() );

                                try {
                                    remoteMessage(message);
                                }
                                catch (RemoteException e) {
                                    e.printStackTrace();
                                }
                            }
                        }else {
                            showAlertDialogOnClick("오류 ", "죄송합니다 시스템 점검 중입니다.\n" + "[" + musicModel.getResultCode()+"]\n"+"확인을 누르면 어플이 종료됩니다.",MainActivity.this);
                        }

                    }else if(result.equals(Constants.NETWORK_NOT_CONNECTED_ERROR)){
                        showAlertNetWorkError();
                    }else if (result.contains(Constants.HTTP_ERROR_MESSAGE)){
                        showAlertDialogOnClick("오류 ", "죄송합니다 시스템 점검 중입니다.\n" + result+"\n" + "확인을 누르면 어플이 종료됩니다.",MainActivity.this);
                    }else if (result.contains(Constants.CODE_JSON_PARSING_ERROR)){
                        showAlertDialogOnClick("오류 ", "죄송합니다 시스템 점검 중입니다.\n" + result+"\n" + "확인을 누르면 어플이 종료됩니다.",MainActivity.this);
                    }
                    else {
                        showAlertDialogOnClick("오류 ", "죄송합니다 시스템 점검 중입니다.\n" + result+"\n" + "확인을 누르면 어플이 종료됩니다.",MainActivity.this);
                    }
                }
            }
        }.execute();


    }

    /**
     * Controller에서 들어오는 interface
     * public static final int PLAY_AND_PAUSE = 4;
     public static final int FASTWIND = 5;
     public static final int NEXTWIND = 6;
     * @param command
     */
    @Override
    public void onFragmentInteraction(int command) {

            try {
                Message msg = Message.obtain();

                msg.what = command;

                switch (msg.what) {
                    case MusicService.PLAY_AND_PAUSE:
                        if( currentMusicPlayIndex ==  musicModels.size()){
                            playbackControlsFragment.setStatus(!playbackControlsFragment.getStatus());
                            currentMusicPlayIndex = musicModels.size() -1;
                            msg.obj = musicModels.get(currentMusicPlayIndex);
                            playbackControlsFragment.setStatus(!playbackControlsFragment.getStatus());

                        } else if( (currentMusicPlayIndex >= 0) || (currentMusicPlayIndex < musicModels.size())) {
                            msg.obj = musicModels.get(currentMusicPlayIndex);
                            playbackControlsFragment.setStatus(!playbackControlsFragment.getStatus());
                            remoteMessage(msg);

                        }
                        break;
                    case MusicService.NEXTWIND:

                        if(NetworkUtil.isNetworkOn(this )) {

                            if ( (currentMusicPlayIndex >= 0) || (currentMusicPlayIndex < musicModels.size()) ) {

//                                Log.d(TAG, "currentMusicPlayIndex" + currentMusicPlayIndex);

                                currentMusicPlayIndex++;

                                if( currentMusicPlayIndex >= musicModels.size()) {
                                    currentMusicPlayIndex = musicModels.size();
                                    if( musicModel != null) {
                                        if ("Y".equals(musicModel.getNextYn())) {
                                            requestGetMusicList(false,true,Constants.SERVER_URL + Constants.GET_MUSIC_LIST, initGenreCd, initItemCount, startIndex,true);
                                        }else if ("N".equals( musicModel.getNextYn() )){
                                            Toast.makeText(this,Constants.NO_MORE_LIST_ITEM_MESSAGE,Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                } else {
                                    msg.obj = musicModels.get(currentMusicPlayIndex);
                                    musicListFragment.invalidateNextItem(currentMusicPlayIndex);
                                    playbackControlsFragment.setStatus(true);
                                    playbackControlsFragment.setItemInfo(
                                            musicModels.get(currentMusicPlayIndex).getTitle(),
                                            musicModels.get(currentMusicPlayIndex).getArtist());

                                    playbackControlsFragment.setAlbumArt( musicModels.get(currentMusicPlayIndex).getImgPath());
                                    remoteMessage(msg);
                                }
                            }
                        }else {
                            showAlertNetWorkError();
                        }
                        break;
                    default:
                        break;
                }


            } catch (RemoteException e) {
                e.printStackTrace();
            }


    }



    /**
     * 리스트에서 들어오는 interface
     * STOP_AND_PLAY
     * @param command
     * @param position
     */
    @Override
    public void onFragmentInteraction(int command, int position) {

        if( NetworkUtil.isNetworkOn( this )) {

            Message msg = Message.obtain();
            msg.what = command;
            msg.obj = musicModels.get(position);

//            Log.d(TAG, "command " + msg.what);
//            Log.d(TAG, "obj " + ((MusicItemModel) musicModels.get(position)).getSite());

            currentMusicPlayIndex = position;

            playbackControlsFragment.setStatus(true);
            playbackControlsFragment.setItemInfo(
                    musicModels.get(position).getTitle(),
                    musicModels.get(position).getArtist());

            playbackControlsFragment.setAlbumArt( musicModels.get(currentMusicPlayIndex).getImgPath());

            if (remoteMessenger != null) {
                try {
                    remoteMessenger.send(msg);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e(TAG, "remote object is null");
            }
        }else {
            showAlertNetWorkError();
        }
    }

    /**
     * 음악 list가 아랫쪽으로 swipe 이벤트가 발생할 경우 오는 callback
     * 네트워크 상태를 체크 후 , 알맞은 처리를 한다.
     * 네트워크에 연결되어 있으면,
     * 서버에서 더 아이템을 불러올수 있으면 불러온다.
     * 아니면, 에러 메세지를 띄운다.
     * 네트워크에 연결되어 있지 않으면 ,
     * alert을 띄운다.
     */
    @Override
    public void onFragmentItemRefresh() {

        if(NetworkUtil.isNetworkOn(this )) {
            if (musicModel == null) {
                musicListFragment.setRefreshing(false);
                requestInitGetMusicList();

            } else if ("Y".equals(musicModel.getNextYn())) {
                musicListFragment.setRefreshing(true);
                requestGetMusicListSwipeEvent(Constants.SERVER_URL + Constants.GET_MUSIC_LIST, initGenreCd, initItemCount, startIndex);

            } else if ("N".equals(musicModel.getNextYn())) {
                musicListFragment.setRefreshing(false);
                Toast.makeText(this,Constants.NO_MORE_LIST_ITEM_MESSAGE,Toast.LENGTH_SHORT).show();

            } else {
                musicListFragment.setRefreshing(false);
                requestGetMusicList(true,false,Constants.SERVER_URL + Constants.GET_MUSIC_LIST,initGenreCd,initItemCount,initStartIndex,false);

            }
        }else {
            musicListFragment.setRefreshing(false);
            showAlertNetWorkError();
        }

    }


    /**
     * 서비스로 음악의 상태를 변경하는 메세지를 보내는 메소드
     * @param msg  음악의 상태값과 음악 아이템의 정보가 담긴 message
     * @throws RemoteException
     */
    public void remoteMessage(Message msg) throws RemoteException{

        if(remoteMessenger !=null){
            remoteMessenger.send(msg);
        }else {
            Log.e(TAG,"remote object is null");
        }
    }


    @Override
    public void onClick(DialogInterface dialog, int which) {

        switch ( which) {
            case AlertDialog.BUTTON_POSITIVE :
                    finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {

        if (isFinish){
            finish();
        }else {
            isFinish = true;
            baseHandler.postDelayed(
                    new Runnable() {
                        @Override
                        public void run() {
                            isFinish = false;
                        }
                    },
                   500);


            Toast.makeText(this,"한번 더 누르면 앱이 종료됩니다. ",Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * 서비스로 음악에 대한 정보를 보내는 Handler 추가적으로 각 fragment의 UI를 변경한다.
     */
    private class ActivityRemoteHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {

            switch(msg.what){
                case MusicService.PAUSE:

                    playbackControlsFragment.setStatus( false) ;
                    break;
                case MusicService.PLAY:

                    playbackControlsFragment.setStatus( true) ;
                    break;

                case MusicService.STOP_AND_PLAY:

                    playbackControlsFragment.setStatus( true) ;
                    playbackControlsFragment.setItemInfo(
                            musicModel.getMusicModels().get(currentMusicPlayIndex).getTitle(),
                            musicModel.getMusicModels().get(currentMusicPlayIndex).getArtist());

                    break;
            }
        }
    }

}
