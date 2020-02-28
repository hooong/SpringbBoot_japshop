package jpabook.jpashop.dommain.item;

import jpabook.jpashop.dommain.Category;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)   // 하나의 테이블에 모두 때려박고 dtype으로 구분
@DiscriminatorColumn(name = "dtype")
@Getter @Setter
public abstract class Item {

    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;
    private int price;
    private int stockQuantitu;

    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();
}
