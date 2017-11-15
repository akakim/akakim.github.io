---
layout: post-none-image
title: MediaPlayerHistory 
tags: [blog][Android][MediaPlayer]
---

# MediaPlayer 이용기

Android에서 MP3나 비디오 재생, 스트리밍을 꽤나 편리하게 해주는 모듈이다.

**++하지만 다루기가 까다롭다 ++**

왜냐하면, 기본적으로 음악 재생 자체(데이터를 불러오는 것 부터 시작해서 기타등등.. ) 가 Asynchronize하게 돌아가게되고, 정확한 Callback이 재공이되지 않기때문이다. 음악 항목들을 List로 보여줘서 한꺼번

[구글에서 올라온 예제코드](https://github.com/googlesamples/android-UniversalMusicPlayer/)를 따라가려하면 꽤나 복잡하다 .

음악을 Sesstion으로 관리하고(MediaPlayer에서 얻는 관점 ) Data 자체를 Singleton 형식으로 관리하기 때문이다. 또 비동기에서 동기를 맞출려고 Queue도 들어가는등 . 이것저것 많이 붙여놓았기 때문이다. 차근차근 봐야 할거같다.


하나의 재생하는건 다른 곳의 자료를 참고해도 간편하게 구현이 가능하다.

만약 이게 list형태로 여러게로 보여주게 된다면 ?
그 때부터 까다로운 문제에 직면하게 된다. 그런 방향으로 생각하게 된다면 많은 문제에 직면하게 된다.
[구글 개발자 문서](https://developer.android.com/reference/android/media/MediaPlayer.html)를 참고하고 여기저기 시행착오를 겪어본 결과.

몇가지 결과 지켜야할 룰이 있다.
1. 반드시 Asynchronized로 호출한다.
2. Mediaplayer는 단 하나의 객체로만 재생한다.
3. Error에 대한 콜백은 반드시 정의하고 MediaPlayer.reset() 을 하도록하면 안정적이게된다.
4. 개발자 문서를 참고하여 상태값들을 일단 모두 정의한다.
5. 4번에서 만든걸 재생 혹은 정지 에러 까지 모든걸 상태를 변경하며 관리한다.
6. MediaPlayer.isPlaying 메소드는 개발자 문서에서도 나왔듯 idle상태에서 호출을 못하므로 반드시 생각한다.
7. 반드시 음악을 정지하고 초기화 한다. -> stop을 호출한뒤 reset을 한다. 안그러면 자꾸 버퍼링이 걸려있다.



- 추가적으로 고민이 된건 리스트 형태로 이루어진 데이터를 누가 어떻게 관리하느냐 였다. 그것을 구글 예제의 방향을 가는게 맞는거같다 .
- 구글의 예제는 ExoPlayer를 이용하기 때문에 더 좋은지는 미지수이다.
- MediaPlayer를 이용하면 구글의 예제보단 간단하게 구현된다.




코드의 구성시 필수적인 것은 다음과 같다. 


> MusicService.java ( 음악을 background에서 재생하기 위한 서비스 객체 )
> MainActivity.java ( 음악을 네트워크 상에서 불러오는 Activity )
> PlayController.java ( app이 foreground상태에서 )
> MusicListFragment.java ( app에서 음악 목록을 관리하는 부분 )
> BroadCastReceiver ( 따로 선언 하진 않았지만, )
> ServerConnection( RemoteHandler를 등록하는 곳 )
> RemoteHanlder ( MainActivity에서 Service로 메세지를 던질때 이용함 서비스에서도 Handler를 이용해 Activity에서 보낸 메세지를 받는다. )
> RemoteView ( 알림영역에 그려줄 CustomView)
> Nofication ( 알림영역에서e도 음악을 제어할수있는 컨트롤러를 띄우기 위함 .)
> NotificatinManager (알림영역에서e도 음악을 제어할수있는 컨트롤러를 띄우기 위함 .)
> 이미지 라이브러리 . (Glide든, Picasso든 뭐든 좋아요 )