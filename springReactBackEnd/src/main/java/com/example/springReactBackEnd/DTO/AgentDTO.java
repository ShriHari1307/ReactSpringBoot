package com.example.springReactBackEnd.DTO;

import com.example.springReactBackEnd.Entity.Agent;
import com.example.springReactBackEnd.Entity.Provider;
import com.example.springReactBackEnd.Entity.ProviderAgentLink;
import com.example.springReactBackEnd.Exception.ProviderManagementException;
import com.example.springReactBackEnd.Repository.*;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class AgentDTO {
    private Long agentId;
    private String firstName;
    private String lastName;
    private String contact;
    private String licenseNumber;
    private Date dateOfJoining;
    private String street;
    private String email;
    private Integer cityId;
    private Integer stateId;
    private Integer status;
    private List<Long> providerIds;

    public static AgentDTO toAgentDTO(Agent agent) {
        return AgentDTO.builder()
                .agentId(agent.getAgentId())
                .firstName(agent.getFirstName())
                .lastName(agent.getLastName())
                .contact(agent.getContact())
                .licenseNumber(agent.getLicenseNumber())
                .dateOfJoining(agent.getDateOfJoining())
                .street(agent.getStreet())
                .email(agent.getEmail())
                .cityId(agent.getCity() != null ? agent.getCity().getCityId() : null)
                .stateId(agent.getState() != null ? agent.getState().getStateId() : null)
                .status(agent.getStatus() != null ? agent.getStatus().getStatusId() : null)
                .providerIds(agent.getProviders() != null ?
                        agent.getProviders().stream()
                                .map(Provider::getProviderId)
                                .collect(Collectors.toList())
                        : new ArrayList<>())
                .build();
    }


    public static Agent toAgentEntity(AgentDTO agentDTO, CityRepository cityRepository, StateRepository stateRepository, ProviderRepository providerRepository,AgentStatusRepository agentStatusRepository) throws ProviderManagementException {
        List<Provider> providers = agentDTO.getProviderIds().stream().map(providerRepository::findByProviderId).collect(Collectors.toList());
        return Agent.builder()
                .agentId(agentDTO.getAgentId())
                .firstName(agentDTO.getFirstName())
                .lastName(agentDTO.getLastName())
                .contact(agentDTO.getContact())
                .licenseNumber(agentDTO.getLicenseNumber())
                .dateOfJoining(agentDTO.getDateOfJoining())
                .street(agentDTO.getStreet())
                .email(agentDTO.getEmail())
                .city(cityRepository.findById(agentDTO.getCityId()).orElseThrow(() -> new ProviderManagementException("City not found")))
                .state(stateRepository.findById(agentDTO.getStateId()).orElseThrow(() -> new ProviderManagementException("State not found")))
                .status(agentStatusRepository.findById(agentDTO.getStatus()).orElseThrow(() -> new ProviderManagementException("Status not found")))
                .providers(providers)
                .build();
    }
}
