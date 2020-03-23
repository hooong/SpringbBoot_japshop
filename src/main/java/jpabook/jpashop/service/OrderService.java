package jpabook.jpashop.service;

import jpabook.jpashop.dommain.Delivery;
import jpabook.jpashop.dommain.Member;
import jpabook.jpashop.dommain.Order;
import jpabook.jpashop.dommain.OrderItem;
import jpabook.jpashop.dommain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import jpabook.jpashop.repository.MemberRepository;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    /**
     * 주문
    */
    @Transactional
    public Long order(Long memberId, Long itemId, int count) {
        // 엔티티 조회
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);

        // 배송정보 생성
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());

        // 주문상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

        // 주문 생성
        Order order = Order.createOrder(member, delivery, orderItem);

        // 주문 저장
        orderRepository.save(order);    // cascade All옵션 덕분에 가능. (private Owner일 경우에 써주는 것이 좋다.)
        /* 이 경우에는 delivery와 orderItem이 Order만이 사용하기 때문에 casecade all을 설정해 order가 persist될때
         강제로 모두 persist가 되게끔 한것이다. 만약 delivery가 다른 곳에서도 사용이 된다면 casecade를 사용하지 말고
         별도의 repository를 만들어서 persist를 해주어야한다.*/

        return order.getId();

    }

    /**
     * 취소
     */
    @Transactional
    public void cancelOrder(Long orderId) {
        // 주문 엔티티 조회
        Order order = orderRepository.findOne(orderId);
        // 주문 취소
        order.cancel();
    }

    /**
     * 검색
     */
    public List<Order> findOrders(OrderSearch orderSearch) {
        return orderRepository.findAllByCriteria(orderSearch);
    }
}
