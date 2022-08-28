package com.jpmc.theater.services;

import com.jpmc.theater.data.model.Customer;
import com.jpmc.theater.exception.NoDataFoundException;
import com.jpmc.theater.utills.IdGenerator;

import java.util.HashMap;
import java.util.Map;

public class CustomerManager {

    private final Map<String, Customer> customerMap;

    public CustomerManager(){
        this.customerMap=new HashMap<>();
    }

    public Customer createCustomer(String name,String email){
        String custId= IdGenerator.nextId(customerMap,"C");
        Customer customer=new Customer(custId,name,email);
        customerMap.put(custId,customer);
        return customer;
    }

    public Customer removeCustomer(String custId){
       if(!customerMap.containsKey(custId)){
           NoDataFoundException.throwException("Customer",custId);
       }
        return customerMap.remove(custId);
    }

    public Map<String, Customer> getCustomerMap() {
        return customerMap;
    }
}
