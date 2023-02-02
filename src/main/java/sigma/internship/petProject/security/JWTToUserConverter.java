package sigma.internship.petProject.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JWTToUserConverter implements Converter<Jwt, UsernamePasswordAuthenticationToken> {

    @Autowired
    UserDetailsService userService;

    @Override
    public UsernamePasswordAuthenticationToken convert(Jwt jwt) {
        UserDetails user = userService.loadUserByUsername(jwt.getSubject());
        return new UsernamePasswordAuthenticationToken(user, jwt, user.getAuthorities());
    }
}
