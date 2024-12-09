package com.example.springReactBackEnd.BO;

import com.example.springReactBackEnd.DTO.AgentDTO;
import com.example.springReactBackEnd.Entity.Agent;
import com.example.springReactBackEnd.Entity.Provider;
import com.example.springReactBackEnd.Entity.ProviderAgentLink;
import com.example.springReactBackEnd.Exception.AgentManagementException;
import com.example.springReactBackEnd.Exception.AgentNotFoundException;
import com.example.springReactBackEnd.Exception.ProviderManagementException;
import com.example.springReactBackEnd.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

@Component
public class AgentBo {
    @Autowired
    private AgentRepository agentRepository;

    @Autowired
    private ProviderRepository providerRepository;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private StateRepository stateRepository;

    @Autowired
    private AgentStatusRepository agentStatusRepository;
    @Autowired
    private ProviderAgentRepository providerAgentRepository;

    @Transactional
    public AgentDTO insert(AgentDTO agentDTO) throws AgentManagementException, ProviderManagementException {
        Agent agent = AgentDTO.toAgentEntity(agentDTO, cityRepository, stateRepository, providerRepository, agentStatusRepository);
        isValidInsert(agent);
//        log.info("Inserting agent: " + agentDTO);

        // Check if Agent Already Exists
        Agent existingAgent = agentRepository.findByEmail(agent.getEmail());
        if (existingAgent != null) {
            throw new AgentManagementException("Agent with email already exists");
        }
        if (agentRepository.findByAgentId(agent.getAgentId()) != null) {
            throw new AgentManagementException("Agent with ID: " + agent.getAgentId() + " already exists");
        }

        Agent existingAgents = agentRepository.findByContact(agent.getContact());
        if (existingAgents != null) {
            throw new AgentManagementException("Agent with Contact already exists");
        }
        if (agentRepository.findByAgentId(agent.getAgentId()) != null) {
            throw new AgentManagementException("Agent with ID: " + agent.getAgentId() + " already exists");
        }

        // Save the Agent
        agentRepository.save(agent);

        // Save Provider-Agent Links
        for (Provider provider : agent.getProviders()) {
            ProviderAgentLink providerAgentLink = new ProviderAgentLink();
            providerAgentLink.setAgent(agent);
            providerAgentLink.setProvider(provider);
            providerAgentRepository.save(providerAgentLink);
        }

//        log.info("Agent inserted successfully: " + agent.getAgentId());
        return AgentDTO.toAgentDTO(agent);
    }



    public Agent findAgents(Long agentsId) throws AgentManagementException, AgentNotFoundException {
//        log.info("Finding agent with ID: " + agentsId);
        validateAgentId(agentsId);
        Agent agent = agentRepository.findByAgentId(agentsId);
        if (agent == null) {
//            log.warn("Agent not found: " + agentsId);
            throw new AgentNotFoundException("Agent not found: " + agentsId);
        }
//        log.info("Agent found: " + agent);
        return agent;
    }

    @Transactional
    public AgentDTO updateAgent(Long agentId, AgentDTO agentDTO) throws AgentManagementException, AgentNotFoundException {
        Agent existingAgent = findAgents(agentId);
        if (existingAgent == null) {
            throw new AgentNotFoundException("Agent with ID " + agentId + " not found");
        }
        existingAgent.setFirstName(agentDTO.getFirstName());
        existingAgent.setLastName(agentDTO.getLastName());
        existingAgent.setEmail(agentDTO.getEmail());
        existingAgent.setStreet(agentDTO.getStreet());
        existingAgent.setCity(cityRepository.findById(agentDTO.getCityId())
                .orElseThrow(() -> new AgentManagementException("City not found")));
        existingAgent.setState(stateRepository.findById(agentDTO.getStateId())
                .orElseThrow(() -> new AgentManagementException("State not found")));
        existingAgent.setContact(agentDTO.getContact());
        existingAgent.setLicenseNumber(agentDTO.getLicenseNumber());
        existingAgent.setDateOfJoining(agentDTO.getDateOfJoining());
        existingAgent.setStatus(agentStatusRepository.findById(agentDTO.getStatus())
                .orElseThrow(() -> new AgentManagementException("Status not found")));
        if (agentDTO.getProviderIds() != null && !agentDTO.getProviderIds().isEmpty()) {
            List<Provider> newProviders = providerRepository.findAllById(agentDTO.getProviderIds());
            if (newProviders.size() != agentDTO.getProviderIds().size()) {
                throw new AgentManagementException("One or more providers not found");
            }
            List<Provider> currentProviders = existingAgent.getProviders();
            for (Provider currentProvider : currentProviders) {
                if (!newProviders.contains(currentProvider)) {
                    providerAgentRepository.deleteByAgentAndProvider(existingAgent, currentProvider);
                }
            }
            for (Provider newProvider : newProviders) {
                if (!currentProviders.contains(newProvider)) {
                    ProviderAgentLink link = new ProviderAgentLink();
                    link.setAgent(existingAgent);
                    link.setProvider(newProvider);
                    providerAgentRepository.save(link);
                }
            }
            existingAgent.setProviders(newProviders);
        }
        isValidInsert(existingAgent);
        Agent updatedAgent = agentRepository.save(existingAgent);
        return AgentDTO.toAgentDTO(updatedAgent);
    }

