package ru.dmitriy.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.dmitriy.commondomain.domain.user.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
