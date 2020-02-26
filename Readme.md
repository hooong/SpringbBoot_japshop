# 실전! 스프링 부트와 JPA 활용

> inflearn의 강의 `실전! 스프링 부트와 JPA활용1 - 웹 애플리케이션 개발`을 따라하며 개인적으로 정리를 해보았습니다.

<br>

##  목차

- ##### 프로젝트 환경설정

<br>

## 프로젝트 환경설정

> 프로젝트를 생성하고 환경설정까지하는 과정을 정리

<br>

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

- ##### 동작 테스트

  - `jpashop/src/main/java/jpabook/jpashop/JpashopApplication.class`  해당 클래스를 실행.
  - `localhost:8080`으로 접속한 뒤 `Whitelabel Error Page`에러가 나온다면 정상 작동.

<br>

### 2. View 환경 설정

> view로는 thymeleaf를 사용한다. 
>
> - [thymeleaf의 공식 사이트](https://www.thymeleaf.org/)
>
> 또한 스프링 가이드 페이지도 참고할 수 있다.
>
> - [스프링 가이드](https://spring.io/guides)

<br>

- #### 특정 뷰를 맵핑하기

  - `src/resources/templates` 폴더 안에 `hello.html`을 만든다.

    ```html
    <!DOCTYPE HTML>
    <html xmlns:th="http://www.thymeleaf.org">
    <head>
        <title>Hello</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" /> </head>
    <body>
    <p th:text="'안녕하세요. ' + ${data}" >안녕하세요. 손님</p>
    </body>
    </html>
    ```

    - 서버사이드 없위 위의 html파일을 열면 "안녕하세요. 손님"이 뜨지만 서버에서 data를 받아오면 "안녕하세요. 'data'"가 나온다. 이것은 thymeleaf의 특징이다.

  - `src/main/java/jpabook/jpashop`에 `HelloController` 클래스를 생성한다.

    ```java
    @Controller
    public class HelloController {
    
        @GetMapping("hello")
        public String hello(Model model) {
            model.addAttribute("data", "hello");
            return "hello";
        }
    }
    ```

    - 여기서 return이 될때 스프링부트의 로직을 통해 `resources:templates/hello.html`이 되어 템플릿을 렌더링해주게 된다.

  - 서버를 실행하고 `localhost:8080/hello`로 접속해 "안녕하세요. hello"가 뜨는지 확인해보자.

<br>

- #### 정적인 페이지 만들기 ( index )

  - `src/static` 폴더 안에 `index.html`을 만든다.

    ```html
    <!DOCTYPE HTML>
    <html xmlns:th="http://www.thymeleaf.org"> <head>
        <title>Hello</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" /> </head>
    <body>
    Hello
    <a href="/hello">hello</a>
    </body>
    </html>
    ```

  - 서버를 다시 실행하고 `localhost:8080`으로 접속하고 "Hello hello"가 뜨고 hello를 클릭하면 위에서 맵핑한 hello페이지로 가는지 확인한다.

  <br>

- 템플릿을 수정하다보면 바꾸고 확인하는 과정을 많이 반복해야하는데 이 과정마다 서버를 새로 키는데는 시간이 많이 걸리고 귀찮다. 따라서 아래 dependency를 추가함으로 편하게 개발을 할 수 있다.

  - `build.gradle`의 dependencies에 `'org.springframework.boot:spring-boot-devtools'`를 추가해준다.

    ```
    dependencies {
    	
    	... 
    	
    	implementation 'org.springframework.boot:spring-boot-devtools'
    	
    	...
    	
    }
    ```

  - 그럼 이제 템플릿을 변경하고 `Build - Recompile`만으로 새로고침을 하면 변경 내용이 반영되는 것을 확인할 수 있을 것이다.

<br>

### 4. H2 DB 설치

> h2는 가볍고 편리해서 개발이나 테스트 용도로 좋다고 한다.

- [h2 공식 사이트](https://www.h2database.com )
- [1.4.199 버전 다운로드 링크](https://h2database.com/h2-2019-03-13.zip )

- ##### 데이터 베이스 파일 생성하기

  - 압축을 풀어준다.
  - terminal에서  `h2/bin`경로에서 `$ ./h2.sh`로 스크립트 파일을 실행시켜준다.
    - ( 필자 맥에서는 permission denied가 떠서 `$ sudo chmod 755 h2.sh`를 통해 해결함. )
  - 최초에는 `jdbc:h2:~/jpashop`로 연결을 하여 `~/jpashop.mv.db`파일을 생성해준다.
  - 이후부터 `jdbc:h2:tcp://localhost/~/jpashop `로 접속을 한다.
    - `h2.sh` 스크립트가 종료되면 데이터 베이스가 동작하지 않는다.