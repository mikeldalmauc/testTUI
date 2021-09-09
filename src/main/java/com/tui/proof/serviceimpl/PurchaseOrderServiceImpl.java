package com.tui.proof.serviceimpl;

import java.util.List;
import java.util.stream.Stream;

import javax.jms.TextMessage;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tui.proof.model.Client;
import com.tui.proof.model.PurchaseOrder;
import com.tui.proof.repository.ClientRepository;
import com.tui.proof.repository.PurchaseOrderRepository;
import com.tui.proof.repository.StageOfOrdersMap;
import com.tui.proof.service.PurchaseOrderService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@Transactional
public class PurchaseOrderServiceImpl implements PurchaseOrderService {
    
    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private PurchaseOrderRepository poRepo;

    @Autowired
    private ClientRepository clientRepo;

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private StageOfOrdersMap stage;
    
    @Value("${tui.activemq.orders.queue}")
    String ordersQueue;
   
    @Value("${tui.activemq.kitchen.queue}")
    String kitchenQueue;

    @Value("${tui.orders.waittime}")
    Integer waittime;
    
    @Override
    public List<PurchaseOrder> search(Client client) {
        return poRepo.findByClient(clientRepo.searchByClientData(client));
    }

    private String createOrderNumber(){
             // This is the next value to the max of the database and the staged.
        return (Stream.concat(
            Stream.of(poRepo.maxOrderNumber()), // max of the database
            stage.getOrdersOnStage().keySet().stream())
                .map(Long::parseLong)
                .reduce(0L , (max, actual) -> max > actual ? max : actual)
                + 1) + "";
    }

    public PurchaseOrder create(PurchaseOrder po){

        po.setNumber(""+createOrderNumber());

        // Put new order on stage
        stage.getOrdersOnStage().put(po.getNumber(), po);

        // Add order number to the 5' waiting Queue
        queueOrderNumber(po.getNumber());

        return po;
    }

    public PurchaseOrder update(PurchaseOrder po){
        
        PurchaseOrder oldPo = stage.getOrdersOnStage().get(po.getNumber());
        String oldNumber = po.getNumber();
        if(oldPo != null){
            // Save again
            String newOrderNumber = create(po).getNumber();
            // Remove old po from queue
            stage.getOrdersOnStage().remove(oldNumber);
            // Update order number to returning po object
            po.setNumber(newOrderNumber);
        }

        // Non changed order number means the Update failed
        return po;
    }

    /**
     * 
     * @param po
     * @return
     */
    private PurchaseOrder queueJsonPo(PurchaseOrder po){

        try {
            po.getClient();
            po.getDeliveryAddress();
            String porderJson = mapper.writeValueAsString(po);
            jmsTemplate.setDeliveryDelay(-1);
            jmsTemplate.setPriority(9);
            jmsTemplate.send(kitchenQueue, s -> {
                TextMessage m = s.createTextMessage(porderJson);
                m.setStringProperty("orderNumber", po.getNumber());
                return m;
            });
    
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }
        
        return po;
    }

    /**
     * 
     * @param po
     * @return
     */
    private String queueOrderNumber(String orderNumber){

        jmsTemplate.setDeliveryDelay(waittime*1000L);  
        jmsTemplate.setPriority(2);
        jmsTemplate.send(ordersQueue, s -> {
            TextMessage m = s.createTextMessage(orderNumber);
            m.setStringProperty("orderNumber", orderNumber);
            return m;
        });

        log.info("Added to queue orderNumber : " + orderNumber);
        return orderNumber;
    }

    /**
     * Message delivery is delayed
     * @param purchaseOrder
     */
    @JmsListener(destination = "${tui.activemq.orders.queue}")
    public void receiveMessage(String payload) {     

        PurchaseOrder poToKitchen = stage.getOrdersOnStage().get(payload);
        if(poToKitchen != null){
            // Remove order from stage
            stage.getOrdersOnStage().remove(payload);
            // Save
            poRepo.save(poToKitchen);
            // Queue to kitchen
            queueJsonPo(poToKitchen);

            log.info("Sending order to kitchen!\n" + poToKitchen.getPilotes() + " pilotes for " + poToKitchen.getClient().getFirstName());
        }else{
            log.info("Order lost:" + payload);
        }
    }

        /**
     * Message delivery is delayed
     * @param purchaseOrder
     */
    @JmsListener(destination = "${tui.activemq.kitchen.queue}")
    public void kitchenReceiveMessage(String payload) {     

        log.info("Order received in kitchen : {}", payload);
        log.info("Cooking pilotes...");
    }

    @Override
    public PurchaseOrder findByNumber(String number) {
        return poRepo.findByNumber(number);
    }

}
