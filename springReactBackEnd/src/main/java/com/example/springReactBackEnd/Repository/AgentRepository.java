package com.example.springReactBackEnd.Repository;

import com.example.springReactBackEnd.Entity.Agent;
import com.example.springReactBackEnd.Entity.Provider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AgentRepository extends JpaRepository<Agent, Long> {
    Agent findByAgentId(Long agentId);
    List<Agent> findByFirstNameContainingIgnoreCase(String firstName);

    public Agent findByEmail(String email);
    public Agent findByContact(String contact);

    @Query("DELETE FROM Agent a WHERE a.firstName = :firstName AND a.lastName = :lastName")
    void deleteByFirstNameAndLastName(String firstName, String lastName);
}


