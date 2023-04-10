package com.eventmaster.backend.UserManagement;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findByEmail(String email);
}
