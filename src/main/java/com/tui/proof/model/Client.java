package com.tui.proof.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.github.javafaker.Faker;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Builder
@Entity
@Table
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Client  implements Serializable{

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name="client_id", unique = true, nullable = false)
  private Long id;

  @NonNull
  private String firstName;
  @NonNull
  private String lastName;
  @NonNull
  private String telephone;
  @NonNull
  private String email;

  public static Client fakeClient(Faker faker){

    return Client.builder()
        .firstName(faker.name().firstName())
        .lastName(faker.name().lastName())
        .telephone(faker.phoneNumber().cellPhone())
        .email(faker.internet().emailAddress())
        .build();
  }
}
