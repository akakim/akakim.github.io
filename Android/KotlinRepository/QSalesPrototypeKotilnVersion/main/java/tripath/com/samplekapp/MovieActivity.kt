package tripath.com.samplekapp

import android.app.PendingIntent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.app.PictureInPictureParams
import android.app.RemoteAction
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.Configuration
import android.graphics.drawable.Icon
import android.net.Uri
import android.support.annotation.DrawableRes
import android.util.Rational
import android.view.View
import android.widget.ScrollView
import tripath.com.samplekapp.widget.MovieView

class MovieActivity : AppCompatActivity() {

    companion object {

        // intent action for media control from picture-in-picture mode
        private val ACTION_MEDIA_CONTROL = "media_control"

        //Intent extra for media controls from Picture-in-Picture mode.
        private val EXTRA_CONTROL_TYPE = "control_type"

        // The request code for play action PendingIntent.  */
        private val REQUEST_PLAY = 1

        // The request code for pause action PendingIntent.  */
        private val REQUEST_PAUSE = 2

        // The request code for info action PendingIntent.  */
        private val REQUEST_INFO = 3

        // The intent extra value for play action.  */
        private val CONTROL_TYPE_PLAY = 1

        // The intent extra value for pause action.  */
        private val CONTROL_TYPE_PAUSE = 2

    }

    // the arguments to be used for picture in picture mode
    // remember api level 26 is required
    private val pictureInPicturePramsBuilder = PictureInPictureParams.Builder()

    /** This shows the video.  */
    private lateinit var mMovieView: MovieView

    /** The bottom half of the screen; hidden on landscape  */
    private lateinit var mScrollView: ScrollView

    /**
     * 인텐트 컨트롤 타입에 따라
     */
    private val mReceiver = object : BroadcastReceiver(){
        override fun onReceive (context: Context, intent: Intent?){
            intent?.let{
                innerIntent ->
                if( innerIntent.action != ACTION_MEDIA_CONTROL ) {
                    return
                }
                // this is where we are called back from picture in picture action items

                val controlType = innerIntent.getIntExtra( EXTRA_CONTROL_TYPE, 0)
                when (controlType){
                    CONTROL_TYPE_PAUSE ->mMovieView.play()
                    CONTROL_TYPE_PLAY -> mMovieView.pause()
                }

//                val controlType = intent.getIntExtra( )
            }
        }
    }

    private val labelPlay: String by lazy { getString(R.string.play) }
    private val labelPause: String by lazy { getString(R.string.pause) }


    /**
     * Moview View의 콜백
     */
    private val mMovieListener = object : MovieView.MovieListener() {

        override fun onMoviewStarted() {
            updatePictureInPicureActions(
                    R.drawable.ic_pause_24dp,
                    labelPause,
                    CONTROL_TYPE_PAUSE,
                    REQUEST_PAUSE
            )
        }

        override fun onMoviewStopped() {

            updatePictureInPicureActions(
                    R.drawable.ic_play_arrow_24dp,
                    labelPlay,
                    CONTROL_TYPE_PLAY,
                    REQUEST_PLAY
            )
        }

        override fun onMovieMinimized() {
            minimize()
         }
    }


    /**
     *
     */
    internal fun updatePictureInPicureActions (@DrawableRes iconId: Int, title: String,
                                               controlType: Int, requestCode: Int){

        val actions = ArrayList<RemoteAction>()

        // this is teh pending inetne that is invoked when user click on the action item.
        // you need to use distinct request code for play and pause , or pendingintent won't
        // be properly updated.
        val intent = PendingIntent.getBroadcast(
                        this@MovieActivity,
                        requestCode,
                        Intent(ACTION_MEDIA_CONTROL).putExtra( EXTRA_CONTROL_TYPE, controlType ),
                        0)

        val icon = Icon.createWithResource(this@MovieActivity,iconId)

        actions.add(
                RemoteAction( Icon.createWithResource(this@MovieActivity, R.drawable.ic_info_24dp) ,
                    getString(R.string.info),
                    getString(R.string.info_description),
                    PendingIntent.getActivity(this@MovieActivity, REQUEST_INFO,Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.info_uri))),0 ))
        )


        pictureInPicturePramsBuilder.setActions( actions )


        setPictureInPictureParams(pictureInPicturePramsBuilder.build())
    }
    /** The arguments to be used for picture in picture mode */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie)

        mMovieView = findViewById( R.id.movie ) as MovieView
        mScrollView = findViewById( R.id.scroll ) as ScrollView

        mMovieView.setMovieListener( mMovieListener )

        findViewById(R.id.pip).setOnClickListener { minimize() }


    }


    override fun onStop() {
        mMovieView.pause()
        super.onStop()

    }

    override fun onRestart() {
        super.onRestart()

        if(!isInPictureInPictureMode){
            mMovieView.showControls()
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        adjustFullScreen(newConfig)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)

        if( hasFocus ) {
            adjustFullScreen( resources.configuration )
        }
    }

    override fun onPictureInPictureModeChanged(isInPictureInPictureMode: Boolean, newConfig: Configuration?) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig)
        if (isInPictureInPictureMode) {
            // Starts receiving events from action items in PiP mode.
            registerReceiver(mReceiver, IntentFilter(ACTION_MEDIA_CONTROL))
        } else {
            // We are out of PiP mode. We can stop receiving events from it.
            unregisterReceiver(mReceiver)
            // Show the video controls if the video is not playing
            if (!mMovieView.isPlaying) {
                mMovieView.showControls()
            }
        }

    }
    /**
     * enters Picture in picture mode
     */

    internal fun minimize (){
        // hide the controll in picture - in -picture mode
        mMovieView.hideControls()

        // calcualte the aspect ratio fof pip screen

        pictureInPicturePramsBuilder.setAspectRatio( Rational(mMovieView.width, mMovieView.height ))
        enterPictureInPictureMode( pictureInPicturePramsBuilder.build() )


    }

    private fun adjustFullScreen( config : Configuration){
        val decorView = window.decorView
        if( config.orientation == Configuration.ORIENTATION_LANDSCAPE ){
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY

            mScrollView.visibility = View.GONE
            mMovieView.setAdjustViewBounds( false )

        }else {
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            mScrollView.visibility = View.VISIBLE
            mMovieView.setAdjustViewBounds( true )
        }
    }
}
