package tripath.com.samplekapp

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast

/**
 *  !! 란 ?
 */
class SplashScreenActivity  : BaseActivity() , View.OnClickListener {
    override fun onClick(p0: View?) {
        when(p0!!.id){
            R.id.button ->{
                var intent = Intent(applicationContext,MainActivity::class.java )
                startActivity( intent )
            }
            else -> {
                Toast.makeText(applicationContext,"unexpeded error",Toast.LENGTH_SHORT).show();
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)


        baseHandler.postDelayed(
            object : Runnable {
                override fun run() {
//                    var intent = Intent(this@SplashScreenActivity,MainActivity::class.java )
//                    startActivity( intent )

                    redirectMainActivityWithAnim(this@SplashScreenActivity)
                }
            },1000)

        findViewById( R.id.button).visibility = View.GONE
        findViewById( R.id.button).setOnClickListener(this )

    }


}
