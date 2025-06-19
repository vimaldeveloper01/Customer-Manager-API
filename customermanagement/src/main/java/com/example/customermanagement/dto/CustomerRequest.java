package com.example.customermanagement.dto;

import com.example.customermanagement.constants.CustomerConstants;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Component
public class CustomerRequest {
    @NotBlank(message = CustomerConstants.NAME_REQUIRED)
    private String name;

    @NotBlank(message = CustomerConstants.EMAIL_REQUIRED)
    @Email(message = CustomerConstants.INVALID_EMAIL)
    @Pattern(regexp = CustomerConstants.EMAIL_REGEX, message = CustomerConstants.INVALID_EMAIL)
    private String email;

    private BigDecimal annualSpend;

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

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime lastPurchaseDate;
}
