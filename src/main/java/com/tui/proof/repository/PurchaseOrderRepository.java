package com.tui.proof.repository;
import java.util.List;

import com.tui.proof.model.Client;
import com.tui.proof.model.PurchaseOrder;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface PurchaseOrderRepository extends CrudRepository<PurchaseOrder, Long> {
    
    @Query
    public PurchaseOrder findByNumber(String number);

    @Query(value = "SELECT po FROM PurchaseOrder po WHERE po.client IN (:clients)")
    public List<PurchaseOrder> findByClient(List<Client> clients);
}
