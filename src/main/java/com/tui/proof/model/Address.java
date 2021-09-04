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
public class Address implements Serializable{
  
  @Id 
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name="address_id", unique = true, nullable = false)
  private Long id;

  @Column(nullable=false)
  private String street;
  @Column(nullable=false)
  private String postcode;
  @Column(nullable=false)
  private String city;
  @Column(nullable=false)
  private String country;

  public static Address fakeAddress(Faker faker){
        
    return Address.builder()
        .city(faker.address().city())
        .street(faker.address().streetAddress())
        .postcode(faker.address().zipCode())
        .country(faker.address().country())
        .build();
  } 
}
