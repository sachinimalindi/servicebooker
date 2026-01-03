package com.servicebooker.api.servce;

import com.servicebooker.api.common.ConflictException;
import com.servicebooker.api.common.NotFoundException;
import com.servicebooker.api.dto.CreateCustomerRequest;
import com.servicebooker.api.dto.CustomerResponse;
import com.servicebooker.api.dto.UpdateCustomerRequest;
import com.servicebooker.api.model.Customer;
import com.servicebooker.api.repository.BusinessRepository;
import com.servicebooker.api.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final BusinessRepository businessRepository;

    public CustomerResponse createCustomer( CreateCustomerRequest request) {

        businessRepository.findByIdAndActiveTrue(request.businessId())
                .orElseThrow(() -> NotFoundException.of("Business", request.businessId().toString()));

        customerRepository.findByBusinessIdAndEmail(request.businessId(), request.email())
                .ifPresent(x -> { throw  new ConflictException("Customer email already exists for this business");
                });

        Customer customer = customerRepository.save(new Customer(request.businessId(), request.fullName(), request.email(), request.phone()));
        return getCustomerResponse(customer);
    }

    @Transactional(readOnly = true)
    public CustomerResponse getCustomer(UUID id) {
        var customer = customerRepository.findById(id)
                .orElseThrow(() -> NotFoundException.of("Customer", id.toString()));

        return getCustomerResponse(customer);
    }


    public CustomerResponse updateCustomer(UUID id, UpdateCustomerRequest request) {

        var existingCustomer = customerRepository.findById(id)
                .orElseThrow(() -> NotFoundException.of("Customer", id.toString()));
        existingCustomer.updateCustomer(request.fullName(), request.phone());
        return getCustomerResponse(customerRepository.save(existingCustomer));

    }

    @Transactional(readOnly = true)
    public Page<CustomerResponse> listOfCustomersByBusiness(UUID businessId, PageRequest pageable) {
        return customerRepository.findByBusinessId(businessId, pageable).map(this::getCustomerResponse);
    }

    private CustomerResponse getCustomerResponse(Customer customer) {

        return new CustomerResponse(customer.getId(), customer.getBusinessId(), customer.getFullName(), customer.getEmail(), customer.getPhone());
    }
}
