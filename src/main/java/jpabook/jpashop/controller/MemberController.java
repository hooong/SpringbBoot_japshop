package jpabook.jpashop.controller;

import jpabook.jpashop.dommain.Address;
import jpabook.jpashop.dommain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/members/new")
    public String createForm(Model model) {
        model.addAttribute("memberForm", new MemberForm());
        return "members/createMemberForm";
    }

    @PostMapping("/members/new")
    public String create(@Valid MemberForm memberForm, BindingResult result) {

        /*
        * form을 새로 만들어서 사용하는 이유
        * 화면과 도메인에서 각각 원하는 데이터가 다를 수 있고 validation도 다를 수 있다.
        * 따라서 화면에 fit한 form을 만들어서 입력을 받고 정제를 한 뒤 엔티티를 사용해 저장하는 것이 좋다.
        * */

        if (result.hasErrors()) {
            return "members/createMemberForm";
        }

        Address address = new Address(memberForm.getCity(), memberForm.getStreet(), memberForm.getZipcode());

        Member member = new Member();
        member.setName(memberForm.getName());
        member.setAddress(address);

        memberService.join(member);
        return "redirect:/";
    }

    @GetMapping("/members")
    public String list(Model model) {

        /* 이 과정에서도 엔티티와 살짝이라도 다르게 화면에 보여준다면 DTO(Data Transfer Object)를 사용하는게 좋다.
        *  물론 템플릿 엔진에서는 엔티티를 넘겨 보여주고 싶은 필드만 보여주어도 되지만
        *  API를 만들때는 꼭 DTO를 사용해야한다. 그렇지 않으면 API스펙이 바뀌어 정말 나쁘다.*/

        List<Member> members = memberService.findMembers();
        model.addAttribute("members", members);
        return "members/memberList";
    }
}
