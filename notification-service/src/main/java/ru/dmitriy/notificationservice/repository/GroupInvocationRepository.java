package ru.dmitriy.notificationservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.dmitriy.commondomain.domain.group.Group;
import ru.dmitriy.commondomain.domain.notification.GroupInvocation;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface GroupInvocationRepository extends JpaRepository<GroupInvocation, Long> {
    List<GroupInvocation> findAllByUsername(String username);

    @Query("""
    SELECT g
    FROM GroupInvocation gi
    JOIN gi.group g ON g.id = gi.group.id
    WHERE gi.id = :id
    """)
    Group getByIdWithGroup(@Param("id") Long id);

    @Query("""
    SELECT gi
    FROM GroupInvocation gi
    WHERE gi.expiresAt < :expired
    """)
    List<GroupInvocation> findAllExpired(@Param("expired") LocalDateTime expired);
}
