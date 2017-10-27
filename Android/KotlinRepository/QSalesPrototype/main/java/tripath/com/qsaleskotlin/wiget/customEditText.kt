package tripath.com.qsaleskotlin.wiget

import android.content.Context
import android.util.AttributeSet
import android.support.v7.widget.AppCompatEditText;
import android.util.Log

/**
 * Created by SSLAB on 2017-07-03.
 */

class customEditText : AppCompatEditText(){

    init {
        Log.d("TAG","getContext");
    }
    constructor(context: Context?) : this(context,null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)


  }

