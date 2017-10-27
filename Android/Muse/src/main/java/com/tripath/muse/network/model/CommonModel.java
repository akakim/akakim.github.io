/*
 *  CommonModel.java 1.0.0 2017/10/23
 *
 * Copyright (c) 2017 RyoRyeong KIM
 * All rights reserved
 *
 * this software is the confidential and proprietary information to Ryo Ryeong KIM
 * you shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with RyoRyeong KiM
 */
package com.tripath.muse.network.model;

import com.tripath.muse.common.Constants;

import org.json.JSONObject;

/**
 * 전문 공통 모델 class
 */
public class CommonModel {

    String resultCode;                                                                              /** 서버 응답코드 */
    String errorMesage;                                                                             /** 에러 메세지   */
    JSONObject responseObject;                                                                      /** 응답 객체     */

    public void setParameter(String data ) throws Exception{

        responseObject = new JSONObject(data);
        resultCode= responseObject.getString("resultCode");
        errorMesage = responseObject.getString("errorMesage");

        Exception e;

        switch (resultCode){
            case Constants.CODE_SYSTEM_ERROR:
                e = new Exception(Constants.ERRMSG_SYSTEM_EXCEPTIONS);
                throw  e;
            case Constants.CODE_INVFMT_ERROR:
                e = new Exception(Constants.ERRMSG_INVFMT_GCODE);
                throw  e;
            case Constants.CODE_SVCEXT_ERROR:
                e= new Exception(Constants.ERRMSG_SVCEXT_DATA_NFD);
                throw  e;
            default:
                e = new Exception();
                throw  e;
        }
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getErrorMesage() {
        return errorMesage;
    }

    public void setErrorMesage(String errorMesage) {
        this.errorMesage = errorMesage;
    }
}
