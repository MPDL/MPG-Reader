package de.mpg.mpdl.reader.service;

import de.mpg.mpdl.reader.common.BasePageRequest;
import de.mpg.mpdl.reader.dto.BookReviewRQ;
import de.mpg.mpdl.reader.model.Review;
import org.springframework.data.domain.Page;

/**
 * @author shidenghui@gmail.com
 * @date 2020/11/9
 */
public interface IReviewService {
    Review submitReview(BookReviewRQ bookReviewRQ);
    Page<Review> getReviews(String bookId, BasePageRequest page);
}
