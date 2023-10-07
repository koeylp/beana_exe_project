package com.exe201.beana.repository;

import com.exe201.beana.entity.Reputation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReputationRepository extends JpaRepository<Reputation, Long> {

    List<Reputation> findAllByStatus(byte status);

    Optional<Reputation> findReputationByStatusAndName(byte status, String name);

    Optional<Reputation> findReputationByStatusAndId(byte status, Long id);
}
