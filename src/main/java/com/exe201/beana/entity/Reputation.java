package com.exe201.beana.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Reputation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "timeCreated", nullable = false)
    @CreationTimestamp
    private Date timeCreated;

    @Column(name = "updated_datetime")
    @UpdateTimestamp
    private Date updatedDatetime;

    @Column(name = "status", nullable = false)
    private byte status;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "reputation")
    private List<Product> productList;

}
