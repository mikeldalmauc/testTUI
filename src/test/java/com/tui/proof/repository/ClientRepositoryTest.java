package com.tui.proof.repository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.tui.proof.model.Client;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Tags(value = { @Tag("IntegrationTest"), @Tag("RepositoryTest") })
class ClientRepositoryTest  {

    @Autowired
    private ClientRepository clientRepository;
    
    private final List<Client> savedClients = new ArrayList<>();

    @BeforeEach
    void setUp(){
        clientRepository.saveAll(
            Stream.iterate(0, n -> n + 1)
            .map(n -> Client.fakeClient())
            .limit(15)
            .collect(Collectors.toList())
        ).forEach(saved -> 
            savedClients.add(saved)
        );
    }

    @Test
    void searchByClientData_EmptyClient_Test(){

        Client model = new Client();
        List<Client> res = clientRepository.searchByClientData(model);
        
        // At max 10 clients are returned
        Assertions.assertEquals(10, res.size());
    }

    @Test
    void searchByClientData_Id_Test(){

        Client model = new Client();
        model.setId(savedClients.get(0).getId());
        List<Client> res = clientRepository.searchByClientData(model);
        
        Assertions.assertTrue(res.size() > 0);
        Assertions.assertEquals(savedClients.get(0).getId(), res.get(0).getId());
    }

    @Test
    void searchByClientData_Id_Invalid_Test(){

        Long maxId =savedClients.stream().map(Client::getId).max(Comparator.naturalOrder()).get();
        
        Client model = new Client();
        model.setId(maxId + 1);
        List<Client> res = clientRepository.searchByClientData(model);
        
        Assertions.assertEquals(0, res.size());
    }
    

    @Test
    void searchByClientData_FirstName_Test(){
        Client model = new Client();
        model.setFirstName(savedClients.get(0).getFirstName());

        List<Client> res = clientRepository.searchByClientData(model);
        
        Assertions.assertTrue(res.size() > 0);
    }

    @Test
    void searchByClientData_FirstNameRegex_Test(){

        Client model = new Client();

        savedClients.stream().filter(c -> c.getFirstName().length() > 5)
        .findFirst().ifPresent(c -> {
            model.setFirstName(c.getFirstName().substring(1, 4));

            List<Client> res = clientRepository.searchByClientData(model);
        
            Assertions.assertTrue(res.size() > 0);
            Assertions.assertTrue(res.stream()
                .filter(cl -> cl.getFirstName().equals(c.getFirstName()))
                .findFirst().isPresent());
        });
    }
    
    @Test
    void searchByClientData_LastName_Test(){
        Client model = new Client();
        model.setLastName(savedClients.get(0).getLastName());

        List<Client> res = clientRepository.searchByClientData(model);
        
        Assertions.assertTrue(res.size() > 0);
    }

    @Test
    void searchByClientData_LastNameRegex_Test(){

        Client model = new Client();

        savedClients.stream().filter(c -> c.getLastName().length() > 5)
        .findFirst().ifPresent(c -> {
            model.setLastName(c.getLastName().substring(1, 4));

            List<Client> res = clientRepository.searchByClientData(model);
        
            Assertions.assertTrue(res.size() > 0);
            Assertions.assertTrue(res.stream()
                .filter(cl -> cl.getLastName().equals(c.getLastName()))
                .findFirst().isPresent());
        });
    }

    @Test
    void searchByClientData_Email_Test(){
        Client model = new Client();
        model.setEmail(savedClients.get(0).getEmail());

        List<Client> res = clientRepository.searchByClientData(model);
        
        Assertions.assertTrue(res.size() > 0);
    }

    @Test
    void searchByClientData_EmailRegex_Test(){

        Client model = new Client();

        savedClients.stream().filter(c -> c.getEmail().length() > 5)
        .findFirst().ifPresent(c -> {
            model.setEmail(c.getEmail().substring(1, 4));

            List<Client> res = clientRepository.searchByClientData(model);
        
            Assertions.assertTrue(res.size() > 0);
            Assertions.assertTrue(res.stream()
                .filter(cl -> cl.getEmail().equals(c.getEmail()))
                .findFirst().isPresent());
        });
    }

    @Test
    void searchByClientData_Telephone_Test(){
        Client model = new Client();
        model.setTelephone(savedClients.get(0).getTelephone());

        List<Client> res = clientRepository.searchByClientData(model);
        
        Assertions.assertTrue(res.size() > 0);
    }

    @Test
    void searchByClientData_TelephoneRegex_Test(){

        Client model = new Client();

        savedClients.stream().filter(c -> c.getTelephone().length() > 5)
        .findFirst().ifPresent(c -> {
            model.setTelephone(c.getTelephone().substring(1, 4));

            List<Client> res = clientRepository.searchByClientData(model);
        
            Assertions.assertTrue(res.size() > 0);
            Assertions.assertTrue(res.stream()
                .filter(cl -> cl.getTelephone().equals(c.getTelephone()))
                .findFirst().isPresent());
        });
    }

    @Test
    void searchByClientData_AllFields_Test(){

        List<Client> res = clientRepository.searchByClientData(savedClients.get(0));
        Assertions.assertEquals(1, res.size());
    }

    @Test
    void searchByClientData_AllFields_BadId_Test(){
        Client c = savedClients.get(0);
        c.setId(-1233L);
        List<Client> res = clientRepository.searchByClientData(c);
        Assertions.assertEquals(0, res.size());
    }

    @Test
    void searchByClientData_AllFields_BadFirstName_Test(){
        Client c = savedClients.get(0);
        c.setFirstName(c.getFirstName()+"someExtra");;
        List<Client> res = clientRepository.searchByClientData(c);
        Assertions.assertEquals(0, res.size());
    }

    @Test
    void searchByClientData_AllFields_BadLastName_Test(){

        Client c = savedClients.get(0);
        c.setFirstName(c.getLastName()+"someExtra");;
        List<Client> res = clientRepository.searchByClientData(c);
        Assertions.assertEquals(0, res.size());
    }

    @Test
    void searchByClientData_AllFields_BadTelephone_Test(){
        Client c = savedClients.get(0);
        c.setFirstName(c.getTelephone()+"someExtra");;
        List<Client> res = clientRepository.searchByClientData(c);
        Assertions.assertEquals(0, res.size());
    }

    @Test
    void searchByClientData_AllFields_BadEmail_Test(){
        Client c = savedClients.get(0);
        c.setFirstName(c.getEmail()+"someExtra");;
        List<Client> res = clientRepository.searchByClientData(c);
        Assertions.assertEquals(0, res.size());
    }

}
