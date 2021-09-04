package com.tui.proof.model;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.anyIterable;
import static org.mockito.Mockito.doAnswer;

import java.util.List;

import com.tui.proof.repository.AddressRepository;
import com.tui.proof.repository.ClientRepository;
import com.tui.proof.repository.PurchaseOrderRepository;

import org.apache.commons.lang3.tuple.Triple;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.DefaultApplicationArguments;

@Tags(value = @Tag("UnitTest"))
public class ModelLoaderTest {

    private Triple<List<Client>, List<Address>, List<PurchaseOrder>> data;

    @Captor
    ArgumentCaptor<List<Client>> clientCaptor;
    @Captor
    ArgumentCaptor<List<Address>> addressCaptor;
    @Captor
    ArgumentCaptor<List<PurchaseOrder>> purchaseOrderCaptor;

    @Mock
    private AddressRepository addressRepo;

    @Mock
    private ClientRepository clientRepo;

    @Mock
    private PurchaseOrderRepository poRepo;

    @InjectMocks
    private ModelLoader modelLoader;

    @BeforeEach
    public void setUp() {
        data = ModelLoader.fakeData(5);
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void fakeData_Clients_Amount_Test() {
        Assertions.assertEquals(5, data.getLeft().size());
    }

    @Test
    public void fakeData_Address_Amount_Test() {
        Assertions.assertEquals(5, data.getMiddle().size());
    }

    @Test
    public void fakeData_PurchaseOrders_Amount_Test() {
        Assertions.assertTrue(data.getRight().size() >= 5);
    }

    @Test
    public void run_Test() throws Exception {

        doAnswer(returnsFirstArg()).when(clientRepo).saveAll(anyIterable());
        doAnswer(returnsFirstArg()).when(addressRepo).saveAll(anyIterable());
        doAnswer(returnsFirstArg()).when(poRepo).saveAll(anyIterable());

        modelLoader.run(new DefaultApplicationArguments(new String[] {}));
        InOrder inOrder = Mockito.inOrder(clientRepo, addressRepo, poRepo);

        inOrder.verify(clientRepo).saveAll(clientCaptor.capture());
        inOrder.verify(addressRepo).saveAll(addressCaptor.capture());
        inOrder.verify(poRepo).saveAll(purchaseOrderCaptor.capture());

        Assertions.assertEquals(200, clientCaptor.getValue().size());
        Assertions.assertEquals(200, addressCaptor.getValue().size());
        Assertions.assertTrue(200 <= purchaseOrderCaptor.getValue().size());
    }
}
