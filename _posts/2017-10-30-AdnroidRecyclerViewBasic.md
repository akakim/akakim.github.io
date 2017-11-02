---
layout: post
title: RecyclerView에 대해서. 
date: 2017.10.30 11:52:00
tags: [Blog,Android,Wiget]
---


# RecyclerView란 

## 1. 히스토리 
Android 5.0과 함께 Support_Libraray-v7의 최신 버전에 정식 추가가 되었다. 

---
기존의 ListView보다 유연하고 성능이 좋다. 
개발자가 커스터마이징 하기엔 많은 제약 조건을 ListView는 가지고 있었다. 이는 구조적인 문제로 인한 제약조건이였다. 한편으로는 ListView의 구조적인 성능문제도 있었다.

---
위에서 나열된 성능 문제와 다양한 형태로 개발자가 커스텀할 수 있도록 유연함을 해결하기위해 탄생하였다.


## 2. 주요 클래스
* Adapter 기존 ListView와같이 Adapter
* ViewHolder - 재활용 View에 대한 모든 서브뷰 보유
* LayoutManager - 아이템 항목 처리
* ItemDecoration - 아이템 항목에서 서브 뷰 처리
* ItemAnimation - 이 아이템 항목이 추가, 제거되거나 정렬될 때 애니메이션 처리

## 3. LayoutManger
RecyclerView를 생성시 반드시 생성 되어야 하며 이를통해 모든 뷰의 레이아웃을 관리한다.

* LinearLayoutManger - 수평/수직 스크롤 리스트
* GridLayoutManger - 그리드 형식
* StaggerdGridLayoutManager - 높이가 불규칙적인 그리드 리스트

개인적으로 드는 의구심은 Grid로 하느냐 Linear로 레이아웃의 아이템을 적용할때 xml 상으로 2개를 다 그려줘야되는건지 아니면 xml을 하나만 해도 상관없는건지 알아봐야될거같습니다.


## 4.ViewHolder
ViewHolder는 기존 ListView에서도 많이 추천되는 패턴이였는데 RecyclerView로 넘어오면서, 강제로 해줘야한다.

## 5.Adapter
기존의 ListView와 유사하다 .

getView 대신 onCreateViewHolder와 onBindviewHolder가 추가되었다. 
예를들어 다음과같이 코딩된다. 
```
public class SampleAdapter extends RecyclerView.Adapter<SampleAdapter.ViewHolder>{


    List<MusicItemModel> musicItemModels = Collections.emptyList();

    private Context context;


    public MainListViewAdapter(List<MusicItemModel> musicItemModels, Context context) {
        this.musicItemModels = musicItemModels;
        this.context                    = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate( R.layout.main_music_list_item,parent,false);

        return new MainListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final MainListViewHolder holder, final int position) {

        holder.textViewTitle.setText(musicItemModels.get(position).getTitle());
    }

    /**
     * @return 어뎁터의 아이템 갯수
     */
    @Override
    public int getItemCount() {

        return musicItemModels.size();
    }

	private class ViewHolder extends RecyclerView.ViewHolder{

		View        rootView;
        TextView    textViewTitle;
   		public ViewHolder(View ItemView){
        
		rootView = ItemView;
        ItemView = ( TextView )findViewById(R.id.textViewTitle);
        }
    
    }

}

```

## 6. 샘플 구현
* app>build.gradle 의 dependency 쪽에 compile 'com.android.support:recyclerview-v7:버전번호 (예를들어 25.3.1)'로 작성한다.
* RecycclerView 선언 ( xml과 java에서든 )
* Adapter <뷰홀더> 클래스 선언
 * RecyclerView.Adapter를 상속받음 .
 * onCreateViewHolder 시점에 LayoutInflater를 이용하여 뷰를 생성한다.
 * onBindViewViewHolder 시점에 Data를 뷰에 설정한다.
   * 이 시점에서 OnClick이벤트를 넣을 수 있는데 , 추가적인 이슈가 있습니다.
* ViewHolder 클래스 선언
 * RecyclerView.ViewHolder 클래스를 상속받는다.

*이 이후는 ListView를 구현하듯 구현하면 끝이난다*

reference
[꿈꾸는 개발자의 로그 RecyclerView에 대해서](http://www.kmshack.kr/2014/10/android-recyclerview/)
