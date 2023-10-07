package com.exe201.beana.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "product'sImage")
public class ProductImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(name = "url", nullable = false)
    private String url;

    @Column(name = "status", nullable = false)
    private byte status;

    @ManyToOne
    @JoinColumn(name = "productId")
    @JsonIgnore
    private Product product;
}
