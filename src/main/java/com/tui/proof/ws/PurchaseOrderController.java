package com.tui.proof.ws;

import javax.validation.Valid;

import com.tui.proof.dto.PurchaseOrderDto;
import com.tui.proof.service.OrderNumberService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping("/orders")
public class PurchaseOrderController {

  @Autowired
  KafkaTemplate<String, String> kafkaTemplate;

  @Autowired
  OrderNumberService orderNumberService;

  @Value(value = "${purchaseOrderTopicName}")
  private String purchaseOrderTopicName;

  @GetMapping("/")
  public ResponseEntity<String> hello() {
    return ResponseEntity.ok("Hello, Im Miquel Montoro and you can order pilotes in this service.");
  }

  @PostMapping("/create")
  @ResponseBody
  public ResponseEntity<PurchaseOrderDto> createPurchaseOrder(@Valid @RequestBody PurchaseOrderDto purchaseOrder) {
    log.info("Creating");

    // Save or update Client
    purchaseOrder.getClient().setId(122323L);

    // Save or update Address
    purchaseOrder.getDeliveryAddress().setId(122323L);

    // Set order number and queue the order
    purchaseOrder.setNumber(orderNumberService.createOrderNumber());
    // kafkaTemplate.send(purchaseOrderTopicName, purchaseOrder.getNumber());

    return ResponseEntity.ok(purchaseOrder);
  }

  @PostMapping("/update")
  @ResponseBody
  public ResponseEntity<PurchaseOrderDto> updatePurchaseOrder(@Valid @RequestBody PurchaseOrderDto purchaseOrder) {
    log.info("update");

    return ResponseEntity.ok(purchaseOrder);
  }
}
