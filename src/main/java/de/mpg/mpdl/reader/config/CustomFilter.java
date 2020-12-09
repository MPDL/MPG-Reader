package de.mpg.mpdl.reader.config;

import de.mpg.mpdl.reader.common.Constants;
import de.mpg.mpdl.reader.model.User;
import de.mpg.mpdl.reader.repository.UserRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static de.mpg.mpdl.reader.config.SecurityConfig.PERMIT_PATTERNS;

/**
 * @author shidenghui@gmail.com
 * @date 2020/11/23
 */
@Component
public class CustomFilter extends GenericFilterBean {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        for(String str : PERMIT_PATTERNS) {
            if (str.equalsIgnoreCase(request.getRequestURI())) {
                chain.doFilter(servletRequest, servletResponse);
                break;
            }
        }
        String email = request.getHeader(Constants.HEADER_EMAIL);
        String sn = request.getHeader(Constants.HEADER_SN);
        User user = userRepository.findByEmail(email);
        if (StringUtils.isBlank(email) || StringUtils.isBlank(sn) || user == null){
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
        } else {
            chain.doFilter(servletRequest, servletResponse);
        }
    }

}