package com.example.springReactBackEnd.BO;

import com.example.springReactBackEnd.DTO.ProviderDTO;
import com.example.springReactBackEnd.Entity.Agent;
import com.example.springReactBackEnd.Entity.Provider;
import com.example.springReactBackEnd.Entity.ProviderAgentLink;
import com.example.springReactBackEnd.Exception.ProviderManagementException;
import com.example.springReactBackEnd.Exception.ProviderNotFoundException;
import com.example.springReactBackEnd.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.regex.Pattern;

@Component
public class ProviderBo{

//    private static final Logger log = Logger.getLogger(ProviderBO.class);

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private StateRepository stateRepository;

    @Autowired
    private ProviderRepository repo;

    @Autowired
    private ProviderTypeRepository providerTypeRepository;

    @Autowired
    private AgentRepository agentRepository;

    @Autowired
    private ProviderAgentRepository providerAgentRepository;

    @Transactional
    public ProviderDTO insert(ProviderDTO providerDTO) throws ProviderManagementException {
        Provider provider;
        try {
            // Convert DTO to Entity
            provider = ProviderDTO.toProviderEntity(providerDTO, cityRepository, stateRepository, providerTypeRepository, agentRepository);
        } catch (Exception e) {
            throw new ProviderManagementException("Error while converting ProviderDTO to Provider entity: " + e.getMessage(), e);
        }
        isValidInsert(provider);
        Provider existingProvider = repo.findByEmail(provider.getEmail());
        if (existingProvider != null) {
            throw new ProviderManagementException("Provider with email already exists");
        }
        Provider existingProviders = repo.findByContactNumber(provider.getContactNumber());
        if (existingProviders != null) {
            throw new ProviderManagementException("Provider with contact already exists");
        }
        Provider savedProvider = repo.save(provider);
        return ProviderDTO.toProviderDTO(savedProvider);
    }


    @Transactional
    public void deleteProviderByName(String providerName) {
        repo.deleteByProviderName(providerName);
    }

    @Transactional(readOnly = true)
    public Provider findProvider(long providerId) throws ProviderManagementException, ProviderNotFoundException {
        validateProviderId(providerId);
        Provider provider = repo.findById(providerId).orElse(null);
        if (provider == null) {
//            log.warn("Provider not found: " + providerId);
            throw new ProviderNotFoundException("Provider not found: " + providerId);
        }
//        log.info("Provider found: ");
        return provider;
    }


    @Transactional(readOnly = true)
    public List<Provider> findAllProviders() {
        return repo.findAll();
    }

    @Transactional
    public List<Provider> deleteProvider(Long providerId) throws ProviderNotFoundException {
        Provider provider = repo.findByProviderId(providerId);
        if (provider == null) {
            throw new ProviderNotFoundException("Provider with ID " + providerId + " not found");
        }

        for (Agent agent : provider.getAgents()) {
            agent.getProviders().remove(provider);
        }
        provider.getAgents().clear();
        repo.save(provider);
        providerAgentRepository.deleteByProvider_ProviderId(providerId);
        repo.delete(provider);

        return findAllProviders();
    }

        @Transactional
        public ProviderDTO updateProvider(Long providerId, ProviderDTO providerDTO) throws ProviderManagementException, ProviderNotFoundException {
            Provider existingProvider = findProvider(providerId);
            if (existingProvider == null) {
                throw new ProviderNotFoundException("Provider with ID " + providerId + " not found");
            }
            existingProvider.setProviderName(providerDTO.getProviderName());
            existingProvider.setEmail(providerDTO.getEmail());
            existingProvider.setStreet(providerDTO.getStreet());
            existingProvider.setCity(cityRepository.findById(providerDTO.getCityId()).orElseThrow(() -> new ProviderManagementException("City not found")));
            existingProvider.setState(stateRepository.findById(providerDTO.getStateId()).orElseThrow(() -> new ProviderManagementException("State not found")));
            existingProvider.setProviderType(providerTypeRepository.findByType(providerDTO.getProviderType()));
            existingProvider.setContactNumber(providerDTO.getContactNumber());
            isValidInsert(existingProvider);
            Provider updatedProvider = repo.save(existingProvider);
    //        log.info("Provider updated successfully: " + updatedProvider.getProviderId());
            return ProviderDTO.toProviderDTO(updatedProvider);
    }

