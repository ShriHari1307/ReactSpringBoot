package com.example.springReactBackEnd.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name = "agent_provider")
public class ProviderAgentLink {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "provider_id") // Match Hibernate's default naming convention
    private Provider provider;

    @ManyToOne
    @JoinColumn(name = "agent_id") // Match Hibernate's default naming convention
    private Agent agent;
}
