package sigma.internship.petProject.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;
import sigma.internship.petProject.dto.TokenDto;
import sigma.internship.petProject.entity.User;
import sigma.internship.petProject.exception.WebException;

import java.text.MessageFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Component
@Slf4j
public class TokenGenerator {

    private static final long ACCESS_TOKEN_EXPIERY = 30;
    private static final ChronoUnit ACCESS_TOKEN_EXPIERY_TIME_UNIT = ChronoUnit.MINUTES;

    private static final long REFRESH_TOKEN_EXPIERY = 90;
    private static final ChronoUnit REFRESH_TOKEN_EXPIERY_TIME_UNIT = ChronoUnit.DAYS;

    @Autowired
    @Qualifier("jwtAccessTokenEncoder")
    JwtEncoder accessTokenEncoder;

    @Autowired
    @Qualifier("jwtRefreshTokenEncoder")
    JwtEncoder refreshTokenEncoder;

    private String createAccessToken(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Instant now = Instant.now();

        JwtClaimsSet claimsSet = JwtClaimsSet.builder()
                .issuer("RAYW Authentication Service")
                .subject(user.getUsername())
                .issuedAt(now)
                .expiresAt(now.plus(
                        ACCESS_TOKEN_EXPIERY,
                        ACCESS_TOKEN_EXPIERY_TIME_UNIT
                ))
                .claim("role", user.getRole())
                .build();

        return accessTokenEncoder.encode(JwtEncoderParameters.from(claimsSet)).getTokenValue();
    }

    private String createRefreshToken(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Instant now = Instant.now();

        JwtClaimsSet claimsSet = JwtClaimsSet.builder()
                .issuer("RAYW Authentication Service")
                .subject(user.getUsername())
                .issuedAt(now)
                .expiresAt(now.plus(
                        REFRESH_TOKEN_EXPIERY,
                        REFRESH_TOKEN_EXPIERY_TIME_UNIT
                ))
                .claim("role", user.getRole())
                .build();

        return refreshTokenEncoder.encode(JwtEncoderParameters.from(claimsSet)).getTokenValue();
    }

    public TokenDto createToken(Authentication authentication) {
        if (!(authentication.getPrincipal() instanceof User user)) {
            throw new WebException(HttpStatus.BAD_REQUEST, MessageFormat.format("principal {0} is not of User type", authentication.getPrincipal().getClass()));
        }

        String refreshToken;
        if (authentication.getCredentials() instanceof Jwt jwt) {
            Instant now = Instant.now();
            Instant expiresAt = jwt.getExpiresAt();
            Duration duration = Duration.between(now, expiresAt);
            long daysUntilExpired = duration.toDays();
            if (daysUntilExpired < 7) {
                refreshToken = createRefreshToken(authentication);
            } else {
                refreshToken = jwt.getTokenValue();
            }
        } else {
            refreshToken = createRefreshToken(authentication);
        }

        return new TokenDto(user.getId(), createAccessToken(authentication), refreshToken);
    }
}
