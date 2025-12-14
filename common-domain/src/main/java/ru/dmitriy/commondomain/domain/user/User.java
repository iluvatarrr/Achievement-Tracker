package ru.dmitriy.commondomain.domain.user;

import jakarta.persistence.*;
import ru.dmitriy.commondomain.domain.goal.Goal;
import ru.dmitriy.commondomain.domain.group.Group;

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
    @Column(unique = true, nullable = false)
    private String username;
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
    @OneToMany(mappedBy = "createdBy", fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<Group> groups = new HashSet<>();

    public User(UserStatus userStatus, UserProfile profile, Set<Role> roles, LocalDateTime createdAt, String password, String username, Long id, Set<Goal> goals, Set<Group> groups) {
        this.userStatus = userStatus;
        this.profile = profile;
        this.roles = roles;
        this.createdAt = createdAt;
        this.password = password;
        this.username = username;
        this.id = id;
        this.goals = goals;
        this.groups = groups;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String email) {
        this.username = email;
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

    public void addRole(Role role) {
        roles.add(role);
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
        return Objects.equals(id, user.id) && Objects.equals(username, user.username) && Objects.equals(password, user.password) && Objects.equals(createdAt, user.createdAt) && Objects.equals(roles, user.roles) && Objects.equals(profile, user.profile) && userStatus == user.userStatus && Objects.equals(goals, user.goals);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password, createdAt, roles, profile, userStatus, goals);
    }

    public Set<Group> getGroups() {
        return groups;
    }

    public void setGroups(Set<Group> groups) {
        this.groups = groups;
    }
}