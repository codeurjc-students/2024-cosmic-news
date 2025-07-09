package es.codeurjc.cosmic_news.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import es.codeurjc.cosmic_news.security.jwt.JwtRequestFilter;
import es.codeurjc.cosmic_news.security.jwt.UnauthorizedHandlerJwt;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

	@Autowired
	private JwtRequestFilter jwtRequestFilter;

	@Autowired
	public RepositoryUserDetailsService userDetailService;

	@Autowired
	private UnauthorizedHandlerJwt unauthorizedHandlerJwt;

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
		return authConfig.getAuthenticationManager();
	}

	@Bean
	DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

		authProvider.setUserDetailsService(userDetailService);
		authProvider.setPasswordEncoder(passwordEncoder());

		return authProvider;
	}

    @Bean
    @Order(1)
    SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
		http.authenticationProvider(authenticationProvider());

		http
			.securityMatcher("/api/**")
			.exceptionHandling(handling -> handling.authenticationEntryPoint(unauthorizedHandlerJwt));

		http
			.authorizeHttpRequests(authorize -> authorize
					// PRIVATE ENDPOINTS

					// PUBLIC ENDPOINTS
					.anyRequest().permitAll()
			);

		// Disable Form login Authentication
		http.formLogin(formLogin -> formLogin.disable());

		// Disable CSRF protection (it is difficult to implement in REST APIs)
		http.csrf(csrf -> csrf.disable());

		// Disable Basic Authentication
		http.httpBasic(httpBasic -> httpBasic.disable());

		// Stateless session
		http.sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		// Add JWT Token filter
		http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	@Bean
	@Order(2)
	SecurityFilterChain webFilterChain(HttpSecurity http) throws Exception {
		http.authenticationProvider(authenticationProvider());

		http
			.authorizeHttpRequests(authorize -> authorize
					// PUBLIC PAGES
					.requestMatchers("/css/**", "/js/**", "/images/**").permitAll()
					.requestMatchers("/").permitAll()
					.requestMatchers("/index").permitAll()
					.requestMatchers("/error").permitAll()
					.requestMatchers("/message").permitAll()

					.requestMatchers("/login").permitAll()
					.requestMatchers("/loginerror").permitAll()
					.requestMatchers("/profile").permitAll()

					.requestMatchers("/user/form").permitAll()
					.requestMatchers("/user/register").permitAll()
					.requestMatchers("/availableMail").permitAll()
					.requestMatchers("/availableNick").permitAll()

					.requestMatchers("/calendar").permitAll()
					.requestMatchers("/events").permitAll()
					.requestMatchers("/event/new").hasRole("ADMIN")
					.requestMatchers("/event/edit").hasRole("ADMIN")
					.requestMatchers("/event/**").hasRole("ADMIN")
					.requestMatchers("/notify").authenticated()

					.requestMatchers("/quizzes").permitAll()
					.requestMatchers("/quizzes/load").permitAll()
					.requestMatchers("/quizz/**/image").permitAll()
					.requestMatchers("/quiz/**").authenticated()
					.requestMatchers("/quizz/review/**").authenticated()
					.requestMatchers("/quizz/submit/**").authenticated()
					.requestMatchers("/quizz/new").hasRole("ADMIN")
					.requestMatchers("/quizz/**/delete").hasRole("ADMIN")
					.requestMatchers("/quizz/**/edit").hasRole("ADMIN")

					.requestMatchers("/news").permitAll()
					.requestMatchers("/new/**").permitAll()
					.requestMatchers("/news/**/image").permitAll()
					.requestMatchers("/news/load").permitAll()
					.requestMatchers("/newsUser/load").permitAll()
					.requestMatchers("/news/new").hasRole("ADMIN")
					.requestMatchers("/news/**/edit").hasRole("ADMIN")
					.requestMatchers("/news/**/delete").hasRole("ADMIN")

					.requestMatchers("/pictures").permitAll()
					.requestMatchers("/pictures/**").permitAll()
					.requestMatchers("/picture/**/image").permitAll()
					.requestMatchers("/pictures/load").permitAll()
					.requestMatchers("/picturesUser/load").authenticated()
					.requestMatchers("/picture/new").hasRole("ADMIN")
					.requestMatchers("/picture/**/edit").hasRole("ADMIN")
					.requestMatchers("/picture/**/delete").hasRole("ADMIN")

					.requestMatchers("/videos").permitAll()
					.requestMatchers("/videos/**").permitAll()
					.requestMatchers("/videos/load").permitAll()
					.requestMatchers("/video/new").hasRole("ADMIN")
					.requestMatchers("/video/**/edit").hasRole("ADMIN")
					.requestMatchers("/video/**/delete").hasRole("ADMIN")

					.requestMatchers("/space").permitAll()


                    // OpenAPI documentation
                    .requestMatchers("/v3/**").permitAll()
                    .requestMatchers("/swagger-ui/**").permitAll()
					
					// Frontend petitions
					.requestMatchers("/front/**").permitAll()

					// PRIVATE PAGES
					.anyRequest().authenticated()
			)
			.formLogin(formLogin -> formLogin
					.loginPage("/login")
					.failureUrl("/login/error")
					.successHandler((request, response, authentication) -> {
						response.sendRedirect("/login");
					})
					.permitAll()
			)
			.logout(logout -> logout
					.logoutUrl("/logout")
					.logoutSuccessUrl("/login")
					.permitAll()
			);

		return http.build();
	}
}