package tripath.com.samplekapp.widget

import android.animation.*
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.renderscript.Sampler
import android.support.annotation.ColorInt
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import tripath.com.samplekapp.R
import java.util.ArrayList

/**
 * Created by SSLAB on 2017-08-10.
 */

open class DilatingDotsProgressBar :View{

    companion object {
        const val TAG :String = "DilatingDotsProgressBar"
        const val START_DELAY_FACTOR        : Double        = 0.35
        const val DEFAULT_GROWTH_MULTIPLIER : Float         = 1.75f
        const val MIN_SHOW_TIME             : Int           = 100
        const val MIN_DELAY                 : Int           = 100
    }

    private var mDotColor                   : Int           = 0
    private var mDotEndColor                : Int           = 0
    private var mAnimationDuration          : Int           = 0
    private var mWidthBetweenDotCenters     : Int           = 0
    private var mNumberDots                 : Int           = 0
    private var mDotRadius                  : Float         = 0.toFloat()
    private var mDotScaleMultiplier         : Float         = 0.toFloat()
    private var mDotMaxRadius               : Float         = 0.toFloat()
    private var mHorizontalSpacing          : Float         = 0.toFloat()
    private var mStartTime                  : Long          = -1
    private var mShouldAnimate              : Boolean       = false
    private var mDismissed                  : Boolean       = false
    private var mIsRunning                  : Boolean       = false
    private var mAnimateColors              : Boolean       = false

    private val mDrawables = ArrayList<DilatingDotDrawable>()
    private val mAnimations = ArrayList<Animator>()


    lateinit var defaultBackground : Drawable

    private val mDelayedHide : Runnable
        get() = Runnable  {
                    mStartTime = -1
                    mIsRunning = false
                    visibility = View.GONE
            stopAnimaion()
        }

    private val mDelayedShow : Runnable
        get() = Runnable {
            if(!mDismissed)
                mStartTime = System.currentTimeMillis()
            visibility = View.VISIBLE
            startAnimations()

        }



