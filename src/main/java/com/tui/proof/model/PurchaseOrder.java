package com.tui.proof.model;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;
import java.util.function.IntPredicate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.github.javafaker.service.FakeValuesService;
import com.github.javafaker.service.RandomService;

import lombok.Builder;
import lombok.Data;

@Builder
@Entity
@Table
@Data
public class PurchaseOrder implements Serializable{
  
  public static final double DEFAULT_PRICE = 1.06;
  public static final String ORDER_NUMBER_FORMAT = "[a-z1-9]{15}";

  @Id 
  @GeneratedValue
  @Column(name="purchase_order_id", unique = true, nullable = false)
  private Long id;

  @Column(nullable=false)
  private String number;
  @Column(nullable=false)
  private int pilotes;
  @Column(nullable=false)
  private double pricePerPilote;
  @Column(nullable=false)
  private double orderTotal;
  
  @ManyToOne(optional=false, fetch=FetchType.LAZY) 
  @JoinColumn(name="client_id", nullable=false, updatable=false)
  private Client client;

  @ManyToOne(optional=false, fetch=FetchType.LAZY) 
  @JoinColumn(name="address_id", nullable=false, updatable=false)
  private Address deliveryAddress;

  public static final IntPredicate validPilotes = n -> List.of(5,10,15).contains(n);

  public static PurchaseOrder fakePurchaseOrder(Client client, Address address){

    FakeValuesService fakeValuesService = new FakeValuesService(
        new Locale("en-GB"), new RandomService());

    int pilotes = Integer.parseInt(fakeValuesService.regexify("5|10|15"));

    return PurchaseOrder.builder()
       .number(fakeValuesService.regexify(ORDER_NUMBER_FORMAT))
       .pilotes(pilotes)
       .pricePerPilote(DEFAULT_PRICE)
       .orderTotal(pilotes*DEFAULT_PRICE)
       .deliveryAddress(address)
       .client(client)
       .build();
  }

}
