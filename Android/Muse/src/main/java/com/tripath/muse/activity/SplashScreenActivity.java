/*
 *  SplashScreenActivity.java 1.0.0 2017/10/23
 *
 * Copyright (c) 2017 RyoRyeong KIM
 * All rights reserved
 *
 * this software is the confidential and proprietary information to Ryo Ryeong KIM
 * you shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with RyoRyeong KiM
 */

package com.tripath.muse.activity;

import android.content.Intent;
import android.os.Bundle;

import com.tripath.muse.MainActivity;
import com.tripath.muse.R;

/**
 * 진입화면
 *
 * @version  1.0.0 23 Oct 2017
 * @author Kim RyoRyeong
 */
public class SplashScreenActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash_screen);

        baseHandler.postDelayed(
                new Runnable() {
                    @Override
                    public void run() {

                        Intent intent = new Intent(SplashScreenActivity.this , MainActivity.class);
                        intent.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
                        overridePendingTransition( 0, android.R.anim.slide_out_right );
                        startActivity(intent);

                        finish();
                    }
                },
                1000
        );
    }

}
