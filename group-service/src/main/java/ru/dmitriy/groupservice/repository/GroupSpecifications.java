package ru.dmitriy.groupservice.repository;

import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;
import ru.dmitriy.commondomain.domain.group.Group;

public abstract class GroupSpecifications {

    public static Specification<Group> hasTitle(String title) {
        return (root, query, cb) ->
                title == null ? null : cb.like(cb.lower(root.get("title")), "%" + title.toLowerCase() + "%");
    }

    public static Specification<Group> hasDescription(String description) {
        return (root, query, cb) ->
                description == null ? null : cb.like(cb.lower(root.get("description")), "%" + description.toLowerCase() + "%");
    }

    public static Specification<Group> hasOwner(String owner) {
        return (root, query, cb) -> {
            if (owner == null) return null;
            var userJoin = root.join("createdBy", JoinType.LEFT);
            return cb.equal(cb.lower(userJoin.get("username")), owner.toLowerCase());
        };
    }

    public static Specification<Group> fetchGoals() {
        return (root, query, cb) -> {
            root.fetch("goals", JoinType.LEFT);
            return null;
        };
    }
}
