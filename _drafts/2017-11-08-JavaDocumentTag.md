---
layout: post
title: javaTAG
tag: java,document
---
# Java Documentation 을 위한 TAG 팁 
## java 개발자 문서 번역 

자바 문서 툴은 특별한 태그들이 자바 문서의 주석과 함깨 내장되어있을 때 그것들을 분석한다. 이러한 문서의 태그들을 당신이 자동으로 생성이 가능하다. 당신의 소스코드로 부터 잘 정돈되게 완성하는 것을 의미한다. 태그는 'at' (= @) 표시로 시작하고 캐이스별로 민감하다. 그들은 반드시 대문자 혹은 소문자 타입으로만 입력되어아한다. 보여줄때와 마찬가지로. 

#### 태그의 종류 
- Block Tags  - tag section 안에 존재한다. 태그 섹션은 주요 서술이 따라온다. Black tag들은 @tag 이런 형태를 띄게 표시함 
- Inline Tags - 주요 서술 안의 어디서든 존재한다. 혹은 Block Tag들을 위한 코맨트 안에 있다. inline tag들은 중괄호로 표시함:{@tag}

TAG 		Introduced in JDK/SDK
**@author**	     	1.0
{@code}  		1.5
**{@docRoot}**		1.3
**@deprecated**		1.0
@exception		1.0
{@inheritDoc}	1.4
**{@link}**			1.2
**{@linkplain}**	1.4
{@literal}		1.5
@param			1.0
@return			1.0
**@see**			1.0
**@serial**			1.2
@serialData		1.2
**@serialField**	1.2
**@since**			1.1
@throws			1.2
**{@value}**		1.4
**@version**		1.0

@author 
-author 옵션이 사용 된경우의 문서 제작시 특정한 텍스트 명을 지정하는데 이용한다. 문서는 여러개의 @author태그를 이용할 수 있다. 각 @author 태그마다 이름을 사용할 수 있다. 

@deprecated
api 가 더이상 사용되지 않음을 지정할 때 쓰인다. 

{@code text} 
`<code>{@literal}</code> `과 같다. 

@param


@return




@exception
 이것은 @throws 와 같다.


@see
 같이 참고하라는 링크나 텍스트의 heading으로 참조하라는 걸 가리킨다. 


@link
 @see 태그와 유사하다. inline-tag가 가능하다는데 같은 페이지 내부에서 이동하는 것인거 같다 @see는 다른 문서를 참조하는 것이고 


@since 
 소프트웨어가 어느시점부터  배포된것을 의미한다. 이 태그는 어떤 주석에도 유효하다. comment,overview,package,class,interface,constructor,method,field을 의미한다. 

@serial

@author
@version
@value

@serial
@serialField
@param
@return 
@thorws 
@exception 과 같다. 생성할 문서에 클래스 명이나 관련된 내용을 throws를  부제목으로 더할 수 있다. 이태그는 메소드나 생성제의 경우에 유효하다. 

{@value}


#### Constructor 와 Method 태그들 
@see
@since
@deprecated
@param
@return
@throws and @exception
@serialData
{@link}
{@linkplain}
{@inheritDoc}
{@docRoot}


### 태그 들이 어디에 쓰여질 수 있을 까 
다음 섹션은 태그들이 사용될 수 있는지를 서술한다. @see,@since,@deprecate,{@link},{@linkplain},and {@docroot} 는 모든 문서의 주석에서 사용될 수 있음을 주목하라.

#### OverView 문서 태그들 
@see
@since
@author
@version
{@link}
{@linkplain}
{@docRoot}


#### Package 문서 태그들
@see
@since
@serial
@author
@version
{@link}
{@linkplain}
{@docRoot}

#### Class와 Interface 문서 태그들 
Class 와 Interface Documentaion Tag들 
@see
@since
@serial
@deprecated
@author
@version
{@link}
{@linkplain}
{@docRoot}

예를들어 
```
/**
 * A Class representing a window on the screen.
 * for example :
 * @see com.tripath.muse.MusicService.java
 * @since  
 * @author   RyoRyeong
 * @versoin   1.0.0
 */
```

#### Field 문서 태그들 
@see
@since
@deprecated
@serial
@serialField
{@link}
{@linkplain}
{@docRoot}
{@value}
```
/**
 * A Field
 * for example :
 * @see com.tripath.muse.MainActivity.java , com.tripath.muse.MusicListFragment.java
 * @since 1.0.0
 */
```


#### Constructor 와 Method 태그들 
@see
@since
@deprecated
@param
@return
@throws and @exception
@serialData
{@link}
{@linkplain}
{@inheritDoc}
{@docRoot}
```
/**
 * A Method get MediaPlayer status then play or stop method
 * @see com.tripath.muse.MusiListFragment.java
 * @since 1.0.0
 * @param   
 * @return for log value
 * @versoin   1.0.0
 */
```

reference
[자바 개발자 문서](https://docs.oracle.com/javase/7/docs/technotes/tools/windows/javadoc.html#javadoctags)
