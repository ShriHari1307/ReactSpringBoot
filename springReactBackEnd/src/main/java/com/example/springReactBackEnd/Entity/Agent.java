package com.example.springReactBackEnd.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Component
@Scope("prototype")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "agents")
public class Agent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "agentId")
    private Long agentId;

    @Column(name = "firstName")
    private String firstName;

    @Column(name = "lastName")
    private String lastName;

    @Column(name = "contact")
    private String contact;

    @Column(name = "licenseNumber")
    private String licenseNumber;

    @Column(name = "dateOfJoining")
    private Date dateOfJoining;

    @ManyToMany(mappedBy = "agents")
    private List<Provider> providers;

    @ManyToOne
    @JoinColumn(name = "cityId", referencedColumnName = "cityId")
    private City city;
    @Column(name = "email", unique = true)
    private String email;
    @Column(name = "street")
    private String street;

    @ManyToOne
    @JoinColumn(name = "stateId", referencedColumnName = "stateId")
    private State state;


    @ManyToOne
    @JoinColumn(name = "status", referencedColumnName = "status_id")
    private AgentStatus status;

}

