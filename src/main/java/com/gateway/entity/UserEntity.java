package com.gateway.entity;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "tbl_user")
@Data
public class UserEntity {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name="user_name",unique = true,nullable = false)
    private String username;

    @Column(name="password",nullable = false)
    private String password;
}
