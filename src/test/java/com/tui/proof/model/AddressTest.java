package com.tui.proof.model;

import com.github.javafaker.Faker;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;

@Tags(value = { @Tag("UnitTest") })
 class AddressTest {

    private Address testAdress;

    @BeforeEach
    void setUp() {
        testAdress = Address.fakeAddress(new Faker());
    }

    @Test
    void fakeAddress_NonNull_City_Test() {
        Assertions.assertNotNull(testAdress.getCity());
    }

    @Test
    void fakeAddress_NonNull_Street_Test() {
        Assertions.assertNotNull(testAdress.getStreet());
    }

    @Test
    void fakeAddress_NonNull_PostCode_Test() {
        Assertions.assertNotNull(testAdress.getPostCode());
    }

    @Test
    void fakeAddress_NonNull_Country_Test() {
        Assertions.assertNotNull(testAdress.getCountry());
    }
}
