package com.lazychess.chessgame.applicationuser;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import com.lazychess.chessgame.models.Role;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Entity
@Table(name = "chess_game_user")
@Getter
@Setter
public class ApplicationUser {

    @Id
    @Column(name = "cgb_app_user_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    @Generated(GenerationTime.INSERT)
    private String id;

    @Column(unique = true)
    @NonNull
    private String username;

    @NonNull
    private String password;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "cgb_app_user_roles", referencedColumnName = "cgb_app_user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private List<Role> roles = new ArrayList<>();

    private Date lastLogin;

    public ApplicationUser() {
    }

    public ApplicationUser(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public ApplicationUser(String id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public ApplicationUser(@NonNull String username, @NonNull String password, List<Role> roles) {
        this.username = username;
        this.password = password;
        this.roles = roles;
    }
}
