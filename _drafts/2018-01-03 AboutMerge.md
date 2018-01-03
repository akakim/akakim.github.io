---
layout:post
title: merge 태그에 대해서 .. 
---

### abstract 
merge Tag를 사용하는 이유 
1. 중복된 코드를 최소화 할수있다. 
2. 뷰 계층을 최적화 시킨다. 
3. 그저 최상위 뷰에 사용하는 Dummy View 이다. 


### StackOverFlow의 답변 내용 
[Stack Over Flow 답변 내용](https://stackoverflow.com/questions/8834898/what-is-the-purpose-of-androids-merge-tag-in-xml-layouts)을 살펴보면 다음과 같은 코드를 예를들며 설명을 하고 있다.

흔히 다음과 같이 제작하여 공통되는 레이아웃들을 포함하여 만들 수 있다. xml 파일을 include 태그를 사용한다는 것은 rendering 시점에 top_level_activity에 넣는 것과 같음을 명심하자.

top_level_activity.xml

```
<LinearLayout
	xmlns:andorid:="http://schemas.andorid.com/apk/res/android"
    android:layout_width = "match_parent"
    android:layout_height = "match_parent"
    android:orientation="vertical">

    <include layout="@layout/header1"/>

    <include layout="@layout/header2"/>


</LinearLayout>
```


예들 들어 header1.xml의 내용
```
<?xml version="1.0" encoding="utf-8">
<TextView xmlns:andorid:="http://schemas.android.,com/apk/res/android"
 .. 그 이외 속성들>
 </TextView>
```

header.2.xml
```
<Button xmlns:andorid:="http://schemas.android.,com/apk/res/android"
 .. 그 이외 속성들>
 </Button>

```

header1과 header2를 보면 뭔가 특별한건 없다. android namespace가 선언되어있음을 생각하면 
화면에 나온 top_level_activity.xml은 다음과 같다. 
```
<LinearLayout
	xmlns:andorid:="http://schemas.andorid.com/apk/res/android"
    android:layout_width = "match_parent"
    android:layout_height = "match_parent"
    android:orientation="vertical">

    <TextView 
     .. 그 이외 속성들>
     </TextView>

    <Butto
     .. 그 이외 속성들>
     </Button>


</LinearLayout>
```

##### header1.xml내용이 다음과 같아진다고 LinearLayout이 선언됬다고 생각하자. 
```
<LinearLayout
	xmlns:andorid:="http://schemas.andorid.com/apk/res/android"
    android:layout_width = "match_parent"
    android:layout_height = "match_parent"
    android:orientation="vertical">

    <TextView 
     .. 기본 속성들>
     </TextView>

    <TextView 
     .. 기본 속성들>
     </TextView>

   <TextView 
     .. 기본 속성들>
     </TextView>

</LinearLayout>
```

이렇게 header1.xml이 바뀌게 된다면. 

```
<LinearLayout
	xmlns:andorid:="http://schemas.andorid.com/apk/res/android"
    android:layout_width = "match_parent"
    android:layout_height = "match_parent"
    android:orientation="vertical">

    <LinearLayout
        android:layout_width = "match_parent"
        android:layout_height = "match_parent"
        android:orientation="vertical">

        <TextView 
         .. 기본 속성들>
         </TextView>

        <TextView 
         .. 기본 속성들>
         </TextView>

       <TextView 
         .. 기본 속성들>
         </TextView>

    </LinearLayout>

    <Button 
     .. 그 이외 속성들>
     </Button>


</LinearLayout>
```

2수준의 LinearLayout이 중복 됨을 알 수 있다.그래서 어떻게 해야될까? 

#### merge Tag를 입력한다.!

이제 이러한 중복되는 이슈를 없에기 위해 dummy tag를 넣는다 

header_merge_used.xml
```
<merge xmlns:andorid="http://schemas.android.com/apk/res/android">
 		<TextView 
         .. 기본 속성들>
         </TextView>

        <TextView 
         .. 기본 속성들>
         </TextView>
</merge>
```

이제 top_level_activity.xml 은 다음과 같이 생성된다. 


```
<LinearLayout
	xmlns:andorid:="http://schemas.andorid.com/apk/res/android"
    android:layout_width = "match_parent"
    android:layout_height = "match_parent"
    android:orientation="vertical">

    <TextView 
     .. 기본 속성들>
     </TextView>

    <TextView 
     .. 기본 속성들>
     </TextView>


    <Button 
     .. 그 이외 속성들>
     </Button>


</LinearLayout>
```

##### 이렇게 하게 되면, 단하나의 계층 구조를 가지게 되고 필요없는 뷰를 회피할 수 있다. 

reference
[개발스토리님 블로그](http://humble.tistory.com/45)
[Stack Over Flow 답변 내용 ](https://stackoverflow.com/questions/8834898/what-is-the-purpose-of-androids-merge-tag-in-xml-layouts)
