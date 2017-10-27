
/*
 *  GetMusicList.java 1.0.0 2017/10/23
 *
 * Copyright (c) 2017 RyoRyeong KIM
 * All rights reserved
 *
 * this software is the confidential and proprietary information to Ryo Ryeong KIM
 * you shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with RyoRyeong KiM
 */

package com.tripath.muse.network.proto;

import android.content.Context;

import com.squareup.okhttp.HttpUrl;
import com.tripath.muse.common.Constants;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tripath.muse.utils.NetworkUtil;


import java.io.IOException;


/**
 * 음악 항목 을 불러오는 객체 ,
 */

public class GetMusicList {

    final String TAG = "GetMusicList";

    OkHttpClient client;                                                                            /** 통신 모듈  */

    public GetMusicList() {
        this.client = new OkHttpClient();
    }


    /**
     * 서버로 전문요청 하는 메소드
     * @param context           호출하는 context
     * @param url               호출하는 URL
     * @param genreCd           음악 장르 코드
     * @param itemCount         호출할 음악 갯수
     * @param countPerPage      음악 아이템의 페이지 수
     * @return
     * @throws IOException
     */
    public Response requestData(Context context , String url, String genreCd, int itemCount, int countPerPage) throws IOException {

        HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();
        urlBuilder.addQueryParameter("genreCd",genreCd);
        urlBuilder.addQueryParameter("itemCount",String.valueOf(itemCount));
        urlBuilder.addQueryParameter("startIndex",String.valueOf(countPerPage));

//        Log.d(TAG,"url parameter : " + urlBuilder.build().toString());
        if(NetworkUtil.isNetworkOn(context)) {

            Request request = new Request.Builder().
                                    url( urlBuilder.build().toString() ).
                                    get().
                                    build();

            Response response = client.newCall(request).execute();
//            Log.d("response codee ",response.code() +"" );
            return response;
        }else {

            IOException ioException = new IOException(Constants.NETWORK_NOT_CONNECTED_ERROR);
            throw ioException;
        }

    }

}
