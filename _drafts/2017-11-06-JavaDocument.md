---
layout:post
title: java 문서작성
tags: javaDocument

---

### 주석의 종류 
- 문서 주석은 `/** */`으로 경계가 결정된다. JavaDoc을 이용하여 HTML 파일 형태로 만들 때 사용된다.
- 구현 주석은 `/^ ^/`에  `//`의해 경계가 결정된다. 개별적 구현에 대한 주석코드와는 상관없는 주석이다.


### 문서 주석 
- 자바 클래스, 인터페이스, 생성자, 메서드 필드 들을 성명 
- 주석 경계기호인 `/** */` 안에 들어감 
- 클래스, 인터페이스, 멤버 하나씩 들어감 
- 선언 전에 나와야됨 
- 최상위 레벨 클래스와 인터페이스는 열어쓰지않고, 멤버들은 들여쓴다. 
- 클래스에 대한 문서 주석의 첫 줄에서 4개의 스페이스로 들여쓰고 그 이후 5개의 스페이스 들여쓰기를 한다. 
- 문서 주석에 적절하지 않는 클래스, 인터페이스,변수,메서드에 대한 정보를 제공하려면 선언 바로 후에 block주석이나 single line 주석을 이용한다.

#### 1.클래스 설명 주석
- import 문 다음에 기술 
- 블록 주석 사용 
- 각 라인은 *로 시작
- 해당클래스의 용도 기술 

예를들어 
```
/**

 * 이 클래스는 ...을 위한 클래스다. 

 * 이 클래스는 ...한 경우 .... 역할을 수행한다.

 * 이 클래스는 ...한 방법으로 사용될 수 있으며, ....

 * ... 기능에 대한 콘트롤은 ... 메소드를 사용하여 수행한다.

 * 이 클래스는 직렬화에 대해 보장되지 않았으므로 멀티쓰레드를 

 * 사용할 경우, ...한 방법을 적용해야 한다.

 *

 * @version 1.02 03/14/01

 * @author 이동훈

 * @see com.javacan.event.SomeEvent

 * @since JDK1.3

 */
```
#### 2. 멤버 변수 설명 주석 
- 변수 상단에 위치
- 블록 주석을 사용 
- 용도,제한사항등을 기술 
- 기술 순서 : Constant - static - primitive - reference

예를들어 
```
public 상수들 (즉, final 또는 static final 멤버 필드) 

public 변수들 



proteted 상수들 

proteted 변수들 



package 상수들 

package 변수들 



private 상수들 

private 변수들 

이런식으로 정리 후 

/**

 * 원주율인 파이값에 대한 가장 근사치를 나타내는 double형의 값.

 */

public static final double PI = 3.14159265358979323846;

다음과 같이 작성한다. 
```

#### 4. 멤버 함수 설명 주석 
- 함수의 상단에 위치 
- 블록주석을 사용
- 메서드 기능을 1~2줄로 간결하게 설명
- 메소드의 파라미터를 type명과 변수명을 적고 간략하게 설명 
- 리턴변수,예외사항 설명 


```
/**
 * 라디안 단위의 각도를 해당하는 도(degree)단위로 변환한다.

 *

 * @param angrad 라디안 단위의 각도

 * @return angrad에 해당하는 각도의 도(degree)

 * @since JDK 1.2

 */

public static double toDegrees(double angrad) {

  return angrad * 180.0 / PI;

}
```

### 구현 주석 
- block
- single-line
- trailing
- endofline

#### block
- 파일,메서드,자료구조,알고리즘의 설명 제공 
- 각각의 파일이 시작될 때와 메서드 전에 사용 
- 메서드 안에 존재하는 block 주석은 설명하는 코드와 같은 레벨로 들여 쓰기 

```
/*

 * 여기에 block comment 작성

 */
```

#### single line 주석 
- 짧은 주석은 뒤에 다라오는 코드와 같은 레벨의 들여쓰기를 하는 한 줄로 나타남 
- 한줄로 안써지면 block 주석 형식을 따라감 

```
if(condition) {

/* single line 주석 */

:

}

    if(condition2){

    */ single line 주석 */

    ....

    }
```


#### trailing 
- 코드에 방해가 안되도록 한쪽끝에 놓는다. 

```
public long getBusnissCalc(){

	long result = -1L;

    if ( ... ) {								/*  something trailer*/


    }else if ( ... ){							/* something trtailer 2 */
    
    
    }

}
```

#### end of line 주석 
- 한 줄 모두 주석처리하거나 한 줄의일부분을 주석처리 
- 본문 주석을 위하여 여러줄에 연속되어 사용하면 안됨


```


if(condition) {                          // end of line 주석

    expression1 ;

    expression2 ;

}

else {                                   // end of line 주석

    expression3 ;

    expression4 ;

}

```
### 블럭태그의 종류 
@author 이름  : 클래스나 인터페이스 제작자 표시 
@version 테스트 : 클래스나 인터페이스 버전정보 
@param 매개변수 - 이름 설명 : 매개변수 설명
@return 설명 : 메소드가 void를 리턴하거나
@exception or @thorws 메소드가 발생 시킬 수 있는 예외를 기술 
@serial : 기본적으로 직렬화 할 수 있는 클래스의 멤버를 설명
@see : 어떤 클래스 , 인터페이스,메소드, 생성자 혹은 URL에 대한 전후 참조 표시 
@since Tag를 가진 객체가 언제 추가 되었는 지를 명시 

reference 
[Rough Existence](http://roughexistence.tistory.com/70)
[wjsb.tistory.com](http://wjsb.tistory.com/31)
[블록태그의 종류](http://6kkki.tistory.com/1)