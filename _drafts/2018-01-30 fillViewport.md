---
layout: post
title: fillViewPort에 관하여 
tag: [ViewPager,ScrollView,NestedScrollView,Android]
---

### 들어가기 앞서... 

저의경험담을 정리할려고 합니다.
어떤 웹사이트의 파싱하는 작업을 했었는데 이게 웬걸? Paging처리가 된 걸 UI에 그리는걸 표현 하고 싶엇씁니다. 
머리를 고민고민할 때 
이런식으로 UI 를 구성하게 됬습니다. 


##### Activity 화면 xml 
```
<CoordinatorLayout>
  <AppBarLayout>
     ...
  </AppBarLayout>
  <NestedScrollView>
	<ViewPager/>
  </NestedScrollView>
</CoordinatorLayout>
```

##### Fragment 화면 xml

```
<FrameLayout>
   <RecyclerView />
</FrameLayout>
```
NestedScrollView > ViewPager > Fragment > RecyclerView > 파싱의 산출물들 출력 이런 과정을 거쳐서 
파싱을 하게 됬습니다.

구조를 이용하여 파싱을 하게 되었습니다. 

그런데 문제는 이런게 발생합니다. 

막상 데이터를 다 긁어오고, 화면에 뿌려주는 것 까지 코딩을 해놓고, View에서 안보이는 겂니다. 
다시말해, RecyclerView의 크기가 파싱하기 전 뷰의 높이가 0인 크기로만 보이기 때문이였죠. ( 이는 개발자 도구에서 레이아웃 영역 표시로 알 수 있었습니다.)



### ScrollView FillViewport 속성에 대하여 


reference 

[스크롤 뷰에 대한 구글 개발자의 번역글](http://blog.naver.com/PostView.nhn?blogId=huewu&logNo=110092722013&viewDate=&currentPage=1&listtype=0)
[스크롤 뷰에 대한 구글 개발자의 원문](http://www.curious-creature.com/2010/08/15/scrollviews-handy-trick/)