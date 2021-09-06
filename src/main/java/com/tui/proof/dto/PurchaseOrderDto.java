package com.tui.proof.dto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

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

}
