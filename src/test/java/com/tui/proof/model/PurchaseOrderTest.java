package com.tui.proof.model;

import java.util.Random;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;

@Tags(value = { @Tag("UnitTest") })
class PurchaseOrderTest {

    private Client testClient;
    private Address testAdress;
    private PurchaseOrder testPurchaseOrder;

    @BeforeEach
    void setUp() {

        testClient = Client.fakeClient();
        testAdress = Address.fakeAddress();
        testPurchaseOrder = PurchaseOrder.fakePurchaseOrder(testClient, testAdress);
    }

    @Test
    void validPilotes_5_Test() {
        Assertions.assertTrue(PurchaseOrder.validPilotes.test(5));
    }

    @Test
    void validPilotes_10_Test() {
        Assertions.assertTrue(PurchaseOrder.validPilotes.test(10));
    }

    @Test
    void validPilotes_15_Test() {
        Assertions.assertTrue(PurchaseOrder.validPilotes.test(15));
    }

    @Test
    void validPilotes_False_Test() {
        (new Random()).ints().limit(30).forEach(n -> {
            if (n != 5 && n != 10 && n != 15)
                Assertions.assertFalse(PurchaseOrder.validPilotes.test(n));
        });
    }

    @Test
    void fakePurchaseOrder_NonNull_Number_Test() {
        Assertions.assertNotNull(testPurchaseOrder.getNumber());
    }

    @Test
    void fakePurchaseOrder_NonNull_Client_Test() {
        Assertions.assertNotNull(testPurchaseOrder.getClient());
    }

    @Test
    void fakePurchaseOrder_NonNull_DeliveryAddress_Test() {
        Assertions.assertNotNull(testPurchaseOrder.getDeliveryAddress());
    }

    @Test
    void fakePurchaseOrder_Valid_Pilotes_Test() {
        Assertions.assertTrue(PurchaseOrder.validPilotes.test(testPurchaseOrder.getPilotes()));
    }

    @Test
    void fakePurchaseOrder_Valid_OrderTotal_Test() {
        Assertions.assertEquals(testPurchaseOrder.getPilotes() * PurchaseOrder.DEFAULT_PRICE,
                testPurchaseOrder.getOrderTotal());
    }

}
