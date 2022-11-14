package com.beeline.testproject.service;

import com.beeline.testproject.model.ResponseModel;
import com.beeline.testproject.model.SalesModel;
import com.beeline.testproject.repository.SalesRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class SalesServiceTest {

    @Mock
    private SalesRepository salesRepository;

    @InjectMocks
    SalesService salesService;

    @Test
    void createNewSaleSuccess() {
        SalesModel salesModel = new SalesModel();
        salesModel.setItem("testItem");
        salesModel.setCustomerId(0L);
        salesModel.setPrice(100);

        ResponseModel<SalesModel> target = new ResponseModel<>();
        target.setSuccess(true);
        target.setMessage("Success");
        target.setData(salesModel);

        ResponseModel<SalesModel> result = salesService.createSale(salesModel);
        assertEquals(target.getSuccess(), result.getSuccess());
    }
}