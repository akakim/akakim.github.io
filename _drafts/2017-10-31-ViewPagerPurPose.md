---
layout: post
title: ViewPager
time: 2017-11-01 10:27:00
img: post-3.jpg
tags: [Blog][Android]
---


# ViewPager 사용기


dependency

* 처음 구상한 내용 

```
<Activity>
	<Toolbar> ... </Toolbar>
    
    <ViewPager>
    
    </ViewPager>

</Activity>


```

이런식으로 생각하고 ViewPager 안쪽으로 3개의 Fragment를 넣고
floating 버튼을 이용하여, Fragment안의 List를 아이템을 갱신하게끔 해줄 생각이였다

그런데 이게 웬일 .. ViewPager는 어뎁터를 무엇으로 설정하느냐에 따라 Fragment가 메모리상에 늘 상주 할 수 있고, 아닐 수도잇다. 

 * FragmentPagerAdapter 
 * FragmentStatePagerAdapter

두가지 이다. 전자의 경우 메모리에 늘 Fragment가 상주해있다. 
후자의 경우 메모리에 늘 상후하고 있지 않는다. 

내가 원하는 Fragment의 UI를 변경하려 했는데 후자의 어뎁터를 이용해도 제대로 안되기 일쑤였다.

결론. ViewPager는 분명 잘 만들었지만, 앱 내부에서 데이터가 갱신되어서 viewPager안의 데이터를 갱신하려면 fragment가 무조건 다시 생성되개금한다 라는 것이다. FragmentSatePagerAdapter를 이용해 보았지만 같은 Fragment로 제작된거라 정확하게 뷰를 갱신 못했다. 

그래서 ViewPager는 정적인 페이지를 노출하고자할때, 사용하는게나을거같다.

뷰페이저안의 목록들이 동적이여야만 한다면, 커스터마이징이 더 들어가야될거같다.

![꿈꾸는개발자의로그](http://www.kmshack.kr/2013/03/viewpager%EC%9D%98-pageradapter-positionnone%EC%9D%98-%EB%B9%84%EB%B0%80/)