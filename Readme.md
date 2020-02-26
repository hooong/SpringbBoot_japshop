# 실전! 스프링 부트와 JPA 활용

> inflearn의 강의 `실전! 스프링 부트와 JPA활용1 - 웹 애플리케이션 개발`을 따라하며 개인적으로 정리를 해보았습니다.

<br>

## 목차

- ##### 프로젝트 환경설정



## 프로젝트 환경설정

> 프로젝트를 생성하고 환경설정까지하는 과정을 정리

### 1. 프로젝트 생성

- ##### 스프링 부트 스타터 ( https://start.spring.io/ ) 사용하여 손쉽게 프로젝트를 생성

  <img width="1792" alt="Screen Shot 2020-02-26 at 5 36 35 PM" src="https://user-images.githubusercontent.com/37801041/75326770-92507b80-58be-11ea-843f-1590aa5f2775.png">

- ##### Gradle 사용

- ##### Dependencies

  - #####  `Spring Web` ( 스프링 부트 웹개발에 필요 )

  - #####  `Thymeleaf` ( jsp대신 사용할 뷰 템플릿 )

  - #####  `Spring Data JPA` ( jpa를 쉽게? 편리하게? 사용하기 위한 라이브러리 )

  - #####  `H2 Database` (h2 DB를 사용하기 위해 필요 )

  - #####  `Lombok` ( Getter나 Setter 같은 것을 annotation만으로 쉽게 설정 가능 )

- ##### `Generate`버튼을 누르면 (Artifact).zip파일을 다운로드

- ##### intelliJ에서 `build.gradle` import

- ##### Lombok을 사용하기 위한 준비

  - `preferences - plugins`에서 `Lombok`를 설치해준다.

  - `preferences - Annotation Processors`에서 `Enable annotation processing`을 체크해 활성화 시켜준다.