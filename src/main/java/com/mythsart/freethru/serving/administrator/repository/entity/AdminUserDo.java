package com.mythsart.freethru.serving.administrator.repository.entity;

import com.mythsart.freethru.serving.administrator.repository.permission.AdminPermission;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;

@Data
@EntityListeners(AuditingEntityListener.class)
@Entity
@GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
@Table(name = "admin_user")
public class AdminUserDo implements UserDetails {

    @Id
    @GeneratedValue(generator = "uuid2")
    @Column(name = "user_uuid", length = 36, nullable = false)
    private String userUuid;

    @Column(name = "permission", nullable = false)
    private long permission = AdminPermission.REGISTER;

    @Column(name = "user_name", length = 32, nullable = false, unique = true)
    private String userName;

    @Column(name = "user_password", length = 64, nullable = false)
    private String userPassWord;

    @Column(name = "user_active", nullable = false)
    private boolean userActive;

    @CreatedDate
    @Column(name = "user_register_time", nullable = false, columnDefinition = "bigint default 0")
    private long userRegisterTime;

    @Transient
    private Collection<? extends GrantedAuthority> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.userPassWord;
    }

    @Override
    public String getUsername() {
        return this.userName;
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
        return this.userActive;
    }

}
