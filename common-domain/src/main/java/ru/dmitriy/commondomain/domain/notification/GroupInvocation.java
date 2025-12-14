package ru.dmitriy.commondomain.domain.notification;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import ru.dmitriy.commondomain.domain.group.Group;
import java.time.LocalDateTime;

@Entity
@Table(name = "group_invocations")
public class GroupInvocation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private Group group;
    private String username;
    private String invitedByName;
    @Enumerated(EnumType.STRING)
    private GroupInvitationStatus status;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime expiresAt;

    public GroupInvocation(Long id, Group group, String username, String invitedByName,
                           GroupInvitationStatus status, LocalDateTime createdAt, LocalDateTime expiresAt) {
        this.id = id;
        this.group = group;
        this.username = username;
        this.invitedByName = invitedByName;
        this.status = status;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
    }

    public GroupInvocation() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getInvitedByName() {
        return invitedByName;
    }

    public void setInvitedByName(String invitedByName) {
        this.invitedByName = invitedByName;
    }

    public GroupInvitationStatus getStatus() {
        return status;
    }

    public void setStatus(GroupInvitationStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }
}
