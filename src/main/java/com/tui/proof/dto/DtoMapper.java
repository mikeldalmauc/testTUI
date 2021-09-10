package com.tui.proof.dto;

import com.tui.proof.model.Address;
import com.tui.proof.model.Client;
import com.tui.proof.model.PurchaseOrder;

public class DtoMapper {
    
    private DtoMapper() {
        throw new IllegalStateException("Utility class");
    }

    public static Client toDo(ClientDto dto){
        
        return Client.builder()
            .id(dto.getId())
            .firstName(dto.getFirstName())
            .lastName(dto.getLastName())
            .email(dto.getEmail())
            .telephone(dto.getTelephone())
            .build();
    }

    public static ClientDto toDto(Client entity){
        
        return ClientDto.builder()
            .id(entity.getId())
            .firstName(entity.getFirstName())
            .lastName(entity.getLastName())
            .email(entity.getEmail())
            .telephone(entity.getTelephone())
            .build();
    }

    public static Address toDo(AddressDto dto){

        return Address.builder()
            .id(dto.getId())
            .city(dto.getCity())
            .street(dto.getStreet())
            .postCode(dto.getPostCode())
            .country(dto.getCountry()).build();
    }

    public static AddressDto toDto(Address entity){

        return AddressDto.builder()
            .id(entity.getId())
            .city(entity.getCity())
            .street(entity.getStreet())
            .postCode(entity.getPostCode())
            .country(entity.getCountry()).build();
    }

    public static PurchaseOrder toDo(PurchaseOrderDto dto){

        return PurchaseOrder.builder()
            .id(dto.getId())
            .number(dto.getNumber())
            .pilotes(dto.getPilotes())
            .pricePerPilote(dto.getPricePerPilote())
            .orderTotal(dto.getOrderTotal())
            .deliveryAddress(toDo(dto.getDeliveryAddress()))
            .client(toDo(dto.getClient()))
            .build();

    }

    public static PurchaseOrderDto toDto(PurchaseOrder entity){

        return PurchaseOrderDto.builder()
            .id(entity.getId())
            .number(entity.getNumber())
            .pilotes(entity.getPilotes())
            .pricePerPilote(entity.getPricePerPilote())
            .orderTotal(entity.getOrderTotal())
            .deliveryAddress(toDto(entity.getDeliveryAddress()))
            .client(toDto(entity.getClient()))
            .build();
    }

}
