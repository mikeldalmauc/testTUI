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

import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;

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
@Indexed
public class Client implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "client_id", unique = true, nullable = false)
  @GenericField
  private Long id;

  @NonNull
  @GenericField
  private String firstName;

  @NonNull
  @GenericField
  private String lastName;

  @NonNull
  @GenericField
  private String telephone;

  @NonNull
  @GenericField
  private String email;

  public static Client fakeClient() {

    Faker faker = new Faker();
    return Client.builder()
      .firstName(faker.name().firstName())
      .lastName(faker.name().lastName())
      .telephone(faker.phoneNumber().cellPhone())
      .email(faker.internet().emailAddress()).build();
  }

  public static List<Client> fakeClients(int m) {
    return Stream.iterate(0, n -> n + 1)
      .map(n -> Client.fakeClient())
      .limit(m)
      .collect(Collectors.toList());
  }
}
