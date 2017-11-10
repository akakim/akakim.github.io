---
layout:post
title:버그 리포팅 툴 
tag:crashReporting
---


## 리포팅툴 종류
- ACRA
- Crashlytics ( Fabric )
- New Relic
- Hockey App
- InstaBug
- Firebase
- Crittercism
- apteligent




##### 1.ACRC 사용률 ( 12.4% )

- 가격
 - 무료
- 쉬운 설치 
- 모든 안드로이 버전과의 호환성
- 유저와 상호작용하는 설정 
- 대중성
- 중간.

- 사용한앱 
- Linkedin , Hotels.com , Flikr

- 개발자들이 이야기하는 장점 
 - 세련된 디자인
 - 편안한 설치
 - 고도의 설정가능성 
 - 서버 호스팅을 할 필요없다 .
 - 자동적으로 구글 시트에 보고한다.

- 개선점
 - 때때로 불안정하고 느리다.
 - 많은 용량을 소비하고 오래된 기록을 지우는데,쉽지않다.
 
 
##### 2.Crashlytics ( Fabric ) 사용률( 66.2% )
2013년 트위터에 팔렸다. Fabric.io에 속하게 되었다. 

- 가격
 - 무료 [firebase 사이트](https://firebase.google.com/pricing/)
- 기능
 - 실시간 crash 보고 
 - IOS도 지원됨.
 - 예외 추적
 - life cycle 모니터링 테스트
 - Fabric과의 통합 
 - Beta App 배포 
- 대중성 
 - 대중적임

- 사용한 앱 
 - Uber ,Spotify , Fruit Ninja

- 개발자들이 이야기하는 장점 
 - 세련된 디자인
 - 편안한 설치
- 개선점
 - 이메일로 오는 보고서의 불편함.

###### 이용방법
setup
1. project > build.gradle 에 다음과 같이 추가 .
```
buildscript {
	repositories {
		maven { url 'https://maven.fabric.io/public' }
	}
	dependencies {
		// The Fabric Gradle plugin uses an open ended version to react
		// quickly to Android tooling updates
		classpath 'io.fabric.tools:gradle:1.+'
	}
	...
	apply plugin: 'io.fabric'
	repositories {
		{ url 'https://maven.fabric.io/public' }
	}
}
```


2. app > build.gradle 에 의존성  추가. 
```
compile('com.crashlytics.sdk.android:crashlytics:2.6.2@aar') {
	transitive = true;
}
```

3. 메니페스트에 선언 
이 때 api키는 fabric에서 프로젝트를 생성한 값이다. 
```
<meta-data
	android:name="io.fabric.ApiKey"
	android:value="<FABRIC_API_KEY>"
/>
```

##### 3.New Relic 사용률 ( 11.8% )
end-to-end 퍼포먼스 모니터링을 제공한다. 

- 가격
 - lite(free) data가 24동안만 남는다. 
 - $75 ~149 (APM)
 - $69 
 - $149
 - $999/per app (New Relic Mobile)

- 특징

- 지질학적인 위치에 따라 느려지거나 crash된다. 

- 사용하는 앱 
 - NY times , Espn , SoundCloud

- 장점
- 쉬운 구현과 쉬운 이해
- dashboard 커스터마이징 

- 개선점 
- crash report의 커스텀 속성의 부제 
- 
##### 4.Hockey App

- 가격 
- 초기 2개앱은 무료 
- 이후 1달에 $10 ~ $500


- 특징 
- app crash의 원일을 찾는대 다양한 검색 툴로 도움을 준다. 
- crash 분석을 쉽게하기위해 기호화되어잇다. 
- 개발 workflow sytems 와 버그 third party 버그 추적 시스탬과 쉬운 통합 .

- 사용하는 앱 
- snapchat,skype,Angry Birds

- 장점
- 쉬운 사용과 배포
- 빠른 버전업과 구 버전과의 호환성
- 버그 포고의 평이함 

- 개선점 
- 설치가 최적화되어있지 않다 
- 꽤 비싸다. 


##### 5.InstaBug
- 가격 
 - $0 ~ $349 1달

- 특징 
- 버그나 crash를 한눈에 볼 수 있다. 
- 연관된 crash들을 묶을 수 있다. 그리고 그들이 차지하는 비중을 쉽게 분석할 수 있다.

- 대중성 
 - 보통 

- 사용하는 앱 
 - Asana
 - JambaJuice
 - Dubsmash

- 장점
- 쉽게 사용이 가능하다.
- beta testing과 보고가 강력하다. 

- 개선점
- 리포트를 커스터마이징하는게 재한되어있다.
- 사회망 채널을 지원하지 않는다. 

##### 6.Firebase


( 2016 . 09 .26 기준 )

- 가격
 - 무료 (light - spark plan)
 - $25 (Flame plan)

- 특징
- crash의 빈도나 효과에 따라 우선순위를 준다.
- device 특성이나 주기, stack trace등 디테일한 자료
- offline 상태에서의 crash 수집 
- 이탈하는 유저 경험에 대한 특징을 개발자에게 제공함

- 대중성 
 - 측정되지 않음 

- 적용된 앱 
 - Kil shot bravo , Love Collage , Happy Fresh

- 장점 
- 설정이 쉽다
- 유저나 특정 그룹에 대한 에러 경험을 알 수 있다. 
- Native Crash가 자동적으로 수집된다. 

- 개선점
 - Google PlayService가 구현되어야만한다, 그렇지않으면 사용할 수 없다 . 
 - 새로운 Crash가 너무 늦게 나타난다 (15분 정도 소요 가능성 )
 - crash 알람이 없다. 
 - crash 상태를 해결할 방법이 없다. 


###### 이용방법
setup

1. 파이어베이스 설치 
2. 파이어베이스 콘솔에서 당신의 프로젝트를 설치 
3. app > build.gradle에 의존성 주입 
`compile 'com.google.firebase:firebase-crash:9.4.0'`
4. 예외를 처리한다. 
` FirebaseCrash.report(exception)`
5. 로그를 커스텀
`FirebaseCrash.log("submit .... ");`

파이어베이스 시스템에서의 사용방법
- 커스텀한 키값이나 유저의 정보를 로깅하지 말것 
- 강제로 crash를 일으키는 메소드는 파이어베이스에 없다.



##### 7.Crittercism 사용률 ( 10.0% )  ( apteligent 이전 버전 )
##### 8.apteligent

- 가격 
 - 무료 
 - $150(Jump Start)
 - Enterprise

- 특징 
 - 실시간으로 알려주는 UI가 유용하다. 
 - 치명적인 에러든 아니든 모니터링함 
 - 실시간 crash report 

- 대중성 
 - 보통 

- 사용한 앱 
 - snapchat , netflix , cnn 

- 장점
- 쉬운 통합 
- crash를 분류하는태 효과적임
- crash에 대한 자세한정보 


- 개선점
- UI 가 때때로 느려짐
- App 관리가 혼란스러움



Free ( In-App feeback , ScreenShot 첨부, 환경 Snapshot)
Bronze Free + BasicIntr

- 기능
 - 사용자 메타데이터 기록 
 - Userflow 기록 
 - 네트워크 요청 기록
 - NDK 지원 
 - IOS도 지원됨.
 - 등등.

- 요구사항 
- 안드로이드 4.0이상 
이용방법 


reference
[비교글](http://blog.safedk.com/sdk-economy/find-whos-crashing-party-mobile-crash-reporting-tools-review/)