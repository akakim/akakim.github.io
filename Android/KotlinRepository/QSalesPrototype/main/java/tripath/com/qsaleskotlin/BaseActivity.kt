package tripath.com.qsaleskotlin

/**
 * Created by SSLAB on 2017-07-03.
 */
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

open class BaseActivity : AppCompatActivity() {

    val defaultHandler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


}