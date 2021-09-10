package com.tui.proof.ws;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.stream.Stream;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tui.proof.configuration.WebSecurityConfig;
import com.tui.proof.dto.AddressDto;
import com.tui.proof.dto.ClientDto;
import com.tui.proof.dto.PurchaseOrderDto;
import com.tui.proof.model.Address;
import com.tui.proof.model.Client;
import com.tui.proof.model.PurchaseOrder;
import com.tui.proof.service.PurchaseOrderService;

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
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@Tags(value = { @Tag("UnitTest"), @Tag("ControllerTest") })
@WebMvcTest(PurchaseOrderController.class)
@Import(WebSecurityConfig.class)
class PuchaseOrderControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @MockBean
    PurchaseOrderService purchaseOrderService;


    AddressDto addressDto;
    ClientDto clientDto;
    PurchaseOrderDto testPODto;
    JsonNode addressJson, clientJson, poJson;

    @BeforeEach
    void setup() {

        addressDto = AddressDto.fakeAddress();
        clientDto = ClientDto.fakeClient();
        testPODto = PurchaseOrderDto.builder()
            .pilotes(5)
            .pricePerPilote(1.06)
            .orderTotal(100.0)
            .deliveryAddress(addressDto)
            .client(clientDto).build();
        
        addressJson = this.mapper.valueToTree(addressDto);
        clientJson = this.mapper.valueToTree(clientDto);
        poJson = this.mapper.valueToTree(testPODto);

        
        when(purchaseOrderService.create(any(PurchaseOrder.class))).then(returnsFirstArg());
        when(purchaseOrderService.update(any(PurchaseOrder.class))).then(returnsFirstArg());
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
        mockMvc.perform(updateMockRequest(testPODto)).andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @ValueSource(ints = {5, 10 ,15})
    void createPurchaseOrder_Valid_Pilotes_Test(int pilotes) throws Exception {
        testPODto.setPilotes(pilotes);
        mockMvc.perform(createMockRequest(testPODto)).andExpect(status().isOk());
        testPODto.setNumber("someNumber");
        mockMvc.perform(updateMockRequest(testPODto)).andExpect(status().isOk());
    }

    @ParameterizedTest
    @CsvSource({
        "pricePerPilote",
        "orderTotal",
        "client",
        "deliveryAddress"
    })
    void createUpdatePurchaseOrder_Invalid_Null_Values_Test(String field) throws Exception {
        poJson = ((ObjectNode) poJson).set(field, null);
        mockMvc.perform(createMockRequest(poJson)).andExpect(status().isBadRequest());
        poJson = ((ObjectNode) poJson).put("number", "someNumber");
        mockMvc.perform(updateMockRequest(poJson)).andExpect(status().isBadRequest());
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
    void createUpdatePurchaseOrder_Invalid_Null_Nested_Values_Test(String field, String subField) throws Exception {
       
        JsonNode node = ((ObjectNode) poJson.get(field)).set(subField, null);
        poJson = ((ObjectNode) poJson).set(field, node);
        mockMvc.perform(createMockRequest(poJson)).andExpect(status().isBadRequest());
        poJson = ((ObjectNode) poJson).put("number", "someNumber");
        mockMvc.perform(updateMockRequest(poJson)).andExpect(status().isBadRequest());
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
    void createUpdatePurchaseOrder_Invalid_Blank_Nested_Values_Test(String field, String subField) throws Exception {
        JsonNode node = ((ObjectNode) poJson.get(field)).put(subField, "");
        poJson = ((ObjectNode) poJson).set(field, node);
        mockMvc.perform(createMockRequest(poJson)).andExpect(status().isBadRequest());
        poJson = ((ObjectNode) poJson).put("number", "someNumber");
        mockMvc.perform(updateMockRequest(poJson)).andExpect(status().isBadRequest());
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
    void createUpdatePurchaseOrder_Valid_Client_Email_Test(String validEmail) throws Exception {
        testPODto.getClient().setEmail(validEmail);
        mockMvc.perform(createMockRequest(testPODto)).andExpect(status().isOk());
        testPODto.setNumber("someNumber");
        mockMvc.perform(updateMockRequest(testPODto)).andExpect(status().isOk());
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
    void createUpdatePurchaseOrder_Invalid_Client_Email_Test(String invalidEmail) throws Exception {
        testPODto.getClient().setEmail(invalidEmail);
        mockMvc.perform(createMockRequest(testPODto)).andExpect(status().isBadRequest());
        testPODto.setNumber("someNumber");
        mockMvc.perform(updateMockRequest(testPODto)).andExpect(status().isBadRequest());
    }

    @Test
    void updatePurchaseOrder_Invalid_Id_Test() throws Exception {
        
        testPODto.setId(2133L);
        mockMvc.perform(updateMockRequest(testPODto)).andExpect(status().isBadRequest());

        testPODto.setNumber(null);
        mockMvc.perform(updateMockRequest(testPODto)).andExpect(status().isBadRequest());

        testPODto.setId(null);
        mockMvc.perform(updateMockRequest(testPODto)).andExpect(status().isBadRequest());

        testPODto.setNumber("");
        mockMvc.perform(updateMockRequest(testPODto)).andExpect(status().isBadRequest());
    }

    @Test
    void createPurchaseOrder_Test() throws Exception {

        mockMvc.perform(createMockRequest(poJson))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.*", hasSize(7)));

        verify(purchaseOrderService, times(1)).create(any(PurchaseOrder.class));
    }

    @Test
    void updatePurchaseOrder_Test() throws Exception {
        poJson = ((ObjectNode) poJson).put("number", "someNumber");
        mockMvc.perform(updateMockRequest(poJson))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.*", hasSize(7)));
        
        verify(purchaseOrderService, times(1)).update(any(PurchaseOrder.class));
    }

    
    @Test
    @WithMockUser(username = "user", password = "user", roles = "USER")
    void searchPurchaseOrder_role_user() throws Exception{
    
        mockMvc.perform(jsonGetMockRequest("/orders/search", clientDto))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", roles = {"ADMIN", "USER"})
    void searchPurchaseOrder_role_admin() throws Exception{

        List<PurchaseOrder> response = List.of(
            PurchaseOrder.fakePurchaseOrder(Client.fakeClient()
            , Address.fakeAddress()),
            PurchaseOrder.fakePurchaseOrder(Client.fakeClient()
            , Address.fakeAddress()));

        when(purchaseOrderService.search(any(Client.class))).thenReturn(response);

        mockMvc.perform(jsonGetMockRequest("/orders/search", clientDto))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)));

        verify(purchaseOrderService, times(1)).search(any(Client.class));
    }
    
    private MockHttpServletRequestBuilder createMockRequest(JsonNode payload) {
        return jsonPostMockRequest("/orders/create", this.mapper.valueToTree(payload));
    }

    private MockHttpServletRequestBuilder createMockRequest(Object payload) {
        return jsonPostMockRequest("/orders/create", this.mapper.valueToTree(payload));
    }

    private MockHttpServletRequestBuilder updateMockRequest(JsonNode payload) {
        return jsonPostMockRequest("/orders/update", this.mapper.valueToTree(payload));
    }
    private MockHttpServletRequestBuilder updateMockRequest(Object payload) {
        return jsonPostMockRequest("/orders/update", this.mapper.valueToTree(payload));
    }
    
    private static MockHttpServletRequestBuilder jsonPostMockRequest(String endpoint, JsonNode payload) {
        return MockMvcRequestBuilders.post(endpoint)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(payload.toString());
    }

    private MockHttpServletRequestBuilder jsonGetMockRequest(String endpoint, Object payload) {
        return MockMvcRequestBuilders.get(endpoint)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(this.mapper.valueToTree(payload).toString());
    }
}
