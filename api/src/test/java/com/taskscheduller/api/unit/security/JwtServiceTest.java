package com.taskscheduller.api.unit.security;

import com.taskscheduller.api.security.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class JwtServiceTest {
    @Test
    void generatesTokenWithSubjectScopeIssuerAndFifteenMinuteLifetime() {
        JwtEncoder encoder = mock(JwtEncoder.class);
        Instant tokenTime = Instant.parse("2026-01-01T00:00:00Z");
        when(encoder.encode(any(JwtEncoderParameters.class))).thenReturn(
                new Jwt("jwt-value", tokenTime, tokenTime.plusSeconds(900), java.util.Map.of("alg", "HS256"),
                        java.util.Map.of("sub", "user@example.com", "iss", "https://task-scheduler-api.local", "scope", "read write")));
        var authentication = new UsernamePasswordAuthenticationToken("user@example.com", "", List.of(
                new SimpleGrantedAuthority("read"), new SimpleGrantedAuthority("write")));

        assertThat(new JwtService(encoder).generateToken(authentication)).isEqualTo("jwt-value");

        var parameters = org.mockito.ArgumentCaptor.forClass(JwtEncoderParameters.class);
        verify(encoder).encode(parameters.capture());
        var claims = parameters.getValue().getClaims();
        assertThat(claims.getSubject()).isEqualTo("user@example.com");
        assertThat(claims.getIssuer().toString()).isEqualTo("https://task-scheduler-api.local");
        assertThat(claims.getClaimAsString("scope")).isEqualTo("read write");
        assertThat(claims.getExpiresAt()).isEqualTo(claims.getIssuedAt().plusSeconds(900));
    }
}
