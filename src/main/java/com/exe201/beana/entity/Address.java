package com.exe201.beana.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "Addresses")
@Builder
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fullName", nullable = false)
    private String fullName;

    @Column(name = "phone", nullable = false)
    private String phone;

    @Column(name = "province", nullable = false)
    private String province;

    @Column(name = "district", nullable = false)
    private String district;

    @Column(name = "ward", nullable = false)
    private String ward;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "timeCreated", nullable = false)
    @CreationTimestamp
    private Date timeCreated;

    @Column(name = "updated_datetime")
    @UpdateTimestamp
    private Date updatedDatetime;

    @Column(name = "status", nullable = false)
    private byte status;

    @ManyToOne
    @JoinColumn(name = "userId")
    @JsonIgnore
    private User user;

    @OneToMany(mappedBy = "address", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Order> orders;



}
