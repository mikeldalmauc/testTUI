package com.tui.proof.service;

import com.tui.proof.repository.PurchaseOrderRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderNumberService {
    
    @Autowired
	private PurchaseOrderRepository poRepo;

    public String createOrderNumber(){
        return "" + (Long.parseLong(poRepo.maxOrderNumber()) + 1);
    }
}
