package com.example.bankcards.service;

import com.example.bankcards.dto.request.UserCreateRequest;
import com.example.bankcards.dto.request.UserUpdateRequest;
import com.example.bankcards.dto.response.UserResponse;
import com.example.bankcards.entity.Role;
import com.example.bankcards.entity.User;
import com.example.bankcards.enums.RoleNameEnum;
import com.example.bankcards.exception.custom.RoleNotFoundException;
import com.example.bankcards.exception.custom.UserAlreadyExistsException;
import com.example.bankcards.exception.custom.UserNotFoundException;
import com.example.bankcards.mapper.UserMapper;
import com.example.bankcards.repository.RoleRepository;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private UserResponse userResponse;
    private UUID userId;
    private String login;
    private String password;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        login = "testuser";
        password = "password123";
        user = User.builder()
                .userId(userId)
                .login(login)
                .passwordHash(password)
                .roles(new HashSet<>())
                .build();
        userResponse = UserResponse.builder()
                .login(login)
                .cardsNumbers(Collections.emptyList())
                .rolesNames(Collections.emptyList())
                .blockRequestStatuses(Collections.emptyList())
                .build();
    }

    @Test
    void getAll_ShouldReturnUserList() {
        // given
        int page = 0;
        int size = 10;
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<User> userPage = new PageImpl<>(Collections.singletonList(user));
        when(userRepository.findAll(pageRequest)).thenReturn(userPage);
        when(userMapper.toResponse(user)).thenReturn(userResponse);

        // when
        List<UserResponse> result = userService.getAll(page, size);

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(userResponse);
        verify(userRepository).findAll(pageRequest);
        verify(userMapper).toResponse(user);
    }

    @Test
    void getById_ShouldReturnUser_WhenUserExists() {
        // given
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userMapper.toResponse(user)).thenReturn(userResponse);

        // when
        UserResponse result = userService.getById(userId);

        // then
        assertThat(result).isEqualTo(userResponse);
        verify(userRepository).findById(userId);
        verify(userMapper).toResponse(user);
    }

    @Test
    void getById_ShouldThrowException_WhenUserDoesNotExist() {
        // given
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> userService.getById(userId))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("User not found with id: " + userId);
        verify(userRepository).findById(userId);
        verify(userMapper, never()).toResponse(any());
    }

    @Test
    void create_ShouldCreateUser_WhenLoginIsUnique() {
        // given
        UserCreateRequest request = new UserCreateRequest(login, password);
        Role role = new Role();
        role.setName(RoleNameEnum.ROLE_USER);
        when(userRepository.existsByLogin(login)).thenReturn(false);
        when(userMapper.toEntity(request)).thenReturn(user);
        when(roleRepository.findByName(RoleNameEnum.ROLE_USER)).thenReturn(Optional.of(role));
        when(userRepository.save(user)).thenReturn(user);

        // when
        UUID createdUserId = userService.create(request);

        // then
        assertThat(createdUserId).isEqualTo(userId);
        verify(userRepository).existsByLogin(login);
        verify(userMapper).toEntity(request);
        verify(roleRepository).findByName(RoleNameEnum.ROLE_USER);
        verify(userRepository).save(user);
    }

    @Test
    void create_ShouldThrowException_WhenLoginAlreadyExists() {
        // given
        UserCreateRequest request = new UserCreateRequest(login, password);
        when(userRepository.existsByLogin(login)).thenReturn(true);

        // when & then
        assertThatThrownBy(() -> userService.create(request))
                .isInstanceOf(UserAlreadyExistsException.class)
                .hasMessage("User with login " + login + " already exists");
        verify(userRepository).existsByLogin(login);
        verify(userMapper, never()).toEntity(any());
        verify(roleRepository, never()).findByName(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    void create_ShouldThrowException_WhenDefaultRoleNotFound() {
        // given
        UserCreateRequest request = new UserCreateRequest(login, password);
        when(userRepository.existsByLogin(login)).thenReturn(false);
        when(userMapper.toEntity(request)).thenReturn(user);
        when(roleRepository.findByName(RoleNameEnum.ROLE_USER)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> userService.create(request))
                .isInstanceOf(RoleNotFoundException.class)
                .hasMessage("Default role not found");
        verify(userRepository).existsByLogin(login);
        verify(userMapper).toEntity(request);
        verify(roleRepository).findByName(RoleNameEnum.ROLE_USER);
        verify(userRepository, never()).save(any());
    }

    @Test
    void update_ShouldUpdateUser_WhenUserExists() {
        // given
        UserUpdateRequest request = new UserUpdateRequest("oldPassword", "newPassword");
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        doNothing().when(userMapper).updateEntity(user, request);
        when(userRepository.save(user)).thenReturn(user);

        // when
        userService.update(userId, request);

        // then
        verify(userRepository).findById(userId);
        verify(userMapper).updateEntity(user, request);
        verify(userRepository).save(user);
    }

    @Test
    void update_ShouldThrowException_WhenUserDoesNotExist() {
        // given
        UserUpdateRequest request = new UserUpdateRequest("oldPassword", "newPassword");
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> userService.update(userId, request))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("User not found with id: " + userId);
        verify(userRepository).findById(userId);
        verify(userMapper, never()).updateEntity(any(), any());
        verify(userRepository, never()).save(any());
    }

    @Test
    void delete_ShouldDeleteUser_WhenUserExists() {
        // given
        when(userRepository.existsById(userId)).thenReturn(true);
        doNothing().when(userRepository).deleteById(userId);

        // when
        userService.delete(userId);

        // then
        verify(userRepository).existsById(userId);
        verify(userRepository).deleteById(userId);
    }

    @Test
    void delete_ShouldThrowException_WhenUserDoesNotExist() {
        // given
        when(userRepository.existsById(userId)).thenReturn(false);

        // when & then
        assertThatThrownBy(() -> userService.delete(userId))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("User not found with id: " + userId);
        verify(userRepository).existsById(userId);
        verify(userRepository, never()).deleteById(any());
    }
}