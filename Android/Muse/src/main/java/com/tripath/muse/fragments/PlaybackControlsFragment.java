/*
 *  PlaybackControlsFragment.java 1.0.0 2017/10/23
 *
 * Copyright (c) 2017 RyoRyeong KIM
 * All rights reserved
 *
 * this software is the confidential and proprietary information to Ryo Ryeong KIM
 * you shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with RyoRyeong KiM
 */

package com.tripath.muse.fragments;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tripath.muse.R;
import com.tripath.muse.service.MusicService;

/**
 * 메인 화면에 하단에 나온 뮤직플레이어 컨트롤러
 */
public class PlaybackControlsFragment extends Fragment implements View.OnClickListener{


    private OnFragmentInteractionListener mListener;

    private ImageView imageViewAlbumArt;
    private ImageButton buttonPlayAndPause;
    private ImageButton buttonFastWind;
    private TextView textViewTitle;
    private TextView textViewSubTitle;

    boolean isPlaying = false;
    public PlaybackControlsFragment() {
        // Required empty public constructor
    }

    public static PlaybackControlsFragment newInstance(String param1, String param2) {

        PlaybackControlsFragment fragment = new PlaybackControlsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_layout_controls, container, false);

        imageViewAlbumArt   = ( ImageView )     view.findViewById( R.id.album_art);
        buttonPlayAndPause  = ( ImageButton )   view.findViewById(R.id.play_pause);
        buttonFastWind      = ( ImageButton )   view.findViewById(R.id.fastWind);
        textViewTitle       = ( TextView )      view.findViewById(R.id.title);
        textViewSubTitle    = ( TextView )      view.findViewById(R.id.artistAndPublish);

        buttonPlayAndPause.setOnClickListener(this);
        buttonFastWind.setOnClickListener( this );
        textViewTitle.setSelected(true);                                                            /** title Text의 애니메이션 효과를 주기 함*/

        view.findViewById(R.id.layoutPlayPause).setOnClickListener( this );
        view.findViewById(R.id.layoutFastWind).setOnClickListener( this );

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;

        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");

        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        mListener = null;

    }

    /**
     * @param title 선택된 음악의 제목
     * @param subTitle 선택된 음악의 부 제목
     */
    public void setItemInfo(String title,String subTitle){

        textViewTitle.setText(title);
        textViewSubTitle.setText(subTitle);
    }

    /**
     * @return 현재 재생 상태값
     */
    public boolean getStatus(){
        return this.isPlaying;
    }

    /**
     *  다음에 될 수 있는 상태가 어떤 것인지 UI 설정
     *  현재 재생 중 이라면, 다음상태는 일시정지가 될 수 있다.
     *  현재 일시정지 상태이면, 다음상태는 재생이 될 수 있다.
     * @param isPlaying
     */
    public void setStatus(final boolean isPlaying){

        this.isPlaying = isPlaying;

        if (isPlaying) {
            buttonPlayAndPause.setBackground(
                    ContextCompat.getDrawable(getActivity(), R.drawable.pause)
            );

        } else {
            buttonPlayAndPause.setBackground(
                    ContextCompat.getDrawable(getActivity(), R.drawable.play)
            );
        }
    }

    /**
     * @param drawable 초기화할 그림
     */
    public void setAlbumArt(Drawable drawable){

        imageViewAlbumArt.setImageDrawable( drawable );
    }

    /**
     * 음악 앨범 이미지를 서버에서 불러와서 갱신함 .
     * @param url 음악 앨범 url
     */
    public void setAlbumArt(String url){

        Picasso.with(getContext()).load(url).error(R.drawable.no_image).into(imageViewAlbumArt);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            /** 버튼 하나에만 놓으면 누를때 범위가 너무 작아서 레이아웃 영역도 같이 잡음 */
            case R.id.fastWind:
            case R.id.layoutFastWind:
                mListener.onFragmentInteraction( MusicService.NEXTWIND);
                break;
            /** 버튼 하나에만 놓으면 누를때 범위가 너무 작아서 레이아웃 영역도 같이 잡음 */
            case R.id.play_pause:
            case R.id.layoutPlayPause:
                mListener.onFragmentInteraction(MusicService.PLAY_AND_PAUSE);
                break;
            default:
                break;
        }
    }


    /**
     * 버튼이 눌려지면 Activity로 메세지를 보내는 interface
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(int command);
    }

}
