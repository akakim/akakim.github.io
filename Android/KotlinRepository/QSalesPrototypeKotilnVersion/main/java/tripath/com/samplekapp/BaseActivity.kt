package tripath.com.samplekapp;

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.android.volley.Response
import com.android.volley.VolleyError
import retrofit2.CallAdapter
import tripath.com.samplekapp.data.CommonData
import tripath.com.samplekapp.data.RestApiService
import tripath.com.samplekapp.restclient.RestApiClient

/**
 * Created by SSLAB on 2017-07-26.
 */

open class BaseActivity : AppCompatActivity() ,Handler.Callback{
//    override fun onResponse(response: Any?) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }
//
//    override fun onErrorResponse(error: VolleyError?) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }


    open val baseHandler :Handler = Handler( this )

    open val resapiService : RestApiService = RestApiService.defaultCreateUrl()
    var backPressMode :Int = 0;


    // sample 변수 선언들.  연습겸 남겨둠.
    var size = 0
    val isEmpty : Boolean get() = this.size == 0
    val isNotEmpty : Boolean = false
    var isEmptyOK : Boolean = true
    var Mode : Int = 10
    val Modesetting : Int get(){
        if( isNotEmpty )
            return 10
        else
            return 20
    }
    val floatingPoing : Float = 10.0f

    val longPointing : Long = 10L

    val resApiClient : RestApiClient = RestApiClient(this)

    lateinit var defaultDialog : AlertDialog


    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d(localClassName,"OnCreated")
        Log.d(localClassName,isEmpty.toString())
        Log.d(localClassName,isNotEmpty.toString())
        Log.d(localClassName,Mode.toString())
        Log.d(localClassName,"Modesetting : " + Modesetting.toString())




    }


    fun isNetworkConntected() : Boolean {
        /**
         * retrieving an instance of conntectivityManager class from the current application context.
         * the connectivityManager class is simply your go to class if you need any information about
         * the network connectivity. it can also be set up to report network connection changes to your applicatoin
         * 현재 어플리케이션 문맥으로 부터 connectivityManager 클래스의 인스턴스를 가져오는 것이다.
         * connectivityManger 클래스는 당신이 가는 클래스이다(?) 만야 ㄱ당신이 네트워크에 대한 정보를 가지기 위해 가져오는 클래스이다.
         *
         * retrieving info
         * class
         *
         *
         */

        // 현재 어플리케이션 문맥으로 부터 connectivityManager 클래스의 인스턴스를 가져오는 것  간단히

        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo // 2
        return networkInfo != null && networkInfo.isConnected
    }


     override fun handleMessage(p0: Message?): Boolean {

        val messageWhat = p0?.what
        val messageObj = p0?.obj


        when ( messageWhat ){
            null -> Log.e(localClassName,"message is null")
            StaticValues.SHOW_DIALOG_MESSAGE -> {
                Log.d(localClassName, "SHOW ailog")
            }
            else ->{
                Log.d(localClassName,"another what ? " + messageWhat)
            }
        }
        return true;
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    protected fun redirectMainActivityWithAnim(activity : AppCompatActivity){

        val intent = Intent( activity , MainActivity::class.java)

        startActivity( intent)
        overridePendingTransition(0,R.anim.anim_fade_in)


    }
}
