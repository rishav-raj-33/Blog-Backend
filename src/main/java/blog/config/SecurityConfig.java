package blog.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import blog.security.JwtAuthenticationEntryPoint;
import blog.security.JwtAuthenticationFilter;

@Configuration
@EnableWebMvc
public class SecurityConfig {
	
	//Necessary reference of class Used in this class
	
	  @Autowired
	    private JwtAuthenticationEntryPoint point;
	    @Autowired
	    private JwtAuthenticationFilter filter;
	    
	    private final String[] PUBLIC_URLS= {
	    		"/swagger-ui/**",
	    		"/webjars/**",
	    		"/v3/api-docs",
	    		"/v2/api-docs",
	    		"/auth/**"
	    };
	    
	    
	    	
	    
           //Bean for the Security Info
	    
		@Bean
	     SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

	        http.csrf(csrf -> csrf.disable())
	                .cors(cors->cors.disable())
	                .authorizeHttpRequests(auth->auth.requestMatchers(PUBLIC_URLS)
	                .permitAll().anyRequest().authenticated())
	                .exceptionHandling(ex-> ex.authenticationEntryPoint(point))
	                .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
	        
	        http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
	                
	        return http.build();
	    }

}
