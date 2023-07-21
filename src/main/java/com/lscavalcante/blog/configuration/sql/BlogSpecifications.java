package com.lscavalcante.blog.configuration.sql;

import com.lscavalcante.blog.model.blog.Blog;
import com.lscavalcante.blog.model.user.User;
import io.micrometer.common.util.StringUtils;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;


public class BlogSpecifications {
    public static Specification<Blog> search(String searchTerm) {
        return (root, query, criteriaBuilder) -> {
            if (StringUtils.isEmpty(searchTerm)) {
                return null;
            }

            String searchStr = "%" + searchTerm.trim().toLowerCase() + "%";
            Predicate idPredicate = criteriaBuilder.like(root.get("id").as(String.class), searchStr);

            Expression<String> titleExpression = criteriaBuilder.function("lower", String.class, root.get("title"));
            Predicate titlePredicate = criteriaBuilder.like(titleExpression, searchStr);

            Expression<String> contentExpression = criteriaBuilder.function("lower", String.class, root.get("content"));
            Predicate contentPredicate = criteriaBuilder.like(contentExpression, searchStr);

            // Create a join between the Blog and User entities
            Join<Blog, User> userJoin = root.join("user", JoinType.LEFT);
            // Get the email field from the User entity
            Expression<String> emailExpression = criteriaBuilder.lower(userJoin.get("email"));
            Predicate emailPredicate = criteriaBuilder.like(emailExpression, searchStr.toLowerCase());

            return criteriaBuilder.or(
                    titlePredicate,
                    contentPredicate,
                    emailPredicate,
                    idPredicate
            );
        };
    }

    public static Specification<Blog> search(String searchTerm, Long userId) {
        return (root, query, criteriaBuilder) -> {
// Example function in SQL POSTGRESQL
//            SELECT *
//                    FROM blogs b
//            LEFT JOIN users u ON b.user_id = u.id
//            WHERE
//                    (LOWER(b.title) LIKE '%searchTerm%' OR
//            LOWER(b.content) LIKE '%searchTerm%' OR
//            LOWER(u.email) LIKE '%searchTerm%' OR
//            CAST(b.id AS TEXT) LIKE '%searchTerm%')
//            AND u.id = userId;


            Predicate userCreatorPredicate = null;

            // Create a join between the Blog and User entities
            Join<Blog, User> userJoin = root.join("user", JoinType.LEFT);
            Expression<Long> userIdExpression = userJoin.get("id");
            userCreatorPredicate = criteriaBuilder.equal(userIdExpression, userId);

            if (!StringUtils.isEmpty(searchTerm)) {
                String searchStr = "%" + searchTerm.trim().toLowerCase() + "%";
                Predicate idPredicate = criteriaBuilder.like(root.get("id").as(String.class), searchStr);

                Expression<String> titleExpression = criteriaBuilder.function("lower", String.class, root.get("title"));
                Predicate titlePredicate = criteriaBuilder.like(titleExpression, searchStr);

                Expression<String> contentExpression = criteriaBuilder.function("lower", String.class, root.get("content"));
                Predicate contentPredicate = criteriaBuilder.like(contentExpression, searchStr);

                // Get the email field from the User entity
                Expression<String> emailExpression = criteriaBuilder.lower(userJoin.get("email"));
                Predicate emailPredicate = criteriaBuilder.like(emailExpression, searchStr.toLowerCase());

                userCreatorPredicate = criteriaBuilder.and(
                        userCreatorPredicate,
                        criteriaBuilder.or(titlePredicate, contentPredicate, emailPredicate, idPredicate)
                );
            }

            return userCreatorPredicate;
        };
    }
}
