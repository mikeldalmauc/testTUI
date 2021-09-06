package com.tui.proof.ws;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tui.proof.dto.AddressDto;
import com.tui.proof.dto.ClientDto;
import com.tui.proof.dto.PurchaseOrderDto;
import com.tui.proof.service.OrderNumberService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@Tags(value = { @Tag("UnitTest"), @Tag("ControllerTest") })
// @ContextConfiguration(classes = {PurchaseOrderDto.class,
// PilotesAmountValidator.class}, loader = AnnotationConfigContextLoader.class)
@WebMvcTest(PurchaseOrderController.class)
class PuchaseOrderControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @MockBean
    OrderNumberService orderNumberService;

    @MockBean
    KafkaTemplate<String, String> kafkaTemplate;

    AddressDto addressDto;
    ClientDto clientDto;
    PurchaseOrderDto testPODto;
    JsonNode addressJson, clientJson, poJson;

    @BeforeEach
    void setup() {

        addressDto = AddressDto.builder()
            .city("bilbao")
            .street("street 2")
            .postCode("48960")
            .country("Spain").build();

        clientDto = ClientDto.builder()
            .firstName("Jhon")
            .lastName("Joe")
            .telephone("+34666777888")
            .email("m@gmail.com").build();

        testPODto = PurchaseOrderDto.builder()
            .pilotes(5)
            .pricePerPilote(1.06)
            .orderTotal(100.0)
            .deliveryAddress(addressDto)
            .client(clientDto).build();

        addressJson = this.mapper.valueToTree(addressDto);
        clientJson = this.mapper.valueToTree(clientDto);
        poJson = this.mapper.valueToTree(testPODto);

    }

    @Test
    void hello_Test() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/orders/")).andExpect(status().isOk())
                .andExpect(content().contentType("text/plain;charset=UTF-8"));
    }

    @Test
    void createPurchaseOrder_Invalid_Null_Pilotes_Test() throws Exception {

        testPODto.setPilotes(null);
        mockMvc.perform(createMockRequest(testPODto)).andExpect(status().isBadRequest());
    }


    private static Stream<Arguments> invalidValueProvider() {
        return Stream.iterate(0, n -> n + 1)
            .filter(n -> n!=5 && n!= 10 && n!= 15)
            .limit(50)
            .map(i -> Arguments.of(i));
    }

    @ParameterizedTest(name = "{index} => pilotes={0}")
    @MethodSource("invalidValueProvider")
    void createPurchaseOrder_Invalid_Pilotes_Test(int pilotes) throws Exception {
        testPODto.setPilotes(pilotes);
        mockMvc.perform(createMockRequest(testPODto)).andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @ValueSource(ints = {5, 10 ,15})
    void createPurchaseOrder_Valid_Pilotes_Test(int pilotes) throws Exception {
        testPODto.setPilotes(pilotes);
        mockMvc.perform(createMockRequest(testPODto)).andExpect(status().isOk());
    }

    @ParameterizedTest
    @CsvSource({
        "pricePerPilote",
        "orderTotal",
        "client",
        "deliveryAddress"
    })
    void createPurchaseOrder_Invalid_Null_Values_Test(String field) throws Exception {
        poJson = ((ObjectNode) poJson).set(field, null);
        mockMvc.perform(createMockRequest(poJson)).andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @CsvSource({
          "client, firstName"
        , "client, lastName"
        , "client, telephone"
        , "client, email"
        , "deliveryAddress, street"
        , "deliveryAddress, postCode"
        , "deliveryAddress, city"
        , "deliveryAddress, country"
    })
    void createPurchaseOrder_Invalid_Null_Nested_Values_Test(String field, String subField) throws Exception {
       
        JsonNode node = ((ObjectNode) poJson.get(field)).set(subField, null);
        poJson = ((ObjectNode) poJson).set(field, node);
        mockMvc.perform(createMockRequest(poJson)).andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @CsvSource({
          "client, firstName"
        , "client, lastName"
        , "client, telephone"
        , "client, email"
        , "deliveryAddress, street"
        , "deliveryAddress, postCode"
        , "deliveryAddress, city"
        , "deliveryAddress, country"
    })
    void createPurchaseOrder_Invalid_Blank_Nested_Values_Test(String field, String subField) throws Exception {
        JsonNode node = ((ObjectNode) poJson.get(field)).put(subField, "");
        poJson = ((ObjectNode) poJson).set(field, node);
        mockMvc.perform(createMockRequest(poJson)).andExpect(status().isBadRequest());
    }
    

    @ParameterizedTest
    @CsvSource({
          "email@example.com"
        , "firstname.lastname@example.com"
        , "email@subdomain.example.com"
        , "firstname+lastname@example.com"
        , "email@123.123.123.123"
        , "email@[123.123.123.123]"
        , "“email”@example.com"
        , "1234567890@example.com"
        , "email@example-one.com"
        , "_______@example.com"
        , "email@example.name"
        , "email@example.museum"
        , "email@example.co.jp"
        , "firstname-lastname@example.com"
        , "email@example.web"
        , "email@111.222.333.44444"
        , "あいうえお@example.com"
        , "email@example"
    })
    void createPurchaseOrder_Valid_Client_Email_Test(String validEmail) throws Exception {
        testPODto.getClient().setEmail(validEmail);
        mockMvc.perform(createMockRequest(testPODto)).andExpect(status().isOk());        
    }

    @ParameterizedTest
    @CsvSource({
          "plainaddress"
        , "@example.com"
        , "Joe Smith <email@example.com>"
        , "email.example.com"
        , "email@example@example.com"
        , ".email@example.com"
        , "email.@example.com"
        , "email..email@example.com"
        , "email@example.com (Joe Smith)"
        , "email@-example.com"
        , "email@example..com"
        , "Abc..123@example.com/*  */"
    })
    void createPurchaseOrder_Invalid_Client_Email_Test(String invalidEmail) throws Exception {
        testPODto.getClient().setEmail(invalidEmail);
        mockMvc.perform(createMockRequest(testPODto)).andExpect(status().isBadRequest());
    }

    @Test
    void createPurchaseOrder_Test() throws Exception {

        when(orderNumberService.createOrderNumber()).thenReturn("34");

        mockMvc.perform(createMockRequest(poJson))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.*", hasSize(7)))
            .andExpect(jsonPath("$.id", nullValue()))
            .andExpect(jsonPath("$.number", is("34")))
            .andExpect(jsonPath("$.pilotes", is(5)))
            .andExpect(jsonPath("$.orderTotal", is(100.0)))
            .andExpect(jsonPath("$.deliveryAddress.*", hasSize(5)))
            .andExpect(jsonPath("$.deliveryAddress.id", notNullValue()))
            .andExpect(jsonPath("$.client.*", hasSize (5)))
            .andExpect(jsonPath("$.client.id", notNullValue()));
    }

    private MockHttpServletRequestBuilder createMockRequest(JsonNode payload) {
        return jsonMockRequest("/orders/create", this.mapper.valueToTree(payload));
    }

    private MockHttpServletRequestBuilder createMockRequest(Object payload) {
        return jsonMockRequest("/orders/create", this.mapper.valueToTree(payload));
    }

    
    private MockHttpServletRequestBuilder updateMockRequest(Object payload) {
        return jsonMockRequest("/orders/update", this.mapper.valueToTree(payload));
    }
    private static MockHttpServletRequestBuilder jsonMockRequest(String endpoint, JsonNode payload) {
        return MockMvcRequestBuilders.post(endpoint).contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON).content(payload.toString());
    }
}
