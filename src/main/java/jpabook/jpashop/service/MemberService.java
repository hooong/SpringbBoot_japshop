package jpabook.jpashop.service;

import jpabook.jpashop.dommain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)  // 조회 트랜잭션에서는 성능이 좋아진다.
@RequiredArgsConstructor    // final이 있는 것의 생성자를 만들어줌.
public class MemberService {

    private final MemberRepository memberRepository;

    // 회원 가입
    @Transactional   // 바로 아래 메서드에 대해서는 우선순위가 높게 적용
    public Long join(Member member) {
        validateDuplicateMember(member);  // 중복 회원 검증
        memberRepository.save(member);  // persist를 해주면 영속성 컨텍스트에 id값이 생성되어 있는 것이 보장됨.

        return member.getId();
    }

    // 이렇게만 하면 동시에 같은 이름으로 가입을 하려할때 문제가 생김.
    // 그래서 이를 방지하기 위해서 db에서 유니크를 써주는 것이 좋음.
    private void validateDuplicateMember(Member member) {
        // EXCEPTION
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    // 회원 전체 조회
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    // 하나의 회원 검색
    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }
}
