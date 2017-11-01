---
layout: post
title: github 홈페이지 제작기 
tags: Liquid
time: 2017-11-01 15:29:50 +300
---

# Liquid란 

## Introduction 

Liquid 코드는 Objects, tags 그리고 filters로 분류된다. 

### objects
objects란 liquid는 페이지안의 보여주는 것이다. 객체와 변수명은 더블 중괄호로 감싸여진다. 

**input**
```
{{ page.title }}

```

**output**
Introduction

이 경우 객체애 대한 내용물은 page.title을 호출하고 객체는 Introductio을 포함하고 있다. 

### Tags
**Tags**는 템플릿을 위한 논리적이고 제어하는 흐름을 생성한다. 그것은 중괄호와 퍼센트 기호로 표현된다.

마크 업은 태그들을 사용하지 않는다 그 태그는 어던 보이는 text를 제공하지 않는다.
이것의 의미하는 것은 당신은 변수를 할당 하고, 조건을 생성하고 루프를 할 수 있다. 
 또한 페이지상에서 어떤 Liquid 로직이 보이는 것과 상관없이 가능하다. 

**input**

```
{% if user %}
Hello{{user.name}}
{% endif %}
```

**output**
Hello Adam!

Tags는 세가지 타입으로 나뉘어진다. 
* 흐름 제어 
* 반복 
* 변수 할당


### Filters

**Filters**는 Liquid 객체의 출력을 변화시킬 수 있다. 그것들은 '|' 로 나눠서 표현 후 하나의 출력으로 












[원문 Liquid 인트로](https://shopify.github.io/liquid/basics/introduction/)