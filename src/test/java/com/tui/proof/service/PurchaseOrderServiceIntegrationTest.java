package com.tui.proof.service;


import javax.transaction.Transactional;

import com.tui.proof.model.PurchaseOrder;
import com.tui.proof.repository.PurchaseOrderRepository;
import com.tui.proof.repository.StageOfOrdersMap;

import org.awaitility.Awaitility;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Tags(value = { @Tag("IntegrationTest"), @Tag("ServiceTest") })
@Transactional
class PurchaseOrderServiceIntegrationTest {
    
    
    @Autowired
    PurchaseOrderService purchaseOrderService;

    @Autowired
    PurchaseOrderRepository pOrderRepository;
    
    @Autowired
    private StageOfOrdersMap stage;

    @BeforeEach
    void setUp() {
    }

    @Test
    void create_Test() throws InterruptedException {

        PurchaseOrder po = PurchaseOrder.fakePurchaseOrder();
        po.setNumber(null);

        PurchaseOrder result = purchaseOrderService.create(po);

        Assertions.assertNotNull(result.getNumber());
        Assertions.assertTrue(stage.getOrdersOnStage().containsKey(result.getNumber()));
        Assertions.assertNull(pOrderRepository.findByNumber(result.getNumber()));

        Awaitility.await().untilAsserted(() -> {
            Assertions.assertFalse(stage.getOrdersOnStage().containsKey(result.getNumber()));
            Assertions.assertNotNull(pOrderRepository.findByNumber(result.getNumber()));
        });
    }

    @Test
    void update_Test() throws InterruptedException {

        PurchaseOrder po = PurchaseOrder.fakePurchaseOrder();
        po.setNumber(null);

        PurchaseOrder result = purchaseOrderService.create(po);
        String oldNumber = result.getNumber();
        Assertions.assertTrue(stage.getOrdersOnStage().containsKey(result.getNumber()));
        
        result.setOrderTotal(1212);;

        PurchaseOrder updated = purchaseOrderService.update(result);
        
        Assertions.assertFalse(stage.getOrdersOnStage().containsKey(oldNumber));
        Assertions.assertTrue(stage.getOrdersOnStage().containsKey(updated.getNumber()));
        Assertions.assertNull(pOrderRepository.findByNumber(updated.getNumber()));

        Awaitility.await().untilAsserted(() -> {
            Assertions.assertFalse(stage.getOrdersOnStage().containsKey(updated.getNumber()));
            Assertions.assertNotNull(pOrderRepository.findByNumber(updated.getNumber()));
        });

    }

    
}
