package com.exe201.beana.repository;

import com.exe201.beana.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, Long> {
    Optional<Address> findAddressByStatusAndId(byte status, Long id);
}
