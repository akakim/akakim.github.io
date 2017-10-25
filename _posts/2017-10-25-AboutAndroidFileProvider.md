## FileProvider 안드로이드 누가 대응기 

[안드로이드 개발자 문서](https://developer.android.com/reference/android/support/v4/content/FileProvider.html)
## FileProvider란 
FileProvider는 ContentProvider의 하위 클래스이다. ContentProvicer는 파일에 대한 공유하는 것을 보안하는 것이다. 파일들을 공유하는 것은 file Uri 스키마 대신, content Uri 스키마를 생성하는 건 앱과 관련되어있다. 

content uri는 당신이 파일을 접근하여 읽고 쓰는 것을 임시적으로 권한들을 사용한다. Intent를 content URI를 포함하여 생성 했기 때문에 content URI를 클라이언트 앱( 당신의 앱이다 ) 으로 보내개 된다. setFlags에 권한을 더 할 수 있다. 이러한 권한들은 클라이언트   클라이언트 앱이 액티비티가 활성화된 상태에서 (권한을) 받는것 (또한 스택에 유지되고 있는동안 ) 가능하게 한다. Intent가 서비스로 가게되면 권한들은 서비스가 동작하는동안 권한들은 유효하다. 

file uri 스키마와 로 통재하기 위해 당신은 시스템의 권한 하에 있는 파일들을 변경해야 할 것이다. 권한들은 가능한 모든 앱에게 당신이 제공하고, 당신이 그들을 바꿀 때까지 남아있게 된다. 이런 수준에서의 접근은 기초적으로 보안이 취약하다.   

FileProvider에서 만들어진 content URI는 향상된 보안적인 접근을 제공한다. FileProvider는 안드로이드의 보안 기반의 핵심이다. 

### fileProvider는 다음 주제들을 포함하고 있다.

1. FilProvider 정의
2. 유효한 파일 명시
3. 파일을 위한 URI Content 를 받는것 
4. 임시적인 권한을 URI에게 주는것 
5. URI Content를 다른 앱에게 제공하는 것 

## 원문 

FileProvider is a special subclass of ContentProvider that facilitates secure sharing of files associated with an app by creating a content:// Uri for a file instead of a file:/// Uri.

A content URI allows you to grant read and write access using temporary access permissions. When you create an Intent containing a content URI, in order to send the content URI to a client app, you can also call Intent.setFlags() to add permissions. These permissions are available to the client app for as long as the stack for a receiving Activity is active. For an Intent going to a Service, the permissions are available as long as the Service is running.

In comparison, to control access to a file:/// Uri you have to modify the file system permissions of the underlying file. The permissions you provide become available to any app, and remain in effect until you change them. This level of access is fundamentally insecure.

The increased level of file access security offered by a content URI makes FileProvider a key part of Android's security infrastructure.

This overview of FileProvider includes the following topics:

1. Defining a FileProvider
2. Specifying Available Files
3. Retrieving the Content URI for a File
4. Granting Temporary Permissions to a URI
5. Serving a Content URI to Another App
