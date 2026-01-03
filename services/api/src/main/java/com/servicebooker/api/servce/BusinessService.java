package com.servicebooker.api.servce;

import com.servicebooker.api.common.NotFoundException;
import com.servicebooker.api.dto.BusinessResponse;
import com.servicebooker.api.dto.UpsertBusinessRequest;
import com.servicebooker.api.model.Business;
import com.servicebooker.api.model.common.Address;
import com.servicebooker.api.repository.BusinessRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class BusinessService {

    private final BusinessRepository businessRepository;

    @Transactional(readOnly = true)
    public BusinessResponse getBusiness(UUID id) {

        var business = businessRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> NotFoundException.of("Business", id.toString()));
        return getBusinessResponse(business);
    }

    public BusinessResponse updateBusiness( UUID id,UpsertBusinessRequest business) {

        var existingBusiness = businessRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> NotFoundException.of("Business", id.toString()));

        Address address = null;
        if (business.address() != null){
            var a = business.address();
            address = new Address(a.line1(), a.line2(), a.suburb(), a.state(), a.postcode(), a.country());
        }
        existingBusiness.updateProfile(business.name(), business.timezone(), business.phone(), business.email(), address , business.active());

        return getBusinessResponse(businessRepository.save(existingBusiness));
    }

    private BusinessResponse getBusinessResponse(Business business) {

        UpsertBusinessRequest.AddressDto address = null;
        if (business.getAddress() != null){
            var a = business.getAddress();
            address = new UpsertBusinessRequest.AddressDto(a.getLine1(), a.getLine2(), a.getSuburb(), a.getState(), a.getPostcode(), a.getCountry());
        }

        return new BusinessResponse(business.getId(), business.getName(), business.getTimezone(), business.getPhone(), business.getEmail(), address,
                business.isActive());

    }

}
