package com.tui.proof.repository;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import com.tui.proof.model.Address;
import com.tui.proof.model.Client;
import com.tui.proof.model.PurchaseOrder;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@DataJpaTest
@EnableJpaRepositories(basePackageClasses  = PurchaseOrderRepository.class)
@EntityScan(basePackageClasses = {PurchaseOrder.class, Client.class, Address.class})
@Tags(value = { @Tag("IntegrationTest"), @Tag("RepositoryTest") })
class PurchaseOrderRepositoryTest {

    @Autowired
    private PurchaseOrderRepository pOrderRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AddressRepository addressRepository;

    @BeforeEach
    void setUp(){

        Stream.iterate(0, n -> n + 1)
        .limit(10)
        .forEach(i -> {
            pOrderRepository.save(PurchaseOrder.fakePurchaseOrder(Client.fakeClient(), Address.fakeAddress()));
        });
    
    }

    @Test()
    void maxOrderNumber_Test() {

        Long max = 0L;
        Iterator<PurchaseOrder> pos = pOrderRepository.findAll().iterator();
        while (pos.hasNext()){
            Long number = Long.parseLong(pos.next().getNumber());
            if(max < number)
                max = number;
        }

        Assertions.assertEquals(max + "",  pOrderRepository.maxOrderNumber());
    }

    @Test
    void findByClient_Test() {

        Assertions.assertTrue(clientRepository.count() > 1);
        Client client = Client.fakeClient();
        Client client2 = Client.fakeClient();

        pOrderRepository.save(PurchaseOrder.fakePurchaseOrder(client, Address.fakeAddress()));
        pOrderRepository.save(PurchaseOrder.fakePurchaseOrder(client, Address.fakeAddress()));
        pOrderRepository.save(PurchaseOrder.fakePurchaseOrder(client2, Address.fakeAddress()));
        pOrderRepository.save(PurchaseOrder.fakePurchaseOrder(client2, Address.fakeAddress()));
        
        List<PurchaseOrder> pos = pOrderRepository.findByClient(List.of(client, client2));

        Assertions.assertEquals(4, pos.size());
    }

    @Test
    void saveAndFind_Test() {

        Client client = clientRepository.save(Client.fakeClient());
        Address address = addressRepository.save(Address.fakeAddress());

        PurchaseOrder po = PurchaseOrder.fakePurchaseOrder(client, address);
        po = pOrderRepository.save(po);

        Assertions.assertEquals(po, pOrderRepository.findById(po.getId()).get());
    }

}

