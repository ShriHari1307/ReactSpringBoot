package com.example.springReactBackEnd.DTO;

import com.example.springReactBackEnd.Entity.*;
import com.example.springReactBackEnd.Exception.ProviderManagementException;
import com.example.springReactBackEnd.Repository.AgentRepository;
import com.example.springReactBackEnd.Repository.CityRepository;
import com.example.springReactBackEnd.Repository.ProviderTypeRepository;
import com.example.springReactBackEnd.Repository.StateRepository;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class ProviderDTO {
    private Long providerId;
    private String providerName;
    private String contactNumber;
    private String email;
    private String providerType;
    private String street;
    private Integer cityId;
    private Integer stateId;
    private List<Long> agentIds;

    public static ProviderDTO toProviderDTO(Provider provider) {
        if (provider == null) {
            return null;
        }
        return ProviderDTO.builder()
                .providerId(provider.getProviderId())
                .providerName(provider.getProviderName())
                .contactNumber(provider.getContactNumber())
                .email(provider.getEmail())
                .providerType(provider.getProviderType().getType())
                .street(provider.getStreet())
                .cityId(provider.getCity().getCityId())
                .stateId(provider.getState().getStateId())
                .agentIds(provider.getAgents() != null ? provider.getAgents().stream()
                        .map(Agent::getAgentId)
                        .collect(Collectors.toList()) : null)
                .build();
    }

    public static Provider toProviderEntity(ProviderDTO providerDTO, CityRepository cityRepository, StateRepository stateRepository, ProviderTypeRepository providerTypeRepository, AgentRepository agentRepository) throws ProviderManagementException {
        return Provider.builder()
                .providerName(providerDTO.getProviderName())
                .contactNumber(providerDTO.getContactNumber())
                .email(providerDTO.getEmail())
                .street(providerDTO.getStreet())
                .providerType(providerTypeRepository.findByType(providerDTO.getProviderType()))
                .city(cityRepository.findById(providerDTO.getCityId()).orElseThrow(() -> new ProviderManagementException("City not found")))
                .state(stateRepository.findById(providerDTO.getStateId()).orElseThrow(() -> new ProviderManagementException("State not found")))
                .build();
    }
}


