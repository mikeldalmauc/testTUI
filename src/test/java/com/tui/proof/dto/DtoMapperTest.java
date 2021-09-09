package com.tui.proof.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

import com.tui.proof.model.Address;
import com.tui.proof.model.Client;
import com.tui.proof.model.PurchaseOrder;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;

@Tags(value = @Tag("UnitTest"))
class DtoMapperTest {
    
    AddressDto addressDto;
    ClientDto clientDto;
    PurchaseOrderDto poDto;

    Address address;
    Client client;
    PurchaseOrder po;

    @BeforeEach
    void setUp(){

        addressDto = AddressDto.fakeAddress();
        clientDto = ClientDto.fakeClient();
        poDto = PurchaseOrderDto.fakePurchaseOrder(clientDto, addressDto);

        address = Address.fakeAddress();
        client = Client.fakeClient();
        po = PurchaseOrder.fakePurchaseOrder(client, address);
    }

    @Test
    void testConstructorIsPrivate() throws NoSuchMethodException, SecurityException {
        Constructor<DtoMapper> constructor = DtoMapper.class.getDeclaredConstructor();
        Assertions.assertTrue(Modifier.isPrivate(constructor.getModifiers()));
        constructor.setAccessible(true);
        try{
            constructor.newInstance();
            Assertions.assertTrue(false);
        }catch(Exception e){
            Assertions.assertTrue(true);
        }
    }

    @Test
    void converToDo_client_Test(){
        Client client = DtoMapper.convertToDo(clientDto);
        assertDoEquals(clientDto, client);
    }
    
    @Test
    void converToDto_client_Test(){
        ClientDto clientDto = DtoMapper.convertToDto(client);
        assertDtoEquals(client, clientDto);
    }
    
    @Test
    void converToDo_address_Test(){
        Address address = DtoMapper.convertToDo(addressDto);
        assertDoEquals(addressDto, address);
    }
    
    @Test
    void converToDto_address_Test(){
        AddressDto addressDto = DtoMapper.convertToDto(address);
        assertDtoEquals(address, addressDto);
    }
    

    @Test
    void converToDo_purchaseOrder_Test(){

        PurchaseOrder po = DtoMapper.convertToDo(poDto);
        assertEquals(poDto.getId(), po.getId());
        assertEquals(poDto.getNumber(), po.getNumber());
        assertEquals(poDto.getPilotes(), po.getPilotes());
        assertEquals(poDto.getPricePerPilote(), po.getPricePerPilote());
        assertEquals(poDto.getOrderTotal(), po.getOrderTotal());
        assertDoEquals(poDto.getDeliveryAddress(), po.getDeliveryAddress());
        assertDoEquals(poDto.getClient(), po.getClient());
    }

    
    @Test
    void converToDto_purchaseOrder_Test(){

        PurchaseOrderDto poDto = DtoMapper.convertToDto(po);
        assertEquals(po.getId(), poDto.getId());
        assertEquals(po.getNumber(), poDto.getNumber());
        assertEquals(po.getPilotes(), poDto.getPilotes());
        assertEquals(po.getPricePerPilote(), poDto.getPricePerPilote());
        assertEquals(po.getOrderTotal(), poDto.getOrderTotal());
        assertDtoEquals(po.getDeliveryAddress(), poDto.getDeliveryAddress());
        assertDtoEquals(po.getClient(), poDto.getClient());
    }

    public static void assertDtoEquals(Client client, ClientDto clientDto){
        assertEquals(client.getId(), clientDto.getId());
        assertEquals(client.getFirstName(), clientDto.getFirstName());
        assertEquals(client.getLastName(), clientDto.getLastName());
        assertEquals(client.getEmail(), clientDto.getEmail());
        assertEquals(client.getTelephone(), clientDto.getTelephone());
    }

    public static void assertDoEquals(ClientDto clientDto, Client client){
        assertEquals(clientDto.getId(), client.getId());
        assertEquals(clientDto.getFirstName(), client.getFirstName());
        assertEquals(clientDto.getLastName(), client.getLastName());
        assertEquals(clientDto.getEmail(), client.getEmail());
        assertEquals(clientDto.getTelephone(), client.getTelephone());
    }
    

    public static void assertDtoEquals(Address address, AddressDto addressDto){
        assertEquals(address.getId(), addressDto.getId());
        assertEquals(address.getCity(), addressDto.getCity());
        assertEquals(address.getCountry(), addressDto.getCountry());
        assertEquals(address.getPostCode(), addressDto.getPostCode());
        assertEquals(address.getStreet(), addressDto.getStreet());
    }

    public static void assertDoEquals(AddressDto addressDto, Address address){
        assertEquals(addressDto.getId(), address.getId());
        assertEquals(addressDto.getCity(), address.getCity());
        assertEquals(addressDto.getCountry(), address.getCountry());
        assertEquals(addressDto.getPostCode(), address.getPostCode());
        assertEquals(addressDto.getStreet(), address.getStreet());
    }

}
