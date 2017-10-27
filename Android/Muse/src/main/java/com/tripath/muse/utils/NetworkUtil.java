
/*
 *  NetworkUtil.java 1.0.0 2017/10/23
 *
 * Copyright (c) 2017 RyoRyeong KIM
 * All rights reserved
 *
 * this software is the confidential and proprietary information to Ryo Ryeong KIM
 * you shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with RyoRyeong KiM
 */
package com.tripath.muse.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


/**
 * 네트워크 관련된 유틸.
 */

public class NetworkUtil {

    /**
     * 와이파이나, 데이터가 연결되어있음을 확인하는 메소드
     * @param context
     * @return 네트워크의 연결 유/무
     */
    public static boolean isNetworkOn( Context context){

        boolean isNetworkOn = false;

        ConnectivityManager connectivityManager  = ( ConnectivityManager ) context.getSystemService( Context.CONNECTIVITY_SERVICE );

        NetworkInfo wifi = connectivityManager.getNetworkInfo( ConnectivityManager.TYPE_WIFI );
        NetworkInfo lte = connectivityManager.getNetworkInfo( ConnectivityManager.TYPE_MOBILE );

        if ( ( wifi != null && wifi.isConnected() ) || ( lte != null && lte.isConnected() ) ) {
            isNetworkOn = true;
        }

        return isNetworkOn;
    }
}
