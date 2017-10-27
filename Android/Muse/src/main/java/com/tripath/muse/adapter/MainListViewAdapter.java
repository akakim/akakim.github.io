/*
 *  MainListViewAdapter.java 1.0.0 2017/10/23
 *
 * Copyright (c) 2017 RyoRyeong KIM
 * All rights reserved
 *
 * this software is the confidential and proprietary information to Ryo Ryeong KIM
 * you shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with RyoRyeong KiM
 */

package com.tripath.muse.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.tripath.muse.R;
import com.tripath.muse.network.model.MusicItemModel;
import com.tripath.muse.uiInterface.AdapterToFragmentInterface;

import java.util.Collections;
import java.util.List;

/**
 * 뮤직 리스트 Adapter
 */

public class MainListViewAdapter extends RecyclerView.Adapter<MainListViewAdapter.MainListViewHolder>{

    final String TAG = MainListViewAdapter.class.getSimpleName();

    /** 뮤직 리스트 activity에서 fragment로 전달후 설정된다. */
    List<MusicItemModel> musicItemModels = Collections.emptyList();

    private Context context;

    /** adapter -> fragment 로 전달하는 인터페이스  결과적으로 activity 까지 이동한다. */
    AdapterToFragmentInterface adapterToFragmentInterface;


    /** 현재 list의 아이템이 선택된 위치 */
    int currentSelectedPosition = -1;


    /** list의 아이템이 선택 되었을 때 color 값 */
    int selectedColor = -1;

    /** list의 아이템이 선택 되지않았을 때 color 값 */
    int notSelectedColor= -1;

    public MainListViewAdapter(List<MusicItemModel> musicItemModels, Context context, AdapterToFragmentInterface adapterToFragmentInterface) {

        this.musicItemModels = musicItemModels;
        this.context                    = context;
        this.adapterToFragmentInterface = adapterToFragmentInterface;

        selectedColor                   = context.getResources().getColor(R.color.selected_text) ;
        notSelectedColor                = context.getResources().getColor(R.color.not_selected_text) ;
    }

    @Override
    public MainListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate( R.layout.main_music_list_item,parent,false);

        return new MainListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final MainListViewHolder holder, final int position) {

        if( currentSelectedPosition == -1){                                                         /** 아무것도 선택되지 않는 경우 */
            holder.textViewTitle.setTextColor(notSelectedColor);
            holder.imageViewPlayButton.setBackgroundResource(R.drawable.list_play_off);

        }
        else  {
            if( position == currentSelectedPosition) {                                               /** 선택 된 경우 색을 변경하고 그 이외의 경우는 선택 되지않는색으로 변경  */
                holder.textViewTitle.setTextColor(selectedColor);
                holder.imageViewPlayButton.setBackgroundResource(R.drawable.list_play_on);

            }
            else {
                holder.textViewTitle.setTextColor(notSelectedColor);
                holder.imageViewPlayButton.setBackgroundResource(R.drawable.list_play_off);

            }
        }

        holder.textViewTitle.setText(musicItemModels.get(position).getTitle());
        holder.textViewSubTitle.setText(musicItemModels.get(position).getArtist());


        Picasso.with( context)                                                                      /** 이미지 로딩 라이브러리 , 에러가 난경우 기본 이미지를 설정한다. */
                .load( musicItemModels.get(position).getImgPath())
                .into(holder.imageViewAlbumArt, new Callback() {

                        @Override
                        public void onSuccess() {
                            //musicItemModels.get(position).setMusicAlbumDrawable( holder.imageViewAlbumArt.getDrawable() );
                        }

                        @Override
                        public void onError() {
                            holder.imageViewAlbumArt
                                    .setImageDrawable(ContextCompat.getDrawable(context, R.drawable.no_image));
                        }});


        holder.rootView                                                                             /** 클릭 시 인터페이스를 동작하고, UI를 갱신한다.*/
                .setOnClickListener( new View.OnClickListener(){

                    @Override
                    public void onClick(View v) {
                        if(adapterToFragmentInterface !=null){
                            adapterToFragmentInterface.onItemClicked(position);
                        }

                        exclusiveSelection( position );
                    }
        });

    }

    /**
     * @return 어뎁터의 아이템 갯수
     */
    @Override
    public int getItemCount() {

        return musicItemModels.size();
    }



    /**
     * 어떤 위치를 지정하면 해당 위치가 선택됨을 표시하는 메소드
     *
     * 이전에 선택된게 있었다면, 원래상태로 돌리고 선택된 것은 업데이트
     * @param pos 클릭된 위치
     */
    public void exclusiveSelection(final int pos){

        // 처음 아무것도 선택이 안된경우
        if( currentSelectedPosition == -1 ){
            notifyItemChanged( pos );
            currentSelectedPosition = pos;

        }else {

            if( currentSelectedPosition != pos){
                notifyItemChanged(currentSelectedPosition);
                notifyItemChanged(pos);
                currentSelectedPosition = pos;

            }

        }
    }


    /**
     * 선택 초기화
     */
    public void initializeSelection(){

        if(currentSelectedPosition != -1){

            currentSelectedPosition = -1;
            for(int i = 0; i< getItemCount(); i++){
                notifyItemChanged(i);
            }

        }
    }

    public class MainListViewHolder extends RecyclerView.ViewHolder{

        View        rootView;
        TextView    textViewTitle;
        TextView    textViewSubTitle;
        ImageView   imageViewAlbumArt;
        ImageView   imageViewPlayButton;

        public MainListViewHolder(View itemView) {
            super(itemView);

            rootView            = itemView;
            textViewTitle       = ( TextView ) itemView.findViewById( R.id.textViewTitle);
            imageViewAlbumArt   = ( ImageView ) itemView.findViewById( R.id.imageViewAlbumArt);
            textViewSubTitle    = ( TextView ) itemView.findViewById( R.id.textViewSubTitle);
            imageViewPlayButton = ( ImageView ) itemView.findViewById( R.id.imageViewPlayButton);

        }

    }
}
