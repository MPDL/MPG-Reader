package de.mpg.mpdl.reader.repository;

import de.mpg.mpdl.reader.model.EBook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Set;

/**
 * @author shidenghui@gmail.com
 * @date 2020/11/17
 */
@Repository
public interface EBookRepository extends BaseRepository<EBook, Long> {
    EBook findByBookId(String BookId);

    Page<EBook> findAllByBookIdIn(Set<String> bookIds, Pageable pageable);

    Page<EBook> findAllByOrderByDownloads(Pageable pageable);

    Page<EBook> findAllByOrderByRating(Pageable pageable);
}
