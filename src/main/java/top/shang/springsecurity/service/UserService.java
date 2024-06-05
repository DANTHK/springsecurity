package top.shang.springsecurity.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import top.shang.springsecurity.dto.*;
import top.shang.springsecurity.entity.User;
import top.shang.springsecurity.enums.RoleEnum;
import top.shang.springsecurity.jwt.JwtProvider;
import top.shang.springsecurity.repository.UserRepository;

import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;

    public Mono<TokenDTO> login(LoginDTO req) {
        return userRepository.findByUsernameOrEmail(req.getUsername(), req.getEmail())
                .filter(user -> passwordEncoder.matches(req.getPassword(), user.getPassword()))
                .map(user -> new TokenDTO(jwtProvider.generateToken(user)))
                .switchIfEmpty(Mono.error(new RuntimeException("bad credentials")));
    }

    public Mono<User> create(CreateUserDTO req) {
        User user = User.builder()
                .username(req.getUsername())
                .email(req.getEmail())
                .password(passwordEncoder.encode(req.getPassword()))
                .roles(RoleEnum.ROLE_ADMIN.name() + ", " + RoleEnum.ROLE_USER.name())
                .build();
        Mono<Boolean> userExists = userRepository.findByUsernameOrEmail(req.getUsername(), req.getEmail())
                .hasElement();
        return userExists.flatMap(exists -> exists ?
                Mono.error(new RuntimeException("username or email already exists")) : userRepository.save(user));
    }

    public Mono<RefreshTokenDTO.RefreshTokenResp> signIn(LoginDTO req) {
        return userRepository.findByUsernameOrEmail(req.getUsername(), req.getEmail())
                .filter(user -> passwordEncoder.matches(req.getPassword(), user.getPassword()))
                .map(user ->
                        new RefreshTokenDTO.RefreshTokenResp(jwtProvider.generateToken(user), jwtProvider.generateRefreshToken(Map.of(), user)))
                .switchIfEmpty(Mono.error(new RuntimeException("bad credentials")));
    }

    public Mono<RefreshTokenDTO.RefreshTokenResp> refresh(RefreshTokenDTO.RefreshTokenReq req) {
        String refreshToken = req.getRefreshToken();
        String username = jwtProvider.getSubject(refreshToken);
        return userRepository.findByUsernameOrEmail(username, username)
                .mapNotNull(user -> {
                    boolean validate = jwtProvider.validate(user, refreshToken);
                    if (validate)
                        return new RefreshTokenDTO.RefreshTokenResp(jwtProvider.generateToken(user), refreshToken);
                    return null;
                })
                .switchIfEmpty(Mono.error(new RuntimeException("login is invalid")));
    }

    public Flux<UserDTO.UserResp> users() {
        return userRepository.findAll()
                .map(user ->
                        new UserDTO.UserResp(
                                user.getId(),
                                user.getUsername(),
                                user.getEmail(),
                                user.getRoles())
                );
    }

    public Mono<UserDTO.UserResp> user(Integer id) {
        return userRepository.findById(id)
                .map(user ->
                        new UserDTO.UserResp(
                                user.getId(),
                                user.getUsername(),
                                user.getEmail(),
                                user.getRoles())
                );
    }
}
