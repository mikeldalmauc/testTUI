package com.tui.proof.model;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.function.IntPredicate;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.github.javafaker.service.FakeValuesService;
import com.github.javafaker.service.RandomService;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Entity
@Table
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseOrder implements Serializable {
  
  public static final double DEFAULT_PRICE = 1.06;
  public static final String ORDER_NUMBER_FORMAT = "[1-9]{15}";

  @Id 
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name="purchase_order_id", unique = true, nullable = false)
  private Long id;

  @Column(unique=true, nullable = false)
  private String number;
  
  private int pilotes;
  
  private double pricePerPilote;
 
  private double orderTotal;
  
  @ManyToOne(fetch=FetchType.EAGER, cascade={CascadeType.PERSIST, CascadeType.MERGE}) 
  @JoinColumn(nullable=false)
  private Client client;

  @ManyToOne(fetch=FetchType.EAGER, cascade={CascadeType.PERSIST, CascadeType.MERGE}) 
  @JoinColumn(nullable=false)
  private Address deliveryAddress;

  public static final IntPredicate validPilotes = n -> List.of(5,10,15).contains(n);

  public static PurchaseOrder fakePurchaseOrder(){
    return fakePurchaseOrder(Client.fakeClient(), Address.fakeAddress());
  }

  public static PurchaseOrder fakePurchaseOrder(Client client, Address address){

    FakeValuesService fakeValuesService = new FakeValuesService(
        new Locale("en-GB"), new RandomService());

    int pilotes = Integer.parseInt(fakeValuesService.regexify("5|10|15"));

    return PurchaseOrder.builder()
       .number(UUID.randomUUID().toString())
       .pilotes(pilotes)
       .pricePerPilote(DEFAULT_PRICE)
       .orderTotal(pilotes*DEFAULT_PRICE)
       .deliveryAddress(address)
       .client(client)
       .build();
  }

}
