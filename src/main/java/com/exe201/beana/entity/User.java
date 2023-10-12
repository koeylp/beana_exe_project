package com.exe201.beana.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Column(name = "name", nullable = false)
    private String name;

    @Pattern(regexp = "(^$|[0-9]{10})")
    private String phone;

    @Column(name = "gender", nullable = false)
    private int gender;

    @Column(name = "dob", nullable = false)
    private Date dob;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    @Lob
    @Column(name = "avatar")
    private String avatar;

    @Column(name = "timeCreated", nullable = false)
    @CreationTimestamp
    private Date timeCreated;

    @Column(name = "updated_datetime")
    @UpdateTimestamp
    private Date updatedDatetime;

    @Column(name = "status", nullable = false)
    private byte status;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    @JsonIgnore
    private List<Address> addresses;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    @JsonIgnore
    private List<Comment> comments;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    @JsonIgnore
    private List<Order> orders;



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
