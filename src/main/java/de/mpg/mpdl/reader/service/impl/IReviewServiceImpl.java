package de.mpg.mpdl.reader.service.impl;

import de.mpg.mpdl.reader.common.BasePageRequest;
import de.mpg.mpdl.reader.common.Constants;
import de.mpg.mpdl.reader.common.PageUtils;
import de.mpg.mpdl.reader.common.ResponseBuilder;
import de.mpg.mpdl.reader.dto.BookReviewRQ;
import de.mpg.mpdl.reader.exception.ReaderException;
import de.mpg.mpdl.reader.model.Review;
import de.mpg.mpdl.reader.repository.ReviewRepository;
import de.mpg.mpdl.reader.service.IEBookService;
import de.mpg.mpdl.reader.service.IReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author shidenghui@gmail.com
 * @date 2020/11/17
 */
@Service
public class IReviewServiceImpl implements IReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private IEBookService bookService;

    @Override
    @Transactional
    public Review submitReview(BookReviewRQ bookReviewRQ, String sn) {
        Review review = reviewRepository.getByBookIdAndSn(bookReviewRQ.getBookId(), sn);
        if(review != null){
            throw new ReaderException(ResponseBuilder.RetCode.ERROR_400002);
        }
        review = new Review();
        review.setBookId(bookReviewRQ.getBookId());
        Optional<Constants.Rating> ratingOptional = Arrays.stream(Constants.Rating.values())
                .filter(p -> p.equals(bookReviewRQ.getRating()))
                .findFirst();
        review.setRating(ratingOptional.get().getRate());
        review.setComment(bookReviewRQ.getComment());
        review.setUserName(bookReviewRQ.getName());
        review.setSn(sn);
        if(bookReviewRQ.getShowOrg()) {
            //TODO how to fetch org, ldap?
            //ou=Max Planck Digital Library,ou=MPG
            review.setOrganization("TODO");
        }
        reviewRepository.save(review);
        bookService.updateScore(bookReviewRQ.getBookId(), bookReviewRQ.getRating());
        return review;
    }

    @Override
    public Page<Review> getReviews(String bookId, BasePageRequest page) {
        Pageable pageable = PageUtils.createPageable(page);
        return reviewRepository.getAllByBookId(bookId, pageable);
    }
}
