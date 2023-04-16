package com.eventmaster.backend.repositories;

import com.eventmaster.backend.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * This interface is used to access the Users in the database.
 *
 * @author Fabian Eilber
 */
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmailAdress(String emailAdress);
    User findUserById(long id);
}
