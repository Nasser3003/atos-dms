package net.atos.configuration;

import net.atos.model.EnumRole;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CustomJwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private final JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        Collection<GrantedAuthority> authorities = Stream.concat(
                jwtGrantedAuthoritiesConverter.convert(jwt).stream(),
                extractResourceRoles(jwt).stream()
        ).collect(Collectors.toSet());

        return new JwtAuthenticationToken(jwt, authorities, jwt.getClaimAsString("sub"));
    }

    private Collection<GrantedAuthority> extractResourceRoles(Jwt jwt) {
        String rolesString = jwt.getClaimAsString("roles");
        if (rolesString == null || rolesString.isEmpty())
            return List.of();

        return Arrays.stream(rolesString.split("\\s+"))
                .map(this::mapToGrantedAuthority)
                .collect(Collectors.toList());
    }

    private GrantedAuthority mapToGrantedAuthority(String role) {
        try {
            EnumRole enumRole = EnumRole.valueOf(role.replace("ROLE_", "").toUpperCase());
            return new SimpleGrantedAuthority("ROLE_" + enumRole.name());
        } catch (IllegalArgumentException e) {
            throw new BadCredentialsException("Invalid role: " + role);
        }
    }
}