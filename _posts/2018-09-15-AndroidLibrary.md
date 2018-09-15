---
layout:post-none-image
title: 안드로이드 라이브러리 프로젝트 분리하기
permallink: /Android/2018/09/15/AndroidLibraryDivide
tags: [Android,Library,AAR,aar]
---


##### 작성하기에 앞서..
필자는 gradle에 대한 지식이 짧아서 .. 나름대로 노가다를 통해 프로젝트를 쪼갰습니다. 방법은 의외로 간단합니다.


##### 원하는 것
구성 :

Common Library ( 앞서 작성한 기능의 집합 및 공통으로 사용할 리소스들이 있는 라이브러리 )
Provider Library ( 공통라이브러리에서, ContentProvider 의 기능을 더 넣은 라이브러리 )
Resolver Libraray ( 공통라이브러리에서 Provider의 기능들을 조금 더 편리하게 제공하는 라이브러리 )

이런식으로 라이브러리가 하나의 프로젝트에 넣었습니다.

Provider와 Resolver쪽만을 라이브러리로 만들고 Common쪽에 있는 manifest 파일의 내용도 필요합니다.
아니 이게 왼걸? Provider 라이브러리를 assemble만 하면 되는 줄알았는데 아니더라고요.


##### 해결
Common Library + Provider Library를 같이 assemble 해줘서 파일 2개를 만듭니다.
안드로이드 앱에 적용시 두파일 implement 를 해주면 됩니다.

