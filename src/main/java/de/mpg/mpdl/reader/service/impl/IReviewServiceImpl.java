package de.mpg.mpdl.reader.service.impl;

import de.mpg.mpdl.reader.common.BasePageRequest;
import de.mpg.mpdl.reader.common.PageUtils;
import de.mpg.mpdl.reader.dto.BookReviewRQ;
import de.mpg.mpdl.reader.model.Review;
import de.mpg.mpdl.reader.repository.ReviewRepository;
import de.mpg.mpdl.reader.service.IReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author shidenghui@gmail.com
 * @date 2020/11/17
 */
@Service
public class IReviewServiceImpl implements IReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Override
    @Transactional
    public Review submitReview(BookReviewRQ bookReviewRQ, String sn) {
        Review review = new Review();
        review.setBookId(bookReviewRQ.getBooId());
        review.setRating(bookReviewRQ.getRating());
        review.setComment(bookReviewRQ.getComment());
        review.setUserName(bookReviewRQ.getName());
        review.setSn(sn);
        if(bookReviewRQ.getShowOrg()) {
            //TODO how to fetch org, ldap?
            //ou=Max Planck Digital Library,ou=MPG
            review.setOrganization("TODO");
        }
        reviewRepository.save(review);
        return review;
    }

    @Override
    public Page<Review> getReviews(String bookId, BasePageRequest page) {
        Pageable pageable = PageUtils.createPageable(page);
        return reviewRepository.getAllByBookId(bookId, pageable);
    }
}
