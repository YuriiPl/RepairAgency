package com.repairagency.repairagencyspring.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)/**/
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;

    private final AccessDeniedHandler accessDeniedHandler;

    @Autowired
    public SecurityConfig(@Qualifier("userDetailsServiceImpl") UserDetailsService userDetailsService, AccessDeniedHandler accessDeniedHandler, AccessDeniedHandler accessDeniedHandler1) {
        this.userDetailsService = userDetailsService;
        this.accessDeniedHandler = accessDeniedHandler1;
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
                http
//                .csrf().disable()
//                .csrf().ignoringAntMatchers("/api/register")
//                .and()
                    .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                .and()
                    .authorizeRequests()
                    .antMatchers("/","/js/*","/api/register").permitAll()
//////                .antMatchers(HttpMethod.GET, "/api/**").hasAuthority(Authority.AUTHORITY_USER.getAuthority())
//                    .antMatchers(HttpMethod.GET, "/api/**").hasAuthority(Permission.PERMISSION_READ.getPermission())
//                    .antMatchers(HttpMethod.POST, "/api/**").hasAuthority(Permission.PERMISSION_WRITE.getPermission())
//                    .antMatchers(HttpMethod.DELETE, "/api/**").hasAuthority(Permission.PERMISSION_WRITE.getPermission())
                    .anyRequest().authenticated()
                .and()
//                    .httpBasic();
                    .formLogin()
                    .loginPage("/auth/login").permitAll()
                    .defaultSuccessUrl("/auth/success")
                .and()
                    .logout()
                    .logoutRequestMatcher(new AntPathRequestMatcher("/auth/logout", "POST"))
                    .invalidateHttpSession(true)
                    .clearAuthentication(true)
                    .deleteCookies("JSESSIONID")
                    .logoutSuccessUrl("/")
                .and()
                .exceptionHandling().accessDeniedHandler(accessDeniedHandler)
        ;

    }

//    @Bean
//    @Override
//    protected UserDetailsService userDetailsService() {
//        return new InMemoryUserDetailsManager(
//                User.builder()
//                        .username("admin")
//                        .password(passwordEncoder().encode("admin"))
//                        .authorities(Role.ADMIN.getAuthorities())
//                        .build(),
//                User.builder()
//                        .username("user").password(passwordEncoder().encode("user"))
//                        .authorities(Role.USER.getAuthorities())
//                        .build()
//        );
//    }

    @Bean
    protected PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(12);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Bean
    protected DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        return daoAuthenticationProvider;
    }
}
