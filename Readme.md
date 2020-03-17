# 실전! 스프링 부트와 JPA 활용

> inflearn의 강의 `실전! 스프링 부트와 JPA활용1 - 웹 애플리케이션 개발`을 따라하며 개인적으로 정리를 해보았습니다.

<br>

##  목차

- ##### 프로젝트 환경설정

  - 프로젝트 생성
  - View 환경 설정
  - h2 DB 설치
  - JPA와 DB 설정
  
- ##### 도메인 분석 설계

  - 요구사항 
  - 도메인 모델 및 테이블
  - 엔티티 클래스 개발

----

<br>

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

### 3. H2 DB 설치

> h2는 가볍고 편리해서 개발이나 테스트 용도로 좋다고 한다.

- [h2 공식 사이트](https://www.h2database.com )
- [1.4.199 버전 다운로드 링크](https://h2database.com/h2-2019-03-13.zip )

- ##### 데이터 베이스 파일 생성하기

  - 압축을 풀어준다.
  - terminal에서  `h2/bin`경로에서 `$ ./h2.sh`로 스크립트 파일을 실행시켜준다.
    - ( 필자 맥에서는 permission denied가 떠서 `$ sudo chmod 755 h2.sh`를 통해 해결함. )
  - 최초에는 `jdbc:h2:~/jpashop`로 연결을 하여 `~/jpashop.mv.db`파일을 생성해준다.

  <img width="489" alt="Screen Shot 2020-02-27 at 12 21 07 AM" src="https://user-images.githubusercontent.com/37801041/75359012-14f52d00-58f7-11ea-847c-5bc0b7e2033c.png">

  - 이후부터 `jdbc:h2:tcp://localhost/~/jpashop `로 접속을 한다.
    - `h2.sh` 스크립트가 종료되면 데이터 베이스가 동작하지 않는다.

<br>

### 4. JPA와 DB 설정

> h2를 설치했으니 이제 스프링부트완 db를 연결해보고 잘 동작하는지 확인을 해보자.

- ##### `application.yml`작성

  > 원래의 프로젝트에는 `application.properties`가 생성되어있겠지만 해당 강의에서는 yaml이 더 편하다고 해서 yml을 사용. properties 파일을 지우고 새로 만들어주자.

  ```yml
  spring:
    datasource:
      url: jdbc:h2:tcp://localhost/~/jpashop;MVCC=TRUE
      username: sa
      password:
      driver-class-name: org.h2.Driver
  
    jpa:
      hibernate:
        ddl-auto: create
      properties:
        hibernate:
  #        show_sql: true
          format_sql: true
  
  logging:
    level:
      org.hibernate.SQL: debug
      org.hibernate.type: trace
  ```

  - `url`에 h2 콘솔에서 등록해준 주소를 넣어주고 `MVCC=TRUE`라는 옵션을 준다. 이 옵션은 h2 1.4.199버전에서만 동작한다. => 만약 1.4.200이 gradle로 받아졌다 `runtimeOnly group: 'com.h2database', name: 'h2', version: '1.4.199'`과 같이 변경하고 gradle을 refresh해주면 된다.

  - `ddl_auto` 옵션은 애플리케이션이 실행 될 때마다 테이블을 drop하고 다시 생성을 하라는 옵션이다. (아래에서 예제로 설명. )

  - `show_sql` 옵션은 System.out으로 실행되는 SQL을 남기는데 아래 `logging`에서 debug모드로 logger를 통해 실행되는 SQL을 출력해주는 것으로  역할은 같은 옵션이다.

    <br>

- #### 동작확인

  - ##### 회원 엔티티 Member 생성

    ```java
    @Entity
    @Getter @Setter
    public class Member {
    
        @Id @GeneratedValue
        private Long id;
        private String username;
    
    }
    ```

    > @GeneratedValue를 사용하면 자동으로 id값을 부여해준다.

  - ##### 회원 레포지토리 MemberRepository 생성

    ```java
    @Repository
    public class MemberRepository {
    
        @PersistenceContext
        private EntityManager em;
    
        public Long save(Member member) {
            em.persist(member);
            return member.getId();
        }
    
        public  Member find(Long id) {
            return em.find(Member.class, id);
        }
    }
    ```

    > 실제 트랜잭션을 하기위한 함수?라고 보면 된다.

  - ##### Test

    > test를 바로 만들어주는 단축키는 `command + shift + t`이다. 그리고 Junit4를 사용해 test를 만들었다.

    ```java
    @RunWith(SpringRunner.class)
    @SpringBootTest
    public class MemberRepositoryTest {
    
        @Autowired MemberRepository memberRepository;
    
        @Test
        @Transactional      // test에 있으면 rollback
        @Rollback(false)
        public void testMember() throws Exception {
            //given
            Member member = new Member();
            member.setUsername("memberA");
    
            //when
            Long saveId = memberRepository.save(member);
            Member findMember = memberRepository.find(saveId);
    
            //then
            Assertions.assertThat(findMember.getId()).isEqualTo(member.getId());
          Assertions.assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
            Assertions.assertThat(findMember).isEqualTo(member);
    
        }
    ```

    > - @Transactional을 붙여주지 않으면 test에러가 난다. 그리고 이 어노테이션이 test에 붙어있다면 test에서 진행이 되고나서 롤백을 시켜준다.
    > - @Rollback(false)를 넣어주면 롤백을 시켜주지 않으므로 테스트를 실행하고 h2콘솔에서 member가 생성되어져 있는 것을 확인할 수 있다.
    > - 마지막의 Assertions에서 `findMember와 member`가 같다. 이것은 한 트랜잭션에서는 캐시에서 가져오는 것으로 동일하다고 한다. 위의 테스트 로그를 봐도 select가 없는 것으로 이것을 알 수 있다.

    - 만약 위와 똑같이 했는데 `"No tests found for given includes: ... "`와 같은 에러가 발생한다면.

      ```
      `preferences - Build, Execution, Deployment - Gradle projects - Run tests using`의 옵션이 `Gradle (Default)`로 되어있다면 'IntelliJ IDEA'로 바꾸고 다시 테스트를 해보자.
      ```

  - ##### Test Log 살펴보기

    - ##### `ddl-auto: create` 옵션으로 인해 실행 할 때마다 drop과 create가 실행된다.

      ```
      drop table member if exists
      
       ...
      
      create table member (
        id bigint not null,
        username varchar(255),
        primary key (id)
      )
      ```

    - ##### `insert`

      ```
      insert 
      into
      	member
      		(username, id) 
      	values
      		(?, ?)
      ```

      - insert를 하고나면 values의 쿼리 파라미터가 (?,?)로 뜬다. 개발할 때 이것들을 확인하면서 하면 편리하므로 이것을 띄우는 두가지 방법을 살펴보자.

        1. `application.yml`의 logging밑에  `org.hibernate.type: trace`를 추가

           > 이렇게 해주면 insert  아래에 다음과 같이 확인할 수 있다.
           >
           > ```
           > 2020-02-27 14:52:49.544 TRACE 31093 --- [           main] o.h.type.descriptor.sql.BasicBinder      : binding parameter [1] as [VARCHAR] - [memberA]
           > 2020-02-27 14:52:49.544 TRACE 31093 --- [           main] o.h.type.descriptor.sql.BasicBinder      : binding parameter [2] as [BIGINT] - [1]
           > ```

        2. 외부 라이브러리 사용

           - `implementation 'com.github.gavlyukovskiy:p6spy-spring-boot-starter:1.5.6' `

           > 해당 라이브러리를 사용하면 다음과 같이 확인 가능하다.
           >
           > ```
           > insert into member (username, id) values ('memberA', 1);
           > ```
           >
           > - 참고 : 외부 라이브러리는 개발이나 테스트 단계에서는 편하게 사용할 수 있지만 실제 운영시스템에 적용을 할때에는 반드시 성능테스트를 해보고 사용하는 것이 좋다고 한다.


<br>

##  도메인 분석 설계

> 프로그램의 요구사항을 토대로 도메인을 분석하고 테이블을 설계하여 엔티티 클래스까지 개발을 해보겠다.

### 1. 요구사항

> 요구사항은 쉽게 생각해서 구현해야 되는 기능과 세부 사항들을 말한다.

- #### 회원 기능

  - 회원 등록
  - 회원 조회

- #### 상품 기능

  - 상품 등록
  - 상품 수정
  - 상품 조회

- #### 주문 기능

  - 상품 주문
  - 주문 내역 조회
  - 주문 취소

- #### 기타 요구사항

  - 상품 제고 관리가 가능
  - 상품은 도서, 음반, 영화 카테고리로 구분할 수 있다.
  - 주문시 배송 정보를 입력할 수 있다.

<br>

### 2. 도메인 및 테이블

> 도메인 모델을 만들고 이를 엔티티로 만들고 이 다이어그램들을 엔티티 클래스 코드로 옮기면 된다.

- #### 도메인 모델

  <img width="921" alt="Screen Shot 2020-02-28 at 4 37 21 PM" src="https://user-images.githubusercontent.com/37801041/75521226-a1643480-5a4a-11ea-8cbc-3853c93b2808.png">

  <br>

- #### 엔티티 분석

  <img width="922" alt="Screen Shot 2020-02-28 at 4 37 29 PM" src="https://user-images.githubusercontent.com/37801041/75521231-a2956180-5a4a-11ea-9088-5f568cac55e2.png">

  <br>

- #### 테이블 분석

  <img width="914" alt="Screen Shot 2020-02-28 at 4 37 40 PM" src="https://user-images.githubusercontent.com/37801041/75521233-a32df800-5a4a-11ea-8b94-62ac46ab3240.png">

> Category와 Item을 다대다 양방향 연관관계로 만들었지만, 실무에서는 사용하지 않는게 좋다고 한다. 해당 강의에서는 이러한 방법도 있다는 것을 알려주려고 했다고한다. 이유는 다대다를 구성하려면 중간 테이블을 두어 1대다, 다대1로 묶어주는데 중간 테이블에 컬럼을 추가할 수 없게된다. 따라서 다대다 대신 중간 엔티티를 하나 만들어주고 1대다, 다대1 로 매핑을 해서 사용하는 것이 좋다고한다.

<br>

### 3. 엔티티 클래스 개발

> 해당 엔티티 클래스를 구현한 코드는 `src/main/java/jpabook/jpashop/domain` 에 있으므로 참고하기바란다.

- 연관관계의 주인을 정할때에는 FK를 가지고 있는 엔티티로 정하는 것이 좋다. ( OneToOne의 경우에는 자주 접근하는 것으로 정하는 것이 좋음.)

- ### 주의점

  #### 1. 엔티티에 가급적 Setter 사용하지 말자

  - 변경 포인트가 너무 많아 유지보수가 어렵다.

  #### 2. 모든 연관관계는 지연로딩(LAZY)로 설정

  - 즉시로딩(EAGER)을 사용하면 예측이 어렵고, JPQL을 실행할 때 n+1문제가 자주 발생.

  - 연관된 엔티티를 함께 조회해야 하면, fetch join 또는 엔티티 그래프 기능을 사용.

  - @XToOne (@ManyToOne, @OneToOne)은 default가 EAGER로 되어있어 꼭 설정해주어야함.

    > `Cmd + Shift + f` 를 사용하여 찾은 다음 모두 바꾼다.

  #### 4. 컬렉션은 필드에서 바로 초기화한다.

  #### 5. 테이블, 컬럼명 생성 전략

  - `SpringPhysicalNamingStrategy` 가 이름을 바꾼다.

    ```
    1. 카멜 케이스 -> 언더스코어 (orderDate -> order_date)
    2. .(점) -> _(언더스코어)
    3. 대문자 -> 소문자
    ```

  
<br>

## 애플리케이션 구현 준비

> 예제를 단순화 하기 위해...
>
> - 로그인과 권한 관리
> - 파라미터 검증과 예외 처리는 최소화
> - 상품은 도서만 구현
> - 카테고리는 사용하지 않음
> - 배송 정보는 사용하지 않음

####  <br>

#### 애플리케이션 아키텍쳐

##### 계층형 구조를 사용 ( Controller -> Service -> Repository -> DB [Domain은 어디서든 접근 가능] )

- Controller, web : 웹 계층
- Service : 비즈니스 로직, 트랜잭션 처리
- Repository : JPA를 직접 사용하는 계층, 엔티티 매니저 사용
- Domain : 엔티티가 모여있는 계층, 모든 계층에서 사용

<br>

##### 패키지 구조

- Jpabook.jpashop
  - domain
  - exception
  - repository
  - service
  - web

<br>

##### 개발 순서

1. Service, Repository 계층 개발
2. Test Case로 작성 후 검증
3. 웹 계층 적용







