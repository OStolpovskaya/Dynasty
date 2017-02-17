package dyn.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/", "/about", "/reg").permitAll()
                .antMatchers("/webjars/**").permitAll()
                .antMatchers("/admin").access("hasRole('ROLE_ADMIN')")
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login").defaultSuccessUrl("/main")
                .permitAll()
                .and()
                .logout()
                .permitAll();

        /*http.authorizeRequests()
                .antMatchers("/", "/about").permitAll()
                .antMatchers("/admin", "/about").access("hasRole('ROLE_ADMIN')")
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login").defaultSuccessUrl("/main")
                .permitAll()
                .and()
                .logout()
                .permitAll();*/

       /*http
                .authorizeRequests()
                .antMatchers("/", "/about", "/greeting").permitAll()
                .antMatchers("/admin").access("hasRole('ROLE_ADMIN')")
                .antMatchers("/main").access("hasRole('ROLE_USER')")
                .and()
                .formLogin()
                .loginPage("/login")
                .usernameParameter("username").passwordParameter("password")
                .defaultSuccessUrl("/main")
                .and()
                .logout().logoutSuccessUrl("/login?logout")
                .and()
                .exceptionHandling().accessDeniedPage("/403")
                .and()
                .csrf();*/

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
        System.out.println("configureGlobal");
        auth
                .inMemoryAuthentication()
                .withUser("user").password("password").roles("USER", "ADMIN");
    }

    @Bean(name = "passwordEncoder")
    public PasswordEncoder passwordencoder() {
        return new BCryptPasswordEncoder();
    }
}
