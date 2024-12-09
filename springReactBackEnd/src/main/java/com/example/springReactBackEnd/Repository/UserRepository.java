package com.example.springReactBackEnd.Repository;

import com.example.springReactBackEnd.Entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users,Long> {
    Optional<Users> findByUserName(String username);

    Optional<Users> findByUserNameAndPassword(String username, String password);
}
