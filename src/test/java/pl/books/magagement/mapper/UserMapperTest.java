package pl.books.magagement.mapper;

import org.junit.jupiter.api.Test;
import pl.books.magagement.model.api.request.PatchUserRequest;
import pl.books.magagement.model.api.request.UserSearchCriteriaRequest;
import pl.books.magagement.model.api.response.UserResponse;
import pl.books.magagement.model.entity.RoleEntity;
import pl.books.magagement.model.entity.UserEntity;
import pl.books.magagement.model.internal.PatchUser;
import pl.books.magagement.model.internal.UserSearchCriteria;
import pl.books.magagement.model.mappers.UserMapper;
import pl.books.magagement.model.mappers.UserMapperImpl;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class UserMapperTest {

    private UserMapper userMapper = new UserMapperImpl();

    @Test
    public void shouldConvertUserSearchCriteriaRequestToInternalWithRoles(){

        //given
        UserSearchCriteriaRequest request = new UserSearchCriteriaRequest(
            "kamil",
            new String[]{"83e81fb3-4fd3-4b01-8d25-d8a5191ab3ea", "d4423b14-e85f-4a58-aa53-1ac0aa30f6a6"}
        );

        //when
        UserSearchCriteria gotCriteria = userMapper.userSearchCriteriaRequestToUserSearchCriteria(request);

        //then
        assertEquals(request.username(), gotCriteria.username());
        assertNotNull(gotCriteria.rolesIds());
        assertEquals(request.rolesIds().length, gotCriteria.rolesIds().length);

        String[] gotRolesIdsStrs = Arrays.stream(gotCriteria.rolesIds())
            .map(roleId -> roleId.toString())
            .toArray(String[]::new);

        assertArrayEquals(request.rolesIds(), gotRolesIdsStrs);
    }

    @Test
    public void shouldConvertUserSearchCriteriaRequestToInternalWithNoRoles(){

        //given
        UserSearchCriteriaRequest request = new UserSearchCriteriaRequest(
            "kamil",
            new String[0]
        );

        //when
        UserSearchCriteria gotCriteria = userMapper.userSearchCriteriaRequestToUserSearchCriteria(request);

        //then
        assertEquals(request.username(), gotCriteria.username());
        assertNotNull(gotCriteria.rolesIds());
        assertEquals(0, gotCriteria.rolesIds().length);
    }

    @Test
    public void shouldConvertUserSearchCriteriaRequestToInternalWhenNull(){

        //given

        //when
        UserSearchCriteria gotCriteria = userMapper.userSearchCriteriaRequestToUserSearchCriteria(null);

        //then
        assertNull(gotCriteria);
    }

    @Test
    public void shouldConvertPatchUserRequestToInternal(){

        //given
        PatchUserRequest request = new PatchUserRequest("kamil");

        //when
        PatchUser gotInternal = userMapper.patchUserRequestToPatchUser(request);

        //then
        assertEquals(request.username(), gotInternal.username());
    }

    @Test
    public void shouldConvertPatchUserRequestToInternalWhenNull(){

        //given

        //when
        PatchUser gotInternal = userMapper.patchUserRequestToPatchUser(null);

        //then
        assertNull(gotInternal);
    }

    @Test
    public void shouldConvertUserEntityToResponseWithRoles(){

        //given
        RoleEntity role = new RoleEntity("ADMIN");
        RoleEntity role1 = new RoleEntity("REVIEWER");

        UserEntity user = UserEntity.builder()
            .id(UUID.randomUUID())
            .username("kamil")
            .roles(Set.of(role, role1))
            .build();

        //when
        UserResponse gotResponse = userMapper.userEntityToUserResponse(user);

        //then
        assertEquals(user.getId(), gotResponse.id());
        assertEquals(user.getUsername(), gotResponse.username());
        assertNotNull(gotResponse.roles());
        assertEquals(user.getRoles().size(), gotResponse.roles().size());
        assertTrue(gotResponse.roles().containsAll(user.getRoles()));
    }

    @Test
    public void shouldConvertUserEntityToResponseWithNoRoles(){

        //given
        UserEntity user = UserEntity.builder()
            .id(UUID.randomUUID())
            .username("kamil")
            .roles(new HashSet<>())
            .build();

        //when
        UserResponse gotResponse = userMapper.userEntityToUserResponse(user);

        //then
        assertEquals(user.getId(), gotResponse.id());
        assertEquals(user.getUsername(), gotResponse.username());
        assertNotNull(gotResponse.roles());
        assertEquals(0, gotResponse.roles().size());
    }

    @Test
    public void shouldConvertUserEntityToResponseWhenNull(){

        //given

        //when
        UserResponse gotResponse = userMapper.userEntityToUserResponse(null);

        //then
        assertNull(gotResponse);
    }
}
