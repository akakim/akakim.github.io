---
layout:post
title: ram 사용량 조사 
---

How memory is counted 
당신이 메모리 프로파일러의 상단에서 볼수있는 숫자들은 메모리 개별의 페이지에 기반한다.그리고 페이지는 당신의 앱이 안드로이드 시스템상에서 제출된 메모리 페이지이다. 이것은 다른 앱이나 시스템과 공유하는 페이지가 아니다. 

메모리안의 카테고리는 다음과 같다. 
- Java : 자바나 코틀린 코드로 할당된 객체들 
- Native : C나 C++코드로 할당된 객체들 
- 당신이 C++코드로 작성하지 않더라도 당신은 native 메모리를 사용할 것이다. 왜냐하면 , 안드로이드 프레임워크는 native 메모리를 당신이 의지하는 다양한 작업에도 사용한다. 예를들어 image asset이나 다른 그래픽요소들 - ( 당신이 자바 혹은 코틀린으로 작성을 하더라도 )를 다룰때이다.
- Graphics: 그래픽 버퍼 큐를 위해 사용하는 메모리 이다. GL surface포함 , GL Texture 상에서 올려진것들이다. ( 이것이 cpu 메모리 상에 공유하는 것임을 주목하라 . GPUS)
- stack : 앱안의 native 와 java의 스택. 이것은 종종 얼마나 많은 스레드가 당신의 앱에서 동작하는 것과 연관이 잇다. 
- Code : 코드나 리소스를 위한 메모리, dex byte코드나 dex코드로 컴파일 또는 최적화 된것, so 라이브러리, 폰트을위한 메모리 
- Other : 시스템이 분류하지 못한 영역 
- Allocated : 코틀린이나 자바 객체가 할당된 객체수. 이것은 C나 C++은 세지 않는다. 


Android 7.1 이하의 기기에 연결될때 이것은 당신의앱에 연결한다. 그래서 어떤 객체들이 당신

[안드로이드 메모리 프로파일러 문서](https://developer.android.com/studio/profile/memory-profiler.html)