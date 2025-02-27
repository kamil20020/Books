package pl.books.magagement.service;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.books.magagement.model.entity.RoleEntity;
import pl.books.magagement.model.entity.UserEntity;
import pl.books.magagement.repository.RoleRepository;
import pl.books.magagement.repository.UserRepository;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    private final UserService userService;

    public Page<RoleEntity> getPage(Pageable pageable){

        if(pageable == null){
            pageable = Pageable.unpaged();
        }

        return roleRepository.findAll(pageable);
    }

    public RoleEntity getById(UUID roleId) throws EntityNotFoundException{

        return roleRepository.findById(roleId)
            .orElseThrow(() -> new EntityNotFoundException("Role not found by given id"));
    }

    public Set<RoleEntity> getUserRoles(UUID userId) throws EntityNotFoundException {

        UserEntity foundUser = userService.getById(userId);

        return foundUser.getRoles();
    }

    @Transactional
    public void createRole(String name) throws EntityExistsException{

        if(roleRepository.existsByNameIgnoreCase(name)){
            throw new EntityExistsException("Duplicate role name");
        }

        RoleEntity newRole = new RoleEntity(name.toUpperCase());

        roleRepository.save(newRole);
    }

    @Transactional
    public RoleEntity grantRole(UUID userId, UUID roleId) throws EntityNotFoundException, EntityExistsException {

        UserEntity foundUser = userService.getById(userId);
        RoleEntity foundRole = getById(roleId);

        if(foundUser.getRoles().contains(foundRole)){
            throw new EntityExistsException("Duplicate role grant");
        }

        foundUser.getRoles().add(foundRole);


        return foundRole;
    }

    @Transactional
    public void revokeRole(UUID userId, UUID roleId) throws EntityNotFoundException{

        UserEntity foundUser = userService.getById(userId);
        RoleEntity foundRole = getById(roleId);

        if(!foundUser.getRoles().contains(foundRole)){
            throw new EntityNotFoundException("User do not have given role");
        }

        foundUser.getRoles().remove(foundRole);
    }
}
