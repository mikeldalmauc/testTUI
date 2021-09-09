package com.tui.proof.model;

import java.util.List;

import com.tui.proof.repository.AddressRepository;
import com.tui.proof.repository.ClientRepository;
import com.tui.proof.repository.PurchaseOrderRepository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ModelLoaderTest {

    @Autowired
    private ClientRepository clientRepo;
    
    @Autowired
    private AddressRepository addressRepo;

    @Autowired 
    private PurchaseOrderRepository poRepo;

    private List<Client> clients;

    private List<Address> addresses;

    @Test
    @Tag("IntegrationTest")
    void fakeData_PurchaseOrders_Amount_Test() {
        clients = Client.fakeClients(5);
        addresses = Address.fakeAddresses(5);
        Assertions.assertTrue(ModelLoader.fakePurchaseOrders(clients, addresses).size() >= 5);
    }

    @Test
    @Tag("IntegrationTest")
    void run_Test() {

        Assertions.assertTrue((long) ModelLoader.MODEL_SIZE <= poRepo.count());
        Assertions.assertEquals((long) ModelLoader.MODEL_SIZE, addressRepo.count());
        Assertions.assertEquals((long) ModelLoader.MODEL_SIZE, clientRepo.count());
    }
}
