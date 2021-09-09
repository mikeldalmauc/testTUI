package com.tui.proof.service;

import java.util.List;

import com.tui.proof.model.Client;
import com.tui.proof.model.PurchaseOrder;

/**
 * 
 */
public interface PurchaseOrderService {
 
    /**
     * 
     * @param client
     * @return
     */
    public List<PurchaseOrder> search(Client client);

    /**
     * 
     * @param po
     * @return
     */
    public PurchaseOrder create(PurchaseOrder po);

    /**
     * 
     * @param number
     * @return
     */
    public PurchaseOrder findByNumber(String number);

    /**
     * 
     * @param po
     * @return
     */
    public PurchaseOrder update(PurchaseOrder po);

}
