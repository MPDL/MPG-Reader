package de.mpg.mpdl.reader.repository;

import de.mpg.mpdl.reader.model.Bookshelf;
import org.springframework.stereotype.Repository;

/**
 * @author denghui.shi@safetytaxfree.com
 * @date 3/22/21
 * @desc
 */
@Repository
public interface BookshelfRepository extends BaseRepository<Bookshelf, Long> {
    Bookshelf findBySn(String sn);
}
