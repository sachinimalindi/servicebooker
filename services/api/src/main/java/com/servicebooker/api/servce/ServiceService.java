package com.servicebooker.api.servce;

import com.servicebooker.api.common.NotFoundException;
import com.servicebooker.api.dto.CreateServiceRequest;
import com.servicebooker.api.dto.UpdateServiceRequest;
import com.servicebooker.api.dto.common.ServiceResponse;
import com.servicebooker.api.model.ServiceOffering;
import com.servicebooker.api.model.common.Money;
import com.servicebooker.api.repository.BusinessRepository;
import com.servicebooker.api.repository.ServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ServiceService {

    private final ServiceRepository serviceRepository;
    private final BusinessRepository businessRepository;

    @Transactional(readOnly = true)
    public List<ServiceResponse> listActiveServices(UUID businessId) {
        List<ServiceOffering> services = serviceRepository.findByBusinessIdAndActiveTrue(businessId);
        return services.stream().map(this::getServiceResponce).toList();
    }

    public ServiceResponse createService( CreateServiceRequest request) {
        businessRepository.findByIdAndActiveTrue(request.businessId())
                .orElseThrow(() -> NotFoundException.of("Business", request.businessId().toString()));
        ServiceOffering service = ServiceOffering.createNew(request.businessId(), request.name(), request.description(), request.durationMinutes(), new Money(request.priceAmount(), request.priceCurrency() ), request.depositType(), request.depositValue());

        return getServiceResponce(serviceRepository.save(service));
    }

    public ServiceResponse updateService(UUID id, UpdateServiceRequest request) {
        var existingService = serviceRepository.findById(id)
                .orElseThrow(() -> NotFoundException.of("Service", id.toString()));
        existingService.updateService(request.name(), request.description(), request.durationMinutes(), request.priceAmount(), request.priceCurrency(), request.depositType(), request.depositValue(), request.active());
        return getServiceResponce(serviceRepository.save(existingService));

    }

    private ServiceResponse getServiceResponce(ServiceOffering service) {
        return new ServiceResponse(service.getId(), service.getBusinessId(), service.getName(), service.getDescription(), service.getDurationMinutes(), service.getPrice().getAmount(), service.getPrice().getCurrency(), service.getDepositPolicy().getType().name(), service.getDepositPolicy().getValue(), service.isActive());
    }
}
