package br.com.fiap.tech.identity.config;

import br.com.fiap.tech.identity.security.JwtAuthenticationFilter;
import br.com.fiap.tech.identity.security.JwtService;
import br.com.fiap.tech.identity.service.CustomUserDetailsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class SecurityConfigTest {

    @Autowired
    private ApplicationContext context;

    @Test
    void beansShouldBeLoaded() {
        assertThat(context.getBean(SecurityFilterChain.class)).isNotNull();
        assertThat(context.getBean(JwtAuthenticationFilter.class)).isNotNull();
        assertThat(context.getBean(AuthenticationProvider.class)).isNotNull();
        assertThat(context.getBean(AuthenticationManager.class)).isNotNull();
        assertThat(context.getBean(PasswordEncoder.class)).isNotNull();
    }
}
