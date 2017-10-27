package tripath.com.samplekapp.widget

import android.content.Context
import android.graphics.Color
import android.media.MediaPlayer
import android.os.Handler
import android.os.Message
import android.support.annotation.RawRes
import android.transition.TransitionManager
import android.util.AttributeSet
import android.util.Log
import android.view.Surface
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.widget.ImageButton
import android.widget.RelativeLayout
import tripath.com.samplekapp.R
import java.lang.ref.WeakReference

/**
 * let 절이 쓰이게 된다면 어떤 변수다 라는걸 표시안하고 그그냥 변수명만 쓰면 자기
 * 자신을 가리키는거같다.
 */

class MovieView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null,
                                          defStyleAttr: Int = 0) :
        RelativeLayout(context,attrs,defStyleAttr){


    companion object {
        private val TAG = "MovieView"
        private val FAST_FORWARD_REWIND_INTERVAL = 5000 // ms

        // the amount of time until we fade out the control
        private val TIMEOUT_CONTROLS = 3000L // ms
    }

    // 자신의 상태의 변화를 알 수있는 콜백
    // monitors all event relate to movieView

    abstract class MovieListener{

        // call when  the video is stated or resumed

        open fun onMoviewStarted(){}

        // called when the video is paused or finished
        open fun onMoviewStopped(){}

        // called when this view should be minimized
        open fun onMovieMinimized(){}
    }

    // shows the video playback
    private val surfaceView : SurfaceView

    // Controls
    private val toggleBtn : ImageButton
    private val shade : View
    private val fastForward : ImageButton
    private val fastRewind : ImageButton
    private val minimize : ImageButton

    // the plays the video. this will be null when no video is set
    internal var mediaPlayer: MediaPlayer? = null

    /** The resource ID for the video to play.  */
    @RawRes
    private var videoResourceId: Int = 0

    /** Whether we adjust our view bounds or we fill the remaining area with black bars  */
    private var adjustViewBounds: Boolean = false

    /** Handles timeout for media controls.  */
    private var timeoutHandler: TimeoutHandler? = null

    /** The listener for all the events we publish.  */
    internal var movieListener: MovieListener? = null

    private var savedCurrentPosition: Int = 0

    init {
        setBackgroundColor( Color.BLACK )

        // inflate the content
        View.inflate(context, R.layout.view_movie, this)
        surfaceView = findViewById  <SurfaceView>   (R.id.surface)
        shade       = findViewById  <View>          (R.id.shade)
        toggleBtn   = findViewById  <ImageButton>   (R.id.toggle)
        fastForward = findViewById  <ImageButton>   (R.id.fast_forward)
        fastRewind  = findViewById  <ImageButton>   (R.id.fast_rewind)
        minimize    = findViewById  <ImageButton>   (R.id.minimize)

        // attributes...

        val a = context.obtainStyledAttributes(attrs,R.styleable.MovieView,
                defStyleAttr,R.style.Widget_PictureInPicture_MovieView  )
        setVideoResourceId( a.getResourceId(R.styleable.MovieView_android_src, 0))
        setAdjustViewBounds( a.getBoolean( R.styleable.MovieView_android_adjustViewBounds,false))
        a.recycle()

        val listener = View.OnClickListener {
            view ->
            when (view.id){
                R.id.surface -> toggleControls()
                R.id.toggle  -> toggle()
                R.id.fast_forward -> fastForwardFun()
                R.id.fast_rewind -> fastRewind()
                R.id.minimize -> movieListener?.onMovieMinimized()
            }

            // TODO : 뭘까 이부분은 .
            // Start or reset the timeout to hide controls
            mediaPlayer?.let { player ->
                if (timeoutHandler == null) {
                    timeoutHandler = TimeoutHandler(this@MovieView)
                }
                timeoutHandler?.let { handler ->
                    handler.removeMessages(TimeoutHandler.MESSAGE_HIDE_CONTROLS)
                    if (player.isPlaying) {
                        handler.sendEmptyMessageDelayed(TimeoutHandler.MESSAGE_HIDE_CONTROLS,
                                TIMEOUT_CONTROLS)
                    }
                }
            }
        }


        surfaceView.setOnClickListener( listener )
        shade.setOnClickListener( listener )

        toggleBtn.setOnClickListener( listener )
        fastForward.setOnClickListener( listener )
        fastRewind.setOnClickListener( listener )
        minimize.setOnClickListener( listener )

        // prepare

        surfaceView.holder.addCallback( object : SurfaceHolder.Callback {
            override fun surfaceChanged(p0: SurfaceHolder?, p1: Int, p2: Int, p3: Int) {

            }

            override fun surfaceDestroyed(p0: SurfaceHolder?) {
              mediaPlayer?.let{
                  savedCurrentPosition = it.currentPosition
                  closeVideo()
              }
            }

            override fun surfaceCreated(holder: SurfaceHolder) {
                openVideo(holder.surface)
            }
        })

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        mediaPlayer?.let {
            player->
            val videoWidth = player.videoWidth
            val videoHeight = player.videoHeight
            if (videoWidth != 0 && videoHeight != 0) {
                val aspectRatio = videoHeight.toFloat() / videoWidth
                val width = View.MeasureSpec.getSize(widthMeasureSpec)
                val widthMode = View.MeasureSpec.getMode(widthMeasureSpec)
                val height = View.MeasureSpec.getSize(heightMeasureSpec)
                val heightMode = View.MeasureSpec.getMode(heightMeasureSpec)

                if( adjustViewBounds ){

                    /**
                     * 가로 사이즈는 정해져있는데 높이는 정해지지 않는경우
                     * 가로의비율에 맞춰서 높이를 설정한다.
                     */
                    if( widthMode == View.MeasureSpec.EXACTLY && heightMode != View.MeasureSpec.EXACTLY){
                        super.onMeasure( widthMeasureSpec ,
                                View.MeasureSpec.makeMeasureSpec( (width * aspectRatio).toInt(),View.MeasureSpec.EXACTLY ))
                    }
                    /**
                     * 높이가 정확한경우 가로를 높이의 비율에 맞춘다.
                     */
                    else if (widthMode != View.MeasureSpec.EXACTLY && heightMode == View.MeasureSpec.EXACTLY) {
                        super.onMeasure(View.MeasureSpec.makeMeasureSpec((height / aspectRatio).toInt(), View.MeasureSpec.EXACTLY), heightMeasureSpec)
                    } else {
                        super.onMeasure( widthMeasureSpec , View.MeasureSpec.makeMeasureSpec( (width * aspectRatio).toInt(),View.MeasureSpec.EXACTLY ))
                    }
                }else {
                    val viewRatio = height.toFloat() / width
                    if (aspectRatio > viewRatio) {
                        val padding = ((width - height / aspectRatio) / 2).toInt()
                        setPadding(padding, 0, padding, 0)
                    } else {
                        val padding = ((height - width * aspectRatio) / 2).toInt()
                        setPadding(0, padding, 0, padding)
                    }
                    super.onMeasure(widthMeasureSpec, heightMeasureSpec)
                }


                return
            }


         }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }


    override fun onDetachedFromWindow() {
        timeoutHandler?.removeMessages ( TimeoutHandler.MESSAGE_HIDE_CONTROLS )
        timeoutHandler = null
        super.onDetachedFromWindow()
    }


    val isPlaying : Boolean get() = mediaPlayer?.isPlaying?:false
    /**
     * Sets the listener to monitor movie events.

     * @param movieListener The listener to be set.
     */
    fun setMovieListener(movieListener: MovieListener?) {
        this.movieListener = movieListener
    }
    fun setVideoResourceId(@RawRes id: Int){
        if( id == videoResourceId ){
            return
        }
        videoResourceId = id
        val surface = surfaceView.holder.surface
        if( surface != null && surface.isValid ){
            closeVideo()
            openVideo(surface)
        }

    }

    fun setAdjustViewBounds( adjustViewBound : Boolean ){
        if( adjustViewBounds == adjustViewBound ){
            return
        }

        adjustViewBounds = adjustViewBound
        if( adjustViewBound ){
            background = null
        }else {
            setBackgroundColor( Color.BLACK )
        }
        requestLayout()
    }


    /**
     * player를 메모리에서 해체한다.
     */
    internal fun closeVideo(){
        mediaPlayer?.release()
        mediaPlayer = null
    }

    internal fun openVideo(surface : Surface){
        if( videoResourceId == 0){
            return
        }

        mediaPlayer = MediaPlayer()
        mediaPlayer?.let {
            player -> player.setSurface( surface )

            try{
                resources.openRawResource( videoResourceId ).use { fd ->
//                    player.setDataSource( fd )
                    player.setOnPreparedListener {
                        mediaPlayer ->
                        //adjust aspect ratio of this view
                        requestLayout()
                        if( savedCurrentPosition > 0 ){
                            mediaPlayer.seekTo( savedCurrentPosition )
                            savedCurrentPosition = 0
                        }else {
                            play()
                        }
                    }
                }

            }catch ( e : Exception){
                Log.e(TAG,"Failed to open Video",e)
            }
        }

    }


    /**
     * 정지하느냐 재생하느냐
     */
    internal fun toggle(){
        mediaPlayer?.let{
            if( it.isPlaying )
                pause()
            else
                play()
        }
    }


    /**
     * 제어 버튼들을 보여주거나 안보여주거나
     */
    internal fun toggleControls(){
        if( shade.visibility == View.VISIBLE ){
            hideControls()
        }else {
            showControls()
        }
    }


    /**
     * 동영상 시작
     */
    fun play(){
        if( mediaPlayer == null){
            return
        }
        mediaPlayer!!.start()

    }


    /**
     * 되감기
     */
    fun fastRewind(){
        mediaPlayer?.let { it.seekTo(it.currentPosition - FAST_FORWARD_REWIND_INTERVAL) }
    }

    /**
     * 뒤로가기
     */
    fun fastForwardFun(){
        mediaPlayer?.let {
            it.seekTo( it.currentPosition - FAST_FORWARD_REWIND_INTERVAL)
        }
    }
    /**
     * 동영상 정지
     */
    fun pause(){
        if( mediaPlayer == null ){
            adjustToggleState()
            return
        }

        mediaPlayer!!.pause()
        keepScreenOn = false
        movieListener?.onMoviewStopped()

    }


    /**
     * 컨트롤버튼 숨김
     */
    fun hideControls(){
        TransitionManager.beginDelayedTransition(this)
        // TODO: visibility 조정.
        shade.visibility = View.INVISIBLE
        toggleBtn.visibility = View.INVISIBLE
        fastForward.visibility = View.INVISIBLE
        fastRewind.visibility = View.INVISIBLE
        minimize.visibility = View.INVISIBLE
    }

    /**
     * 컨트롤 버튼 보여줌
     */
    fun showControls(){
        TransitionManager.beginDelayedTransition(this)
        // TODO: visibility 조정.
        shade.visibility = View.VISIBLE
        toggleBtn.visibility = View.VISIBLE
        fastForward.visibility = View.VISIBLE
        fastRewind.visibility = View.VISIBLE
        minimize.visibility = View.VISIBLE
    }


    /**
     * 재생중이냐 일시정지중이냐에 따른 이미지 버튼 교체
     */
    internal fun adjustToggleState(){

        mediaPlayer?.let{
            if( it.isPlaying ){
                toggleBtn.contentDescription = resources.getString( R.string.pause )
                toggleBtn.setImageResource( R.drawable.ic_pause_64dp)
            } else {
                toggleBtn.contentDescription = resources.getString( R.string.play )
                toggleBtn.setImageResource( R.drawable.ic_play_arrow_64dp )
            }
        }
    }



    // 타임아웃 정책에 대한 클래스 .

    private class TimeoutHandler(view: MovieView): Handler(){
        private val mMovieViewRef : WeakReference<MovieView> = WeakReference(view)

        override fun handleMessage(msg: Message){
            when (msg.what){
                MESSAGE_HIDE_CONTROLS ->{
                    mMovieViewRef.get()?.hideControls()
                }
                else ->super.handleMessage(msg)
            }
        }

        companion object {
            internal val MESSAGE_HIDE_CONTROLS = 1
        }
    }
}