package com.example.inventory_management_system.service;

import com.example.inventory_management_system.model.PurchaseItem;
import com.example.inventory_management_system.repository.PurchaseItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PurchaseItemService {

    private final PurchaseItemRepository purchaseItemRepository;

    public PurchaseItemService(PurchaseItemRepository purchaseItemRepository) {
        this.purchaseItemRepository = purchaseItemRepository;
    }

    public PurchaseItem createPurchaseItem(PurchaseItem purchaseItem) {
        return purchaseItemRepository.save(purchaseItem);
    }

    public List<PurchaseItem> getAllPurchaseItems() {
        return purchaseItemRepository.findAll();
    }

    public PurchaseItem getPurchaseItem(Long id) {
        return purchaseItemRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Purchase item not found"));
    }

}




