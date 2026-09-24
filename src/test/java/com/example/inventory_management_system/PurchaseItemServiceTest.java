package com.example.inventory_management_system;

import com.example.inventory_management_system.model.PurchaseItem;
import com.example.inventory_management_system.repository.PurchaseItemRepository;
import com.example.inventory_management_system.service.PurchaseItemService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PurchaseItemServiceTest {

    private final PurchaseItemRepository purchaseItemRepository =
            mock(PurchaseItemRepository.class);

    private final PurchaseItemService purchaseItemService =
            new PurchaseItemService(purchaseItemRepository);

    @Test
    void createPurchaseItemReturnsSavedPurchaseItem() {

        PurchaseItem purchaseItem = new PurchaseItem();
        purchaseItem.setPurchaseItemId(1L);
        purchaseItem.setQuantity(10);
        purchaseItem.setPurchasePrice(new BigDecimal("47.00"));

        when(purchaseItemRepository.save(purchaseItem))
                .thenReturn(purchaseItem);

        PurchaseItem result =
                purchaseItemService.createPurchaseItem(purchaseItem);

        assertEquals(1L, result.getPurchaseItemId());
        assertEquals(10, result.getQuantity());
        assertEquals(
                new BigDecimal("47.00"),
                result.getPurchasePrice()
        );
    }




    @Test
    void getAllPurchaseItemsReturnsAllPurchaseItems() {

        PurchaseItem purchaseItem1 = new PurchaseItem();
        purchaseItem1.setPurchaseItemId(1L);

        PurchaseItem purchaseItem2 = new PurchaseItem();
        purchaseItem2.setPurchaseItemId(2L);

        when(purchaseItemRepository.findAll())
                .thenReturn(List.of(purchaseItem1, purchaseItem2));

        List<PurchaseItem> result =
                purchaseItemService.getAllPurchaseItems();

        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getPurchaseItemId());
        assertEquals(2L, result.get(1).getPurchaseItemId());
    }


    @Test
    void getPurchaseItemReturnsPurchaseItem() {

        PurchaseItem purchaseItem = new PurchaseItem();
        purchaseItem.setPurchaseItemId(1L);
        purchaseItem.setQuantity(10);
        purchaseItem.setPurchasePrice(new BigDecimal("47.00"));

        when(purchaseItemRepository.findById(1L))
                .thenReturn(Optional.of(purchaseItem));

        PurchaseItem result =
                purchaseItemService.getPurchaseItem(1L);

        assertEquals(1L, result.getPurchaseItemId());
        assertEquals(10, result.getQuantity());
        assertEquals(
                new BigDecimal("47.00"),
                result.getPurchasePrice()
        );
    }


    @Test
    void getPurchaseItemThrowsExceptionWhenNotFound() {

        when(purchaseItemRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                RuntimeException.class,
                () -> purchaseItemService.getPurchaseItem(999L)
        );
    }

    

}

