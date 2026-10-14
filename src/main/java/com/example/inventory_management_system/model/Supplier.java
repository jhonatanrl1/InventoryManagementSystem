package com.example.inventory_management_system.model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;


@Entity
public class Supplier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long supplierId;

    private String name;

    private String contactName;

    private String email;

    private String phone;


    @Column(name = "normalized_phone")
    @JsonIgnore
    private String normalizedPhone;


    public Long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


    public String getNormalizedPhone() {
        return normalizedPhone;
    }


    public void setNormalizedPhone(String normalizedPhone) {
        this.normalizedPhone = normalizedPhone;
    }



}

