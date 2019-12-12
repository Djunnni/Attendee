package ml.attendee.security;


import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	
	@Autowired
	DataSource dataSource;
	
	@Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/resources/**");
    }
	
	@Override
	public void configure(HttpSecurity http) throws Exception {		
		http.authorizeRequests()
			.antMatchers("/private","/","/info","/signup/**","/activity/**","/auth/**").permitAll()
			.antMatchers("/loginFail","/signupSuccess","/signupFail","/emailAuthFail","/emailAuth","/emailAuthDone").permitAll()
			.antMatchers("/robot.txt","/robots.txt","/favicon.ico","/css/**","/fonts/**","/js/**","/imgs/**","/webjars/**").permitAll().anyRequest().permitAll()
			.antMatchers("/member/**").access("hasRole('MEMBER')")
			.anyRequest().authenticated()
		.and()
			.formLogin().loginPage("/").permitAll()
			.loginProcessingUrl("/login").permitAll()
			.failureUrl("/loginFail") // default
			.usernameParameter("username")
            .passwordParameter("password")
			.permitAll()
			.defaultSuccessUrl("/member/home")
		.and()
			.logout().logoutRequestMatcher(new AntPathRequestMatcher("/member/logout"))
			.logoutSuccessUrl("/")
			.invalidateHttpSession(true)
		.and()
			.csrf();
			
	}
	@Autowired
	protected void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		final String usernameQuery = " select mid as username,mpw as password,case when enabled='Y' then true else false end as enabled from abapp_members where enabled like 'Y' and mid=?";
		final String authQuery = "select a.mid as username,b.role_name as authority from abapp_members as a inner join abapp_member_roles as b where b.member_id = a.id and a.mid=?";
		auth
			.jdbcAuthentication()
				.dataSource(dataSource)
				.usersByUsernameQuery(usernameQuery)
				.authoritiesByUsernameQuery(authQuery)
				.passwordEncoder(passwordEncoder());
			
	}
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
