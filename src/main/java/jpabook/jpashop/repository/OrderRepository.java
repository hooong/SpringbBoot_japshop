package jpabook.jpashop.repository;

import jpabook.jpashop.dommain.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

    private final EntityManager em;

    public void save(Order order) {
        em.persist(order);
    }

    public Order findOne(Long id) {
        return em.find(Order.class, id);
    }

    // 주문 검색기능
    public List<Order> findAllByString(OrderSearch orderSearch) {

        // 만약 회원명과 상태가 모두 들어오는 경우라면 아래와 같이 쓰면 된다.
        /*
            return em.createQuery("select o from Order o join o.member m" +
                    " where o.status = :status " +
                    " and m.name like :name", Order.class)
                    .setParameter("status", orderSearch.getOrderStatus())
                    .setParameter("name", orderSearch.getMemberName())
                    .setMaxResults(1000)  // 최대 1000건
                    .getResultList();
        */

        // 그러나 이름과 회원명이 둘 다 NULL로 들어온다면 아래와 같을 것이다.
        /*
            return em.createQuery("select o from Order o join o.member m", Order.class)
                    .setMaxResults(1000)  // 최대 1000건
                    .getResultList();
        */

        // 따라서 동적으로 jpql을 만들어야 한다.

        // 아래의 방법은 동적으로 jpql을 만드는 방법 중 하나이지만 사용하기에는 너무 복잡하다.
        String jpql = "select o From Order o join o.member m";
        boolean isFirstCondition = true;

        //주문 상태 검색
        if (orderSearch.getOrderStatus() != null) {
            if (isFirstCondition) {
                jpql += " where";
                isFirstCondition = false;
            } else {
                jpql += " and";
            }
            jpql += " o.status = :status"; }

        //회원 이름 검색
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            if (isFirstCondition) {
                jpql += " where";
                isFirstCondition = false;
            } else {
                jpql += " and";
            }
            jpql += " m.name like :name"; }

        TypedQuery<Order> query = em.createQuery(jpql, Order.class)
                                    .setMaxResults(1000); //최대 1000건

        if (orderSearch.getOrderStatus() != null) {
            query = query.setParameter("status", orderSearch.getOrderStatus());
        }

        if (StringUtils.hasText(orderSearch.getMemberName())) {
            query = query.setParameter("name", orderSearch.getMemberName()); }

        return query.getResultList();
    }

    /**
     * JPA Criteria
     * 이것도 실무에서는 사용하기 별로다.
     * 쿼리가 생기는 것이 눈에 잘 띄지않아서 유지보수에 너무 좋지 못하다.
     * java문법을 사용하면서 유지보수에도 좋은 querydsl을 사용하는 것이 좋기는 하다.
     */
    public List<Order> findAllByCriteria(OrderSearch orderSearch) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Order> cq = cb.createQuery(Order.class);
        Root<Order> o = cq.from(Order.class);
        Join<Object, Object> m = o.join("member", JoinType.INNER);

        List<Predicate> criteria = new ArrayList<>();

        // 주문 상태 검색
        if (orderSearch.getOrderStatus() != null) {
            Predicate status = cb.equal(o.get("status"), orderSearch.getOrderStatus());
            criteria.add(status);
        }
        // 회원 이름 검색
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            Predicate name = cb.like(m.<String>get("name"), "%" +
                    orderSearch.getMemberName() + "%");
            criteria.add(name);
        }

        cq.where(cb.and(criteria.toArray(new Predicate[criteria.size()])));
        TypedQuery<Order> query = em.createQuery(cq).setMaxResults(1000); //최대 1000건
        return query.getResultList();

    }
}

