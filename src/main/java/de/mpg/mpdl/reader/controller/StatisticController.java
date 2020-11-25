package de.mpg.mpdl.reader.controller;

import de.mpg.mpdl.reader.common.BasePageRequest;
import de.mpg.mpdl.reader.common.BaseResponseDTO;
import de.mpg.mpdl.reader.common.BeanUtils;
import de.mpg.mpdl.reader.common.PageUtils;
import de.mpg.mpdl.reader.common.ResponseBuilder;
import de.mpg.mpdl.reader.dto.EBookStatisticRes;
import de.mpg.mpdl.reader.model.EBook;
import de.mpg.mpdl.reader.service.IEBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author shidenghui@gmail.com
 * @date 2020/11/17
 */
@RestController
@RequestMapping("/rest/statistic")
public class StatisticController {

    @Autowired
    private IEBookService eBookService;

    /**
     * inform download action
     */
    @PostMapping(value = "/downloaded/{bookId}")
    public BaseResponseDTO<EBookStatisticRes> downloadNotification(@RequestHeader(name = "X-SN") String sn,
                                                                   @PathVariable String bookId) {
        EBook eBook = eBookService.notifyDownloads(bookId, sn);
        EBookStatisticRes eBookStatisticRes = BeanUtils.convertObject(eBook, EBookStatisticRes.class);
        return ResponseBuilder.buildSuccess(eBookStatisticRes);
    }

    /**
     * Most Downloaded Books*:
     * Display the most downloaded books based on {DeviceSerialNumber}.(Load on demand)
     */
    @PostMapping(value = "/topDownloads")
    public BaseResponseDTO<Page<EBookStatisticRes>> getByDownloads(@Validated @RequestBody(required = false)
                                                                               BasePageRequest pageRequest) {
        Page<EBook> eBookPage = eBookService.getTopDownloadsBooks(pageRequest);
        return ResponseBuilder.buildSuccess(PageUtils.adapterPage(eBookPage, EBookStatisticRes.class));
    }

    /**
     * Top Rated Books*:
     * Display the top books rated by users. (Load on demand)
     */
    @PostMapping(value = "/topScores")
    public BaseResponseDTO<Page<EBookStatisticRes>> getByScores(@Validated @RequestBody(required = false)
                                                                            BasePageRequest pageRequest) {
        Page<EBook> eBookPage = eBookService.getTopRatedBooks(pageRequest);
        return ResponseBuilder.buildSuccess(PageUtils.adapterPage(eBookPage, EBookStatisticRes.class));
    }
}
