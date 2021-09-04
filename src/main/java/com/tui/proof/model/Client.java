package com.tui.proof.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.github.javafaker.Faker;

import lombok.Builder;
import lombok.Data;

@Builder
@Entity
@Table
@Data
public class Client  implements Serializable{

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name="client_id", unique = true, nullable = false)
  private Long id;

  @Column(nullable=false)
  private String firstName;
  @Column(nullable=false)
  private String lastName;
  @Column(nullable=false)
  private String telephone;
  
  public static Client fakeClient(Faker faker){

    return Client.builder()
        .firstName(faker.name().firstName())
        .lastName(faker.name().lastName())
        .telephone(faker.phoneNumber().cellPhone())
        .build();
  }
}
