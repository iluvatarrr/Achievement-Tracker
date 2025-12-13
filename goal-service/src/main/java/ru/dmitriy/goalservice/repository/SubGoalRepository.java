package ru.dmitriy.goalservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.dmitriy.commondomain.domain.goal.SubGoal;

public interface SubGoalRepository extends JpaRepository<SubGoal, Long> {
}
