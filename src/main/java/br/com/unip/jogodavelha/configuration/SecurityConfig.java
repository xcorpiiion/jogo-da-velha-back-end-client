package br.com.unip.jogodavelha.configuration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import br.com.unip.jogodavelha.security.JwtAuthentificationFilter;
import br.com.unip.jogodavelha.security.JwtAuthorizationFilter;
import br.com.unip.jogodavelha.security.utils.JwtUtil;

import static java.util.Arrays.asList;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Getter
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String[] PUBLIC_MATCHERS = {
            "/h2-console/**"
    };

    private static final String[] PUBLIC_MATCHERS_READ_ONLY = {
            "/logged/**",
            "/clientes/**",
    };

    private static final String[] PUBLIC_MATCHERS_POST = {
            "/clientes",
            "/login",
            "/testConnection/**",
            "/auth/forgot/**",
            "/auth/refreshToken/**",
    };

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private Environment environment;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        if (asList(this.getEnvironment().getActiveProfiles()).contains("test")) {
            http.headers().frameOptions().disable();
        }
        http.cors().and().csrf().disable();
        http.authorizeRequests()
                .antMatchers(PUBLIC_MATCHERS).permitAll()
                .antMatchers(GET, PUBLIC_MATCHERS_READ_ONLY).permitAll()
                .antMatchers(PUBLIC_MATCHERS_POST).permitAll()
                .anyRequest()
                .authenticated();
        http.addFilter(new JwtAuthentificationFilter(this.authenticationManager(), this.getJwtUtil()));
        http.addFilter(new JwtAuthorizationFilter(this.authenticationManager(), this.getJwtUtil(), this.getUserDetailsService()));
        http.sessionManagement().sessionCreationPolicy(STATELESS).and().headers().frameOptions().sameOrigin();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(this.getUserDetailsService()).passwordEncoder(this.bCryptPasswordEncoder());
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration().applyPermitDefaultValues();
        corsConfiguration.setAllowedMethods(asList("POST", "PUT", "GET", "DELETE", "OPTIONS"));
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        /* O meu Security vai ignorar essas urls que são do swagger */
        web.ignoring().antMatchers("/v2/api-docs", "/configuration/uri", "/swagger-resources/**",
                "/cofiguration/security", "/swagger-ui.html", "/webjars/**");
    }

}
