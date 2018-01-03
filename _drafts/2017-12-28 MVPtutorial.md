---
layout:post
title: mvp 튜토이얼 . 
Tag:[Android][tutorial]
---

### MVP를 적용하기 위해선 
Test 코드 작성이 편리해진다. 

### MVP란
먼저 각 역할과 목적이 중요하다. 

MVP = View , Presenter , Model 이다. 

- Model
 - Data와 관련된 부분 담당
 - Data의 전반저깅ㄴ 부분을 Model에서 담당하고, 네트워크 로컬 데이터 등을 포함한다.
- View
 - 사용자의 실질적인 이벤트가 발생
 - 완전한 view형태를 가지도록 설계한다. 계산하거나 데이터를 가져오는 행위 등은 Presenter가 처리하도록한다. 
- Presenter
 - View에서 전달받은 이벤트를 처리한다. 처리한 결과물 (데이터나 기타등등)을 다시 View에 전달한다. 
 - View(예륻들어 Activity나, Fragment 혹은 View나 ViewGroup을 상속받는것들 )와는 무관한 






reference
[꿈 많은 개발자가 되자](http://thdev.tech/androiddev/2016/10/12/Android-MVP-Intro.html)