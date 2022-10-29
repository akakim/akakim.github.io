---
layout: post
title: git ignore 파일 draft
---

#### .gitignore이란

Project에 원하지않는 Backup File이나, LogFile, 혹은 컴파일된 파일들을 Git에서 ㅔㅈ외할 수있는 설정 file이다. 
1. Git ignore파일 만들기 
- 항상 최상위 Directory에 존재해야한다. 


Git이나 SVN등에서 서버에 올리지 않아야할 파일들이 있습니다.
- 예를들어 컴파일 이후 실행파일


Git ignore 파일의 예시
(Android Studio가 기본으로 생성하는 Project경로/.gitignore 파일 기준)
```
*.iml
.gradle
/local.properties
/.idea/workspace.xml
/.idea/libraries
.DS_Store
/build
/captures
.externalNativeBuild
```

다음과 같이 작성이 되어있습니다.

이것을 다시 풀어보자면,

기본적으로 .gitIgnore 파일 기준으로.

1. 확장자가 iml인 모든 파일
2. .gradle drectory에 들어있는 것들
3. GIT_IGNORE_PATH/local.properties 파일
4. GIT_IGNORE_PATH/.idea/workspace.sml 파일
5. GIT_IGNORE_PATH/.idea/libraries 폴더 무시 
6. .DS_Store 에 들어있는 파일
7. GIT_IGNORE_PATH/.idea/libraries 폴더 무시
8. 


### Pattern Form

** 패턴(pattern)은 규칙라고 보통 해석을 한다. gitignore에서는 폴더(or diectory)의 경로나, 파일 명,파일명의 규칙등 을 통틀어서 의미한다. **


 - 빈라인 한 칸은 파일이 없는것과 같다 그래서 읽기 위한 구분자로서 간주된다.
 - "#" 으로 시작되는 것은 주석으로 간주된다. 백슬러쉬 ("\")를 해쉬로 시작하는 패턴들을 위한 첫 해쉬 앞에 놓는다. 
 - 백슬러쉬가 쿼트(Quote)로써 함깨 있지 않는이상 추적하는 공간은 무시된다.
 - 접두사 느낌표"!"는 규칙을 부정하는 것이다. 선택하는 느낌표는 

An optional prefix "!" which negates the pattern; any matching file excluded by a previous pattern will become included again. It is not possible to re-include a file if a parent directory of that file is excluded. Git doesn’t list excluded directories for performance reasons, so any patterns on contained files have no effect, no matter where they are defined. Put a backslash ("\") in front of the first "!" for patterns that begin with a literal "!", for example, "\!important!.txt".


If the pattern ends with a slash, it is removed for the purpose of the following description, but it would only find a match with a directory. In other words, foo/ will match a directory foo and paths underneath it, but will not match a regular file or a symbolic link foo (this is consistent with the way how pathspec works in general in Git).
- 만일 슬래쉬("/") 패턴으로 끝난다면, 그것은 
- 
If the pattern does not contain a slash /, Git treats it as a shell glob pattern and checks for a match against the pathname relative to the location of the .gitignore file (relative to the toplevel of the work tree if not from a .gitignore file).

Otherwise, Git treats the pattern as a shell glob suitable for consumption by fnmatch(3) with the FNM_PATHNAME flag: wildcards in the pattern will not match a / in the pathname. For example, "Documentation/*.html" matches "Documentation/git.html" but not "Documentation/ppc/ppc.html" or "tools/perf/Documentation/perf.html".

A leading slash matches the beginning of the pathname. For example, "/*.c" matches "cat-file.c" but not "mozilla-sha1/sha1.c".

Two consecutive asterisks ("**") in patterns matched against full pathname may have special meaning:

A leading "**" followed by a slash means match in all directories. For example, "**/foo" matches file or directory "foo" anywhere, the same as pattern "foo". "**/foo/bar" matches file or directory "bar" anywhere that is directly under directory "foo".

A trailing "/**" matches everything inside. For example, "abc/**" matches all files inside directory "abc", relative to the location of the .gitignore file, with infinite depth.

A slash followed by two consecutive asterisks then a slash matches zero or more directories. For example, "a/**/b" matches "a/b", "a/x/b", "a/x/y/b" and so on.

Other consecutive asterisks are considered invalid.





[git ignore 문서](https://git-scm.com/docs/gitignore)