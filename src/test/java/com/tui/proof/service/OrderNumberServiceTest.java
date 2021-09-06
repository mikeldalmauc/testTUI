package com.tui.proof.service;

import static org.mockito.Mockito.doReturn;

import com.tui.proof.repository.PurchaseOrderRepository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@Tags(value = @Tag("UnitTest"))
class OrderNumberServiceTest {
    
    @Mock
    private PurchaseOrderRepository poRepo;

    @InjectMocks
    private OrderNumberService orderNumberService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void createOrderNumber_Test() {
        doReturn("23").when(poRepo).maxOrderNumber();
        Assertions.assertEquals("24",  orderNumberService.createOrderNumber());
    }
}
