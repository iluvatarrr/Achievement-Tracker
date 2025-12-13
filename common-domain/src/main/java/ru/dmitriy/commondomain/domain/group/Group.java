package ru.dmitriy.commondomain.domain.group;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import ru.dmitriy.commondomain.domain.goal.Goal;
import ru.dmitriy.commondomain.domain.user.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "groups")
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private String organisation;
    private Boolean isPublic;
    @Enumerated(EnumType.STRING)
    private GroupStatus groupStatus;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User createdBy;
    @OneToMany(mappedBy = "group",fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
    private Set<GroupMember> members = new HashSet<>();
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;
    @ManyToMany
    @JoinTable(
            name = "groups_goals",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "goal_id")
    )
    private List<Goal> goals = new ArrayList<>();

    public Group(Long id, String title, String description, String organisation, Boolean isPublic,
                 GroupStatus groupStatus, User createdBy, Set<GroupMember> members,
                 LocalDateTime createdAt, List<Goal> goals) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.organisation = organisation;
        this.isPublic = isPublic;
        this.groupStatus = groupStatus;
        this.createdBy = createdBy;
        this.members = members;
        this.createdAt = createdAt;
        this.goals = goals;
    }

    public Group() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOrganisation() {
        return organisation;
    }

    public void setOrganisation(String organisation) {
        this.organisation = organisation;
    }

    public Boolean getPublic() {
        return isPublic;
    }

    public void setPublic(Boolean aPublic) {
        isPublic = aPublic;
    }

    public GroupStatus getGroupStatus() {
        return groupStatus;
    }

    public void setGroupStatus(GroupStatus groupStatus) {
        this.groupStatus = groupStatus;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public Set<GroupMember> getMembers() {
        return members;
    }

    public void setMembers(Set<GroupMember> members) {
        this.members = members;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<Goal> getGoals() {
        return goals;
    }

    public void setGoals(List<Goal> goals) {
        this.goals = goals;
    }

    public void addMember(User user, GroupRole role) {
        GroupMember member = new GroupMember();
        member.setGroup(this);
        member.setUser(user);
        member.setGroupRole(role);
        this.members.add(member);
    }

    public void removeMember(User user) {
        this.members.removeIf(member -> member.getUser().equals(user));
    }
}
