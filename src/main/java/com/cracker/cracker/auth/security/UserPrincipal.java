package com.cracker.cracker.auth.security;

import com.cracker.cracker.user.entity.UserRole;
import com.cracker.cracker.user.entity.Users;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

public class UserPrincipal implements UserDetails {
    private Users user;

    private String email;

    public UserPrincipal(String email) {
        this.email = email;
    }

    public UserPrincipal(Users user) {
        this.user = user;
    }

    public Users getUser() {
        return user;
    }

    private static final String ROLE_PREFIX = "ROLE_";

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        UserRole userRole = user.getRole();
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(ROLE_PREFIX + userRole.toString());
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(authority);
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
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
