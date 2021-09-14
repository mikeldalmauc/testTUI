package com.tui.proof.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import com.github.javafaker.Faker;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientDto {
 
    private Long id;
  
    @NotBlank(message = "First Name is mandatory")
    private String firstName;
    
    @NotBlank(message = "Last Name is mandatory")
    private String lastName;
    
    @NotBlank(message = "First Name is mandatory")
    @Pattern(regexp="^(\\+)?(\\d{1,2})?[( .-]*(\\d{3})[) .-]*(\\d{3,4})[ .-]?(\\d{4})$", message="Invalid phone number")
    private String telephone;

    @NotBlank(message = "Email Name is mandatory")
    @Email(message="Invalid email")
    private String email;

    public static ClientDto fakeClient() {
        Faker faker = new Faker();
        return ClientDto.builder()
            .firstName(faker.name().firstName())
            .lastName(faker.name().lastName())
            .telephone(faker.phoneNumber().cellPhone())
            .email(faker.internet().emailAddress())
            .build();
      }
}
