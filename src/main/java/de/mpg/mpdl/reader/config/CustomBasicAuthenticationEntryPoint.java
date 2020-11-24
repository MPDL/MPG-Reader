package de.mpg.mpdl.reader.config;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author shidenghui@gmail.com
 * @date 2020/11/23
 */
@Component
public class CustomBasicAuthenticationEntryPoint extends BasicAuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authEx) {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
    }

    @Override
    public void afterPropertiesSet() {
        setRealmName("MPDL");
        super.afterPropertiesSet();
    }
}
