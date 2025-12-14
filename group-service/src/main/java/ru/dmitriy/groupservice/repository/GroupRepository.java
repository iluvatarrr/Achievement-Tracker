package ru.dmitriy.groupservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.dmitriy.commondomain.domain.goal.Goal;
import ru.dmitriy.commondomain.domain.group.Group;

import java.util.List;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long>, JpaSpecificationExecutor<Group> {
    @Query("""
            SELECT DISTINCT g
            FROM Group g
            LEFT JOIN g.members gm
            WHERE g.isPublic = true
               OR gm.user.id = :userId
            """)
    List<Group> findAllPublicGroupOrMember(@Param("userId") Long id);
}
