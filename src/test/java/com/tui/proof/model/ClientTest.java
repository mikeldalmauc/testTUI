package com.tui.proof.model;

import com.github.javafaker.Faker;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;

@Tags(value = { @Tag("UnitTest") })
class ClientTest {

    private Client testClient;

    @BeforeEach
    void setUp() {
        testClient = Client.fakeClient(new Faker());
    }

    @Test
    void fakeClient_NonNull_Name_Test() {
        Assertions.assertNotNull(testClient.getFirstName());
    }

    @Test
    void fakeClient_NonNull_LastName_Test() {
        Assertions.assertNotNull(testClient.getLastName());
    }

    @Test
    void fakeClient_NonNull_Telephone_Test() {
        Assertions.assertNotNull(testClient.getTelephone());
    }
}
