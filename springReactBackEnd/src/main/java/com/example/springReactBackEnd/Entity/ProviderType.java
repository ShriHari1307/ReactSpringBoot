package com.example.springReactBackEnd.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "provider_type")  // Changed to snake_case
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProviderType {
    @Id
    @Column(name = "type_id")
    private Integer typeId;

    @Column(name = "type")
    private String type;
}