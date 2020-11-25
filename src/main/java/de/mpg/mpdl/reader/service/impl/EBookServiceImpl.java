package de.mpg.mpdl.reader.service.impl;

import de.mpg.mpdl.reader.common.BasePageRequest;
import de.mpg.mpdl.reader.common.Constants;
import de.mpg.mpdl.reader.common.GsonUtils;
import de.mpg.mpdl.reader.common.PageUtils;
import de.mpg.mpdl.reader.dto.RecordDTO;
import de.mpg.mpdl.reader.dto.RecordResponseDTO;
import de.mpg.mpdl.reader.dto.SearchItem;
import de.mpg.mpdl.reader.dto.SearchResponseDTO;
import de.mpg.mpdl.reader.model.EBook;
import de.mpg.mpdl.reader.repository.EBookRepository;
import de.mpg.mpdl.reader.service.IEBookService;
import io.micrometer.core.instrument.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @author shidenghui@gmail.com
 * @date 2020/11/9
 */
@Service
public class EBookServiceImpl implements IEBookService {

    @Value("${ebook.api.base.url}")
    private String eBookAPIUrl;

    @Value("${ebook.cover.base.url}")
    private String eBookCoverUrl;

    @Autowired
    private EBookRepository eBookRepository;

    @Autowired
    private RestTemplate restTemplate;


    @Override
    public List<SearchItem> searchRemoteBooks(String keyword) {
        UriComponentsBuilder builder =
                UriComponentsBuilder.fromUriString(eBookAPIUrl + "/search")
                        .queryParam("lookfor", keyword)
                        .queryParam("type", "AllFields")
                        .queryParam("filter[]", "~prodcode_str_mv:Springer")
                        .queryParam("sort", "relevance")
                        .queryParam("page", "1")
                        .queryParam("limit", "20")
                        .queryParam("prettyPrint", "false")
                        .queryParam("lng", "en");
//        SearchResponseDTO responseDTO = restTemplate.getForObject(builder.buildAndExpand().toUri(), SearchResponseDTO.class);
        String response;
        try (InputStream inputStream = EBookServiceImpl.class.getResourceAsStream("/response/yoga.json")) {
            response = IOUtils.toString(inputStream);
        } catch (IOException e) {
            response = "{}";
        }
        SearchResponseDTO responseDTO = GsonUtils.fromJson(response, SearchResponseDTO.class);

        for (SearchItem searchItem : responseDTO.getRecords()) {
            if (!searchItem.getIsbns().isEmpty()) {
                searchItem.setThumbnail(eBookCoverUrl + searchItem.getIsbns().get(0));
            }
            if (searchItem.getAuthorsPrimary() == null) {
                searchItem.setAuthorsPrimary(searchItem.getAuthorsSecondary());
            }
        }
        return responseDTO.getRecords();
    }


