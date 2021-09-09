package com.tui.proof.model;

import static java.lang.Math.floor;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;

import com.tui.proof.repository.PurchaseOrderRepository;

import org.apache.commons.math3.distribution.ParetoDistribution;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.mapping.SearchMapping;
import org.hibernate.search.mapper.orm.work.SearchWorkspace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
public class ModelLoader implements ApplicationRunner {
	
    public static final Integer MODEL_SIZE = 50;

    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;
    
    @PersistenceContext
    private EntityManager entityManager;
    
	@Autowired 
    private PurchaseOrderRepository poRepo;

    public static List<PurchaseOrder> fakePurchaseOrders(List<Client> clients, List<Address> addresses){

        /**
         * 1 order min
         * α = 2 is the shape parameter: this is the Pareto index
         * a 80/20 proportion
         * view https://en.wikipedia.org/wiki/Pareto_distribution
         */
        ParetoDistribution dist = new ParetoDistribution(1, 2);

        return Stream.iterate(0, n -> n + 1)
            .map(n -> {
                int numberOfOrders = (int) floor(dist.sample());
                return Stream.iterate(0, i -> i + 1)
                    .map(i -> PurchaseOrder
                    .fakePurchaseOrder(clients.get(n), addresses.get(n)))
                    .limit(numberOfOrders);
            })
            .limit(clients.size())
            .flatMap(Function.identity())
            .collect(Collectors.toList());
    }

    public void run(ApplicationArguments args) {

        // Indexes are cleared before saving all data.
        SearchMapping searchMapping = Search.mapping(entityManagerFactory); 
		SearchWorkspace allEntitiesWorkspace = searchMapping.scope( Object.class ).workspace();         
        allEntitiesWorkspace.purge();

        List<PurchaseOrder> pos = fakePurchaseOrders(Client.fakeClients(MODEL_SIZE), Address.fakeAddresses(MODEL_SIZE));
        poRepo.saveAll(pos);

        log.info(pos.size() + " fake purchase orders created");
        log.info(MODEL_SIZE + " fake address created");
        log.info(MODEL_SIZE + " fake purchase orders created");
    }

}