    private void isValidInsert(Provider provider) throws ProviderManagementException {
//        log.debug("Validating provider data for insertion: " + provider);

        if (provider.getProviderName() == null) {
//            log.error("Provider Name cannot be null");
            throw new ProviderManagementException("Provider Name cannot be null");
        }

        if (provider.getEmail() == null) {
//            log.error("Provider Email cannot be null");
            throw new ProviderManagementException("Provider email cannot be null");
        }

        if (provider.getProviderType() == null) {
//            log.error("Provider Type cannot be null");
            throw new ProviderManagementException("Provider type cannot be null");
        }

        if (provider.getStreet() == null) {
//            log.error("Provider Street cannot be null");
            throw new ProviderManagementException("Provider street cannot be null");
        }

        if (provider.getCity() == null) {
//            log.error("Provider City cannot be null");
            throw new ProviderManagementException("Provider city cannot be null");
        }

        if (provider.getState() == null) {
//            log.error("Provider State cannot be null");
            throw new ProviderManagementException("Provider state cannot be null");
        }

        if (!provider.getProviderName().matches("[A-Za-z ]+")) {
//            log.error("Provider Name should only contain letters, no digits allowed: " + provider.getProviderName());
            throw new ProviderManagementException("Provider Name should only contain letters, no digits allowed: " + provider.getProviderName());
        }



        if (provider.getContactNumber() == null || provider.getContactNumber().isEmpty()) {
//            log.error("Provider contact number cannot be null or empty");
            throw new ProviderManagementException("Provider contact number cannot be null or empty");
        }

        if (!provider.getContactNumber().matches("[0-9]+")) {
//            log.error("Contact number should only contain digits: " + provider.getContactNumber());
            throw new ProviderManagementException("Contact number should only contain digits: " + provider.getContactNumber());
        }

        if (provider.getContactNumber().length() != 10) {
//            log.error("Provider contact number must be 10 digits: " + provider.getContactNumber());
            throw new ProviderManagementException("Provider contact number must be 10 digits: " + provider.getContactNumber());
        }

        if (!isValidEmail(provider.getEmail())) {
//            log.error("Invalid email format: " + provider.getEmail());
            throw new ProviderManagementException("Invalid email format: " + provider.getEmail());
        }

//        log.debug("Provider data validated successfully for insertion.");
    }

    private boolean isValidEmail(String email) {
//        log.debug("Validating provider email: " + email);
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        Pattern pattern = Pattern.compile(emailRegex);
        return email != null && pattern.matcher(email).matches();
    }

    private void validateProviderId(Long providerId) throws ProviderManagementException {
//        log.debug("Validating provider ID: " + providerId);
        if (providerId == null) {
//            log.error("Provider ID cannot be empty");
            throw new ProviderManagementException("Provider ID cannot be empty");
        }

//        log.debug("Provider ID {} validated successfully. " + providerId);
    }

    public Long countTotalProviders() {
        return repo.countTotalProviders();
    }

    public List<Provider> findProvidersWithMinimumAgents() {
        return repo.findProvidersWithMinimumAgents();
    }

    public List<ProviderCountByStateProjectionInterface> countProvidersByState() {
        return repo.countProvidersByState();
    }

    public List<ProviderIdProjectionInterface> findProviderNamesAndEmailsOrdered() {
        return repo.findProviderNamesAndEmailsOrdered();
    }

    public List<Provider> findProvidersInnerJoinAgents() {
        return repo.findProvidersInnerJoinAgents();
    }

    public List<Provider> findAllProvidersLeftJoinAgents() {
        return repo.findAllProvidersLeftJoinAgents();
    }

    public List<Provider> findAllProvidersRightJoinAgents() {
        return repo.findAllProvidersRightJoinAgents();
    }

    public List<ProviderAgentProjectionInterface> findAllProvidersCrossJoinAgents() {
        return repo.findAllProvidersCrossJoinAgents();
    }

    public List<ProviderProjectionInterface> findBySelectedColumn() throws ProviderManagementException {
        return repo.findBySelectedColumn();
    }

    public List<Provider> findAllByOrderByCreatedAtDesc() {
        return repo.findAllByOrderByCreatedAtDesc();
    }

    public List<Provider> findAllWithAgents() {
        return repo.findAllWithAgents();
    }
}
