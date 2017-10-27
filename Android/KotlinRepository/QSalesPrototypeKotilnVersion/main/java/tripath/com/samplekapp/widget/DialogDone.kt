package tripath.com.samplekapp.widget

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.widget.Button
import android.widget.TextView
import tripath.com.samplekapp.R

/**
 * Created by SSLAB on 2017-08-01.
 */
class DialogDone : Dialog {

    private lateinit var titleView : TextView
    private lateinit var contentView : TextView
    private lateinit var confirmBtn : Button


    constructor(context: Context?) : this(context,-1)
    constructor(context: Context?, themeResId: Int) : super(context, themeResId) {
        setContentView( R.layout.dialog_done )

        setCancelable(false)

        titleView =  findViewById<TextView>(R.id.textTitle)
        contentView = findViewById<TextView>(R.id.textContent)
        confirmBtn = findViewById<Button>(R.id.btnDone)
    }
    constructor(context: Context?, cancelable: Boolean, cancelListener: DialogInterface.OnCancelListener?) : super(context, cancelable, cancelListener){
        setContentView( R.layout.dialog_done )
        titleView =  findViewById<TextView>(R.id.textTitle)
        contentView = findViewById<TextView>(R.id.textContent)
        confirmBtn = findViewById<Button>(R.id.btnDone)
    }
    constructor(context: Context?, cancelable: Boolean, cancelListener: ((DialogInterface) -> Unit)?) : super(context, cancelable, cancelListener){
        setContentView( R.layout.dialog_done )
        titleView =  findViewById<TextView>(R.id.textTitle)
        contentView = findViewById<TextView>(R.id.textContent)
        confirmBtn = findViewById<Button>(R.id.btnDone)
    }


    fun setDialogTitle(title: CharSequence?) : DialogDone {
        titleView?.setText( title )
        return this
    }

    fun setContent(contents : CharSequence?) : DialogDone{
        contentView?.setText(contents)
        return this
    }



}