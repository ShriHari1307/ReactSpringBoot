package com.example.springReactBackEnd.Service;

//import com.example.springReactBackEnd.BO.ProviderBo;
import com.example.springReactBackEnd.BO.ProviderBo;
import com.example.springReactBackEnd.DTO.ProviderDTO;
import com.example.springReactBackEnd.Entity.Provider;
import com.example.springReactBackEnd.Exception.ProviderManagementException;
import com.example.springReactBackEnd.Exception.ProviderNotFoundException;
import com.example.springReactBackEnd.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProviderService {

    @Autowired
    private ProviderRepository providerRepository;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private StateRepository stateRepository;

    @Autowired
    private ProviderTypeRepository providerTypeRepository;

    @Autowired
    private AgentRepository agentRepository;

    @Autowired
    ProviderBo providerBo;

//    public ProviderDTO createProvider(ProviderDTO providerDTO) throws ProviderManagementException {
//        Provider provider = ProviderDTO.toProviderEntity(providerDTO, cityRepository, stateRepository, providerTypeRepository, agentRepository);
//        providerRepository.save(provider);
//        return ProviderDTO.toProviderDTO(provider);
//    }
//
//    public ProviderDTO getProvider(String providerId) {
//        Provider provider = providerRepository.findByProviderId(providerId);
//        return ProviderDTO.toProviderDTO(provider);
//    }


    public ProviderDTO insert(ProviderDTO providerDTO) throws ProviderManagementException {
        try {
            return providerBo.insert(providerDTO);
        } catch (ProviderManagementException e) {
            throw new ProviderManagementException("Error while inserting provider: " + e.getMessage(), e);
        }
    }

    public ProviderDTO findProvider(Long providerId) throws ProviderManagementException, ProviderNotFoundException {
        Provider provider = providerBo.findProvider(providerId);
        if (provider == null) {
            throw new ProviderNotFoundException("Provider with id: " + providerId + " not found");
        }
        return ProviderDTO.toProviderDTO(provider);
    }

    public List<ProviderDTO> findAllProviders() throws ProviderManagementException{
        List<Provider> allProviders = providerBo.findAllProviders();
        return allProviders.stream()
               .map(ProviderDTO::toProviderDTO)
               .toList();
    }

    public List<Provider> deleteProvider(Long providerId) throws ProviderNotFoundException {
        return providerBo.deleteProvider(providerId);
    }

    public void removeProviderByName(String providerName) {
        providerBo.deleteProviderByName(providerName);
    }

    public ProviderDTO updateProvider(Long providerId,ProviderDTO providerDTO) throws ProviderNotFoundException, ProviderManagementException {
        return providerBo.updateProvider(providerId,providerDTO);
    }

    public List<ProviderProjectionInterface> findBySelectedColumn() throws ProviderManagementException {
        return providerBo.findBySelectedColumn();
    }

    public Long countTotalProviders() {
        return providerBo.countTotalProviders();
    }

    public List<ProviderDTO> findProvidersWithMinimumAgents() {
        return providerBo.findProvidersWithMinimumAgents().stream()
                .map(ProviderDTO::toProviderDTO).toList();
    }

    public List<ProviderCountByStateProjectionInterface> countProvidersByState() {
        return providerBo.countProvidersByState();
    }

    public List<ProviderIdProjectionInterface> findProviderNamesAndEmailsOrderedByName() {
        return providerBo.findProviderNamesAndEmailsOrdered();
    }

    public List<ProviderDTO> findProvidersInnerJoinAgents() {
        return providerBo.findProvidersInnerJoinAgents().stream()
                .map(ProviderDTO::toProviderDTO)
                .toList();
    }

    public List<ProviderDTO> findAllProvidersLeftJoinAgents() {
        return providerBo.findAllProvidersLeftJoinAgents().stream()
                .map(ProviderDTO::toProviderDTO).toList();
    }

    public List<ProviderDTO> findAllProvidersRightJoinAgents() {
        return providerBo.findAllProvidersRightJoinAgents().stream()
                .map(ProviderDTO::toProviderDTO).toList();
    }

    public List<ProviderAgentProjectionInterface> findAllProvidersCrossJoinAgents() {
        return providerBo.findAllProvidersCrossJoinAgents();
    }

    public List<ProviderDTO> findAllByOrderByCreatedAtDesc() {
        return providerBo.findAllByOrderByCreatedAtDesc().stream()
                .map(ProviderDTO::toProviderDTO).toList();
    }
    public List<ProviderDTO> searchProvidersByName(String name) {
        List<Provider> providers = providerRepository.findByProviderNameContainingIgnoreCase(name);
        return providers.stream()
                .map(ProviderDTO::toProviderDTO)
                .toList();
    }



    public List<ProviderDTO> findAllWithAgents() {
        return providerBo.findAllWithAgents().stream()
                .map(ProviderDTO::toProviderDTO).toList();
    }
}
