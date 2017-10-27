---
layout: post
title: TextView의 스크롤링
---
# TextView의 스크롤링에 대하여  

### MP3 제목처럼 물결이 흐르는 듯한 애니메이션 

먼저, TextView를 커스터마이징하여 Animation 효과를 주는 방향으로 생각했지만 생각했던것 만큼 나오진 않았다.
애니메이션을 넣는 순간, 뷰가 옆공간을 침범한다던지 애니메이션을 우에서 좌로 줄려고 하니, 원하는효과는 나오지 않았다. 

찾다보니 다음과 같은걸 발견했다. 

- - -

TextView xml 태그에 다음과 같은 속성을 추가한다. 
```
android:focusable="true"
android:singleLine="true"
android:ellpsize="marquee"
android:scrollHoriaontally="true"
android:focusableInTouchMode="true"
```

해당 TextView를 자바상으로 다음과 같은 코딩한다.
`TextView text = (TextView)findViewById(R.id.text).setSelect(true)`

이런식으로 좌에서 우로가는 애니메이션 효과가 가능하다.
[출처 어느 티스토리 블로거님](http://sunwoont.tistory.com/entry/TextView-%EC%8A%AC%EB%9D%BC%EC%9D%B4%EB%94%A9)


### 텍스트 세로 스크롤링

흔히들 ScrollView를 이용하여 Scroll을 줄 생각을 할것이다.
**근데 ... 굳이 넣지않아도된다.**

TextView xml 태그에 다음과 같은 속성을 추가한다. 
```
 android:minHeight="150dp"
 android:maxHeight="450dp"
 android:scrollbars="vertical"
 android:scrollbarStyle="outsideInset"

```

해당 TextView를 자바상으로 다음과 같은 코딩한다.
`TextView text = (TextView)findViewById(R.id.text)`
`  text.setMovementMethod( new ScrollingMovementMethod() );`


이런식으로 주게 된다면 TextView 자체적으로 스크롤리 되게끔 할 수 있다. 
~~스크롤뷰는 왜있는거지..~~
~~**TextView 에 별의 별것을 해놓았다...**~~