package pl.books.magagement.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.books.magagement.model.api.request.CreateRoleRequest;
import pl.books.magagement.model.entity.RoleEntity;
import pl.books.magagement.service.RoleService;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/roles")
public class RoleController {

    private final RoleService roleService;

    @GetMapping
    public ResponseEntity<Page<RoleEntity>> getPage(@ParameterObject Pageable pageable){

        Page<RoleEntity> gotRolesPage = roleService.getPage(pageable);

        return ResponseEntity.ok(gotRolesPage);
    }

    @PostMapping
    public ResponseEntity<RoleEntity> createRole(@RequestBody @Valid CreateRoleRequest createRoleRequest){

        RoleEntity createdRole = roleService.createRole(createRoleRequest.name());

        return ResponseEntity.status(HttpStatus.CREATED).body(createdRole);
    }

    @DeleteMapping(value = "/{roleId}")
    public ResponseEntity<Void> deleteRoleById(@PathVariable("roleId") String roleIdStr){

        UUID roleId = UUID.fromString(roleIdStr);

        roleService.deleteById(roleId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
