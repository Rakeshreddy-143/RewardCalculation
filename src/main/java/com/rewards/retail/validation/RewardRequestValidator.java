package com.rewards.retail.validation;

import com.rewards.retail.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class RewardRequestValidator {

    private CustomerService customerService;

    @Autowired
    public RewardRequestValidator(CustomerService customerService) {
        this.customerService = customerService;
    }
    public void validate(Long customerId, LocalDate from, LocalDate to) {
        if (customerId == null || customerId <= 0) {
            throw new IllegalArgumentException("Invalid customerId: must be a positive number.");
        }
        if( !customerService.isCustomerExists(customerId) ) {
            throw new IllegalArgumentException("Customer with ID " + customerId + " does not exist.");
        }
        if ((from == null && to != null) || (from != null && to == null)) {
            throw new IllegalArgumentException("Both 'from' and 'to' dates must be provided together or both omitted.");
        }
        if (from != null) {
            if (from.isAfter(to)) {
                throw new IllegalArgumentException("'from' date must not be after 'to' date.");
            }
            if (from.isAfter(LocalDate.now()) || to.isAfter(LocalDate.now())) {
                throw new IllegalArgumentException("Dates cannot be in the future.");
            }
        }
    }
}