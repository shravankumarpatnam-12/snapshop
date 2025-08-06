package com.ecommerce.snapshop.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String roleId;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private AppRole roleName;

    public Role(AppRole roleName) {
        this.roleName = roleName;
    }

}
