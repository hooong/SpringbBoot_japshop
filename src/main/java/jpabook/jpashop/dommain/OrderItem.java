package jpabook.jpashop.dommain;

import jpabook.jpashop.dommain.item.Item;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.FetchType.*;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)  // 서비스에서 생성자를 사용하지 못하게 함.
public class OrderItem {

    @Id @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private int orderPrice; // 주문 가격
    private int count;  // 주문 수량

    //==생성 메서드==//
    public static OrderItem createOrderItem(Item item, int orderPrice, int count) {
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        // 여기서 item의 price를 쓰지 않는 이유는 쿠폰이나 할인 같은 경우를 생각하는 것임.
        orderItem.setOrderPrice(orderPrice);
        orderItem.setCount(count);

        item.removeStock(count);
        return orderItem;
    }

    //==비즈니스 로직==//
    public void cancel() {
        // 주문 아이템이 가지고 있는 Item의 Stock을 주문수량인 count만큼 다시 더해줌.
        getItem().addStock(count);
    }

    //==조회 로직==//
    /**
     * 주문 전체 가격 조회
     */
    public int getTotalPrice() {
        return orderPrice * count;
    }
}
