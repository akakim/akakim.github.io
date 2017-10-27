/*
 *  MusicModel.java 1.0.0 2017/10/23
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 서버로 음악 항목을 요청하는 전문
 */

public class MusicModel extends  CommonModel  {

    List<MusicItemModel> musicModels = new ArrayList<>();                                           /** 음악 항목           */

    String endIndex;                                                                                /** 다음 가리키는 index */
    String nextYn;                                                                                  /** 다음 페이지 유/무   */
    String genreCd;                                                                                 /** 장르 코드           */
    String mainADURL;

    public void setParameter(String data ) throws Exception{
//        Log.d(getClass().getSimpleName(),"response data" + data );
        super.setParameter(data);

        endIndex    = responseObject.getString("endIndex");
        nextYn      = responseObject.getString("nextYn");
        genreCd     = responseObject.getString("genreCd");
        mainADURL   = responseObject.getString("mainADURL");
            if (!responseObject.isNull("music")) {
                JSONArray jsonArray = responseObject.getJSONArray("music");
                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    MusicItemModel musicItemModel = new MusicItemModel();

                    musicItemModel.setMuseSeq(jsonObject.getString("museSeq"));
                    musicItemModel.setGenreCd(jsonObject.getString("genreCd"));
                    musicItemModel.setLabel1Cd(jsonObject.getString("label1Cd"));
                    musicItemModel.setLabel2Cd(jsonObject.getString("label2Cd"));
                    musicItemModel.setMuseNo(jsonObject.getString("museNo"));
                    musicItemModel.setTitle(jsonObject.getString("title"));
                    musicItemModel.setAlbumSeq(jsonObject.getString("albumSeq"));
                    musicItemModel.setSource(jsonObject.getString("source"));
                    musicItemModel.setArtist(jsonObject.getString("artist"));
                    musicItemModel.setImgPath(jsonObject.getString("imgPath"));
                    musicItemModel.setTrackTotCnt(jsonObject.getString("trackTotCnt"));
                    musicItemModel.setDuration(jsonObject.getString("duration"));
                    musicItemModel.setPubDate(jsonObject.getString("pubDate"));
                    musicItemModel.setRegdateDt(jsonObject.getString("regdateDt"));
                    musicItemModel.setUpdateDt(jsonObject.getString("updateDt"));
                    musicItemModel.setSite(jsonObject.getString("site"));

                    musicModels.add(musicItemModel);

                }
            } else {
                JSONException jsonException = new JSONException("서버의 데이터 음악 목록을 불러오기를 실패했습니다");

                setResultCode( Constants.CODE_JSON_PARSING_ERROR );
                throw jsonException;
            }
    }

    public List<MusicItemModel> getMusicModels() {
        return musicModels;
    }

    public void setMusicModels(List<MusicItemModel> musicModels) {
        this.musicModels = musicModels;
    }

    public String getEndIndex() {
        return endIndex;
    }

    public void setEndIndex(String endIndex) {
        this.endIndex = endIndex;
    }

    public String getNextYn() {
        return nextYn;
    }

    public void setNextYn(String nextYn) {
        this.nextYn = nextYn;
    }

    public String getGenreCd() {
        return genreCd;
    }

    public void setGenreCd(String genreCd) {
        this.genreCd = genreCd;
    }

    public String getMainADURL() {
        return mainADURL;
    }

    public void setMainADURL(String mainADURL) {
        this.mainADURL = mainADURL;
    }
}
