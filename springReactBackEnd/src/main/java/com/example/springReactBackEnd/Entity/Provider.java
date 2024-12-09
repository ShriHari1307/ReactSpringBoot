package com.example.springReactBackEnd.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
@Entity
@NamedQuery(name = "findAllByOrderByCreatedAtDesc", query = "SELECT p FROM Provider p ORDER BY p.createdAt DESC")
@NamedQuery(name = "joinNamedQuery", query = "SELECT p FROM Provider p JOIN p.agents a")
@Table(name = "providers")
public class Provider {

//    @Id
//    @Column(name = "provider_id")
//    @NotNull
//    private String providerId;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "provider_id")
    private Long providerId;

    @Column(name = "provider_name", nullable = false)
    private String providerName;

    @ManyToOne
    @JoinColumn(name = "provider_type", referencedColumnName = "type_id", nullable = false)
    private ProviderType providerType;

    @Column(name = "contact_number", nullable = false)
    private String contactNumber;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "street", nullable = false)
    private String street;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "city", referencedColumnName = "cityID")
    private City city;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "state", referencedColumnName = "stateID")
    private State state;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private Date createdAt;

    @Column(nullable = false)
    @UpdateTimestamp
    private Date updatedAt;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "agent_provider",
            joinColumns = @JoinColumn(name = "provider_id"),
            inverseJoinColumns = @JoinColumn(name = "agent_id")
    )
    private List<Agent> agents;
}