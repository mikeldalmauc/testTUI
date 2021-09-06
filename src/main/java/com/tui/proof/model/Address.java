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
public class Address implements Serializable{
  
  @Id 
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name="address_id", unique = true, nullable = false)
  private Long id;

  @NonNull
  private String street;
  @NonNull
  private String postCode;
  @NonNull
  private String city;
  @NonNull
  private String country;

  public static Address fakeAddress(Faker faker){
        
    return Address.builder()
        .city(faker.address().city())
        .street(faker.address().streetAddress())
        .postCode(faker.address().zipCode())
        .country(faker.address().country())
        .build();
  } 
}
