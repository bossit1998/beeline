package com.beeline.testproject.controller;

import com.beeline.testproject.model.SalesModel;
import com.beeline.testproject.service.SalesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class SalesController {

    @Autowired
    private SalesService salesService;

    @PostMapping(value = "/sale/create")
    public ResponseEntity<?> createNewSale(@RequestBody SalesModel salesModel) {
        return salesService.createNewSale(salesModel);
    }

    @GetMapping(value = "/sale/get")
    public ResponseEntity<?> getSale(@RequestParam Long id) {
        return salesService.getSale(id);
    }

    @PostMapping(value = "/sale/update")
    public ResponseEntity<?> updateSale(@RequestBody SalesModel salesModel) {
        return salesService.updateSale(salesModel);
    }

    @DeleteMapping(value = "/sale/delete")
    public ResponseEntity<?> deleteSale(@RequestBody SalesModel salesModel) {
        return salesService.deleteSale(salesModel);
    }

    @GetMapping(value = "/sales")
    public ResponseEntity<?> getSales(@RequestParam(required = false) String item,
                                      @RequestParam(required = false) Long customerId,
                                      @RequestParam(required = false) Integer page,
                                      @RequestParam(required = false) Integer pageSize) {
        return salesService.getSales(item, customerId, page, pageSize);
    }

    @GetMapping(value = "/item")
    public ResponseEntity<?> getItems() {
        return salesService.getItems();
    }

    @GetMapping(value = "/CUSTOMER/{CUSTOMER_ID}/sales")
    public ResponseEntity<?> getSalesByCustomer(@PathVariable("CUSTOMER_ID") Long customerId,
                                                @RequestParam(value = "sort", defaultValue = "desc", required = false) String sort,
                                                @RequestParam(required = false) Integer page,
                                                @RequestParam(required = false) Integer pageSize) {
        return salesService.getSalesByCustomer(customerId, sort, page, pageSize);
    }

    @GetMapping(value = "/report/revenue")
    public ResponseEntity<?> getRevenue() {
        return salesService.getRevenue();
    }
}