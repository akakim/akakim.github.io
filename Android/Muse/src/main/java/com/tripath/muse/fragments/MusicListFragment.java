/*
 *  MusicListFragment.java 1.0.0 2017/10/23
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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tripath.muse.R;
import com.tripath.muse.adapter.MainListViewAdapter;
import com.tripath.muse.network.model.MusicItemModel;
import com.tripath.muse.service.MusicService;
import com.tripath.muse.uiInterface.AdapterToFragmentInterface;
import com.tripath.muse.views.SwipeRefreshLayoutBottom;

import java.util.ArrayList;
import java.util.List;

/**
 * 음악 목록을 보여주는 Fragment
 *
 * @version  1.0.0 23 Oct 2017
 * @author Kim RyoRyeong
 */
public class MusicListFragment extends Fragment implements AdapterToFragmentInterface,SwipeRefreshLayoutBottom.OnRefreshListener {

    final String                            TAG = getClass().getSimpleName();
    /** 음악 목록이 들어가는 RecyclerView */
    RecyclerView                            musicList;

    /** 음악 목록이 들어가는 RecyclerView와 연결할 adapter */
    MainListViewAdapter                     musicAdapter;

    /** adapter에 들어갈 list */
    List<MusicItemModel>                    musicItemModelList = new ArrayList<>();

    /** RecyclerView의 부모 view list의 페이징처리를 위해 넣음 */
    SwipeRefreshLayoutBottom                musicContainer;
    LinearLayoutManager                     layoutManager;

    /** fragment -> activity 의 인터페이스 */
    private OnFragmentInteractionListener   mListener;



    public MusicListFragment() {

    }

    public static MusicListFragment newInstance(String param1, String param2) {

        MusicListFragment fragment = new MusicListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view       = inflater.inflate(R.layout.fragment_music_list, container, false);

        musicContainer  =  ( SwipeRefreshLayoutBottom ) view.findViewById( R.id.musicContainer );
        musicList       =  ( RecyclerView )             view.findViewById( R.id.musicList );

        musicContainer.setOnRefreshListener(this);
        musicContainer.setColorSchemeResources(R.color.selected_text);

        layoutManager =  new LinearLayoutManager(getContext());
        musicList.setLayoutManager( layoutManager );

        musicAdapter = new MainListViewAdapter(musicItemModelList,getContext(),this);

        musicList.setAdapter( musicAdapter );

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
     * 갱신 여부를 view로 보내어 view의 상태를 변경한다.
     * @param isRefreshing 갱신 여부
     */
    public void setRefreshing(boolean isRefreshing){

        musicContainer.setRefreshing( isRefreshing );
    }


    /**
     * 음악 항목의 리스트를 갱신한다.
     * @param itemList
     */
    public void setListItem(List itemList){

        musicItemModelList.clear();
        musicItemModelList.addAll(itemList);
        musicAdapter.notifyDataSetChanged();
    }

    /**
     * 음악 항목의 리스트를 더한다.
     * @param itemList
     */
    public void addListItem(List itemList){

        musicItemModelList.addAll(itemList);
        musicAdapter.notifyDataSetChanged();
    }


    /**
     * 새로운 아이템이 생성된 경우 list를 자동 스크롤 시킨다 .
     * 단, 애니메이션 효과는 없다 .
     * @param index
     */
    public void scrollSelf(int index){

        if( musicItemModelList.size() >= 0 ) {
            layoutManager.scrollToPositionWithOffset(index, 0);
        }
    }

    public void onRefreshComplete() {

        musicContainer.setRefreshing( false );
    }

    /**
     * 선택된 아이템을 초기화 시킨다.
     */
    public void initializeSelection(){

        musicAdapter.initializeSelection();
    }

    /**
     * RecyclerView에서 각 아이템은 화면에 보이는것 +1개정도가 바인딩되어, 이용할 수 있다.
     * 만약 화면에서 벗어나게 된다면, 정상적으로 UI가 업데이트가 되지않으므로
     * 아랫쪽으로 이동 후 뷰가 업데이트가 되도록 했다.
     * @param position
     */
    public void invalidateNextItem( final int position ){

        if ( musicList.findViewHolderForAdapterPosition( position ) != null){
            MainListViewAdapter musicAdapter = (MainListViewAdapter)musicList.getAdapter();
            musicAdapter.exclusiveSelection(position);

        }
        else {

            if( musicList.getChildCount() != position + 1 ) {                                       /** indexOutOfRange 방지 */
                musicList.smoothScrollToPosition(position + 1);
                MainListViewAdapter musicAdapter = (MainListViewAdapter) musicList.getAdapter();
                musicAdapter.exclusiveSelection(position);

            }
        }
    }

    /**
     * 데이터 갱신 실패시 에러 인터페이스
     */
    @Override
    public void onRefresh() {

        if(mListener!=null) {
            mListener.onFragmentItemRefresh();

        } else {
            mListener.onFragmentErrorOccured("데이터를 갱신하는데 실패했습니다.");

        }
    }


    /**
     * list item -> activity로 무엇이 클릭됬는지 정보를 보내는 메소드
     * @param position activity에서 service로 보낼 메세지에 들어가는 값 .
     */
    @Override
    public void onItemClicked(int position) {

        mListener.onFragmentInteraction(MusicService.STOP_AND_PLAY,position);
    }


    /**
     * fragment -> activity로 전달하는 인터페이스
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(int command,int position);                                       /** 선택된 아이템 재생 */
        void onFragmentItemRefresh();                                                               /** 음악 목록 갱신 */
        void onFragmentErrorOccured(String message);                                                /** 에러 발생시 전달. */
    }

}
