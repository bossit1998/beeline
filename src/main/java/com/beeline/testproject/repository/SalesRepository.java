package com.beeline.testproject.repository;

import com.beeline.testproject.entity.Sales;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface SalesRepository extends JpaRepository<Sales, Long> {

    Optional<Sales> findSalesByIdAndIsDeletedFalse(Long id);

    @Query(value = "select s.item as item, s.price as price from Sales s group by s.item, s.price")
    List<Map<String, Object>> getUniqueItems();

    Page<Sales> findAllBy(Pageable pageable);

    Page<Sales> findSalesByItem(String item, Pageable pageable);

    Page<Sales> findSalesByCustomerId(Long customerId, Pageable pageable);

    Page<Sales> findSalesByCustomerIdOrderByPriceAsc(Long customerId, Pageable pageable);

    Page<Sales> findSalesByCustomerIdOrderByPriceDesc(Long customerId, Pageable pageable);

    Page<Sales> findSalesByItemAndCustomerId(String item, Long customerId, Pageable pageable);

    @Query(value = "select s.customerId as customerId, sum(s.price) as revenue from Sales s " +
            "group by s.customerId")
    List<Map<String, Object>> getSalesRevenueByCustomers();
}