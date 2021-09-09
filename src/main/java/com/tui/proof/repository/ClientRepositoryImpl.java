package com.tui.proof.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.tui.proof.model.Client;

import org.apache.logging.log4j.util.Strings;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class ClientRepositoryImpl implements ClientRepositoryCustom {

    @PersistenceContext
	private EntityManager entityManager;

    @Override
    @Transactional(readOnly = true)
    public List<Client> searchByClientData(Client client) {
        
        SearchSession searchSession = Search.session( entityManager ); 

        return searchSession.search(Client.class)
            .where(f -> f.bool(b -> {
                b.must( f.matchAll() ); 
                if (client.getId() != null ) { 
                    b.must(f.match().field("id")
                        .matching(client.getId()).boost( 4.0f ));
                }
                if (Strings.isNotBlank(client.getFirstName())) {
                    b.must(f.wildcard().field("firstName")
                        .matching("*" + client.getFirstName()+ "*").boost( 1.5f ));
                }
                if (Strings.isNotBlank(client.getLastName())) {
                    b.must(f.wildcard().field("lastName")
                        .matching("*" + client.getLastName()+ "*").boost( 3.0f ));
                }
                if (Strings.isNotBlank(client.getEmail())) {
                    b.must(f.wildcard().field("email")
                        .matching("*" + client.getEmail()+ "*"));
                }
                if (Strings.isNotBlank(client.getTelephone())) {
                    b.must(f.wildcard().field("telephone")
                        .matching("*" + client.getTelephone()+ "*"));
                }
            })) 
            .fetchHits(10);
    }
}
