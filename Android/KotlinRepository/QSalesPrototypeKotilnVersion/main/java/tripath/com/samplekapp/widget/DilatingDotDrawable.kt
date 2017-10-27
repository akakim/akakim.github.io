package tripath.com.samplekapp.widget

import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.Log

/**
 * Created by SSLAB on 2017-08-10.
 */

class DilatingDotDrawable(color : Int , radius : Float, maxRadius : Float) : Drawable() {

    companion object {
        val TAG = "DilatingDotDrawable"
    }
    internal val mDirtyBounds : Rect = Rect(0,0,0,0)
    var mPaint : Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    var radius : Float =0.0f

    var maxRadius : Float
        get() = this.maxRadius
        set( value ){ setBounds( value) }

    init{
        Log.d(TAG,"initialize.... " )
        mPaint.color = color
        mPaint.style = Paint.Style.FILL
        mPaint.strokeCap = Paint.Cap.ROUND
        mPaint.strokeJoin = Paint.Join.ROUND


        this.radius = radius
        this.maxRadius = maxRadius
    }






// ...?????
    constructor() : this(0xffff,0.0f,0.0f)
//    constructor(color : Int , radius : Float, maxRadius : Float)  {
//
//        mPaint.color = color
//        mPaint.style = Paint.Style.FILL
//        mPaint.strokeCap = Paint.Cap.ROUND
//        mPaint.strokeJoin = Paint.Join.ROUND
//
//
//        this.radius = radius
//
//
//    }



    fun setBounds( maxRadius: Float ){
        Log.d(TAG,"setBound! : " + maxRadius)
        this.maxRadius = maxRadius
        mDirtyBounds.bottom = this.maxRadius.toInt() * 2  + 2
        mDirtyBounds.right = this.maxRadius.toInt() * 2 + 2
    }

    override fun draw(canvas: Canvas?) {
        val bound : Rect = this.bounds

        canvas!!.drawCircle(bound.centerX().toFloat(),bound.centerY().toFloat(),radius -1, mPaint )

    }

    override fun setAlpha(alpha: Int) {
        if( alpha != mPaint.alpha ){
            mPaint.alpha = alpha
            this.invalidateSelf()
        }
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
       mPaint.colorFilter = colorFilter
       this.invalidateSelf()
    }

    override fun getOpacity(): Int {
        return PixelFormat.TRANSLUCENT
    }

    fun setColor(color : Int){
        mPaint.color = color
        invalidateSelf()
    }

    fun refreshRadisu(radius: Float){
        this.radius =radius
        invalidateSelf()
    }
//    fun setRadius(radius: Float){
//
//    }

    override fun getIntrinsicWidth(): Int {
        return maxRadius.toInt() * 2  + 2
    }

    override fun getDirtyBounds(): Rect {
        return mDirtyBounds
    }

    override fun onBoundsChange(bounds: Rect?) {
        super.onBoundsChange(bounds)
        mDirtyBounds.offsetTo( bounds!!.left,bounds!!.top )
    }
}