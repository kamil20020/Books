package pl.books.magagement.service;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;
import pl.books.magagement.config.JwtService;
import pl.books.magagement.model.api.response.LoginResponse;
import pl.books.magagement.model.entity.UserEntity;
import pl.books.magagement.model.internal.PatchUser;
import pl.books.magagement.model.internal.UserSearchCriteria;
import pl.books.magagement.repository.UserRepository;
import pl.books.magagement.specification.UserSpecification;

import static org.springframework.data.jpa.domain.Specification.*;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    
    public Page<UserEntity> searchUsers(UserSearchCriteria userSearchCriteria, Pageable pageable) {

        if(pageable == null){
            pageable = Pageable.unpaged();
        }

        List<Specification<UserEntity>> specifications = new ArrayList<>();

        if(userSearchCriteria.username() != null && !userSearchCriteria.username().isEmpty()){
            specifications.add(
                UserSpecification.matchFirstname(userSearchCriteria.username())
            );
        }

        if(userSearchCriteria.rolesIds() != null){

            for(UUID roleId : userSearchCriteria.rolesIds()){

                specifications.add(
                    UserSpecification.hasRole(roleId)
                );
            }
        }

        return userRepository.findAll(
            where(
                anyOf(specifications)
            ),
            pageable
        );
    }

    public UserEntity getById(UUID id) throws EntityNotFoundException {

        return userRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("User not found by given id"));
    }

    public String login(String username, String password) throws IllegalArgumentException{

        Optional<UserEntity> foundUserOpt = userRepository.findByUsernameIgnoreCase(username);

        if(foundUserOpt.isEmpty() || !passwordEncoder.matches(password, foundUserOpt.get().getPassword())){

            throw new IllegalArgumentException("Invalid username or password");
        }

        return jwtService.generateAccessToken(foundUserOpt.get());
    }

    @Transactional
    public UserEntity register(String username, String password) throws EntityExistsException{

        if(userRepository.existsByUsernameIgnoreCase(username)){
            throw new EntityExistsException("Duplicate username");
        }

        String encryptedPassword = passwordEncoder.encode(password);

        UserEntity newUser = UserEntity.builder()
            .username(username)
            .password(encryptedPassword)
            .roles(new HashSet<>())
            .build();

        return userRepository.save(newUser);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        return userRepository.findByUsernameIgnoreCase(username.toLowerCase())
            .orElseThrow(() -> new UsernameNotFoundException("User with given username does not exist"));
    }

    @Transactional
    public UserEntity patchUserById(UUID userId, PatchUser newData) throws EntityNotFoundException{

        UserEntity foundUser = getById(userId);

        if(newData.username() != null){

            String newUsername = newData.username();

            if(userRepository.existsByUsernameIgnoreCase(newUsername)){
                throw new EntityExistsException("Duplicate username");
            }

            foundUser.setUsername(newUsername);
        }

        return foundUser;
    }

    @Transactional
    public void deleteById(UUID userId) throws EntityNotFoundException{

        if(!userRepository.existsById(userId)){
            throw new EntityNotFoundException("User was not found by given id");
        }

        userRepository.deleteById(userId);
    }
}
