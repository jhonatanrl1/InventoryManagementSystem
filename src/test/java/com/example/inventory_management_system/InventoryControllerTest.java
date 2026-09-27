package com.example.inventory_management_system;

import com.example.inventory_management_system.controller.InventoryController;
import com.example.inventory_management_system.service.InventoryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class InventoryControllerTest {

    @Mock
    private InventoryService inventoryService;

    @InjectMocks
    private InventoryController inventoryController;

    @Test
    void getInventoryValueReturnsValue() {

        BigDecimal expectedValue = new BigDecimal("949.85");

        when(inventoryService.calculateInventoryValue())
                .thenReturn(expectedValue);

        BigDecimal result = inventoryController.getInventoryValue();

        assertEquals(expectedValue, result);

        verify(inventoryService).calculateInventoryValue();
    }





}