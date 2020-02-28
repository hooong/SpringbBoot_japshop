package jpabook.jpashop.dommain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;

@Embeddable
@Getter
public class Address {
    // 값 타입은 변경 불가능하게 설계해야함.

    private String city;
    private String street;
    private String zipcode;

    // jpa 스펙상 기본 생성자가 있어야함.
    // private는 안되도 protected까지는 지원
    protected Address() {
    }

    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }
}
