package com.example.springReactBackEnd.Repository;

import com.example.springReactBackEnd.Entity.Provider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProviderRepository extends JpaRepository<Provider, Long> {

    Provider findByProviderId(long providerId);
    Provider findByEmail(String email);
    Provider findByContactNumber(String contactNumber);


    // Select specific columns
    @Query("SELECT p.providerId AS providerId, p.providerName AS providerName, p.email AS email FROM Provider p")
    List<ProviderProjectionInterface> findBySelectedColumn();

    // Count total providers
    @Query("SELECT COUNT(p) FROM Provider p")
    Long countTotalProviders();

    // Providers with minimum agents
    @Query("SELECT p FROM Provider p WHERE SIZE(p.agents) = (SELECT MIN(SIZE(p2.agents)) FROM Provider p2)")
    List<Provider> findProvidersWithMinimumAgents();

    // Count providers by state
    @Query("SELECT p.state.stateName AS stateName, COUNT(p) AS providerCount FROM Provider p GROUP BY p.state.stateName")
    List<ProviderCountByStateProjectionInterface> countProvidersByState();

    // Provider names and emails ordered by name
    @Query("SELECT p.providerName AS providerName, p.email AS email FROM Provider p ORDER BY p.providerName ASC")
    List<ProviderIdProjectionInterface> findProviderNamesAndEmailsOrdered();

    // Providers with an inner join on agents
    @Query("SELECT p FROM Provider p JOIN p.agents a")
    List<Provider> findProvidersInnerJoinAgents();

    // Providers with a left join on agents
    @Query("SELECT p FROM Provider p LEFT JOIN p.agents a")
    List<Provider> findAllProvidersLeftJoinAgents();

    // Providers with a right join on agents
    @Query("SELECT p FROM Provider p RIGHT JOIN p.agents a")
    List<Provider> findAllProvidersRightJoinAgents();

    // Providers cross join with agents (non-joined query)
    @Query("SELECT p.providerName AS providerName, p.email AS providerEmail, a.firstName AS agentFirstName, a.lastName AS agentLastName, a.email AS agentEmail FROM Provider p, Agent a")
    List<ProviderAgentProjectionInterface> findAllProvidersCrossJoinAgents();

    // Custom query: Find all providers ordered by creation date
    @Query("SELECT p FROM Provider p ORDER BY p.createdAt DESC")
    List<Provider> findAllByOrderByCreatedAtDesc();

    // Custom join query for providers and agents
    @Query("SELECT p FROM Provider p JOIN FETCH p.agents a")
    List<Provider> findAllWithAgents();

    List<Provider> findByProviderNameContainingIgnoreCase(String name);

    void deleteByProviderName(String providerName);
}
