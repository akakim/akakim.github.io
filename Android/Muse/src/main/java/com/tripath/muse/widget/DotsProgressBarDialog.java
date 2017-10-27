/*
 *  DotsProgressBarDialog.java 1.0.0 2017/10/23
 *
 * Copyright (c) 2017 RyoRyeong KIM
 * All rights reserved
 *
 * this software is the confidential and proprietary information to Ryo Ryeong KIM
 * you shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with RyoRyeong KiM
 */

package com.tripath.muse.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Window;

import com.tripath.muse.R;

/**
 * 공통 다이얼로그 ...으로 나온다.
 */
public class DotsProgressBarDialog extends Dialog {
    /** 공통 다이얼로그를 그린 view */
    DilatingDotsProgressBar dilatingProgressBar = null;

    public DotsProgressBarDialog(@NonNull Context context) {
        super(context);

        requestWindowFeature( Window.FEATURE_NO_TITLE);

        setContentView(R.layout.progress_bar);

        dilatingProgressBar = (DilatingDotsProgressBar )findViewById( R.id.dilatingProgressBar);

        getWindow().setBackgroundDrawable( new ColorDrawable( Color.TRANSPARENT ));
        setCancelable(false);

    }



    @Override
    public void show() {
        super.show();

        if(dilatingProgressBar != null){
            Log.d(getClass().getSimpleName(),"progressbar is showing...... ");
            dilatingProgressBar.showNow();
        }else {
            Log.e(getClass().getSimpleName(),"progressbar is not!! shown");
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();

        if(dilatingProgressBar != null){
            dilatingProgressBar.hide();
        }
    }

    @Override
    public boolean isShowing() {
        return super.isShowing();
    }

    @Override
    public void setOnCancelListener(@Nullable OnCancelListener listener) {
        super.setOnCancelListener(listener);
    }
}
