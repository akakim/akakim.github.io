/*
 *  DilatingDotDrawable.java 1.0.0 2017/10/23
 *
 * Copyright (c) 2017 RyoRyeong KIM
 * All rights reserved
 *
 * this software is the confidential and proprietary information to Ryo Ryeong KIM
 * you shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with RyoRyeong KiM
 */
package com.tripath.muse.widget;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

/**
 * 공통 다이얼로그의 ... 중 . 하나의 Drawable class
 */

public class DilatingDotDrawable extends Drawable {

    private static final String TAG = DilatingDotDrawable.class.getSimpleName();
    /** 원을 그릴 객체 */
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    /** 반지름  */
    private float radius;

    /** 최대 반지름  */
    private float maxRadius;

    /** 원이 들어갈 사각형 애니메이션 효과시 범위가 증가되었다가 줄어듬 */
    final Rect dirtyBounds = new Rect(0, 0, 0, 0);


    public DilatingDotDrawable(final int color, final float radius, final float maxRadius) {

        paint.setColor(color);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeJoin(Paint.Join.ROUND);
        this.radius = radius;
        setMaxRadius(maxRadius);

    }

    public void setMaxRadius(final float maxRadius) {

        this.maxRadius = maxRadius;
        dirtyBounds.bottom = (int) (maxRadius * 2) + 2;
        dirtyBounds.right = (int) (maxRadius * 2) + 2;
    }

    @Override
    public void draw(Canvas canvas) {

        final Rect bounds = getBounds();
        canvas.drawCircle(bounds.centerX(), bounds.centerY(), radius - 1, paint);
    }

    @Override
    public void setAlpha(int alpha) {

        if (alpha != paint.getAlpha()) {
            paint.setAlpha(alpha);
            invalidateSelf();
        }
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {

        paint.setColorFilter(colorFilter);
        invalidateSelf();
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    public void setColor(int color) {

        paint.setColor(color);
        invalidateSelf();
    }

    public void setRadius(float radius) {

        this.radius = radius;
        invalidateSelf();
    }

    public float getRadius() {
        return radius;
    }

    @Override
    public int getIntrinsicWidth() {
        return (int) (maxRadius * 2) + 2;
    }

    @Override
    public int getIntrinsicHeight() {
        return (int) (maxRadius * 2) + 2;
    }

    @Override
    public Rect getDirtyBounds() {
        return dirtyBounds;
    }

    @Override
    protected void onBoundsChange(final Rect bounds) {
        super.onBoundsChange(bounds);

        dirtyBounds.offsetTo(bounds.left, bounds.top);
    }


}
