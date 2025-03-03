package pl.books.magagement.controller;

import jakarta.persistence.EntityExistsException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import pl.books.magagement.model.api.request.*;
import pl.books.magagement.model.api.response.LoginResponse;
import pl.books.magagement.model.api.response.UserResponse;
import pl.books.magagement.model.entity.RoleEntity;
import pl.books.magagement.model.entity.UserEntity;
import pl.books.magagement.model.internal.PatchUser;
import pl.books.magagement.model.internal.UserSearchCriteria;
import pl.books.magagement.model.mappers.UserMapper;
import pl.books.magagement.service.RevokedRefreshTokenService;
import pl.books.magagement.service.RoleService;
import pl.books.magagement.service.UserService;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {

    private final UserService userService;
    private final RoleService roleService;
    private final RevokedRefreshTokenService revokedRefreshTokenService;

    private final UserMapper userMapper;

    @GetMapping
    public ResponseEntity<Page<UserEntity>> searchByCriteria(
        @ParameterObject UserSearchCriteriaRequest criteriaRequest,
        @ParameterObject Pageable pageable
    ){
        UserSearchCriteria criteria = userMapper.userSearchCriteriaRequestToUserSearchCriteria(criteriaRequest);

        Page<UserEntity> foundUsersPage = userService.searchUsers(criteria, pageable);

        return ResponseEntity.ok(foundUsersPage);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest){

        log.debug("Login started");

        LoginResponse response = userService.login(loginRequest.username(), loginRequest.password());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh-access-token")
    public ResponseEntity<LoginResponse> refresh(@RequestBody RefreshTokenRequest request){

        String refreshToken = request.refreshToken();

        LoginResponse response = userService.refreshAccessToken(refreshToken);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request){

        String token = request.getHeader("Authorization").substring(6);

        revokedRefreshTokenService.addToken(token);

        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<?> register(@RequestBody @Valid RegisterRequest registerRequest){

        UserEntity newUser;

        try{
            newUser = userService.register(registerRequest.username(), registerRequest.password());
        }
        catch(EntityExistsException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

    @GetMapping("/{userId}/roles")
    public ResponseEntity<Set<RoleEntity>> getUserRoles(@PathVariable("userId") String userIdStr){

        UUID userId = UUID.fromString(userIdStr);

        Set<RoleEntity> userRoles = roleService.getUserRoles(userId);

        return ResponseEntity.ok(userRoles);
    }

    @PostMapping("/{userId}/roles/{roleId}")
    public ResponseEntity<RoleEntity> grantRole(
        @PathVariable("userId") String userIdStr,
        @PathVariable("roleId") String roleIdStr
    ){
        UUID userId = UUID.fromString(userIdStr);
        UUID roleId = UUID.fromString(roleIdStr);

        RoleEntity newRole = roleService.grantRole(userId, roleId);

        return ResponseEntity.status(HttpStatus.CREATED).body(newRole);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<UserResponse> patchById(
        @PathVariable("userId") String userIdStr,
        @RequestBody PatchUserRequest request
    ){
        UUID userId = UUID.fromString(userIdStr);
        PatchUser internalPatchUser = userMapper.patchUserRequestToPatchUser(request);

        UserEntity patchedUser = userService.patchUserById(userId, internalPatchUser);

        UserResponse userResponse = userMapper.userEntityToUserResponse(patchedUser);

        return ResponseEntity.ok(userResponse);
    }

    @DeleteMapping("/{userId}/roles/{roleId}")
    public ResponseEntity<Void> revokeRole(
        @PathVariable("userId") String userIdStr,
        @PathVariable("roleId") String roleIdStr
    ){
        UUID userId = UUID.fromString(userIdStr);
        UUID roleId = UUID.fromString(roleIdStr);

        roleService.revokeRole(userId, roleId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping(value = "/{userId}")
    public ResponseEntity<Void> removeById(@PathVariable("userId") String userIdStr){

        UUID userId = UUID.fromString(userIdStr);

        userService.deleteById(userId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
