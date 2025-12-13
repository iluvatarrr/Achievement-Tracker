package ru.dmitriy.groupservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.dmitriy.commondomain.domain.group.Group;

import java.util.List;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
    @Query("SELECT g FROM Group g JOIN GroupMember gm ON g.id = gm.group.id JOIN User u ON gm.user = u")
    List<Group> findAllPublicGroupOrMember(Long id);
}
