package de.mpg.mpdl.reader.controller;

import de.mpg.mpdl.reader.common.BasePageRequest;
import de.mpg.mpdl.reader.common.BaseResponseDTO;
import de.mpg.mpdl.reader.common.BeanUtils;
import de.mpg.mpdl.reader.common.PageUtils;
import de.mpg.mpdl.reader.common.ResponseBuilder;
import de.mpg.mpdl.reader.dto.BookReviewRQ;
import de.mpg.mpdl.reader.dto.ReadingListRemoveRQ;
import de.mpg.mpdl.reader.dto.ReadingListRes;
import de.mpg.mpdl.reader.dto.ReviewRes;
import de.mpg.mpdl.reader.model.ReadingList;
import de.mpg.mpdl.reader.model.Review;
import de.mpg.mpdl.reader.service.IReadingListService;
import de.mpg.mpdl.reader.service.impl.IReviewServiceImpl;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author shidenghui@gmail.com
 * @date 2020/11/2
 */
@RestController
@RequestMapping("/rest/user")
public class UserController {
    private final IReadingListService readingListService;
    private final IReviewServiceImpl reviewService;

    public UserController(IReadingListService readingListService, IReviewServiceImpl reviewService) {
        this.readingListService = readingListService;
        this.reviewService = reviewService;
    }

    /**
     * Add a book into reading list
     */
    @PostMapping(value = "/readinglist/add/{bookId}")
    public BaseResponseDTO<ReadingListRes> addIntoReadingList(@RequestHeader(name = "X-SN") String sn,
                                                              @PathVariable String bookId) {
        ReadingList readingList = readingListService.addBookingIntoReadingList(bookId, sn);
        ReadingListRes readingListRes = BeanUtils.convertObject(readingList, ReadingListRes.class);
        return ResponseBuilder.buildSuccess(readingListRes);
    }

    /**
     * Remove a book from reading list
     */
    @PostMapping(value = "/readinglist/delete")
    public BaseResponseDTO<ReadingListRes> removeFromReadingList(@RequestHeader(name = "X-SN") String sn,
                                                                 @Validated @RequestBody ReadingListRemoveRQ removeRQ)
            throws Exception {
        ReadingList readingList = readingListService.removeFromReadingList(removeRQ, sn);
        ReadingListRes readingListRes =  BeanUtils.convertObject(readingList, ReadingListRes.class);
        return ResponseBuilder.buildSuccess(readingListRes);
    }


    /**
     * My Reading List (no shown if reading list is empty):
     * Display the books in My Reading List. (This column will not be shown if there is no content in it.)
     */
    @GetMapping(value = "/readinglist")
    public BaseResponseDTO<ReadingListRes> getReadingList(@RequestHeader(name = "X-SN") String sn,
                                                          @Validated @RequestBody BasePageRequest pageRequest) {
        ReadingList readingList = readingListService.getReadingBySn(sn);
        //TODO fetch books and paging
        ReadingListRes readingListRes = BeanUtils.convertObject(readingList, ReadingListRes.class);
        return ResponseBuilder.buildSuccess(readingListRes);
    }

    /**
     * Write a review for a book
     * Click the button to create review of the book:
     * - Tap the star button to give the book an overall rating. (5 stars in total)
     * - An input filed for writing comment. (optional)
     * - An input field in which the user can leave her/his name if she/he wants.(optional)
     * - A drop-down menu for selecting the organization.(optional)
     * - The Submit button.
     */
    @PostMapping(value = "/review")
    public BaseResponseDTO<ReviewRes> submitReview(@RequestHeader(name = "X-SN") String sn,
                                                   @Validated @RequestBody BookReviewRQ bookReviewRQ) {
        Review review = reviewService.submitReview(bookReviewRQ, sn);
        ReviewRes reviewRes = BeanUtils.convertObject(review, ReviewRes.class);
        return ResponseBuilder.buildSuccess(reviewRes);
    }



    @PostMapping(value = "/{bookId}/reviews")
    public BaseResponseDTO<Page<ReviewRes>> getBookReviewList(@PathVariable String bookId,
                                                              @Validated @RequestBody BasePageRequest pageRequest) {
        Page<Review> reviews = reviewService.getReviews(bookId, pageRequest);
        return ResponseBuilder.buildSuccess(PageUtils.adapterPage(reviews, ReviewRes.class));

    }
}
