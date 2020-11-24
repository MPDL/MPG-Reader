package de.mpg.mpdl.reader.repository;

import de.mpg.mpdl.reader.model.ReadingList;
import org.springframework.stereotype.Repository;

/**
 * @author shidenghui@gmail.com
 * @date 2020/11/17
 */
@Repository
public interface ReadingListRepository extends BaseRepository<ReadingList, Long> {
    ReadingList getBySn(String sn);
}
