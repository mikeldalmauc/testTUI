package com.tui.proof.dto;

import java.util.Locale;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.github.javafaker.service.FakeValuesService;
import com.github.javafaker.service.RandomService;
import com.tui.proof.validation.PilotesAmountConstraint;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseOrderDto {

  public static final double DEFAULT_PRICE = 1.06;
  public static final String ORDER_NUMBER_FORMAT = "[1-9]{15}";

  private Long id;
   
  private String number;

  @PilotesAmountConstraint
  private Integer pilotes;

  @NotNull(message = "Price is mandatory")
  private Double pricePerPilote;

  @NotNull(message = "Order total is mandatory")
  private Double orderTotal;

  @NotNull(message = "Client is mandatory")
  @Valid
  private ClientDto client;

  @NotNull(message = "Address is mandatory")
  @Valid
  private AddressDto deliveryAddress;

  public static PurchaseOrderDto fakePurchaseOrder(ClientDto client, AddressDto address){

    FakeValuesService fakeValuesService = new FakeValuesService(
        new Locale("en-GB"), new RandomService());

    int pilotes = Integer.parseInt(fakeValuesService.regexify("5|10|15"));

    return PurchaseOrderDto.builder()
       .number(fakeValuesService.regexify(ORDER_NUMBER_FORMAT))
       .pilotes(pilotes)
       .pricePerPilote(DEFAULT_PRICE)
       .orderTotal(pilotes*DEFAULT_PRICE)
       .deliveryAddress(address)
       .client(client)
       .build();
  }

}