    @Override
    public RecordDTO getRemoteBookById(String bookId) {
        UriComponentsBuilder builder =
                UriComponentsBuilder.fromUriString(eBookAPIUrl + "/record")
                        .queryParam("id", bookId)
                        .queryParam("prettyPrint", "false")
                        .queryParam("lng", "en")
                        .queryParam("field[]", "abstract", "authorsPrimary", "authorsSecondary", "id", "isbns",
                                "title", "urlPdf_str", "publicationDates", "publishers", "downloads");

        /**
        RecordResponseDTO responseDTO = restTemplate.getForObject(builder.buildAndExpand().toUri(), RecordResponseDTO.class);
        if (responseDTO != null && responseDTO.getRecords() != null) {
            for (RecordDTO record : responseDTO.getRecords()) {
                if (record.getDownloads() == null) {
                    record.setIsPdf(true);
                    record.setDownloadUrl("");
                } else {
                    for (DownloadDTO downloadDTO : record.getDownloads()) {
                        if (downloadDTO.getDesc().equalsIgnoreCase("epub")) {
                            record.setDownloadUrl(downloadDTO.getUrl());
                            record.setIsPdf(false);
                            break;
                        } else {
                            record.setDownloadUrl(downloadDTO.getUrl());
                            //todo: reverse
                            record.setIsPdf(true);
                        }
                    }
                }
                if (!record.getIsbns().isEmpty()) {
                    record.setThumbnail(eBookCoverUrl + record.getIsbns().get(0));
                }
                if (record.getAuthorsPrimary() == null) {
                    record.setAuthorsPrimary(record.getAuthorsSecondary());
                }
            }
        }
         */

        //todo: remove dummy data
        String response;
        try (InputStream inputStream = EBookServiceImpl.class.getResourceAsStream("/response/EB000900844.json")) {
            response = IOUtils.toString(inputStream);
        } catch (IOException e) {
            response = "{}";
        }
        RecordResponseDTO responseDTO = GsonUtils.fromJson(response, RecordResponseDTO.class);
        String[] urls = new String[]{"https://keeper.mpdl.mpg.de/f/6cd11bdbe4894c4c85f8/?dl=1",
                "https://keeper.mpdl.mpg.de/f/7524353b2721433fae88/?dl=1",
                "https://keeper.mpdl.mpg.de/f/ce025a1d52b740e6aa11/?dl=1",
                "https://keeper.mpdl.mpg.de/f/723cd4a7d8884ddd9d04/?dl=1",
                "https://keeper.mpdl.mpg.de/f/1e0c737e4b164a829dc3/?dl=1",
                "https://keeper.mpdl.mpg.de/f/0ebd4edad15142d0b803/?dl=1",
                "https://keeper.mpdl.mpg.de/f/f672dffa8dc34c95842a/?dl=1",
                "https://keeper.mpdl.mpg.de/f/d14ff8ba4a3e4cda81e2/?dl=1"
        };
        boolean[] formats = new boolean[]{true, false};
        for (RecordDTO record : responseDTO.getRecords()) {
            int format = record.getTitle().length() % 2;
            int index = record.getTitle().length() % 8;
            record.setIsPdf(formats[format]);
            record.setDownloadUrl(urls[index]);
            if (!record.getIsbns().isEmpty()) {
                record.setThumbnail(eBookCoverUrl + record.getIsbns().get(0));
            }
            if (record.getAuthorsPrimary() == null) {
                record.setAuthorsPrimary(record.getAuthorsSecondary());
            }
        }
        //todo: remove dummy data
        return responseDTO.getRecords().get(0);
    }

    @Override
    @Transactional
    public EBook notifyDownloads(String bookId, String sn) {
        EBook eBook = eBookRepository.findByBookId(bookId);
        if (eBook == null) {
            eBook = new EBook(bookId);
        }
        if (!eBook.getDownloadedBySn().contains(sn)) {
            eBook.setDownloads(eBook.getDownloads() + 1);
            eBook.getDownloadedBySn().add(sn);
        }
        eBookRepository.save(eBook);
        return eBook;
    }

    @Override
    public Page<EBook> getTopDownloadsBooks(BasePageRequest page) {
        Pageable pageable = PageUtils.createPageable(page.getPageNumber(), page.getPageSize(), Sort.Direction.DESC, "downloads");
        return eBookRepository.findAllByOrderByDownloads(pageable);
    }

    @Override
    public Page<EBook> getTopRatedBooks(BasePageRequest page) {
        Pageable pageable = PageUtils.createPageable(page.getPageNumber(), page.getPageSize(), Sort.Direction.DESC, "rating");
        return eBookRepository.findAllByOrderByRating(pageable);
    }

    @Override
    public EBook getByBookId(String bookId) {
        return eBookRepository.findByBookId(bookId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateScore(String bookId, Constants.Rating rating) {
        EBook eBook = eBookRepository.findByBookId(bookId);
        if (eBook == null) {
            eBook = new EBook(bookId);
        }
        eBook.caculateRatingAndScore(rating);
        eBookRepository.save(eBook);
    }
}
