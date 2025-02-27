package pl.books.magagement.specification;

import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Path;
import org.springframework.data.jpa.domain.Specification;
import pl.books.magagement.model.entity.RoleEntity;
import pl.books.magagement.model.entity.RoleEntity_;
import pl.books.magagement.model.entity.UserEntity;
import pl.books.magagement.model.entity.UserEntity_;

import java.util.List;
import java.util.UUID;

public class UserSpecification {

    public static Specification<UserEntity> matchFirstname(String firstname){

        String finalFirstname = firstname.toLowerCase();

        return (root, query, builder) -> {

            Path<String> dbUsernamePath = root.get(UserEntity_.USERNAME);

            Expression<String> dbUsernameExpression = builder.lower(dbUsernamePath);

            return builder.like(dbUsernameExpression, "%" + finalFirstname + "%");
        };
    }

    public static Specification<UserEntity> hasRole(UUID roleId){

        return (root, query, builder) -> {

//          Join<RoleEntity, UserEntity> roles = root.join(UserEntity_.ROLES);
//
//          Path<String> rolesNamePath = roles.get(RoleEntity_.NAME);

//          Expression<String> lowerRolesNames = builder.lower(rolesNamePath);
//          Expression<String> lowerUsersRolesNames = builder.lower(usersRolesNamesPath);

            Path<List<RoleEntity>> usersRolesPath = root.get(UserEntity_.ROLES);
            Path<UUID> usersRolesIdsPath = usersRolesPath.get(RoleEntity_.ID);

            return builder.equal(
                usersRolesIdsPath, roleId
            );
        };
    }
}
