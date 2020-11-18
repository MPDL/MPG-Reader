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

import java.util.List;

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
                .attributes("uid", "cn", "sn")
                .where("objectclass").is("inetOrgPerson");

        List<String> result = ldapTemplate.search(query,
                (AttributesMapper<String>) attrs -> (String) attrs.get("uid").get());
        for(String str: result){
            User user = userRepository.findByEmail(str);
            if(user == null){
                user = new User();
                user.setEmail(str);
                userRepository.save(user);
            }
        }
    }


}