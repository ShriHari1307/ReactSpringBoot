package com.example.springReactBackEnd.Repository;

import com.example.springReactBackEnd.Entity.ProviderType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProviderTypeRepository extends JpaRepository<ProviderType, Integer> {
    ProviderType findByType(String providerType);
}

