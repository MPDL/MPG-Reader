package de.mpg.mpdl.reader.controller;

import de.mpg.mpdl.reader.common.BasePageRequest;
import de.mpg.mpdl.reader.common.BaseResponseDTO;
import de.mpg.mpdl.reader.common.BeanUtils;
import de.mpg.mpdl.reader.common.PageUtils;
import de.mpg.mpdl.reader.common.ResponseBuilder;
import de.mpg.mpdl.reader.dto.BookReviewRQ;
import de.mpg.mpdl.reader.dto.EBookStatisticRes;
import de.mpg.mpdl.reader.dto.ReadingListRemoveRQ;
import de.mpg.mpdl.reader.dto.ReadingListRes;
import de.mpg.mpdl.reader.dto.ReviewRes;
import de.mpg.mpdl.reader.model.EBook;
import de.mpg.mpdl.reader.model.ReadingList;
import de.mpg.mpdl.reader.model.Review;
import de.mpg.mpdl.reader.repository.EBookRepository;
import de.mpg.mpdl.reader.service.IReadingListService;
import de.mpg.mpdl.reader.service.impl.IReviewServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
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
    private final EBookRepository eBookRepository;

    @Autowired
    public UserController(IReadingListService readingListService, IReviewServiceImpl reviewService,
                          EBookRepository eBookRepository) {
        this.readingListService = readingListService;
        this.reviewService = reviewService;
        this.eBookRepository = eBookRepository;
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
                                                                 @Validated @RequestBody ReadingListRemoveRQ removeRQ) {
        ReadingList readingList = readingListService.removeFromReadingList(removeRQ, sn);
        ReadingListRes readingListRes =  BeanUtils.convertObject(readingList, ReadingListRes.class);
        return ResponseBuilder.buildSuccess(readingListRes);
    }


    /**
     * My Reading List (no shown if reading list is empty):
     * Display the books in My Reading List. (This column will not be shown if there is no content in it.)
     */
    @PostMapping(value = "/readinglist")
    public BaseResponseDTO<Page<EBookStatisticRes>> getReadingList(@RequestHeader(name = "X-SN") String sn,
                                                      @Validated @RequestBody(required = false) BasePageRequest page) {
        ReadingList readingList = readingListService.getReadingBySn(sn);
        if(readingList != null && readingList.getBookIds().size() > 0) {
            Pageable pageable = PageUtils.createPageable(page);
            Page<EBook> bookPage = eBookRepository.findAllByBookIdIn(readingList.getBookIds(), pageable);
            return ResponseBuilder.buildSuccess(PageUtils.adapterPage(bookPage, EBookStatisticRes.class));
        }
        return ResponseBuilder.buildSuccess();
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
                                                   @RequestHeader(name = "X-Email") String email,
                                                   @Validated @RequestBody BookReviewRQ bookReviewRQ) {
        Review review = reviewService.submitReview(bookReviewRQ, sn, email);
        ReviewRes reviewRes = BeanUtils.convertObject(review, ReviewRes.class);
        return ResponseBuilder.buildSuccess(reviewRes);
    }
}
