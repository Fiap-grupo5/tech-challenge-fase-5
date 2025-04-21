package br.com.fiap.tech.identity.config;

import br.com.fiap.tech.identity.security.JwtService;
import br.com.fiap.tech.identity.service.CustomUserDetailsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = SecurityConfig.class)
class SecurityConfigTest {

    @Autowired
    private ApplicationContext context;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private CustomUserDetailsService userDetailsService;

    @Test
    void securityConfigShouldBeLoaded() {
        assertThat(context.getBean(SecurityConfig.class)).isNotNull();
        assertThat(context.getBean(PasswordEncoder.class)).isNotNull();
    }
}
