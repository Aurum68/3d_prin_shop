package org.practice._3d_prin_shop.security;

import org.practice._3d_prin_shop.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class UserPrincipal implements UserDetails {

    private final User user;

    public UserPrincipal(User user) {
        this.user = user;
    }

    public Long getId() {return user.getId();}

    public String getEmail() {return user.getEmail();}

    public String getRole() {return user.getRole();}

    public String getFirstName() {return user.getFirstName();}

    public String getLastName() {return user.getLastName();}

    public boolean isBlocked() {return user.isBlocked();}

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(user.getRole()));
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }
}
