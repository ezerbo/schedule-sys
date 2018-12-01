package com.ss.schedulesys.security.jwt;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import com.ss.schedulesys.config.ScheduleSysProperties;
import com.ss.schedulesys.config.ScheduleSysProperties.Security.Authentication.Jwt;
import com.ss.schedulesys.domain.ScheduleSysUser;
import com.ss.schedulesys.repository.ScheduleSysUserRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class TokenProvider {

    private static final String AUTHORITIES_KEY = "auth";

    private String secretKey;

    private long tokenValidityInSeconds;

    private long tokenValidityInSecondsForRememberMe;

    @Autowired
    private ScheduleSysProperties scheduleSysProperties;
    
    @Autowired
    private ScheduleSysUserRepository userRepository;

    @PostConstruct
    public void init() {
    	Jwt jwt = scheduleSysProperties.getSecurity().getAuthentication().getJwt();
        this.secretKey = jwt.getSecret();
        this.tokenValidityInSeconds = 1000 * jwt.getTokenValidityInSeconds();
        this.tokenValidityInSecondsForRememberMe = 1000 * jwt.getTokenValidityInSecondsForRememberMe();
    }

    public String createToken(Authentication authentication, Boolean rememberMe) {
        String authorities = authentication.getAuthorities().stream()
            .map(authority -> authority.getAuthority())
            .collect(Collectors.joining(","));

        long now = (new Date()).getTime();
        Date validity = rememberMe ? new Date(now + this.tokenValidityInSecondsForRememberMe) 
        		: new Date(now + this.tokenValidityInSeconds);
        Optional<ScheduleSysUser> user = userRepository.findOneByUsername(authentication.getName());
        return Jwts.builder()
            .setSubject(authentication.getName())
            .claim("email", user.get().getEmailAddress())
            .claim(AUTHORITIES_KEY, authorities)
            .signWith(SignatureAlgorithm.HS256, secretKey)
            .setExpiration(validity)
            .compact();
    }

    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parser()
            .setSigningKey(secretKey)
            .parseClaimsJws(token)
            .getBody();

        Collection<? extends GrantedAuthority> authorities =
            Arrays.asList(claims.get(AUTHORITIES_KEY).toString().split(",")).stream()
                .map(authority -> new SimpleGrantedAuthority(authority))
                .collect(Collectors.toList());

        User principal = new User(claims.getSubject(), "",
            authorities);

        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            log.info("Invalid JWT signature: " + e.getMessage());
            return false;
        }
    }
}
