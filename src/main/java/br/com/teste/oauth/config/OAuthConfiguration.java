package br.com.teste.oauth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

@Configuration
public class OAuthConfiguration {

    //--- Tutorial
    //https://projects.spring.io/spring-security-oauth/docs/oauth2.html

    //--- obter token
    //curl -X POST -k --user 'client:secret' -d 'grant_type=client_credentials'  http://localhost:8080/oauth/token

    //--- Acesso com Token
    //curl -ik -H "Accept: application/json" -H "Authorization: Bearer 32913420-c8e2-4ceb-bc17-f8b983b2d5df" -X GET  http://localhost:8080/test

    private static final String SPARKLR_RESOURCE_ID = "sparklr";

    @Configuration
    @EnableResourceServer
    protected static class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

        @Override
        public void configure(ResourceServerSecurityConfigurer resources) {
            resources.resourceId(SPARKLR_RESOURCE_ID).stateless(false);
        }

        @Override
        public void configure(final HttpSecurity http) throws Exception {
            http.authorizeRequests()//
                    .antMatchers("/").permitAll().antMatchers("/test").authenticated();
        }

    }

    @Configuration
    @EnableAuthorizationServer
    protected static class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

        @Autowired
        private TokenStore tokenStore;

        @Autowired
        private AuthenticationManager authenticationManager;

        @Override
        public void configure(final AuthorizationServerEndpointsConfigurer configurer) throws Exception {
            configurer.authenticationManager(authenticationManager).tokenStore(tokenStore).approvalStoreDisabled();
        }

        @Override
        public void configure(final ClientDetailsServiceConfigurer clients) throws Exception {
            clients.inMemory()//
                    .withClient("client").secret("secret")//
                    .resourceIds(SPARKLR_RESOURCE_ID).authorizedGrantTypes("client_credentials", "password", "authorization_code", "refresh_token")//
                    .scopes("read", "write")//
                    .accessTokenValiditySeconds(300);
        }

        @Bean
        public TokenStore tokenStore() {
            return new InMemoryTokenStore();
        }

    }

}
