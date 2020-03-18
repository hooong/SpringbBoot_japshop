package jpabook.jpashop.repository;

import jpabook.jpashop.dommain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    // @PersistenceContext의 경우 최신 스프링 데이터 jpa에서 @Autowired로도 가능하게 만들어져있다.
    // 따라서 생성자 주입 방식으로 방식을 통일 시켜줄 수 있다.
    private final EntityManager em;

    public void save(Member member) {
        em.persist(member);
    }

    public Member findOne(Long id) {
        return em.find(Member.class, id);
    }

    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class).getResultList();  // jpql을 사용한다. (Sql과 다르게 엔티티로 찾음)
    }

    // 이름으로 멤버를 찾는 로직
    public List<Member> findByName(String name) {
        return em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();
    }
}
