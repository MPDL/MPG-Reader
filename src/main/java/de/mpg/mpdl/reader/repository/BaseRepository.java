package de.mpg.mpdl.reader.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

/**
 * @author shidenghui@gmail.com
 * @date 2020/11/17
 */
@NoRepositoryBean
public interface BaseRepository <T, ID extends Serializable> extends CrudRepository<T, ID> {

}