    constructor(context: Context?): this(context, null)
    constructor(context: Context?,attrs: AttributeSet? ): this (context,attrs,-1)
    constructor(context: Context?,attrs: AttributeSet?,defStyleAttr: Int) : this(context,attrs,defStyleAttr,-1)
    constructor(context: Context?,attrs: AttributeSet?,defStyleAttr: Int,defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes){


        defaultBackground = rootView.background
        val a : TypedArray = getContext().obtainStyledAttributes( attrs , R.styleable.DilatingDotsProgressBar)
        mNumberDots = a.getInt( R.styleable.DilatingDotsProgressBar_dd_numDots, 3 )
        mDotRadius = a.getDimension( R.styleable.DilatingDotsProgressBar_android_radius, 8f )
        mDotColor = a.getColor( R.styleable.DilatingDotsProgressBar_android_color , 0xff9c27b0.toInt() )
        mDotScaleMultiplier = a.getFloat(R.styleable.DilatingDotsProgressBar_dd_scaleMultiplier, DEFAULT_GROWTH_MULTIPLIER)
        mAnimationDuration = a.getInt(R.styleable.DilatingDotsProgressBar_dd_animationDuration, 300)
        mHorizontalSpacing = a.getDimension(R.styleable.DilatingDotsProgressBar_dd_horizontalSpacing, 12f)
        a.recycle()

        mShouldAnimate = false
        mAnimateColors = mDotColor != mDotEndColor

        calculateMaxRadius()
        calculateWidthBetweenDotCenters()

        initDots()
        updateDots()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if( computeMaxHeight().toInt() != h || computeMaxWidth().toInt() != w ){
            updateDots()
        }
    }


    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        removeCallbacks()
    }

    fun removeCallbacks(){
        removeCallbacks(mDelayedHide)
        removeCallbacks(mDelayedShow)
    }

    fun reset(){
        hideNow()
    }

    /**
     * Hide the progress view if it is visible. The progress view will not be
     * hidden until it has been shown for at least a minimum show time. If the
     * progress view was not yet visible, cancels showing the progress view.
     */

    fun hide(){
        hide(MIN_SHOW_TIME)
    }

    fun hide(delay : Int ){
        mDismissed = true
        removeCallbacks(mDelayedShow)
        val diff : Long = System.currentTimeMillis() - mStartTime
        if( (diff >= delay) || (mStartTime == -1L)){
            mDelayedHide.run()
        } else {
            postDelayed( mDelayedHide,delay - diff)
        }
    }

    fun hideNow(){
        hide(0)
    }

    fun show(){
        show(MIN_DELAY)
    }

    fun showNow(){
        show(0)
    }

    fun show(delay : Int){
        if(mIsRunning)
            return

        mIsRunning = true

        mStartTime = -1
        mDismissed = false
        removeCallbacks( mDelayedHide )

        if(delay == 0 ){
            mDelayedShow.run()
        } else {
            postDelayed( mDelayedShow, delay.toLong() )
        }
    }


    override fun onDraw(canvas: Canvas?) {
        if(shouldAnimate()){
            for ( dot in mDrawables ){
                dot.draw(canvas)
            }
        }
    }



    override fun verifyDrawable (who : Drawable) : Boolean {
        if(shouldAnimate()){
            return mDrawables.contains(who)
        }
        return super.verifyDrawable(who)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
      setMeasuredDimension( computeMaxWidth().toInt() , computeMaxHeight().toInt())
    }

    private fun computeMaxWidth () : Float{
        return mDotMaxRadius * 2;
    }

    private fun computeMaxHeight () :Float {
        return computeWidth() + ( (mDotMaxRadius - mDotRadius ) * 2)
    }
    protected fun shouldAnimate(): Boolean {
        return mShouldAnimate
    }

    private fun computeWidth() : Float{
        return (((mDotRadius * 2) + mHorizontalSpacing ) * mDrawables.size) - mHorizontalSpacing
    }

    /**
     *
     */
    private fun calculateMaxRadius(){
        mDotMaxRadius = mDotRadius * mDotScaleMultiplier
    }


    private fun calculateWidthBetweenDotCenters() {
        mWidthBetweenDotCenters = mDotRadius.toInt() * 2 + mHorizontalSpacing.toInt()
    }

    private fun initDots(){
        mDrawables.clear()
        mAnimations.clear()

        var i = 1
        while (i<=mNumberDots){
            val dot = DilatingDotDrawable ( mDotColor, mDotRadius, mDotMaxRadius )
            dot.callback = this
            mDrawables.add(dot)

            val startDelay : Long = ( i -1 ).toLong() * (START_DELAY_FACTOR * mAnimationDuration).toLong()

            /**
             * 애니메이터 설정. 그러니까.. 각 각의 애니메이션을 정의하고
             * 애니메이션이 끝나도 계속 돌아가게끔 설정.
             *
             */
            val growAnimator : ValueAnimator = ObjectAnimator.ofFloat(dot,"radius",mDotRadius, mDotMaxRadius, mDotRadius )
            growAnimator.duration = mAnimationDuration.toLong()

            growAnimator.interpolator = AccelerateDecelerateInterpolator()

            if ( i == mNumberDots ){
                growAnimator.addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        if (shouldAnimate()) {
                            startAnimations()
                        }
                    }
                })
            }

            growAnimator.startDelay = startDelay

            mAnimations.add(growAnimator); // 각각의 순서에 맞게 애니메이션 객체를 삽입하는 것같다.

            if( mAnimateColors ){
                val colorAnimator : ValueAnimator =  ValueAnimator.ofInt(mDotEndColor,mDotColor)
                colorAnimator.duration = mAnimationDuration.toLong()
                colorAnimator.setEvaluator( ArgbEvaluator() )
                colorAnimator.addUpdateListener {
                    valueAnimator: ValueAnimator? ->
                        dot.setColor( valueAnimator!!.animatedValue as Int ) // 문제의 여지가 많아보인다 .. 히익.. 이게 돌아갈까 ?
                }

                if( i == mNumberDots ){
                    colorAnimator.addListener(
                            object : AnimatorListenerAdapter(){
                                override fun onAnimationEnd(animation: Animator?) {
                                   if( shouldAnimate()){
                                       startAnimations();
                                   }
                                }
                            }
                    )
                }

                colorAnimator.startDelay = startDelay

                mAnimations.add(colorAnimator)
            }
            i = i + 1
        }
    }


    fun updateDots(){
        if( mDotRadius <= 0 ){
            mDotRadius = this.height / 2 / mDotScaleMultiplier

            var left : Int = (mDotMaxRadius - mDotRadius).toInt()
            var right : Int = (left + mDotRadius *2 ).toInt() + 2
            var top : Int = 0
            var bottom : Int = mDotMaxRadius.toInt() * 2 + 2

            var i : Int = 0

//            for ( dot in mDrawables ){
//                dot.radius =mDotRadius
//                dot.setBounds(left,top,right,bottom)
//
//            }
            while ( i < mDrawables.size ){
                val dot = mDrawables.get( i )
                dot.radius =mDotRadius
                dot.setBounds(left,top,right,bottom)

                val growAnimator = mAnimations.get( i ) as ValueAnimator
                growAnimator.setFloatValues( mDotRadius , mDotRadius * mDotScaleMultiplier, mDotRadius )

                left += mWidthBetweenDotCenters
                right += mWidthBetweenDotCenters
                i = i+ 1
            }

        }

    }



    protected fun startAnimations(){
        mShouldAnimate = true
        for( anim in mAnimations ){
            anim.start()
        }
    }

    protected fun stopAnimaion(){
        mShouldAnimate = false
        removeCallbacks()
        for( anim in mAnimations ){
            anim.cancel()
        }
    }
    private fun setupDots(){
        initDots()
        updateDots()
        showNow()
    }


    /**
     * getter and Setter
     */

    fun setDotRadius( radius : Float ){
        reset()
        mDotRadius = radius
        calculateMaxRadius()
        calculateWidthBetweenDotCenters()
        setupDots()
    }


    fun setDotSpacing(horizontalSpacing: Float) {
        reset()
        mHorizontalSpacing = horizontalSpacing
        calculateWidthBetweenDotCenters()
        setupDots()
    }

    fun setGrowthSpeed(growthSpeed: Int) {
        reset()
        mAnimationDuration = growthSpeed
        setupDots()
    }

    fun setNumberOfDots(numDots: Int) {
        reset()
        mNumberDots = numDots
        setupDots()
    }

    fun setDotScaleMultiplier(multiplier: Float) {
        reset()
        mDotScaleMultiplier = multiplier
        calculateMaxRadius()
        setupDots()
    }

    fun setDotColor(color: Int) {
        if (color != mDotColor) {
            if (mAnimateColors) {
                // Cancel previous animations
                reset()
                mDotColor = color
                mDotEndColor = color
                mAnimateColors = false

                setupDots()

            } else {
                mDotColor = color
                for (dot in mDrawables) {
                    dot.setColor(mDotColor)
                }
            }
        }
    }

    /**
     * Set different start and end colors for dots. This will result in gradient behaviour. In case you want set 1 solid
     * color - use [.setDotColor] instead

     * @param startColor starting color of the dot
     * *
     * @param endColor   ending color of the dot
     */
    fun setDotColors(@ColorInt startColor: Int, @ColorInt endColor: Int) {
        if (mDotColor != startColor || mDotEndColor != endColor) {
            if (mAnimateColors) {
                reset()
            }
            mDotColor = startColor
            mDotEndColor = endColor

            mAnimateColors = mDotColor != mDotEndColor

            setupDots()
        }
    }


    fun getDotGrowthSpeed(): Int {
        return mAnimationDuration
    }

    fun getDotRadius(): Float {
        return mDotRadius
    }

    fun getHorizontalSpacing(): Float {
        return mHorizontalSpacing
    }

    fun getNumberOfDots(): Int {
        return mNumberDots
    }

    fun getDotScaleMultiplier(): Float {
        return mDotScaleMultiplier
    }

}