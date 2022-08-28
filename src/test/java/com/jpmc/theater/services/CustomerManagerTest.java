package com.jpmc.theater.services;

import com.jpmc.theater.TestBase;
import com.jpmc.theater.data.model.Customer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CustomerManagerTest extends TestBase {

    CustomerManager customerManager;

    @BeforeAll
    void setUp() {
        customerManager=customerManager();
    }

    @Test
    public void testRemoveException(){
        Executable executable=() -> customerManager.removeCustomer("InvalidCustomerId");
        String expectedErrorMessage="No Customer found for the given input InvalidCustomerId";
        assertNoDataException(executable, expectedErrorMessage);
    }

    @Test
    public void testCreateSuccess(){
        customerManager.getCustomerMap().clear();
        Customer customer=customerManager.createCustomer("ABC","ABC&Gmail.com");
        assertTrue(customerManager.getCustomerMap().size()==1);
        assertEquals("C1",customer.getId());
    }

    @Test
    public void testRemoveSuccess(){
        customerManager.getCustomerMap().clear();
        Customer customer=customerManager.createCustomer("ABC","ABC&Gmail.com");
        assertTrue(customerManager.getCustomerMap().size()==1);
        Customer removedCust=customerManager.removeCustomer(customer.getId());
        assertTrue(customerManager.getCustomerMap().size()==0);
        assertEquals(removedCust,customer);
    }

}