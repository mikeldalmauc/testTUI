package com.tui.proof.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verifyNoInteractions;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tui.proof.model.Client;
import com.tui.proof.model.PurchaseOrder;
import com.tui.proof.repository.ClientRepository;
import com.tui.proof.repository.PurchaseOrderRepository;
import com.tui.proof.repository.StageOfOrdersMap;
import com.tui.proof.serviceimpl.PurchaseOrderServiceImpl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.util.ReflectionTestUtils;

@Tags(value = { @Tag("UnitTest"), @Tag("ServiceTest") })
class PurchaseOrderServiceTest {
    
    
    @InjectMocks
    PurchaseOrderService purchaseOrderService = new PurchaseOrderServiceImpl();

    @Mock
    PurchaseOrderRepository poRepo;

    @Mock
    ClientRepository clientRepo;
    
    @Mock
    StageOfOrdersMap stage;
    
    @Mock
    JmsTemplate jmsTemplate;

    @Mock
    ObjectMapper mapper;

    Map<String, PurchaseOrder> stagedOrders;

    Long max;
     
    String ordersQueue = "ordersQueue";
    String kitchenQueue = "kitchenQueue";
    Integer waittime = 5;


    @BeforeEach
    void setUp() {

        stagedOrders = Stream.iterate(0, n -> n + 1)
            .map(n -> PurchaseOrder.fakePurchaseOrder())
            .limit(15)
            .collect(Collectors.toMap(PurchaseOrder::getNumber, Function.identity()));
        
        MockitoAnnotations.initMocks(this);
        
        ReflectionTestUtils.setField(purchaseOrderService, "ordersQueue", ordersQueue);
        ReflectionTestUtils.setField(purchaseOrderService, "kitchenQueue", kitchenQueue);
        ReflectionTestUtils.setField(purchaseOrderService, "waittime", waittime);

        Mockito.doNothing().when(jmsTemplate).setDeliveryDelay(anyLong());
        Mockito.doNothing().when(jmsTemplate).setPriority(anyInt());
        Mockito.doNothing().when(jmsTemplate).send(anyString(), any());

        Mockito.doReturn(stagedOrders).when(stage).getOrdersOnStage();

        max = stagedOrders.keySet().stream().map(Long::parseLong)
        .max(Comparator.naturalOrder()).get() ;
        Mockito.doReturn(1555656 + "").when(poRepo).maxOrderNumber();
    }
    
    @Test
    void search_Test(){

        Client clientData = Client.fakeClient();

        List<Client> clients = List.of(
            Client.fakeClient()
            ,Client.fakeClient()
            ,Client.fakeClient());

        List<PurchaseOrder> pos = List.of(
            PurchaseOrder.fakePurchaseOrder() 
            ,PurchaseOrder.fakePurchaseOrder() 
        );

        Mockito.doReturn(clients).when(clientRepo).searchByClientData(any(Client.class));
        Mockito.doReturn(pos).when(poRepo).findByClient(anyList());

        List<PurchaseOrder> results = purchaseOrderService.search(clientData); 

        InOrder inOrder =  Mockito.inOrder(clientRepo, poRepo);
        inOrder.verify(clientRepo).searchByClientData(clientData);
        inOrder.verify(poRepo).findByClient(clients);

        Assertions.assertEquals(pos, results);
    }

    @Test
    void create_Test(){

        PurchaseOrder po = purchaseOrderService.create(PurchaseOrder.fakePurchaseOrder());

        Assertions.assertEquals((max + 1)+"", po.getNumber());
        Assertions.assertNull(po.getId());
        Assertions.assertNull(po.getDeliveryAddress().getId());
        Assertions.assertNull(po.getClient().getId());
        Assertions.assertEquals(16, stagedOrders.size());
        
        assertQueueOrderNumber(po.getNumber());
    }

    @Test
    void update_Not_Updatable_Test(){

        PurchaseOrder toUpdatePo = PurchaseOrder.fakePurchaseOrder();
        PurchaseOrder po = purchaseOrderService.update(toUpdatePo);

        Assertions.assertEquals(toUpdatePo.getNumber(), po.getNumber());
        
        verifyNoInteractions(jmsTemplate);
    }

    @Test
    void update_Test(){

        PurchaseOrder toUpdatePo = stagedOrders.values().stream().findAny().get();
        String orig = toUpdatePo.getNumber();
        PurchaseOrder po = purchaseOrderService.update(toUpdatePo);

        Assertions.assertNotEquals(orig, po.getNumber());
        Assertions.assertEquals((max + 1)+"", po.getNumber());
        Assertions.assertEquals(15, stagedOrders.size());

        assertQueueOrderNumber(po.getNumber());
    }

    @Test
    void findByNumber_Test(){
        PurchaseOrder orig = PurchaseOrder.fakePurchaseOrder();
        Mockito.doReturn(orig).when(poRepo).findByNumber(anyString());
        
        PurchaseOrder po = purchaseOrderService.findByNumber("number");

        Assertions.assertEquals(orig, po);
        Mockito.verify(poRepo).findByNumber("number");
    }

    
    @Test
    void createOrderNumber_Test() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{

        Method method = PurchaseOrderServiceImpl.class.getDeclaredMethod("createOrderNumber");
        method.setAccessible(true);
        String res = (String) method.invoke(purchaseOrderService);

        Assertions.assertEquals((max + 1)+"", res);
    }
    
    @Test
    void queueOrderNumber_Test() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{

        Method method = PurchaseOrderServiceImpl.class.getDeclaredMethod("queueOrderNumber", String.class);
        method.setAccessible(true);
        String res = (String) method.invoke(purchaseOrderService, "number");
        Assertions.assertEquals("number", res);

        assertQueueOrderNumber("number");
    }

    @Test
    void queueJsonPo_Test() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, JsonProcessingException{
        PurchaseOrder orig = PurchaseOrder.fakePurchaseOrder();
        doReturn("serialized").when(mapper).writeValueAsString(any());
       
        Method method = PurchaseOrderServiceImpl.class.getDeclaredMethod("queueJsonPo", PurchaseOrder .class);
        method.setAccessible(true);
        PurchaseOrder res = (PurchaseOrder) method.invoke(purchaseOrderService, orig);
        Assertions.assertEquals(orig, res);

        InOrder inOrder = Mockito.inOrder(jmsTemplate,mapper);
        inOrder.verify(mapper).writeValueAsString(orig);
        inOrder.verify(jmsTemplate).setDeliveryDelay(-1);
        inOrder.verify(jmsTemplate).setPriority(9);
        inOrder.verify(jmsTemplate).send(eq(kitchenQueue), any());
    }

    @Test
    void queueJsonPo_Exception_Test() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, JsonProcessingException{
        PurchaseOrder orig = PurchaseOrder.fakePurchaseOrder();
        doThrow(JsonProcessingException.class).when(mapper).writeValueAsString(any());
       
        Method method = PurchaseOrderServiceImpl.class.getDeclaredMethod("queueJsonPo", PurchaseOrder .class);
        method.setAccessible(true);
        PurchaseOrder res = (PurchaseOrder) method.invoke(purchaseOrderService, orig);
        Assertions.assertEquals(orig, res);

        verifyNoInteractions(jmsTemplate);
    }


    private void assertQueueOrderNumber(String number){
        InOrder inOrder = Mockito.inOrder(jmsTemplate);
        inOrder.verify(jmsTemplate).setDeliveryDelay(waittime*1000);
        inOrder.verify(jmsTemplate).setPriority(2);
        inOrder.verify(jmsTemplate).send(eq(ordersQueue), any());
    }
}
