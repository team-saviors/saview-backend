package server.jwt.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import server.jwt.oauth.PrincipalDetails;
import server.user.dto.response.TokenResponse;
import server.user.entity.User;
import server.user.repository.RefreshTokenRepository;
import server.user.service.UserService;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

import static lombok.AccessLevel.PRIVATE;

@Slf4j
@Getter
@FieldDefaults(makeFinal = true, level = PRIVATE)
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    String secret = "cos_jwt_token";

    AuthenticationManager authenticationManager;
    UserService userService;
    RefreshTokenRepository refreshTokenRepository;

    ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        try {
            ObjectMapper om = new ObjectMapper();
            User user = om.readValue(request.getInputStream(), User.class);

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword());
            return authenticationManager.authenticate(authenticationToken);

        } catch (IOException e) {
            log.error("", e);
        }
        return null;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException {
        log.debug("successfulAuthentication");
        PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();
        String accessToken;

        if (principalDetails.getUser().getRole().equals("ROLE_ADMIN")) {
            accessToken = JWT.create()
                .withSubject(principalDetails.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 14)))
                .withClaim("id", principalDetails.getUser().getUserId())
                .withClaim("email", principalDetails.getUser().getEmail())
                .sign(Algorithm.HMAC512(secret));
        } else {
            accessToken = JWT.create()
                .withSubject(principalDetails.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + (1000 * 60 * 30)))
//                    .withExpiresAt(new Date(System.currentTimeMillis() + (1000 * 60)))
                .withClaim("id", principalDetails.getUser().getUserId())
                .withClaim("email", principalDetails.getUser().getEmail())
                .sign(Algorithm.HMAC512(secret));
        }
        String refreshToken = JWT.create()
            .withSubject(principalDetails.getUsername())
            .withExpiresAt(new Date(System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 14)))
//                .withExpiresAt(new Date(System.currentTimeMillis() + (1000 * 60 * 10)))
            .sign(Algorithm.HMAC512(secret));

        String email = principalDetails.getUser().getEmail();
        if (refreshTokenRepository.findByEmail(email).isPresent()) {
            refreshTokenRepository.deleteByEmail(email);
        }
        userService.updateRefreshToken(email, refreshToken);

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("utf-8");
        response.setHeader("Access-Control-Allow-Origin", "*");

        TokenResponse tokenResponse = TokenResponse.builder().userId(principalDetails.getUser().getUserId()).accessToken("Bearer " + accessToken).refreshToken("Bearer " + refreshToken).build();

        String tokens = objectMapper.writeValueAsString(tokenResponse);
        response.getWriter().write(tokens);


    }

}
