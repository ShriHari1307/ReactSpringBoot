package com.example.springReactBackEnd.Repository;

import com.example.springReactBackEnd.Entity.AgentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AgentStatusRepository extends JpaRepository<AgentStatus,Integer> {
}
