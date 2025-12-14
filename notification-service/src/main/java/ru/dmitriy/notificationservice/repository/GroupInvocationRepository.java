package ru.dmitriy.notificationservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.dmitriy.commondomain.domain.notification.GroupInvocation;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface GroupInvocationRepository extends JpaRepository<GroupInvocation, Long> {
    List<GroupInvocation> findAllByUsername(String username);

    @Query("""
    SELECT gi
    FROM GroupInvocation gi
    WHERE gi.expiresAt < :expired
    """)
    List<GroupInvocation> findAllExpired(@Param("expired") LocalDateTime expired);
}
