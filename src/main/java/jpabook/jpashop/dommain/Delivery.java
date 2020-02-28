package jpabook.jpashop.dommain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.FetchType.*;

@Entity
@Getter @Setter
public class Delivery {

    @Id @GeneratedValue
    @Column(name = "delivery_id")
    private Long id;

    @OneToOne(mappedBy = "delivery", fetch = LAZY)
    private Order order;

    @Embedded   // 값 타입 사용법
    private Address address;

    @Enumerated(EnumType.STRING)  // Ordinary를 쓰면 순서가 다 밀릴 수 있다.
   private DeliveryStatus status;  // READY, COMP
}
