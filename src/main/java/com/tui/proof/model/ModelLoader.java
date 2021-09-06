package com.tui.proof.model;

import static java.lang.Math.floor;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.github.javafaker.Faker;
import com.tui.proof.repository.AddressRepository;
import com.tui.proof.repository.ClientRepository;
import com.tui.proof.repository.PurchaseOrderRepository;

import org.apache.commons.lang3.tuple.Triple;
import org.apache.commons.math3.distribution.ParetoDistribution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
public class ModelLoader implements ApplicationRunner {
	
	@Autowired AddressRepository addressRepo;
	
	@Autowired ClientRepository clientRepo;

	@Autowired PurchaseOrderRepository poRepo;

    /**
     * 
     * @param m 
     * @return m clients, m addresses, a pareto distribution of orders for each client-address pair.
     */
    public static Triple<List<Client>, List<Address>, List<PurchaseOrder>> fakeData(int m){
        
        // Insert some fake data 
	    List<Client> clients = Stream.iterate(0, n -> n + 1)
            .map(n -> new Faker())
            .map(Client::fakeClient)
            .limit(m)
            .collect(Collectors.toList());

        List<Address> addresses = Stream.iterate(0, n -> n + 1)
            .map(n -> new Faker())
            .map(Address::fakeAddress)
            .limit(m)
            .collect(Collectors.toList());
    
        /**
         * 1 order min
         * α is the shape parameter: this is the Pareto index
         * view https://anylogic.help/advanced/functions/pareto.html
         */
        ParetoDistribution dist = new ParetoDistribution(1, 2);

        List<PurchaseOrder> purchaseOrders = Stream.iterate(0, n -> n + 1)
            .map(n -> {
                int numberOfOrders = (int) floor(dist.sample());
                return Stream.iterate(0, i -> i + 1)
                    .map(i -> PurchaseOrder
                    .fakePurchaseOrder(clients.get(n), addresses.get(n)))
                    .limit(numberOfOrders);
            })
            .limit(m)
            .flatMap(Function.identity())
            .collect(Collectors.toList());

            return Triple.of(clients, addresses, purchaseOrders);
    }


    public void run(ApplicationArguments args) {

                 
        Triple<List<Client>, List<Address>, List<PurchaseOrder>> data = fakeData(200);

        clientRepo.saveAll(data.getLeft());
        log.info(data.getLeft().size() + " clients created");

        addressRepo.saveAll(data.getMiddle());
        log.info(data.getMiddle().size() + " address created");

        poRepo.saveAll(data.getRight());
        log.info(data.getRight().size() + " purchase orders created");
    }

    
    
}
