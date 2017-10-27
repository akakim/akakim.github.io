package tripath.com.qsaleskotlin

import android.content.Intent
import android.os.Bundle
import android.os.Handler

/**
 * Created by SSLAB on 2017-07-03.
 */


class SplashScreen : BaseActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val runnable = Runnable {
              fun run(){


            }
        }

        defaultHandler.postDelayed(runnable,1000);
//        startActivity( )
    }
}