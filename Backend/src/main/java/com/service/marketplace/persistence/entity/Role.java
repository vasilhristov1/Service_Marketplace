package com.service.marketplace.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "roles")
public class Role extends BaseEntity implements GrantedAuthority {

    @Column(name = "name")
    private String name;

    @Override
    public String getAuthority() {
        return this.name;
    }
}
