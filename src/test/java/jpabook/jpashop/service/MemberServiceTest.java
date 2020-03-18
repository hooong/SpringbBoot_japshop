package jpabook.jpashop.service;

import jpabook.jpashop.dommain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)    // 스프링과 테스트 통합하기 위함.
@SpringBootTest  // 스프링 컨네이너 안에서 테스트를 하기 위함. 없으면 @Autowired가 안됨.
@Transactional  // 이 어노테이션이 Test에서 쓰이면 자동으로 롤백을 함.
public class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    EntityManager em;

    @Test
    public void 회원가입() throws Exception {
        //given
        Member member = new Member();
        member.setName("Hong");

        //when
        Long savedId = memberService.join(member);

        //then
        // em.flush();
        // 기존 테스트에서는 자동으로 Rollback을하고 db로 flush가 되지 않기 때문에 영속성 컨텍스트에만 들어가고 rollback이 된다.
        // 그래서 EntityManager를 주입받고 flush를 해주면 insert 쿼리가 나가는 것을 로그에서 확인가능하고
        // @Rollback(false) 어노테이션을 사용해 실제 디비에서도 확인해볼 수 있음.
        assertEquals(member, memberRepository.findOne(savedId));
    }

    // try-catch를 사용안해도 되는 방법이다.
    @Test(expected = IllegalStateException.class)
    public void 중복_회원_예외() throws Exception {
        //given
        Member member1 = new Member();
        member1.setName("hong1");

        Member member2 = new Member();
        member2.setName(("hong1"));

        //when
        memberService.join(member1);
        memberService.join(member2); // 여기서 예외 발생.

        //then
        fail("예외가 발생해야 한다.");
    }
}