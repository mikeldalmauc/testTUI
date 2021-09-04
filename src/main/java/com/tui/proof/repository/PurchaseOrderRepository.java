package com.tui.proof.repository;
import com.tui.proof.model.PurchaseOrder;
import org.springframework.data.repository.CrudRepository;

public interface PurchaseOrderRepository extends CrudRepository<PurchaseOrder, Long>{
    
}
