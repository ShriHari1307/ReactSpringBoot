package com.example.springReactBackEnd.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Data
@Entity
@Table(name = "agent_status")
public class AgentStatus {

    @Id
    @Column(name = "status_id", unique = true, nullable = false)
    private Integer statusId;

    @Column(name = "status")
    private String status;
}
