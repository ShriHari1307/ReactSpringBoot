package com.example.springReactBackEnd.Repository;

import com.example.springReactBackEnd.Entity.Agent;
import com.example.springReactBackEnd.Entity.Provider;
import com.example.springReactBackEnd.Entity.ProviderAgentLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProviderAgentRepository extends JpaRepository<ProviderAgentLink,Long> {
    List<ProviderAgentLink> findByProvider_ProviderId(Long providerId);
    void deleteByProvider_ProviderId(Long providerId);
    List<ProviderAgentLink> findByAgent_AgentId(Long agentId);
    void deleteByAgent_AgentId(Long agentId);
    void deleteByAgentAndProvider(Agent existingAgent, Provider currentProvider);
}
