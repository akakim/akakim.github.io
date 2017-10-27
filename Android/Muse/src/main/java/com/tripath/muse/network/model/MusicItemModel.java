/*
 *  MusicItemModel.java 1.0.0 2017/10/23
 *
 * Copyright (c) 2017 RyoRyeong KIM
 * All rights reserved
 *
 * this software is the confidential and proprietary information to Ryo Ryeong KIM
 * you shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with RyoRyeong KiM
 */
package com.tripath.muse.network.model;

/**
 * 음악 항목
 */
public class MusicItemModel {

    String museSeq;                                                                                 /** 음악 순번   */
    String genreCd;                                                                                 /** 장르 코드   */
    String label1Cd;                                                                                /** 라벨 코드 1 */
    String label2Cd;                                                                                /** 라벨 코드 2 */
    String museNo;                                                                                  /** */
    String title;                                                                                   /** 음악 제목   */
    String albumSeq;                                                                                /** 앨범 순번   */
    String source;                                                                                  /** 음악 파일 명*/
    String artist;                                                                                  /** 가수 명     */
    String site;                                                                                    /** */
    String imgPath;                                                                                 /** 앨범 이미지 경로 */
    String trackTotCnt;                                                                             /** */
    String duration;                                                                                /** */
    String pubDate;                                                                                 /** 배포 날짜   */
    String regdateDt;                                                                               /** 등록 날짜   */
    String updateDt;                                                                                /** 갱신된 날짜 */

    public String getMuseSeq() {
        return museSeq;
    }

    public void setMuseSeq(String museSeq) {
        this.museSeq = museSeq;
    }

    public String getGenreCd() {
        return genreCd;
    }

    public void setGenreCd(String genreCd) {
        this.genreCd = genreCd;
    }

    public String getLabel1Cd() {
        return label1Cd;
    }

    public void setLabel1Cd(String label1Cd) {
        this.label1Cd = label1Cd;
    }

    public String getLabel2Cd() {
        return label2Cd;
    }

    public void setLabel2Cd(String label2Cd) {
        this.label2Cd = label2Cd;
    }

    public String getMuseNo() {
        return museNo;
    }

    public void setMuseNo(String museNo) {
        this.museNo = museNo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAlbumSeq() {
        return albumSeq;
    }

    public void setAlbumSeq(String albumSeq) {
        this.albumSeq = albumSeq;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getTrackTotCnt() {
        return trackTotCnt;
    }

    public void setTrackTotCnt(String trackTotCnt) {
        this.trackTotCnt = trackTotCnt;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public String getRegdateDt() {
        return regdateDt;
    }

    public void setRegdateDt(String regdateDt) {
        this.regdateDt = regdateDt;
    }

    public String getUpdateDt() {
        return updateDt;
    }

    public void setUpdateDt(String updateDt) {
        this.updateDt = updateDt;
    }


    @Override
    public String toString() {
        return super.toString();
    }
}
