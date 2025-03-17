package com.swarup.e_restaurants.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.swarup.e_restaurants.model.OrderDetails;


public interface OrderDetailsRepository extends JpaRepository<OrderDetails, Integer>{
    
    List<OrderDetails> findAllByCustomerId(int customerId);

    List<OrderDetails> findAllByRestId(int restId);

    @Query(value = "SELECT * FROM mst_orderdetails where rest_id=?1 and bill_no!=0 and purchase_date between ?2 and ?3 order by purchase_date desc", nativeQuery = true)
    List<OrderDetails> fetchResturantWiseSale(int restId, String startDate, String endDate);


    @Query(value = "SELECT * FROM mst_orderdetails WHERE order_status IN ('A', 'R') AND (delivary_person_id IS NULL OR (delivary_person_id = ?1)) order by purchase_date desc, purchase_time asc", nativeQuery = true)
    List<OrderDetails>  fetchOpenOrderForPickDelivary(Integer userId);
}
