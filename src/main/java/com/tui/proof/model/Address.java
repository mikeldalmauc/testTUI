package com.tui.proof.model;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

  public static Address fakeAddress(){
        
    Faker faker = new Faker();

    return Address.builder()
        .city(faker.address().city())
        .street(faker.address().streetAddress())
        .postCode(faker.address().zipCode())
        .country(faker.address().country())
        .build();
  } 
  public static List<Address> fakeAddresses(int m){
    
    return Stream.iterate(0, n -> n + 1)
        .map(n -> Address.fakeAddress())
        .limit(m)
        .collect(Collectors.toList());
  } 
}
