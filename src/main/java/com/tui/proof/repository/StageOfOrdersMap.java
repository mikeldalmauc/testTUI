package com.tui.proof.repository;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.tui.proof.model.PurchaseOrder;

import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
public class StageOfOrdersMap {

    /**
     * This map contains orders temporarily until they are ready to 
     * be sent to kitchen.
     * 
     * During the time orders are "on stage", they are allowed to be updated.
     * After this, orders are sent to kitchen and persisted.
     */
    private Map<String,PurchaseOrder> ordersOnStage = Collections.synchronizedMap(new HashMap<String, PurchaseOrder>());

}
