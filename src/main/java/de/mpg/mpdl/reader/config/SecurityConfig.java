package de.mpg.mpdl.reader.config;

import de.mpg.mpdl.reader.common.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

/**
 * @author shidenghui@gmail.com
 * @date 2020/11/20
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomBasicAuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    private CustomFilter customFilter;

    @Value("${basic.auth.client}")
    private String client;

    @Value("${basic.auth.secret}")
    private String secret;

    public static final String[] STATIC_CONTENTS = new String[] {
            "/mpgReaderDisclaimer.html",
            "/mpgReaderPrivacyPolicy.html",
            "/mpgReaderTerms.html",
            "/img/favicon.ico",
            "/css/header.css",
            "/css/footer.css",
            "/css/center-status-info-box.css"
    };

    public static final String[] PERMIT_PATTERNS = new String[] {
            "/v2/api-docs",
            "/configuration/ui",
            "/swagger-resources/**",
            "/swagger-ui.html",
            "/webjars/**",
            "/actuator/**"
    };

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser(client)
                .password(passwordEncoder().encode(secret))
                .authorities(Constants.Role.user.toString());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers(PERMIT_PATTERNS).permitAll()
                .anyRequest().authenticated()
                .and().httpBasic().authenticationEntryPoint(authenticationEntryPoint)
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().csrf().disable()
                .addFilterAfter(customFilter, BasicAuthenticationFilter.class)
                .logout().permitAll()
                .logoutSuccessUrl("/");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
