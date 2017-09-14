package io.jee.alaska.sso.client;

import javax.annotation.Resource;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import io.jee.alaska.sso.RememberMeCommonServices;
import io.jee.alaska.sso.SSOConstant;
import io.jee.alaska.sso.SSOProperties;

@EnableConfigurationProperties({SSOProperties.class})
public class WebSecuritySSOConfigurerAdapter extends WebSecurityConfigurerAdapter {
	
	@Resource
	private UserDetailsService userDetailsService;
	@Resource
	private SSOProperties ssoProperties;
	
	@Bean
	public AuthenticationEntryPoint jeeAuthenticationEntryPoint() {
		return new JeeAuthenticationEntryPoint();
	}
	
	@Bean
	public AuthenticationProvider jeeAuthenticationProvider() {
		JeeAuthenticationProvider jeeAuthenticationProvider = new JeeAuthenticationProvider();
		jeeAuthenticationProvider.setUserDetailsService(userDetailsService);
		return jeeAuthenticationProvider;
	}

	@Bean
	public JeeAuthenticationFilter jeeAuthenticationFilter() throws Exception {
		JeeAuthenticationFilter jeeAuthenticationFilter = new JeeAuthenticationFilter();
		jeeAuthenticationFilter
				.setAuthenticationManager(authenticationManagerBean());
		return jeeAuthenticationFilter;
	}
	
	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth)
			throws Exception {
		auth.authenticationProvider(jeeAuthenticationProvider());
	}
	
	public LogoutSuccessHandler logoutSuccessHandler(){
		JeeLogoutSuccessHandler logoutSuccessHandler = new JeeLogoutSuccessHandler();
		logoutSuccessHandler.setDefaultTargetUrl(ssoProperties.getUrl()+"/logout");
		return logoutSuccessHandler;
	}
	
	@Bean
	public RememberMeServices rememberMeServices(){
		RememberMeCommonServices rememberMeServices = new RememberMeCommonServices(SSOConstant.COOKIE_KEY, userDetailsService);
		rememberMeServices.setCookieName(SSOConstant.REMEBER_COOKIE_NAME);
		return rememberMeServices;
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.addFilter(new JeeExceptionTranslationFilter(jeeAuthenticationEntryPoint()))
		.addFilterBefore(jeeAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class).exceptionHandling()
		.authenticationEntryPoint(jeeAuthenticationEntryPoint())
		.and().logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessHandler(logoutSuccessHandler())
		.and().rememberMe().key(SSOConstant.COOKIE_KEY).rememberMeServices(rememberMeServices());
	}

}
