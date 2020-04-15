package jpabook.jpashop.service;

import jpabook.jpashop.dommain.item.Book;
import jpabook.jpashop.dommain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional
    public void saveItem(Item item) {
        itemRepository.save(item);
    }

    // 준영속 엔티티를 변경감지를 이용하여 update하는 방법
    @Transactional
    public Item updateItem(Long itemId, String name, int price, int stockQuantity) {
        // db에서 찾아온 객체에서 수정을 하니까 변경감지를 하게 된다.
        Item findItem = itemRepository.findOne(itemId);
        // 아래처럼 set을 해주는 것보다 메서드를 만들어 엔티티 자체에서 수정을 하는 것이 좋다.
        findItem.setPrice(price);
        findItem.setName(name);
        findItem.setStockQuantity(stockQuantity);
        return findItem;
    }

    public List<Item> findItems() {
        return itemRepository.findAll();
    }

    public Item findOne(Long itemId) {
        return itemRepository.findOne(itemId);
    }

}
