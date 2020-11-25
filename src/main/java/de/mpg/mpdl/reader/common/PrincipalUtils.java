package de.mpg.mpdl.reader.common;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;
/**
 * @author shidenghui@gmail.com
 * @date 2020/11/23
 */
public class PrincipalUtils {
    public static Optional<Object> getPrincipal() {
        return Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .map(Authentication::getPrincipal);
    }

}
