---
layout: post
title: ViewPager의 기본적인 구성 및 예제
description: ViewPager Basic
permalink : /Android/2018/01/30/ViewPager
tags: [Android,ViewPager,UI]
---
##ViewPager란.

### 예제

##### tab이들어간 구글 플레이스토어 화면 

![구글 예제 ](https://github.com/akakim/akakim.github.io/tree/master/assets/img/ViewPagerGoogle.png)

##### tab이 안들어간 Genie 뮤직 화면 
![지니 예제 ](https://github.com/akakim/akakim.github.io/tree/master/assets/img/ViewPagerGenie.png)

### 구조

다음과 같이 옆으로 당기는 이벤트(이하 Swipe event)를 하게 되면 다음 항목을 선택하거나, 다음 항목들을 보여줄 때 응용하게 되는 UI 입니다. 

ViewPager의 구조는 다음과 같습니다.

ViewPager - Adapter - Fragment

와 같이 RecyclerView나 Gridview, ListView 와 같이 비슷한 구조라고 할 수있습니다.

이것을 이용하게 될 때 다양하게 응용할 수 있습니다.

포인트는 응용을 하게 될 때는 정적인 구성 ( Activity 에서 Fragment들을 생성할 때 몇개를 구현할지 분명히 아는 것 )인지 동적인 구성이 될지를 
구분해서 사용해야됩니다. 구현의 편의상은 정적인 구성이 역시 좀 더 편하죠.

### Adapter

뷰 페이저를 이용한 종류에도 몇가지 종류가 있습니다. 자신이 구현하고자하는 특색에 맞게 이용하시면 됩니다.
FragmentPagerAdapter , FragmentStatePagerAdapter, PagerAdapter

### 예제 코드

##### 1 . xml 에서 ViewPager 선언

```
<CoordinatorLayout>
   <AppBarLayout>
     <Toolbar/>
    </AppBarLayout>
    // 필요하다면, PagerStrip을 여기다가 놓는다.
    <ViewPager/>
</CoordinatorLayout>
```
##### 2 . Activity에서 Adapter 선언


- Adapter 클래스 선언

```
 class PagerAdapter extends FragmentState{
   @Override
   public TestAdapter(FragmentManager fm, List<Fragment> fragments){
    // 설정
   }


     페이저의 위치에 따른 보고싶은 Fragment들을 설정함.
    @Override
    public Fragment getItem(int position) {
          return fragments.get(position);
    }

	Fragment 의 갯수
    @Override
    public int getCount() {
       return fragments.size();
    }
```

##### 3. ViewPager에 Adapter 설정

```

    ViewPager viewPager; 
   @Override
    public void onCreate(Bundle savedInstance ) {
		super.onCreate(savedInstance)
        viewPager = ( ViewPager ) findViewById(R.id.viewPager);
        
        TestAdapter adapter = new TestAdapter ( getSupportFragmentManager(), (내가 선언한 Fragemnt List) ); 
        viewPager.setAdapter( adapter);
    }
```

### 느낀점 

디테일한 콜백에 대해선 나중에 더 포스팅하겠지만, 어떤 UI가 몇개가 그려올지 알수없다면, 입문하실때는 정적인 페이지 부터 구성하는게 더 편합니다.

정적인 페이지를 구성하고 나면, 이후에 동적인 페이지를 설정하는 방법이 있는데, 그걸 천천히 따라 가시면 될거같습니다. 
Adapter를 상속받아 구현하는 클래스는 코드 작성하는 입장에서 좀 더 자신의 주관에 맞게끔 할 수있겠구나 하는 생각이 듭니다. 

좀 더 자세한 내용과 실습한 내용들을 찾아가며 포스팅을 하도록 하겠습니다~

reference 

[이상한 모임 블로그](https://blog.weirdx.io/post/1548)

[꿈꾸는 개발자의 로그](http://www.kmshack.kr/?s=ViewPager)
