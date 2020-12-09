package de.mpg.mpdl.reader.controller;

import de.mpg.mpdl.reader.common.BasePageRequest;
import de.mpg.mpdl.reader.common.BaseResponseDTO;
import de.mpg.mpdl.reader.common.BeanUtils;
import de.mpg.mpdl.reader.common.PageUtils;
import de.mpg.mpdl.reader.common.ResponseBuilder;
import de.mpg.mpdl.reader.dto.CitationRS;
import de.mpg.mpdl.reader.dto.EBookStatisticRes;
import de.mpg.mpdl.reader.dto.RecordDTO;
import de.mpg.mpdl.reader.dto.ReviewRes;
import de.mpg.mpdl.reader.dto.SearchItem;
import de.mpg.mpdl.reader.model.EBook;
import de.mpg.mpdl.reader.model.ReadingList;
import de.mpg.mpdl.reader.model.Review;
import de.mpg.mpdl.reader.repository.ReviewRepository;
import de.mpg.mpdl.reader.service.IEBookService;
import de.mpg.mpdl.reader.service.IReadingListService;
import de.mpg.mpdl.reader.service.IReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/rest/ebook")
public class EBooksController {
    @Autowired
    private IEBookService eBookService;

    @Autowired
    private IReviewService reviewService;

    @Autowired
    private IReadingListService readingListService;

    @Autowired
    private ReviewRepository reviewRepository;

    @GetMapping(value = "/search")
    public BaseResponseDTO<List<SearchItem>> searchEBooks(String keyword) {
        List<SearchItem> searchItems = eBookService.searchRemoteBooks(keyword);
        for(SearchItem searchItem: searchItems){
            EBook eBook = eBookService.getByBookId(searchItem.getId());
            if(eBook != null) {
                searchItem.setRating(eBook.getRating());
            }
        }
        return ResponseBuilder.buildSuccess(searchItems);
    }

    @GetMapping(value = "/record")
    public BaseResponseDTO<RecordDTO> getRecordById(String bookId) {
        RecordDTO recordDTO = eBookService.getRemoteBookById(bookId);
        eBookService.createEBookIfNotExists(bookId);
        return ResponseBuilder.buildSuccess(recordDTO);
    }

    /**
     * Info-Page of the Book:
     *  The average rating of the book and the amount of the reviews
     *  The rating given by the user
     */
    @GetMapping(value = "/{bookId}")
    public BaseResponseDTO<EBookStatisticRes> getBookStatistics(@RequestHeader(name = "X-SN") String sn,
                                                                @PathVariable String bookId) {
        EBook eBook = eBookService.getByBookId(bookId);
        EBookStatisticRes eBookStatisticRes = BeanUtils.convertObject(eBook, EBookStatisticRes.class);
        ReadingList readingList = readingListService.getReadingBySn(sn);
        if(readingList != null && readingList.getBookIds().contains(bookId)) {
            eBookStatisticRes.setInReadingList(true);
        }
        Review review = reviewRepository.getByBookIdAndSn(bookId, sn);
        if(review != null){
            eBookStatisticRes.setReviewedByMe(true);
        }
        return ResponseBuilder.buildSuccess(eBookStatisticRes);
    }

    @PostMapping(value = "/{bookId}/reviews")
    public BaseResponseDTO<Page<ReviewRes>> getBookReviewList(@PathVariable String bookId,
                                              @Validated @RequestBody(required = false) BasePageRequest pageRequest) {
        Page<Review> reviews = reviewService.getReviews(bookId, pageRequest);
        return ResponseBuilder.buildSuccess(PageUtils.adapterPage(reviews, ReviewRes.class));
    }

    /**
     * https://ebooks.mpdl.mpg.de/ebooks/Record/EB000402687/Cite
     *
     * Styles of Citation (Guidelines will be delivered separately):
     * APA Citation:
     * Hauser, B. (2013). Yoga Traveling: Bodily Practice in Transcultural Perspective (1st ed. 2013.).
     * Cham: Springer International Publishing.
     *
     * Chicago Style Citation:
     * Hauser, Beatrix. Yoga Traveling: Bodily Practice in Transcultural Perspective. 1st ed. 2013. Cham: Springer
     * International Publishing, 2013.
     *
     * MLA Citation
     * Hauser, Beatrix. Yoga Traveling: Bodily Practice in Transcultural Perspective. 1st ed. 2013. Cham: Springer
     * International Publishing, 2013.
     */
    @GetMapping(value = "/{bookId}/citations")
    public BaseResponseDTO<CitationRS> getCitation(@PathVariable String bookId) throws IOException {
        CitationRS citationRS = eBookService.fetchCitation(bookId);
        return ResponseBuilder.buildSuccess(citationRS);
    }
}