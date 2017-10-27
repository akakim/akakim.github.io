/*
 *  DilatingDotsProgressBar.java 1.0.0 2017/10/23
 *
 * Copyright (c) 2017 RyoRyeong KIM
 * All rights reserved
 *
 * this software is the confidential and proprietary information to Ryo Ryeong KIM
 * you shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with RyoRyeong KiM
 */

package com.tripath.muse.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.tripath.muse.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 공통으로 보여주는 view DilatingDotDrawable 
 */

public class DilatingDotsProgressBar extends View {

    public static final String TAG = DilatingDotsProgressBar.class.getSimpleName();
    public static final double START_DELAY_FACTOR = 0.35;
    private static final float DEFAULT_GROWTH_MULTIPLIER = 1.75f;
    private static final int MIN_SHOW_TIME = 100; // ms
    private static final int MIN_DELAY = 100; // ms

    private int dotColor;
    private int dotEndColor;
    private int animationDuration;
    private int widthBetweenDotCenters;
    private int numberDots;
    private float dotRadius;
    private float dotScaleMultiplier;
    private float dotMaxRadius;
    private float horizontalSpacing;
    private long startTime = -1;
    private boolean shouldAnimate;
    private boolean dismissed = false;
    private boolean isRunning = false;
    private boolean animateColors = false;

    /** ... 원형을 모아놓은 List */
    private ArrayList<DilatingDotDrawable> drawables = new ArrayList<>();

    /** 각각의 애니메이션 객체 */
    private final List<Animator> animations = new ArrayList<>();

    Drawable defaultBackGround;

    /** delayed runnable to stop the progress */
    private final Runnable mDelayedHide = new Runnable() {
        @Override
        public void run() {

            startTime = -1;
            isRunning = false;
            setVisibility(View.GONE);
            stopAnimations();
        }
    };

    /** delayed runnable to start the progress */
    private final Runnable mDelayedShow = new Runnable() {
        @Override
        public void run() {

            if (!dismissed) {
                startTime = System.currentTimeMillis();
                setVisibility(View.VISIBLE);
                startAnimations();
            }
        }
    };

    public DilatingDotsProgressBar(Context context) {
        this(context, null);
    }

    public DilatingDotsProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DilatingDotsProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {

        defaultBackGround = getRootView().getBackground();
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.DilatingDotsProgressBar);
        numberDots = a.getInt(R.styleable.DilatingDotsProgressBar_dd_numDots, 3);
        dotRadius = a.getDimension(R.styleable.DilatingDotsProgressBar_android_radius, 8);
        dotColor = a.getColor(R.styleable.DilatingDotsProgressBar_android_color, 0xff9c27b0);
        dotEndColor = a.getColor(R.styleable.DilatingDotsProgressBar_dd_endColor, dotColor);
        dotScaleMultiplier = a.getFloat(R.styleable.DilatingDotsProgressBar_dd_scaleMultiplier, DEFAULT_GROWTH_MULTIPLIER);
        animationDuration = a.getInt(R.styleable.DilatingDotsProgressBar_dd_animationDuration, 300);
        horizontalSpacing = a.getDimension(R.styleable.DilatingDotsProgressBar_dd_horizontalSpacing, 12);
        a.recycle();

        shouldAnimate = false;
        animateColors = dotColor != dotEndColor;
        calculateMaxRadius();
        calculateWidthBetweenDotCenters();

