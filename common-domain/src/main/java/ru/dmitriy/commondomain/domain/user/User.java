package ru.dmitriy.commondomain.domain.user;

import jakarta.persistence.*;
import ru.dmitriy.commondomain.domain.goal.Goal;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Table(name = "users")
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String password;
    private LocalDateTime createdAt;
    @Column(name = "role")
    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "user_roles")
    private Set<Role> roles;
    @OneToOne(mappedBy = "user",cascade = CascadeType.ALL)
    private UserProfile profile;
    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;
    @OneToMany(mappedBy = "user",
            cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<Goal> goals = new HashSet<>();
//    @ManyToMany(mappedBy = "users")
//    private Set<Group> groups = new HashSet<>();

    public User(UserStatus userStatus, UserProfile profile, Set<Role> roles, LocalDateTime createdAt, String password, String email, Long id, Set<Goal> goals) {
        this.userStatus = userStatus;
        this.profile = profile;
        this.roles = roles;
        this.createdAt = createdAt;
        this.password = password;
        this.email = email;
        this.id = id;
        this.goals = goals;
    }

    public User(Long id) {
        this.id = id;
    }

    public User() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public UserProfile getProfile() {
        return profile;
    }

    public void setProfile(UserProfile profile) {
        this.profile = profile;
    }

    public UserStatus getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(UserStatus userStatus) {
        this.userStatus = userStatus;
    }

    public Set<Goal> getGoals() {
        return goals;
    }

    public void setGoals(Set<Goal> goals) {
        this.goals = goals;
    }

    public void addGoal(Goal goal) {
        this.goals.add(goal);
    }

    public void removeGoal(Goal goal) {
        this.goals.remove(goal);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        User user = (User) object;
        return Objects.equals(id, user.id) && Objects.equals(email, user.email) && Objects.equals(password, user.password) && Objects.equals(createdAt, user.createdAt) && Objects.equals(roles, user.roles) && Objects.equals(profile, user.profile) && userStatus == user.userStatus && Objects.equals(goals, user.goals);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, password, createdAt, roles, profile, userStatus, goals);
    }
}