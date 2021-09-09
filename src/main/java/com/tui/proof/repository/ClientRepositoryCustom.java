package com.tui.proof.repository;

import java.util.List;

import com.tui.proof.model.Client;

interface ClientRepositoryCustom {

    /**
     * 
     * @param client
     * @return Clients matching search. Uninformed fields will be ingored. 
     */
    List<Client> searchByClientData(Client client);

}