        initDots();
        updateDots();
    }

    @Override
    protected void onSizeChanged(final int w, final int h, final int oldw, final int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        if (computeMaxHeight() != h || w != computeMaxWidth()) {
            updateDots();
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        removeCallbacks();
    }

    private void removeCallbacks() {

        removeCallbacks(mDelayedHide);
        removeCallbacks(mDelayedShow);
    }

    public void reset() {
        hideNow();
    }

    /**
     * Hide the progress view if it is visible. The progress view will not be
     * hidden until it has been shown for at least a minimum show time. If the
     * progress view was not yet visible, cancels showing the progress view.
     */
    @SuppressWarnings ("unused")
    public void hide() {
        hide(MIN_SHOW_TIME);
    }

    public void hide(int delay) {

        dismissed = true;
        removeCallbacks(mDelayedShow);
        long diff = System.currentTimeMillis() - startTime;
        if ((diff >= delay) || (startTime == -1)) {
            mDelayedHide.run();
        } else {
            if ((delay - diff) <= 0) {
                mDelayedHide.run();
            } else {
                postDelayed(mDelayedHide, delay - diff);
            }
        }
    }

    /**
     * Show the progress view after waiting for a minimum delay. If
     * during that time, hide() is called, the view is never made visible.
     */
    @SuppressWarnings ("unused")
    public void show() {

        show(MIN_DELAY);
    }

    @SuppressWarnings ("unused")
    public void showNow() {
        show(0);
    }

    @SuppressWarnings ("unused")
    public void hideNow() {
        hide(0);
    }

    public boolean isHide(){
        return dismissed;
    }
    public void show(int delay) {

        if (isRunning) {
            return;
        }

        isRunning = true;

        startTime = -1;
        dismissed = false;
        removeCallbacks(mDelayedHide);


        if (delay == 0) {
            mDelayedShow.run();
        } else {
            postDelayed(mDelayedShow, delay);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {

        if (shouldAnimate()) {
            for (DilatingDotDrawable dot : drawables) {
                dot.draw(canvas);
            }
        }
    }

    @Override
    protected boolean verifyDrawable(final Drawable who) {

        if (shouldAnimate()) {
            return drawables.contains(who);
        }
        return super.verifyDrawable(who);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        setMeasuredDimension((int) computeMaxWidth(), (int) computeMaxHeight());
    }

    private float computeMaxHeight() {
        return dotMaxRadius * 2;
    }

    private float computeMaxWidth() {
        return computeWidth() + ((dotMaxRadius - dotRadius) * 2);
    }

    private float computeWidth() {
        return (((dotRadius * 2) + horizontalSpacing) * drawables.size()) - horizontalSpacing;
    }

    private void calculateMaxRadius() {
        dotMaxRadius = dotRadius * dotScaleMultiplier;
    }

    private void calculateWidthBetweenDotCenters() {
        widthBetweenDotCenters = (int) (dotRadius * 2) + (int) horizontalSpacing;
    }

    private void initDots() {

        drawables.clear();
        animations.clear();
        for (int i = 1; i <= numberDots; i++) {
            final DilatingDotDrawable dot = new DilatingDotDrawable(dotColor, dotRadius, dotMaxRadius);
            dot.setCallback(this);
            drawables.add(dot);

            final long startDelay = (i - 1) * (int) (START_DELAY_FACTOR * animationDuration);

            // Sizing
            ValueAnimator growAnimator = ObjectAnimator.ofFloat(dot, "radius", dotRadius, dotMaxRadius, dotRadius);
            growAnimator.setDuration(animationDuration);
            growAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
            if (i == numberDots) {
                growAnimator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if (shouldAnimate()) {
                            startAnimations();
                        }
                    }
                });
            }
            growAnimator.setStartDelay(startDelay);

            animations.add(growAnimator);

            if (animateColors) {
                // Gradient
                ValueAnimator colorAnimator = ValueAnimator.ofInt(dotEndColor, dotColor);
                colorAnimator.setDuration(animationDuration);
                colorAnimator.setEvaluator(new ArgbEvaluator());
                colorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                    @Override
                    public void onAnimationUpdate(ValueAnimator animator) {
                        dot.setColor((int) animator.getAnimatedValue());
                    }

                });
                if (i == numberDots) {
                    colorAnimator.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            if (shouldAnimate()) {
                                startAnimations();
                            }
                        }
                    });
                }
                colorAnimator.setStartDelay(startDelay);

                animations.add(colorAnimator);
            }
        }
    }

    private void updateDots() {

        if (dotRadius <= 0) {
            dotRadius = getHeight() / 2 / dotScaleMultiplier;
        }

        int left = (int) (dotMaxRadius - dotRadius);
        int right = (int) (left + dotRadius * 2) + 2;
        int top = 0;
        int bottom = (int) (dotMaxRadius * 2) + 2;

        for (int i = 0; i < drawables.size(); i++) {
            final DilatingDotDrawable dot = drawables.get(i);
            dot.setRadius(dotRadius);
            dot.setBounds(left, top, right, bottom);
            ValueAnimator growAnimator = (ValueAnimator) animations.get(i);
            growAnimator.setFloatValues(dotRadius, dotRadius * dotScaleMultiplier, dotRadius);

            left += widthBetweenDotCenters;
            right += widthBetweenDotCenters;
        }
    }

    protected void startAnimations() {

        shouldAnimate = true;
        for (Animator anim : animations) {
            anim.start();
        }
    }

    protected void stopAnimations() {

        shouldAnimate = false;
        removeCallbacks();
        for (Animator anim : animations) {
            anim.cancel();
        }
    }

    protected boolean shouldAnimate() {
        return shouldAnimate;
    }

    public void setDotRadius(float radius) {

        reset();
        dotRadius = radius;
        calculateMaxRadius();
        calculateWidthBetweenDotCenters();
        setupDots();
    }

    public void setDotSpacing(float horizontalSpacing) {

        reset();
        this.horizontalSpacing = horizontalSpacing;
        calculateWidthBetweenDotCenters();
        setupDots();
    }

    public void setGrowthSpeed(int growthSpeed) {

        reset();
        animationDuration = growthSpeed;
        setupDots();
    }

    public void setNumberOfDots(int numDots) {

        reset();
        numberDots = numDots;
        setupDots();
    }

    public void setDotScaleMultiplier(float multiplier) {

        reset();
        dotScaleMultiplier = multiplier;
        calculateMaxRadius();
        setupDots();
    }

    public void setDotColor(int color) {
        if (color != dotColor) {
            if (animateColors) {
                // Cancel previous animations
                reset();
                dotColor = color;
                dotEndColor = color;
                animateColors = false;

                setupDots();

            } else {
                dotColor = color;
                for (DilatingDotDrawable dot : drawables) {
                    dot.setColor(dotColor);
                }
            }
        }
    }

    /**
     * Set different start and end colors for dots. This will result in gradient behaviour. In case you want set 1 solid
     * color - use {@link #setDotColor(int)} instead
     *
     * @param startColor starting color of the dot
     * @param endColor   ending color of the dot
     */
    public void setDotColors(@ColorInt int startColor, @ColorInt int endColor) {

        if (dotColor != startColor || dotEndColor != endColor) {
            if (animateColors) {
                reset();
            }
            dotColor = startColor;
            dotEndColor = endColor;

            animateColors = dotColor != dotEndColor;

            setupDots();
        }
    }

    private void setupDots() {

        initDots();
        updateDots();
        showNow();
    }

    public int getDotGrowthSpeed() {
        return animationDuration;
    }

    public float getDotRadius() {
        return dotRadius;
    }

    public float getHorizontalSpacing() {
        return horizontalSpacing;
    }

    public int getNumberOfDots() {
        return numberDots;
    }

    public float getDotScaleMultiplier() {
        return dotScaleMultiplier;
    }

    @Override
    public void setBackgroundColor(@ColorInt int color) {
        super.setBackgroundColor(color);
    }
}
