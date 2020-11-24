package de.mpg.mpdl.reader.repository;

import de.mpg.mpdl.reader.model.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

/**
 * @author shidenghui@gmail.com
 * @date 2020/11/17
 */
@Repository
public interface ReviewRepository extends BaseRepository<Review, Long> {
    Page<Review> getAllByBookId(String bookId, Pageable pageable);
    Page<Review> getAllBySn(String sn, Pageable pageable);
}
