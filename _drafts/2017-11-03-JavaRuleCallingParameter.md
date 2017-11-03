---
layout: post
title: java는 call by value 인가 call by reference 인가
date:   2017-11-03 17:30:20 +0300
tags: java,call by value,call by reference
---

# Java 는 Call by reference를 기본으로 하는가 아니면 Call by Value 를 기본으로 하는가 .


---
### **말하기에 앞서 C나 C++에서 포인터에 대한 간단한 개념을 이해하고 있으면 됩니다.**

## 1. Call by Value 란
어떤 변수를 값을 참조++**만**++ 하고 참조한 변수값에 반영 하지 않는 것 이다.


## 2. Call by Reference란
어떤 변수를 값을 참조한다. 참조한 변수에 뭔가 값이 변경된다면 같이 변경 하는 것이다. 

## 3. C언어의 예제

흔히들 Swap 함수를 예제로 든다.

```
int main (){

	int a = 10;
    int b = 20 ;
    
    swapCallByValue ( a, b );
    
    swapCallByReference( &a , &b );

}

// 1 이 경우 main의 입장에서 
// a = 10 b = 20값을 가진다. 
void swapCallByValue( int a , int b ){
  int tmp = a ;
  a = b;
  b = tmp;

}

// 2 이 경우 main의 입장에서 
// a = 20 b= 10 의 값으로 정상적으로 바뀌게 된다. 
void swapCallByValue( int *a , int *b ){
  int tmp = (*a) ;
  a = b;
  b = &tmp;

}

1.  1 경우는 메모리 상에서 메인의 a와 b의 값이 swap 함수의 인자로 만 전달된다.
2.  함수의 메모리 영역에서만 a 와 b의 복사된 값이 변경된다. 
3.  함수가 끝나고, Main의 a 와 b 에는 변경된 값이 반영 되지 않는다. 

1. 2 경우 메인의 a와 b의 주소 값을 swap 함수의 인자로 전달한다. 
2. 함수에서 swap을 하고 참조한 주소값을 변경한다. 
3. 함수가 끝나고, Main과 a와 b의 값은 변경된 값이 전달된다. 

```

---

결론적으로  Call by Value 와 Call by Reference의 차이점이란.

쉽게말해서 어떤 변수를 참고 했다. 
어떤 변수를 변경한 값을 반영하면 Call by Reference 
아니면 Call by Value 이다. 


그렇다면 자바는 기본적으로 Call by reference일까 아닐까 . ?
답은 반반 이였다. 

### 샘플 코드 
```
public static void main(String[] args) {
		// TODO Auto-generated method stub


		System.out.println( " ----- call by value -----");
		Person p = new Person( "wooowooo");
		System.out.println(p.toString());
		callByValue(p);

		System.out.println(p.toString());

        //1.  p의 필드 값이 정상적으로 변경된다.  
		Person p2 = new Person( "person12");

		System.out.println(" ---- call by referernce -----" ) ;
		System.out.println(p2.toString());
		callByReference(p2);
      
		//2.  p가 변경되질 않는걸 확인 할 수 있다.
		System.out.println(p2.toString());


		// 이 후는 사용자가 만들지 않는 클래스에 대해서 테스팅을 했다. 
		System.out.println(" ---- in String case of call by referernce -----" ) ;

		String inputValue = " input Value for String ";
		System.out.println(inputValue);
		callByReferenced(inputValue);
		System.out.println(inputValue);
		boolean isPrimitive = false;

		System.out.println(" ---- in boolean case of call by referernce -----" ) ;


		System.out.println("isPrimitive : " + isPrimitive);
		reference(isPrimitive);
		System.out.println("isPrimitive : " + isPrimitive);

	}

	public static void reference(boolean isPrimitive){
		isPrimitive = true;
	}
	public static void callByValue(Person p ){
		p.name = "CallByValueKevin";
	}

	public static void callByReference ( Person p){
		p = new Person("CallByReferencePerson");
	}

	public static void callByReferenced( String input){
		input = "hey i ' m referernced input";
	}
	static class Person{
		public String name;

		Person(String name){
			this.name = name;
		}

		@Override
		public String toString(){
			return "Person name : " + this.name;

		}
	}
```


예제를 실행 시켜보면 확인 할 수 있지만 
자바는 인스턴스는 Call by Value 방식으로 값을 참조하고 
인스턴스 안의 멤버들은 Call by Reference로 값이 반영됨을 알 수 있습니다. 

다음 과같이 java의 인용문이 있습니다. 

>It seems that the designers of Java wanted to make sure nobody confused their object pointers with the evil manipulable pointers of C and C++, so they decided to call them references instead. So Java books usually say that objects are stored as references, leading people to think that objects are passed by reference. They’re not, they’re passed by value, it’s just that the value [on the stack at JVM level] is a pointer.
>
번역 : 이것은 자바 디자이너들이 어느 누구도 객체의 포인터들에 대해서 햇갈리지 않도록하는 것으로 보인다. C나 C++의 포인터가 악마적으로 변형하는 것을 햇갈리는 것이다. 그래서 그들은 그것들을 참조하는 것 대신 사용하도록 결정했다. 그래서 자바는 종종 객채들을 참고하는 것처럼 저장됨을 예약해놓고, 사람들이 참조로 객체를 전달하는 것처럼 생각하도록 이끌었다. 그것은 아니다 그들은 값으로 전달된다. 그것은 포인터의 값일 뿐이다. 


reference
[샘플 코드 참고 1 ](http://trypsr.tistory.com/74)
[샘플 코드 참고 2 ](http://mussebio.blogspot.kr/2012/05/java-call-by-valuereference.html)