package in.co.test.ucf.configs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import in.co.test.ucf.models.Role;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	private static final Logger LOGGER = LoggerFactory.getLogger(WebSecurityConfig.class);
	private static final String[] resources = new String[] { "/include/**", "/css/**", "/icons/**", "/img/**", "/js/**", "/layer/**", "/static/**" };

	@Autowired
	public void configureGlobal(final AuthenticationManagerBuilder auth) throws Exception {
		// Specify the person in charge of the login and encryption of the password
		// auth.loginService(loginService).passwordEncoder(passwordEncoder());
		LOGGER.info("In WebSecurityConfig.configureGlobal()");
		// Using in memory for now
		auth.inMemoryAuthentication()
		.withUser("testMaker").password("{noop}password").roles(Role.MAKER.name())
		.and()
		.withUser("testChecker").password("{noop}password").roles(Role.CHECKER.name())
		.and()
		.withUser("testAdmin").password("{noop}password").roles(Role.ADMIN.name());
	}

	@Override
	protected void configure(final HttpSecurity http) throws Exception {
		LOGGER.info("In WebSecurityConfig.configure()");
		http
		.authorizeRequests()
		.antMatchers(resources).permitAll()
		.antMatchers("/", "/index").permitAll()
		.anyRequest().authenticated()
		.and()
		.formLogin()
		.loginPage("/login")
		.permitAll()
		.defaultSuccessUrl("/createform")
		.failureUrl("/login?error=true")
		.usernameParameter("userName")
		.passwordParameter("password")
		.and()
		.csrf().disable()
		.logout()
		.permitAll()
		.logoutSuccessUrl("/login?logout");
	}
}
