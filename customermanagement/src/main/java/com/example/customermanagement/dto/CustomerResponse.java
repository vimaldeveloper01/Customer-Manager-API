package com.example.customermanagement.dto;

import com.example.customermanagement.constants.CustomerConstants;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;


public class CustomerResponse {
    private UUID id;
    private String name;
    private String email;
    private BigDecimal annualSpend;
    private LocalDateTime lastPurchaseDate;
    private String tier;

    public void calculateTier() {
        if (annualSpend == null) {
            this.tier = CustomerConstants.SILVER;
            return;
        }
        LocalDateTime now = LocalDateTime.now();
        long months = lastPurchaseDate != null ? ChronoUnit.MONTHS.between(lastPurchaseDate, now) : Long.MAX_VALUE;
        if (annualSpend.compareTo(new BigDecimal("10000")) >= 0 && months <= 6) {
            this.tier = CustomerConstants.PLATINUM;
        } else if (annualSpend.compareTo(new BigDecimal("1000")) >= 0 && months <= 12) {
            this.tier = CustomerConstants.GOLD;
        } else {
            this.tier = CustomerConstants.SILVER;
        }
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public BigDecimal getAnnualSpend() {
        return annualSpend;
    }

    public void setAnnualSpend(BigDecimal annualSpend) {
        this.annualSpend = annualSpend;
    }

    public LocalDateTime getLastPurchaseDate() {
        return lastPurchaseDate;
    }

    public void setLastPurchaseDate(LocalDateTime lastPurchaseDate) {
        this.lastPurchaseDate = lastPurchaseDate;
    }

    public String getTier() {
        return tier;
    }

    public void setTier(String tier) {
        this.tier = tier;
    }
}
