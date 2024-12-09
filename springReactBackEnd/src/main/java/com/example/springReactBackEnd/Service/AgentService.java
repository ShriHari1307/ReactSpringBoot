package com.example.springReactBackEnd.Service;

//import com.example.springReactBackEnd.BO.AgentBo;
//import com.example.springReactBackEnd.BO.ProviderBo;
import com.example.springReactBackEnd.BO.AgentBo;
import com.example.springReactBackEnd.DTO.AgentDTO;
import com.example.springReactBackEnd.DTO.ProviderDTO;
import com.example.springReactBackEnd.Entity.Agent;
import com.example.springReactBackEnd.Entity.AgentStatus;
import com.example.springReactBackEnd.Entity.Provider;
import com.example.springReactBackEnd.Entity.ProviderAgentLink;
import com.example.springReactBackEnd.Exception.AgentManagementException;
import com.example.springReactBackEnd.Exception.AgentNotFoundException;
import com.example.springReactBackEnd.Exception.ProviderManagementException;
import com.example.springReactBackEnd.Exception.ProviderNotFoundException;
import com.example.springReactBackEnd.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgentService {

    @Autowired
    private AgentBo agentBo;

    @Autowired
    private AgentRepository agentRepository;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private StateRepository stateRepository;

    @Autowired
    private ProviderRepository providerRepository;

    @Autowired
    private AgentStatusRepository agentStatusRepository;

    @Autowired
    private ProviderAgentRepository providerAgentRepository;

    public AgentDTO createAgent(AgentDTO agentDTO) throws ProviderManagementException, AgentManagementException {
//        Agent agent = AgentDTO.toAgentEntity(agentDTO, cityRepository, stateRepository, providerRepository,agentStatusRepository);
//        agentRepository.save(agent);
//        for(Provider provider : agent.getProviders()){
//            ProviderAgentLink providerAgentLink = new ProviderAgentLink();
//            providerAgentLink.setAgent(agent);
//            providerAgentLink.setProvider(provider);
//            providerAgentRepository.save(providerAgentLink);
//        }
//        return AgentDTO.toAgentDTO(agent);

        return agentBo.insert(agentDTO);
    }

    public AgentDTO findAgent(Long agentId) throws AgentManagementException, AgentNotFoundException {
        Agent agent = agentBo.findAgents(agentId);
        if (agent == null) {
            throw new AgentNotFoundException("Agent with id: " + agentId + " not found");
        }
        return AgentDTO.toAgentDTO(agent);
    }

    public List<AgentDTO> findAllAgents() {
        List<Agent> allAgents = agentBo.findAllAgents();
        return allAgents.stream().map(AgentDTO::toAgentDTO).toList();
    }

    public List<Agent> deleteAgent(Long agentId) throws AgentNotFoundException, AgentManagementException {
        return agentBo.deleteAgent(agentId);
    }

    public AgentDTO updateAgent(Long agentId,AgentDTO agentDTO) throws AgentNotFoundException, AgentManagementException {
        return agentBo.updateAgent(agentId,agentDTO);
    }

    public void removeAgentByName(String firstName,String lastName) {
        agentBo.deleteAgentByName(firstName,lastName);
    }

    public List<AgentDTO> searchAgentsByName(String firstName) {
        List<Agent> agents = agentRepository.findByFirstNameContainingIgnoreCase(firstName);
        return agents.stream()
                .map(AgentDTO::toAgentDTO)
                .toList();
    }
}
