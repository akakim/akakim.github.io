/*
 *  Constants.java 1.0.0 2017/10/23
 *
 * Copyright (c) 2017 RyoRyeong KIM
 * All rights reserved
 *
 * this software is the confidential and proprietary information to Ryo Ryeong KIM
 * you shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with RyoRyeong KiM
 */

package com.tripath.muse.common;


/**
 * 공통으로 사용하는 상수값
 *
 * @version  1.0.0 23 Oct 2017
 * @author Kim RyoRyeong
 */

public interface Constants {

    int SYSTEM_NETWORK_SETTING = 105;

    String SERVER_URL               = "http://113.30.102.135:8090/apis/v1";                         /** 기본 api server URL                       */

    String GET_MUSIC_LIST           = "/music/list/genre";                                          /** 음악 목록을 불러오는 URL                  */

    String CODE_SYSTEM_ERROR        = "9000";                                                       /** 서버 내부 에러 코드                       */
    String ERRMSG_SYSTEM_EXCEPTIONS = "System Exceptions Error";                                    /** 서버 내부 에러 메세지                     */

    String CODE_INVFMT_ERROR        = "1000";                                                       /** 서버로 요청 전문 에러                     */
    String ERRMSG_SVCEXT_DATA_NFD   = "Data Not Found";                                             /** 서버로 요청 전문 메세지                   */

    String CODE_SVCEXT_ERROR        = "2000";                                                       /** 서버 시스템 에러                           */
    String ERRMSG_INVFMT_GCODE      = "Invalid Genre Code";                                         /** 서버 시스템 에러 메세지                    */

    String HTTP_ERROR_MESSAGE               = "HTTP_ERROR_STATUS";                                  /** Http 통신 에러                             */
    String NETWORK_NOT_CONNECTED_ERROR      = "NETWORK_NOT_CONNECTED_ERROR";                        /** 모바일 기기에 네트워크 연결이 안된 에러    */

    String CODE_JSON_PARSING_ERROR          = "9001";                                               /** json parsing 에러                          */
    String CODE_IO_EXCEPTION_ERROR          = "9002";                                               /** 서버와 연결 실패 에러코드                  */
    String CODE_CONNECTION_REFUSE_ERROR     = "9003";                                               /** 서버의 연결이 거부된 에러코드              */
    String CODE_UNEXPECTED_ERROR            = "9999";                                               /** 예상치못한 에러코드                        */

    String NO_MORE_LIST_ITEM_MESSAGE        ="더 이상 곡이 존재하지 않습니다.";                     /** 음악 목록을 더 불러올수 없는 에러 메세지   */

}
