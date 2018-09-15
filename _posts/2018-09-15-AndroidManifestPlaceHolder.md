---
layout:post-none-image
title: 서로다른 버전의 앱 배포하는 꿀팁.
permalink: /Android/2018/09/15/AndroidManifestPlaceHolder
tags: [Android,Library,AAR,aar,flavor]
---


##### 작성하기에 앞서..Android studio 3.1.4 기준입니다.
필자가 라이브러리를 테스트 하게 되면서 매우 불편했습니다.
같은 라이브러리를 여러 앱에다가 설치하고 테스트를 해야했습니다.
단순하게 프로젝트를 여러개 만들어서 aar 파일을 각각의 프로젝트에 넣고 적용한다면 시간이 ..오래걸리죠.

[박상권의 삽질블로그](http://gun0912.tistory.com/74?category=560271)를 참고하게 됬는데
띠요옹? Flavor가 안됩니다.

[안드로이드 개발자문서 참조](https://developer.android.com/studio/build/build-variants)
신버전의 flavor 선언  app의 build.gradle
예를들어 이렇게 선언되는거죠. 
```
  andriod{
  ...
  buildTypes{
   ...
  }


   flavorDimensions "customFlavor"
   productFlavors{
     alpha{
       applicationIdSuffix ".alphaversion"
       dimension "customFlavor"
     }

     beta{
       applicationIdSuffix ".betaversion"
       dimension "customFlavor"
     }

      gamma{
       applicationIdSuffix ".gammaversion"
       dimension "customFlavor"
     }
   }
  }
```

처음에는 **순진하게 개발자 문서 참고했다가 Build시 파일명이 flavorDiension의 내용들이 다 더해지는걸보고 충격적이였습니다.**~~망할~~



reference
[박상권의 삽질블로그](http://gun0912.tistory.com/74?category=560271)