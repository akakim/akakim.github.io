/*
 *  BaseActivity.java 1.0.0 2017/10/23
 *
 * Copyright (c) 2017 RyoRyeong KIM
 * All rights reserved
 *
 * this software is the confidential and proprietary information to Ryo Ryeong KIM
 * you shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with RyoRyeong KiM
 */

package com.tripath.muse.activity;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;


/**
 * 모든 Activity가 상속받는 class
 *
 * @version  1.0.0 23 Oct 2017
 * @author Kim RyoRyeong
 */
public class BaseActivity extends AppCompatActivity{

    /** 공통적으로 사용하는 핸들러 */
    protected Handler baseHandler;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = getWindow().getDecorView();
        baseHandler = new Handler();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {                                       /** Marshmallow 이상에서 statusbar icon 색처리 */
            if (view != null) {
                view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        }



    }

    @Override
    protected void onDestroy() {

        if (baseHandler != null){
            baseHandler = null;
        }

        super.onDestroy();
    }
}
