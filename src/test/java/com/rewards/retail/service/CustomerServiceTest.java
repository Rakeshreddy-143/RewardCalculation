package com.rewards.retail.service;

import com.rewards.retail.entity.Customer;
import com.rewards.retail.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomerServiceTest {

    private CustomerRepository customerRepository;
    private CustomerService customerService;

    @BeforeEach
    void setUp() {
        customerRepository = mock(CustomerRepository.class);
        customerService = new CustomerService(customerRepository);
    }

    @Test
    void testIsCustomerExists_whenCustomerExists() {
        Long customerId = 1L;
        when(customerRepository.existsById(customerId)).thenReturn(true);

        boolean exists = customerService.isCustomerExists(customerId);
        assertTrue(exists);
        verify(customerRepository, times(1)).existsById(customerId);
    }

    @Test
    void testIsCustomerExists_whenCustomerDoesNotExist() {
        Long customerId = 2L;
        when(customerRepository.existsById(customerId)).thenReturn(false);

        boolean exists = customerService.isCustomerExists(customerId);
        assertFalse(exists);
        verify(customerRepository, times(1)).existsById(customerId);
    }

    @Test
    void testGetCustomerById_whenCustomerExists() {
        Long customerId = 3L;
        Customer mockCustomer = new Customer();
        mockCustomer.setId(customerId);
        mockCustomer.setName("Alice");
        mockCustomer.setEmail("alice@example.com");
        mockCustomer.setPhoneNumber("9999999999");

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(mockCustomer));

        Customer result = customerService.getCustomerById(customerId);
        assertNotNull(result);
        assertEquals("Alice", result.getName());
        assertEquals("alice@example.com", result.getEmail());
        assertEquals("9999999999", result.getPhoneNumber());

        verify(customerRepository, times(1)).findById(customerId);
    }

    @Test
    void testGetCustomerById_whenCustomerDoesNotExist() {
        Long customerId = 4L;
        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                customerService.getCustomerById(customerId));

        assertEquals("Customer not found with ID: 4", exception.getMessage());
        verify(customerRepository, times(1)).findById(customerId);
    }
}
