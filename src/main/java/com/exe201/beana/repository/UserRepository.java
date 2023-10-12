package com.exe201.beana.repository;

import com.exe201.beana.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByStatusAndUsername(byte status, String username);

    Optional<User> findUserByStatusAndEmail(byte status, String email);

    Optional<User> findUserByStatusAndPhone(byte status, String phone);

    Optional<User> findUserByStatusAndId(byte status, Long id);

}
