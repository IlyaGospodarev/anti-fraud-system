package antifraud.security;

import antifraud.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    public static final String ADMINISTRATOR = "ADMINISTRATOR";
    public static final String MERCHANT = "MERCHANT";
    public static final String SUPPORT = "SUPPORT";
    @Autowired
    UserService userService;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(bCryptPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .httpBasic()
                .and()
                .authorizeRequests()
                .antMatchers( "/api/antifraud/suspicious-ip/**").hasRole(SUPPORT)
                .antMatchers( "/api/antifraud/stolencard/**").hasRole(SUPPORT)
                .antMatchers(HttpMethod.GET, "/api/antifraud/history/**").hasRole(SUPPORT)
                .antMatchers(HttpMethod.PUT, "/api/antifraud/transaction/**").hasRole(SUPPORT)
                .antMatchers(HttpMethod.POST, "/api/antifraud/transaction/**").hasRole(MERCHANT)
                .antMatchers(HttpMethod.PUT, "/api/auth/access/**").hasRole(ADMINISTRATOR)
                .antMatchers(HttpMethod.PUT, "/api/auth/role/**").hasRole(ADMINISTRATOR)
                .antMatchers(HttpMethod.GET, "/api/auth/list/**").hasAnyRole(ADMINISTRATOR, SUPPORT)
                .antMatchers(HttpMethod.DELETE, "/api/auth/user/**").hasRole(ADMINISTRATOR)
                .antMatchers("/api/auth/user").permitAll()
                .antMatchers("/actuator/shutdown").permitAll()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }
}
