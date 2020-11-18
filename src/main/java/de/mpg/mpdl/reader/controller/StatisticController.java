package de.mpg.mpdl.reader.controller;

import de.mpg.mpdl.reader.common.BasePageRequest;
import de.mpg.mpdl.reader.common.BaseResponseDTO;
import de.mpg.mpdl.reader.common.BeanUtils;
import de.mpg.mpdl.reader.common.PageUtils;
import de.mpg.mpdl.reader.common.ResponseBuilder;
import de.mpg.mpdl.reader.dto.EBookRes;
import de.mpg.mpdl.reader.model.EBook;
import de.mpg.mpdl.reader.service.IEBookService;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author shidenghui@gmail.com
 * @date 2020/11/17
 */
@RestController
@RequestMapping("/rest/statistic")
public class StatisticController {
    private final IEBookService eBookService;

    public StatisticController(IEBookService eBookService) {
        this.eBookService = eBookService;
    }

    /**
     * inform download action
     */
    @PostMapping(value = "/downloaded/{bookId}")
    public BaseResponseDTO<EBookRes> downloadNotification(@PathVariable String bookId) {
        EBook eBook = eBookService.notifyDownloads(bookId);
        EBookRes eBookRes = BeanUtils.convertObject(eBook, EBookRes.class);
        return ResponseBuilder.buildSuccess(eBookRes);
    }

    /**
     * Most Downloaded Books*:
     * Display the most downloaded books based on {DeviceSerialNumber}.(Load on demand)
     */
    @GetMapping(value = "/topDownloads")
    public BaseResponseDTO<Page<EBookRes>> getByDownloads(@Validated @RequestBody BasePageRequest pageRequest) {
        Page<EBook> eBookPage = eBookService.getTopDownloadsBooks(pageRequest);
        return ResponseBuilder.buildSuccess(PageUtils.adapterPage(eBookPage, EBookRes.class));
    }

    /**
     * Top Rated Books*:
     * Display the top books rated by users. (Load on demand)
     */
    @GetMapping(value = "/topScores")
    public BaseResponseDTO<Page<EBookRes>> getByScores(@Validated @RequestBody BasePageRequest pageRequest) {
        Page<EBook> eBookPage = eBookService.getTopRatedBooks(pageRequest);
        return ResponseBuilder.buildSuccess(PageUtils.adapterPage(eBookPage, EBookRes.class));
    }

    /**
     * Info-Page of the Book:
     *  The average rating of the book and the amount of the reviews
     *  The rating given by the user
     */
    @GetMapping(value = "/{bookId}")
    public BaseResponseDTO<EBookRes> getBookStatistics(@PathVariable String bookId) {
        EBook eBook = eBookService.getByBookId(bookId);
        EBookRes eBookRes = BeanUtils.convertObject(eBook, EBookRes.class);
        return ResponseBuilder.buildSuccess(eBookRes);
    }
}