    @Transactional
    public void deleteAgentByName(String firstName,String lastName ) {
        agentRepository.deleteByFirstNameAndLastName(firstName,lastName);
    }



    @Transactional
    public List<Agent> deleteAgent(Long agentId) throws AgentNotFoundException, AgentManagementException {
        Agent agent = agentRepository.findByAgentId(agentId);
        if(agent == null){
            throw new AgentNotFoundException("Agent not found: " + agentId);
        }
        for(Provider provider : agent.getProviders()){
            provider.getAgents().remove(agent);
        }
        agent.getProviders().clear();
        agentRepository.save(agent);
        providerAgentRepository.deleteByAgent_AgentId(agentId);
        agentRepository.delete(agent);
        return findAllAgents();
    }




    public List<Agent> findAllAgents() {
//        log.info("Fetching all agents");
        List<Agent> agents = agentRepository.findAll();
//        log.info("Total agents found: " + agents.size());
        return agents;
    }

    public void isValidInsert(Agent agents) throws AgentManagementException {
//        log.debug("Validating agent data for insertion: " + agents);
        if (!isValidEmail(agents.getEmail())) {
//            log.error("Invalid email format: " + agents.getEmail());
            throw new AgentManagementException("Invalid email format: " + agents.getEmail());
        }

        if (agents.getProviders() == null) {
//            log.error("Agent cannot be null");
            throw new AgentManagementException("Agent cannot be null");
        }
        if (agents.getCity() == null) {
//            log.error("City cannot be null");
            throw new AgentManagementException("City cannot be null");
        }
        if (agents.getState() == null) {
//            log.error("State cannot be null");
            throw new AgentManagementException("State cannot be null");
        }
        if (agents.getStatus() == null) {
//            log.error("Agent status cannot be null");
            throw new AgentManagementException("Agent status cannot be null");
        }
        if (agents.getLicenseNumber() == null) {
//            log.error("License Number cannot be null");
            throw new AgentManagementException("License Number cannot be null");
        }
        if (agents.getDateOfJoining() == null) {
//            log.error("Date of Joining cannot be null");
            throw new AgentManagementException("Date of Joining cannot be null");
        }
        if (agents.getFirstName() == null) {
//            log.error("First Name cannot be null");
            throw new AgentManagementException("First Name cannot be null");
        }
        if (agents.getLastName() == null) {
//            log.error("Last Name cannot be null");
            throw new AgentManagementException("Last Name cannot be null");
        }
        if (agents.getEmail() == null) {
//            log.error("Email cannot be null");
            throw new AgentManagementException("Email cannot be null");
        }
        if (!agents.getFirstName().matches("[A-Za-z ]+")) {
//            log.error("Agent Name should only contain letters, no digits allowed: " + agents.getFirstName());
            throw new AgentManagementException("Agent Name should only contain letters, no digits allowed: " + agents.getFirstName());
        }
        if (!agents.getLastName().matches("[A-Za-z ]+")) {
//            log.error("Agent Name should only contain letters, no digits allowed: " + agents.getLastName());
            throw new AgentManagementException("Agent Name should only contain letters, no digits allowed: " + agents.getLastName());
        }

        if (agents.getContact() == null || agents.getContact().isEmpty()) {
//            log.error("Agent contact number cannot be null or empty");
            throw new AgentManagementException("Agent contact number cannot be null or empty");
        }
        if (!agents.getContact().matches("[0-9]+")) {
//            log.error("Contact number should only contain digits: " + agents.getContact());
            throw new AgentManagementException("Contact number should only contain digits: " + agents.getContact());
        }
        if (agents.getContact().length() != 10) {
//            log.error("Agent contact number must be 10 digits: " + agents.getContact());
            throw new AgentManagementException("Agent contact number must be 10 digits: " + agents.getContact());
        }
        if (agents.getDateOfJoining().after(new Date())) {
//            log.error("Date of Joining cannot be in future");
            throw new AgentManagementException("Date of Joining cannot be in future");
        }
//        log.debug("Agent data validated successfully for insertion.");
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        Pattern pattern = Pattern.compile(emailRegex);
        return email != null && pattern.matcher(email).matches();
    }

    @Transactional
    public List<Agent> deleteAgents(Long agentId) throws AgentNotFoundException, AgentManagementException {
        if (findAgents(agentId) == null) {
            throw new AgentNotFoundException("Provider with ID " + agentId + " not found");
        }
//        log.info("Deleting provider with ID: " + agentId);
        agentRepository.deleteById(agentId);
//        log.info("Provider deleted successfully: " + agentId);
        return findAllAgents();
    }

    private void validateAgentId(Long agentId) throws AgentManagementException {
        if (agentId == null) {
//            log.error("Agent ID cannot be empty");
            throw new AgentManagementException("Agent ID cannot be empty");
        }

//        log.debug("Agent ID {} validated successfully." + agentId);
    }
}
