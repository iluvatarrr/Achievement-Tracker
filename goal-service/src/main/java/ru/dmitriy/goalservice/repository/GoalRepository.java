package ru.dmitriy.goalservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.dmitriy.commondomain.domain.goal.Goal;
import java.util.List;
import java.util.Optional;

@Repository
public interface GoalRepository extends JpaRepository<Goal, Long>, JpaSpecificationExecutor<Goal> {
    @Query("SELECT DISTINCT g FROM Goal g LEFT JOIN FETCH g.subGoalList WHERE g.id = :id")
    Optional<Goal> findByIdWithSubGoals(Long id);

    List<Goal> findByGroupId(Long groupId);
    List<Goal> findAllByUserId(Long userId);
}