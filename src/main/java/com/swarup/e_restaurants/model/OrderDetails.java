package com.swarup.e_restaurants.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.swarup.e_restaurants.audit.Auditable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "mst_orderdetails")
@JsonIgnoreProperties({ "createdBy", "createdDate", "lastModifiedBy", "lastModifiedDate"})
@EntityListeners(AuditingEntityListener.class)
public class       OrderDetails extends Auditable<String>{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int itemId;
    private int billNo;
    private int restId;
    private float rate;
    private int qty;
    private float totalAmount;
    private int customerId;
    private String orderBy;
    private String modeOfPayment;
    private String delivaryAddress;
    private String orderStatus;
    private String contactNo;
    private LocalDate purchaseDate;
    private LocalTime purchaseTime;
    private LocalDate delivaryDate;
    private LocalTime delivaryTime;
    private LocalDateTime expectedDelivary;
    private Integer delivaryPersonId;

    @Transient
    private String images;

    @Transient
    private String itemName;

    @Transient
    private String resturentName;

    @Transient
    private OrderBill orderBill;

    @Transient
    private float platformCharge;

    @Transient
    private float netEarning;

    @Transient
    private float net;

    @Transient
    private float tax;

    @Transient
    private float gross;

    @Transient
    private String pickSelf;

    @Transient
    private String restAddress;
}
