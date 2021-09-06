package com.tui.proof.dto;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressDto {
    
  private Long id;

  @NotBlank(message = "Street is mandatory")
  private String street;
  @NotBlank(message = "City is mandatory")
  private String postCode;
  @NotBlank(message = "Post Code is mandatory")
  private String city;
  @NotBlank(message = "Country is mandatory")
  private String country;
}
