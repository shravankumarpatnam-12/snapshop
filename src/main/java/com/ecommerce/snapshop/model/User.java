package com.ecommerce.snapshop.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="users", uniqueConstraints = {
        @UniqueConstraint(columnNames = "username"),
        @UniqueConstraint(columnNames = "email")
})
public class User {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long userId;

    @NotBlank
    @Size(max=20)
    @Column(name="username")
    private String username;

    @NotBlank
    @Size(min=5)
    @Column(name="password")
    private String password;

    @NotBlank
    @Size(max=12)
    @Email
    @Column(name="email")
    private String email;

    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }
    @Getter
    @Setter
    @ManyToMany(cascade={CascadeType.PERSIST, CascadeType.MERGE},
                fetch=FetchType.EAGER)
    @JoinTable(name="user_role",
            joinColumns  = @JoinColumn(name="user_id"),
            inverseJoinColumns = @JoinColumn(name="role_id")
    )
    private Set<Role> roles =new HashSet<>();
}
