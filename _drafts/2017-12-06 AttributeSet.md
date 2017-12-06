
### Attribute의 키 값 이란. 
뷰의 속성을 정의하는 이름이다. 

커스텀 뷰를 제작 시 각자에 맞게 일관성 있게 보이고 싶다면 style 태그를 적용하면 된다.
이 style 태그의 속성을 enum과 유사하게 이름 자체를 정의 할 수 있다. 

그것이 attribute set이다. 
Attribute set은 UI의 속성을 정의하는 Interface이다. 

Attirbute set을 이용하여 뷰가 가지고있는 속성 값을 Key와 value처럼 쌍으로 이루어지는 형태처럼 xml 에서 적용이 가능하다. 

예를들어 다음과 같이 Attr은 xml상에서 정의한 AttributeSet이다. 


```<resources>
    <declare-styleable name="CET">
        <attr name="MaxLines" format="integer"/>
        <attr name="MinLines" format="integer"/>
        <attr name="TextColor" format="color"/>
        <attr name="InputType" format="integer" />
        <attr name="Hint" format="string" />
    </declare-styleable></resources>
```
 attibute를 적용한 커스텀 뷰이다. 
 
 ```
 <com.test.ui.ClearableEditText
        xmlns:cet="http://schemas.android.com/apk/res/com.test.ui"
        android:id="@+id/clearableEditText2"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        cet:MaxLines="2"
        cet:MinLines="1"
        cet:TextColor="#0000FF"
        cet:InputType="" <---I cant set this property--------->
        cet:Hint="Clearable EditText Hint">
</com.test.ui.ClearableEditText>
```
코드에서 보이듯 cet으로 정의한 스타일의 속성값들이 적용될 수 있다. 
 
