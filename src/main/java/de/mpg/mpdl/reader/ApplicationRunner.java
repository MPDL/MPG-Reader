package de.mpg.mpdl.reader;

import de.mpg.mpdl.reader.model.User;
import de.mpg.mpdl.reader.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.context.annotation.Profile;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.query.LdapQuery;
import org.springframework.stereotype.Component;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;

import static org.springframework.ldap.query.LdapQueryBuilder.query;

/**
 * @author shidenghui@gmail.com
 * @date 2020/11/9
 */
@Component
@Profile("!test")
public class ApplicationRunner implements org.springframework.boot.ApplicationRunner {

    @Autowired
    private LdapTemplate ldapTemplate;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(ApplicationArguments args) {
        LdapQuery query = query()
                //.base("dc=mpadmanager,dc=de")
                .attributes("uid", "cn", "sn", "ou", "dn")
                .where("objectclass").is("inetOrgPerson");
                //.or("objectclass").is( "organizationalUnit")
                //.or("objectclass").is( "person")
                //.or("objectclass").is( "top")
        ldapTemplate.search(query, new UserAttributesMapper());
    }


    private class UserAttributesMapper implements AttributesMapper<User> {
        @Override
        public User mapFromAttributes(Attributes attrs) throws NamingException {
            String email = (String) attrs.get("uid").get();
            User user = userRepository.findByEmail(email);
            if (user == null) {
                user = new User();
                user.setEmail(email);
            }

            if( attrs.get("ou") != null) {
                user.setOu((String) attrs.get("ou").get());
            } else {
                user.setOu("MPG");
            }
            userRepository.save(user);
            return user;
        }
    }


}