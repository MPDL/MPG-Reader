package de.mpg.mpdl.reader.service.impl;

import de.mpg.mpdl.reader.common.BasePageRequest;
import de.mpg.mpdl.reader.common.Constants;
import de.mpg.mpdl.reader.common.GsonUtils;
import de.mpg.mpdl.reader.common.PageUtils;
import de.mpg.mpdl.reader.common.ResponseBuilder;
import de.mpg.mpdl.reader.dto.CitationRS;
import de.mpg.mpdl.reader.dto.DownloadDTO;
import de.mpg.mpdl.reader.dto.RecordDTO;
import de.mpg.mpdl.reader.dto.RecordResponseDTO;
import de.mpg.mpdl.reader.dto.SearchItem;
import de.mpg.mpdl.reader.dto.SearchResponseDTO;
import de.mpg.mpdl.reader.exception.ReaderException;
import de.mpg.mpdl.reader.model.EBook;
import de.mpg.mpdl.reader.repository.EBookRepository;
import de.mpg.mpdl.reader.service.IEBookService;
import io.micrometer.core.instrument.util.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
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
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
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

    @Value("${ebook.mock.mode}")
    private boolean mock;

    @Autowired
    private EBookRepository eBookRepository;

    @Autowired
    private RestTemplate restTemplate;


    @Override
    public List<SearchItem> searchRemoteBooks(String keyword, int pageNumber, int pageSize) {
        List<SearchItem> ret = new LinkedList<>();
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(eBookAPIUrl + "/search")
                        .queryParam("lookfor", keyword)
                        .queryParam("type", "AllFields")
                        .queryParam("filter[]", "~prodcode_str_mv:Springer")
                        .queryParam("sort", "relevance")
                        .queryParam("page", ++pageNumber+"")
                        .queryParam("limit", pageSize+"")
                        .queryParam("prettyPrint", "false")
                        .queryParam("lng", "en");
        SearchResponseDTO responseDTO = mock ? buildMockUpSearchResult():
                restTemplate.getForObject(builder.buildAndExpand().toUri(), SearchResponseDTO.class);
        if (responseDTO != null && responseDTO.getRecords() != null) {
            for (SearchItem searchItem : responseDTO.getRecords()) {
                if (!searchItem.getIsbns().isEmpty()) {
                    searchItem.setThumbnail(eBookCoverUrl + searchItem.getIsbns().get(0));
                }
                if (searchItem.getAuthorsPrimary() == null) {
                    searchItem.setAuthorsPrimary(searchItem.getAuthorsSecondary());
                }
            }
            ret = responseDTO.getRecords();
        }
        return ret;
    }

    @Override
    public RecordDTO getRemoteBookById(String bookId) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(eBookAPIUrl + "/record")
                        .queryParam("id", bookId)
                        .queryParam("prettyPrint", "false")
                        .queryParam("lng", "en")
                        .queryParam("field[]", "abstract", "authorsPrimary", "authorsSecondary", "id",
                                "isbns", "title", "urlPdf_str", "publicationDates", "publishers", "downloads", "url");
        RecordResponseDTO responseDTO = mock ? buildMockUpGetBookResult(bookId):
                restTemplate.getForObject(builder.buildAndExpand().toUri(), RecordResponseDTO.class);
        if (responseDTO != null && responseDTO.getRecords() != null) {
            for (RecordDTO record : responseDTO.getRecords()) {
                if (record.getDownloads() == null) {
                    record.setPdf(true);
                    record.setDownloadUrl("");
                } else {
                    for (DownloadDTO downloadDTO : record.getDownloads()) {
                        if ("epub".equalsIgnoreCase(downloadDTO.getDesc())) {
                            record.setDownloadUrl(downloadDTO.getUrl());
                            record.setPdf(false);
                            break;
                        } else {
                            record.setDownloadUrl(downloadDTO.getUrl());
                            record.setPdf(true);
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
            return responseDTO.getRecords().get(0);
        } else {
            throw new ReaderException(ResponseBuilder.RetCode.ERROR_400004);
        }
    }

    @Override
    @Transactional
    public EBook notifyDownloads(String bookId, String sn) {
        EBook eBook = createEBookIfNotExists(bookId);
        if (!eBook.getDownloadedBySn().contains(sn)) {
            eBook.setDownloads(eBook.getDownloads() + 1);
            eBook.getDownloadedBySn().add(sn);
        }
        eBookRepository.save(eBook);
        return eBook;
    }

    @Override
    public Page<EBook> getTopDownloadsBooks(BasePageRequest page) {
        Pageable pageable = PageUtils.createPageable(page.getPageNumber(), page.getPageSize(), Sort.Direction.DESC,
                "downloads");
        return eBookRepository.findAllByOrderByDownloadsDesc(pageable);
    }

    @Override
    public Page<EBook> getTopRatedBooks(BasePageRequest page) {
        Pageable pageable = PageUtils.createPageable(page.getPageNumber(), page.getPageSize(), Sort.Direction.DESC,
                "rating");
        return eBookRepository.findAllByOrderByRatingDesc(pageable);
    }

    @Override
    public EBook getByBookId(String bookId) {
        return eBookRepository.findByBookId(bookId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateScore(String bookId, Constants.Rating rating) {
        EBook eBook = createEBookIfNotExists(bookId);
        eBook.caculateRatingAndScore(rating);
        eBookRepository.save(eBook);
    }

    @Override
    public CitationRS fetchCitation(String bookId) throws IOException {
        HashMap<String, String> hashMap = extractCitations(bookId);
        CitationRS citationRS = new CitationRS();
        citationRS.setBookId(bookId);
        citationRS.setCitationContents(hashMap);
        return citationRS;
    }

    @Override
    @Transactional
    public EBook createEBookIfNotExists(String bookId) {
        EBook eBook = eBookRepository.findByBookId(bookId);
        if (eBook == null) {
            eBook = new EBook(bookId);
            RecordDTO recordDTO = getRemoteBookById(bookId);
            eBook.setBookName(recordDTO.getTitle());
            eBook.setBookCoverURL(recordDTO.getThumbnail());
            eBookRepository.save(eBook);
        }
        return eBook;
    }


    private HashMap<String, String> extractCitations(String bookId) {
        HashMap<String, String> hashMap = new LinkedHashMap<>(5);
        try {
            String url = "https://ebooks.mpdl.mpg.de/ebooks/Record/" + bookId + "/Cite";
            Document doc = Jsoup.connect(url).get();
            Elements citations = doc.select("#content");
            for (Element element : citations) {
                for (int i = 0; i < element.children().size(); i++) {
                    if (element.children().get(i).text().contains("APA")) {
                        hashMap.put("APA", element.children().get(i + 1).text());
                    }
                    if (element.children().get(i).text().contains("Chicago")) {
                        hashMap.put("Chicago", element.children().get(i + 1).text());
                    }
                    if (element.children().get(i).text().contains("MLA")) {
                        hashMap.put("MLA", element.children().get(i + 1).text());
                    }
                }
            }
        } catch (Exception e) {
            throw new ReaderException(ResponseBuilder.RetCode.ERROR_400003);
        }
        return hashMap;
    }

    public SearchResponseDTO buildMockUpSearchResult() {
        String response;
        try (InputStream inputStream = EBookServiceImpl.class.getResourceAsStream("/response/yoga.json")) {
            response = IOUtils.toString(inputStream);
        } catch (IOException e) {
            response = "{}";
        }
        return GsonUtils.fromJson(response, SearchResponseDTO.class);
    }

    public RecordResponseDTO buildMockUpGetBookResult(String bookId) {
        String jsonFile;
        boolean isPDF = true;
        int downloadIndex = 0;
        if ("EB000900844".equals(bookId)) {
            jsonFile = "/response/EB000900844.json";
        } else {
            jsonFile = "/response/EB000402687.json";
            isPDF = false;
            downloadIndex = 5;
        }

        String response;
        try (InputStream inputStream = EBookServiceImpl.class.getResourceAsStream(jsonFile)) {
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
        for (RecordDTO record : responseDTO.getRecords()) {
            record.setPdf(isPDF);
            record.setDownloadUrl(urls[downloadIndex]);
            if (!record.getIsbns().isEmpty()) {
                record.setThumbnail(eBookCoverUrl + record.getIsbns().get(0));
            }
            if (record.getAuthorsPrimary() == null) {
                record.setAuthorsPrimary(record.getAuthorsSecondary());
            }
        }
        return responseDTO;
    }


}
