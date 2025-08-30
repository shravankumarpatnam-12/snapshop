package com.ecommerce.snapshop.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Set;


@Entity
@Data
@NoArgsConstructor
@Table(name="addresses")
public class Address {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long addressId;
    @NotBlank
    @Size(min = 2, message = "Building Name should be at least 2 characters")
    private String buildingName;
    @NotBlank
    @Size(min = 5, message = "Street Name be at least 5 characters")
    private String street;

    @NotBlank
    private String city;
    @NotBlank
    private String state;
    @NotBlank
    private String country;
    @NotBlank
    private Long pinCode;

    @ToString.Exclude
    @ManyToMany(mappedBy="addresses")
    private Set<User> users;

    public Address(String buildingName, String street, String city, String state, String country, Long pinCode) {
        this.buildingName = buildingName;
        this.street = street;
        this.city = city;
        this.state = state;
        this.country = country;
        this.pinCode = pinCode;
    }
}
