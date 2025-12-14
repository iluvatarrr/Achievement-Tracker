package ru.dmitriy.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.dmitriy.commondomain.domain.group.Group;
import ru.dmitriy.commondomain.domain.group.GroupRole;
import ru.dmitriy.commondomain.domain.user.User;
import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    @Query("""
        SELECT COUNT(g) > 0
        FROM Goal g
        WHERE g.id = :goalId
          AND g.user.id = :userId""")
    boolean userHasGoal(@Param("userId") Long userId,
                        @Param("goalId") Long goalId);

    @Query("""
        SELECT COUNT(sg) > 0
        FROM SubGoal sg
        WHERE sg.id = :subGoalId
          AND sg.goal.user.id = :userId""")
    boolean userHasSubGoal(@Param("userId") Long userId,
                        @Param("subGoalId") Long subGoalId);

    @Query("""
        SELECT COUNT(m) > 0
        FROM Group g
             JOIN g.members m
        WHERE g.id = :groupId
          AND m.user.id = :userId
          AND m.groupRole IN :groupRoles
        """)
    boolean checkAccessByGroupRoles(@Param("userId") Long userId,
                                    @Param("groupId") Long groupId,
                                    @Param("groupRoles") Set<GroupRole> groupRoles);

}
