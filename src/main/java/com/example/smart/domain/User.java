package com.example.smart.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "account")
@Getter
@Setter
public class User {
    @Id
    private String id;
    private String password;
    private String serialNumber;
    private String salt;
}
