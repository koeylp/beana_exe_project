package com.exe201.beana.repository;

import com.exe201.beana.entity.Skin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SkinRepository extends JpaRepository<Skin, Long> {
    Optional<Skin> findSkinByStatusAndName(byte status, String name);

    List<Skin> findAllByStatus(byte status);

    Optional<Skin> findSkinByStatusAndId(byte status, Long id);

}
