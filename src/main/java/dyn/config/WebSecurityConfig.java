package dyn.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests().anyRequest().permitAll();

        /*
        http
                .authorizeRequests()
                .antMatchers("/", "/about", "/greeting").permitAll()
                .antMatchers("/admin*//**").access("hasRole('ROLE_ADMIN')")
         .anyRequest().authenticated()
         .and()
         .formLogin()
         .loginPage("/login")
         .permitAll()
         .defaultSuccessUrl("/main")
         .and()
         .logout()
         .permitAll();*/
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .inMemoryAuthentication()
                .withUser("user").password("password").roles("USER");
    }
}
