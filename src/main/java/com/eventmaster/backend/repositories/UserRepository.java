package com.eventmaster.backend.repositories;

import com.eventmaster.backend.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmailAdress(String emailAdress);
}
