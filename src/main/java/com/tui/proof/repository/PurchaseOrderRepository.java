package com.tui.proof.repository;
import com.tui.proof.model.PurchaseOrder;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface PurchaseOrderRepository extends CrudRepository<PurchaseOrder, Long>{
    
    @Query(value = "select max(number) from PurchaseOrder", nativeQuery=true)
    public String maxOrderNumber();
}
