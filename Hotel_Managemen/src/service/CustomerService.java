package service;

import model.Customer;
import java.util.*;

public class CustomerService {
    private static final CustomerService instance = new CustomerService();
    private final Map<String, Customer> customers = new HashMap<>();

    private CustomerService() { }  // Private constructor

    public static CustomerService getInstance() {
        return instance;
    }

    public void addCustomer(String firstName, String lastName, String email) {
        String normalizedEmail = email.toLowerCase();  // Convert email to lowercase

        if (customers.containsKey(normalizedEmail)) {
            System.out.println("Error: Customer with this email already exists!");
            return;
        }

        Customer customer = new Customer(firstName, lastName, normalizedEmail);
        customers.put(normalizedEmail, customer);
        System.out.println("Customer added successfully!");
    }

    public Customer getCustomer(String email) {
        return customers.get(email.toLowerCase());  // Lookup using normalized email
    }

    public Collection<Customer> getAllCustomers() {
        return customers.values();
    }
}
