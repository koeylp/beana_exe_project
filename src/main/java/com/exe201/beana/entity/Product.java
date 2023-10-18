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
@Builder
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "price", nullable = false)
    private double price;

    @Lob
    @Column(name = "description", columnDefinition = "MEDIUMTEXT")
    private String description;

    @Lob
    @Column(name = "mainFunction", columnDefinition = "MEDIUMTEXT")
    private String mainFunction;

    @Column(name = "ingredients", columnDefinition = "MEDIUMTEXT")
    @Lob
    private String ingredients;

    @Column(name = "specification", columnDefinition = "MEDIUMTEXT")
    @Lob
    private String specification;

    @Column(name = "certification", columnDefinition = "MEDIUMTEXT")
    @Lob
    private String certification;

    @Column(name = "soldQuantity")
    private int soldQuantity;

    @Column(name = "rate")
    private float rate;

    @Lob
    @Column(name = "howToUse", columnDefinition = "MEDIUMTEXT")
    private String howToUse;

    @Column(name = "status", nullable = false)
    private byte status;

    @Column(name = "timeCreated", nullable = false)
    @CreationTimestamp
    private Date timeCreated;

    @Column(name = "updated_datetime", nullable = false)
    @UpdateTimestamp
    private Date updatedDatetime;

    @ManyToOne
    @JoinColumn(name = "childCategoryId")
    private ChildCategory childCategory;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "product")
    @JsonIgnore
    private List<ProductSkin> productSkins;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "product")
    @JsonIgnore
    private List<Comment> comments;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "product")
    @JsonIgnore
    private List<OrderDetails> orderDetailsList;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "product")
    @JsonIgnore
    private List<ProductImage> productImageList;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "reputationId")
    private Reputation reputation;

}
