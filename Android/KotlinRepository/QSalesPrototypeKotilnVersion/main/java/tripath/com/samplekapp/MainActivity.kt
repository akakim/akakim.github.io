package tripath.com.samplekapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.android.volley.Response
import com.android.volley.VolleyError
import tripath.com.samplekapp.data.User
import tripath.com.samplekapp.restclient.RestApiClient

class MainActivity : BaseActivity() , View.OnClickListener,Response.Listener<User>, Response.ErrorListener{

    val TAG = javaClass.simpleName

    lateinit var edId : EditText
    lateinit var edPassword : EditText
    override fun onResponse(response: User?) {


        Log.d(TAG,"onResponse")
        Log.d(TAG,response.toString())

        if( response == null){
            Toast.makeText(this , "response is null " , Toast.LENGTH_SHORT ).show()
        }else if(response is User ){


            Toast.makeText(this , response.toString(), Toast.LENGTH_SHORT ).show()

        }else {
            Toast.makeText(this , "another object is in... ", Toast.LENGTH_SHORT ).show()
        }


    }

    override fun onErrorResponse(error: VolleyError?) {

        Log.d(TAG,"onError occured")
        error?.printStackTrace()

        if( error != null){
            Toast.makeText(this , "error occured " + error.message, Toast.LENGTH_SHORT ).show()
        }else {
            Toast.makeText(this , "error is null .. " , Toast.LENGTH_SHORT ).show()
        }
//        val stackTrace = error?.stackTrace
//
//        error.message
//        if(stackTrace != null) {
//            for (stack in stackTrace) {
//
//            }
//
//
//        }else {
//            Toast.makeText(this , "stackTrace is null ", Toast.LENGTH_SHORT ).show()
//        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        resapiService
        setTitle(localClassName);

        edId  = findViewById(R.id.edId) as EditText
        edPassword = findViewById( R.id.edPwd) as EditText

        findViewById( R.id.btnLogin).setOnClickListener( this )

        edId.setText("jaehoon_park@kolon.com")
        edPassword.setText("1q2w3e4r5t")

    }

    override fun onClick(p0: View?) {
        when (p0?.id){
            R.id.btnLogin -> {

                val user = User(edId.text.toString(),edPassword.text.toString(),"","" )
                resApiClient.loginCheck(user,this,this)
            }
            else -> {
                Toast.makeText(this,"hello error",Toast.LENGTH_SHORT).show()
            }
        }
    }
}
