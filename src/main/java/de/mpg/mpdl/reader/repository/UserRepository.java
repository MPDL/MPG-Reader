package de.mpg.mpdl.reader.repository;

import de.mpg.mpdl.reader.model.User;
import org.springframework.stereotype.Repository;

/**
 * @author shidenghui@gmail.com
 * @date 2020/11/9
 */
@Repository
public interface UserRepository extends BaseRepository<User, Long> {
    User findByEmail(String email);
}
