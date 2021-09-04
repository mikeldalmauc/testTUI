package com.tui.proof.model;

import java.util.Random;

import com.github.javafaker.Faker;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;

@Tags(value = { @Tag("UnitTest") })
public class PurchaseOrderTest {

    private Client testClient;
    private Address testAdress;
    private PurchaseOrder testPurchaseOrder;

    @BeforeEach
    public void setUp() {

        testClient = Client.fakeClient(new Faker());
        testAdress = Address.fakeAddress(new Faker());
        testPurchaseOrder = PurchaseOrder.fakePurchaseOrder(testClient, testAdress);
    }

    @Test
    public void validPilotes_5_Test() {
        Assertions.assertTrue(PurchaseOrder.validPilotes.test(5));
    }

    @Test
    public void validPilotes_10_Test() {
        Assertions.assertTrue(PurchaseOrder.validPilotes.test(10));
    }

    @Test
    public void validPilotes_15_Test() {
        Assertions.assertTrue(PurchaseOrder.validPilotes.test(15));
    }

    @Test
    public void validPilotes_False_Test() {
        (new Random()).ints().limit(30).forEach(n -> {
            if (n != 5 && n != 10 && n != 15)
                Assertions.assertFalse(PurchaseOrder.validPilotes.test(n));
        });
    }

    @Test
    public void fakePurchaseOrder_NonNull_Number_Test() {
        Assertions.assertNotNull(testPurchaseOrder.getNumber());
    }

    @Test
    public void fakePurchaseOrder_NonNull_Client_Test() {
        Assertions.assertNotNull(testPurchaseOrder.getClient());
    }

    @Test
    public void fakePurchaseOrder_NonNull_DeliveryAddress_Test() {
        Assertions.assertNotNull(testPurchaseOrder.getDeliveryAddress());
    }

    @Test
    public void fakePurchaseOrder_Valid_Pilotes_Test() {
        Assertions.assertTrue(PurchaseOrder.validPilotes.test(testPurchaseOrder.getPilotes()));
    }

    @Test
    public void fakePurchaseOrder_Valid_OrderTotal_Test() {
        Assertions.assertEquals(testPurchaseOrder.getPilotes() * PurchaseOrder.DEFAULT_PRICE,
                testPurchaseOrder.getOrderTotal());
    }

}
