package com.diploma.DPAR.controller;


import com.diploma.DPAR.domain.JwtRefreshToken;
import com.diploma.DPAR.domain.User;
import com.diploma.DPAR.security.jwt.AccessToken;
import com.diploma.DPAR.security.jwt.JwtAuthenticationException;
import com.diploma.DPAR.security.jwt.JwtTokenProvider;
import com.diploma.DPAR.service.JwtRefreshTokenService;
import com.diploma.DPAR.service.UserService;
import com.diploma.DPAR.service.dto.AuthenticationRequestDTO;
import com.diploma.DPAR.service.dto.RefreshTokenDTO;
import com.diploma.DPAR.service.dto.SignInResponseDTO;
import com.diploma.DPAR.service.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping(value = "/api/v1/auth/")
public class AuthenticationController {

    private AuthenticationManager authenticationManager;
    private JwtTokenProvider jwtTokenProvider;
    private UserService userService;
    private JwtRefreshTokenService jwtRefreshTokenService;
    private UserMapper userMapper;

    @Autowired
    public AuthenticationController(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider,
                                    UserService userService, JwtRefreshTokenService jwtRefreshTokenService,
                                    UserMapper userMapper) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
        this.jwtRefreshTokenService = jwtRefreshTokenService;
        this.userMapper = userMapper;
    }


    @GetMapping("")
    public String getInfo() {
        return "Добро пожаловать";
    }

    @PostMapping("login")
    public ResponseEntity<SignInResponseDTO> login(@RequestBody AuthenticationRequestDTO requestDto) {
        String username = requestDto.getUsername();
        User loadedUser;
        if (username == null) {
            loadedUser = userService.findByUsername(requestDto.getUsername());
            if (loadedUser == null) {
                throw new UsernameNotFoundException("Error load user");
            }
            username = loadedUser.getUsername();
        }
        try {
            loadedUser = userService.findByUsername(username);
            authenticationManager
                    .authenticate(
                            new UsernamePasswordAuthenticationToken(username, requestDto.getPassword())
                    );


            AccessToken accessToken  = jwtTokenProvider.createToken(username, loadedUser.getRoles());
            JwtRefreshToken refreshToken = createRefreshToken(loadedUser);

            SignInResponseDTO response = new SignInResponseDTO();
            response.setAccessToken(accessToken.getToken());
            response.setExpiredIn(accessToken.getExpiredIn());
            response.setRefreshToken(refreshToken.getToken());
            response.setUser(userMapper.fromEntity(loadedUser));
            return ResponseEntity.ok().body(response);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username or password");
        }
    }


    @PostMapping("refreshToken")
    public ResponseEntity<SignInResponseDTO> refreshToken(@RequestBody RefreshTokenDTO refreshTokenDto) {
        return jwtRefreshTokenService.findById(refreshTokenDto.getRefreshToken()).map(jwtRefreshToken -> {
            jwtRefreshTokenService.delete(jwtRefreshToken);

            User user = jwtRefreshToken.getUser();
            log.info("roles", user.getRoles());
            AccessToken accessToken = jwtTokenProvider.createToken(user.getUsername(), user.getRoles());
            JwtRefreshToken refreshToken = createRefreshToken(user);

            SignInResponseDTO response = new SignInResponseDTO();
            response.setAccessToken(accessToken.getToken());
            response.setExpiredIn(accessToken.getExpiredIn());
            response.setRefreshToken(refreshToken.getToken());
            response.setUser(userMapper.fromEntity(user));

            return ResponseEntity.ok().body(response);
        }).orElseThrow(() -> new JwtAuthenticationException("Invalid refresh token"));

    }

    private JwtRefreshToken createRefreshToken(User user) {

        String refreshToken = jwtTokenProvider.createRefreshToken();
        JwtRefreshToken jwtRefreshToken = new JwtRefreshToken();
        jwtRefreshToken.setToken(refreshToken);
        jwtRefreshToken.setUser(user);
        jwtRefreshToken = jwtRefreshTokenService.save(jwtRefreshToken);
        return jwtRefreshToken;
    }

}
