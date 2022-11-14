package com.beeline.testproject.service;

import com.beeline.testproject.entity.Sales;
import com.beeline.testproject.model.ResponseModel;
import com.beeline.testproject.model.SalesModel;
import com.beeline.testproject.repository.SalesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.beeline.testproject.util.Util.getPageable;

@Service
public class SalesService {

    @Autowired
    private SalesRepository salesRepository;

    public ResponseEntity<?> createNewSale(SalesModel salesModel) {
        return ResponseEntity.ok(createSale(salesModel));
    }

    public ResponseEntity<?> getSale(Long id) {
        Optional<Sales> optionalSale = salesRepository.findSalesByIdAndIsDeletedFalse(id);
        if (optionalSale.isEmpty()) {
            return ResponseEntity.ok(new ResponseModel<>(false, "Sale not found", null));
        }

        return ResponseEntity.ok(new ResponseModel<>(true, "Success", getSale(optionalSale.get()).getData()));
    }

    public ResponseEntity<?> updateSale(SalesModel salesModel) {
        return ResponseEntity.ok(editSale(salesModel));
    }

    public ResponseEntity<?> deleteSale(SalesModel salesModel) {
        return ResponseEntity.ok(removeSale(salesModel));
    }

    public ResponseEntity<?> getSales(String item, Long customerId, Integer page, Integer pageSize) {
        Pageable pageable = getPageable(page, pageSize);

        Page<Sales> salesPage;

        if (item != null && customerId != null) {
            salesPage = salesRepository.findSalesByItemAndCustomerId(item, customerId, pageable);
        } else if (item == null && customerId != null) {
            salesPage = salesRepository.findSalesByCustomerId(customerId, pageable);
        } else if (item != null) {
            salesPage = salesRepository.findSalesByItem(item, pageable);
        } else {
            salesPage = salesRepository.findAllBy(pageable);
        }

        Map<String, Object> pageResponse = new HashMap<>();
        pageResponse.put("totalElements", salesPage.getTotalElements());
        pageResponse.put("totalPages", salesPage.getTotalPages());
        pageResponse.put("data", salesPage.getContent().stream().map(sales -> getSale(sales).getData()).collect(Collectors.toList()));

        return ResponseEntity.ok(new ResponseModel<>(true, "Success", pageResponse));
    }

    public ResponseEntity<?> getItems() {
        List<String> itemsListResponse = new ArrayList<>();

        List<Map<String, Object>> uniqueItemsList = salesRepository.getUniqueItems();

        if (uniqueItemsList.isEmpty()) {
            return ResponseEntity.ok(new ResponseModel<>(true, "Success", itemsListResponse));
        }

        for (Map<String, Object> sale : uniqueItemsList) {
            itemsListResponse.add(sale.get("item") + "-" + sale.get("price"));
        }

        return ResponseEntity.ok(new ResponseModel<>(true, "Success", itemsListResponse));
    }

    public ResponseEntity<?> getSalesByCustomer(Long customerId, String sort, Integer page, Integer pageSize) {
        Pageable pageable = getPageable(page, pageSize);

        Page<Sales> salesPage;
        if (sort.equals("asc")) {
            salesPage = salesRepository.findSalesByCustomerIdOrderByPriceAsc(customerId, pageable);
        } else {
            salesPage = salesRepository.findSalesByCustomerIdOrderByPriceDesc(customerId, pageable);
        }

        Map<String, Object> pageResponse = new HashMap<>();
        pageResponse.put("totalElements", salesPage.getTotalElements());
        pageResponse.put("totalPages", salesPage.getTotalPages());
        pageResponse.put("data", salesPage.stream().map(sales -> getSale(sales)).collect(Collectors.toList()));

        return ResponseEntity.ok(new ResponseModel<>(true, "Success", pageResponse));
    }

    public ResponseEntity<?> getRevenue() {
        List<Map<String, Object>> salesList = salesRepository.getSalesRevenueByCustomers();

        if (salesList.isEmpty()) {
            return ResponseEntity.ok(new ResponseModel<>(true, "Success", salesList));
        }

        List<Map<String, Object>> salesResponse = new ArrayList<>();
        for (Map<String, Object> sale : salesList) {
            Map<String, Object> revenue = new HashMap<>();
            revenue.put("customerId", sale.get("customerId"));
            revenue.put("revenue", sale.get("revenue"));
            salesResponse.add(revenue);
        }

        return ResponseEntity.ok(new ResponseModel<>(true, "Success", salesResponse));
    }

    public ResponseModel<SalesModel> createSale(SalesModel salesModel) {
        try {
            Sales sale = new Sales();
            sale.setCustomerId(salesModel.getCustomerId());
            sale.setItem(salesModel.getItem());
            sale.setPrice(salesModel.getPrice());
            sale.setCreatedDate(LocalDateTime.now());

            salesRepository.save(sale);

            return new ResponseModel<>(true, "Success", salesModel);
        } catch (Exception e) {
            return new ResponseModel<>(false, e.getMessage(), null);
        }
    }

    public ResponseModel<SalesModel> editSale(SalesModel salesModel) {
        try {
            Optional<Sales> optionalSale = salesRepository.findById(salesModel.getId());

            if (optionalSale.isEmpty()) {
                return new ResponseModel<>(false, "Sale not found", null);
            }

            Sales sale = optionalSale.get();

            sale.setCustomerId(salesModel.getCustomerId() == null ? sale.getCustomerId() : salesModel.getCustomerId());
            sale.setItem(salesModel.getItem() == null ? sale.getItem() : salesModel.getItem());
            sale.setPrice(salesModel.getPrice() == null ? sale.getPrice() : salesModel.getPrice());
            salesRepository.save(sale);

            return new ResponseModel<>(true, "Success", salesModel);
        } catch (Exception e) {
            return new ResponseModel<>(false, e.getMessage(), null);
        }
    }

    public ResponseModel<SalesModel> removeSale(SalesModel salesModel) {
        try {
            Optional<Sales> optionalSale = salesRepository.findById(salesModel.getId());

            if (optionalSale.isEmpty()) {
                return new ResponseModel<>(false, "Sale not found", null);
            }

            Sales sale = optionalSale.get();
            sale.setIsDeleted(true);
            sale.setDeletedDate(LocalDateTime.now());

            salesRepository.save(sale);

            return new ResponseModel<>(true, "Success", null);
        } catch (Exception e) {
            return new ResponseModel<>(false, e.getMessage(), null);
        }
    }

    public ResponseModel<SalesModel> getSale(Sales sale) {
        try {
            SalesModel salesModel = new SalesModel();

            salesModel.setId(sale.getId());
            salesModel.setCustomerId(sale.getCustomerId());
            salesModel.setItem(sale.getItem());
            salesModel.setPrice(sale.getPrice());

            return new ResponseModel<>(true, "Success", salesModel);
        } catch (Exception e) {
            return new ResponseModel<>(false, e.getMessage(), null);
        }
    }
}