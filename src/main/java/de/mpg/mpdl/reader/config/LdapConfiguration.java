package de.mpg.mpdl.reader.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;

/**
 * @author shidenghui@gmail.com
 * @date 2020/11/9
 * @desc
 */
@Configuration
public class LdapConfiguration {

    @Value("${ldap.url}")
    private String ldapUrl;

    @Value("${ldap.partitionSuffix}")
    private String ldapUrlSuffix;

    @Value("${ldap.principal}")
    private String ldapPrincipal;

    @Value("${ldap.password}")
    private String ldapPassword;

    @Bean
    public LdapContextSource contextSource() {
        LdapContextSource contextSource = new LdapContextSource();
        contextSource.setUrl(ldapUrl);
        contextSource.setBase(ldapUrlSuffix);
        contextSource.setUserDn(ldapPrincipal);
        contextSource.setPassword(ldapPassword);
        return contextSource;
    }

    @Bean
    public LdapTemplate ldapTemplate() {
        return new LdapTemplate(contextSource());
    }
}
