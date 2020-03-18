package jpabook.jpashop.repository;

import jpabook.jpashop.dommain.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {

    private final EntityManager em;

    public void save(Item item) {
        // 이 부분은 나중에 다시 설명.
        if (item.getId() == null) {
            // item은 jpa에 저장되기 전까지 id값이 없다.
            em.persist(item);
        } else {
            // 이미 있는 item을 강제로 업데이트
            em.merge(item);
        }
    }

    public Item findOne(Long id) {
        return em.find(Item.class, id);
    }

    public List<Item> findAll() {
        return em.createQuery("select i from Item i", Item.class)
                .getResultList();
    }
}
