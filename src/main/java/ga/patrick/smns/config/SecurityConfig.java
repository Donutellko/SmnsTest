package ga.patrick.smns.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("admin").password(encoder().encode("smns")).roles("ADMIN", "USER")
                .and()
                .withUser("user").password(encoder().encode("smns")).roles("USER")
                .and()
                .withUser("sensor").password(encoder().encode("smns")).roles("SENSOR");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                    .formLogin()
                .and()
                    .authorizeRequests()
                        .antMatchers("/*").hasAnyRole("SENSOR", "USER", "ADMIN")
                        .antMatchers("/api/add").hasAnyRole("SENSOR", "USER", "ADMIN")
                        .antMatchers("/api/**").hasAnyRole("USER", "ADMIN")
                        .anyRequest().hasRole("ADMIN")
                .and()
                    .headers().frameOptions().sameOrigin()
                .and()
                    .logout()
        ;
    }

}

