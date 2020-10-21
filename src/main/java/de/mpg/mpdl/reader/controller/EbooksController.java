package de.mpg.mpdl.reader.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import de.mpg.mpdl.reader.dto.RecordResponseDTO;
import de.mpg.mpdl.reader.dto.SearchItem;
import de.mpg.mpdl.reader.dto.SearchResponseDTO;
import de.mpg.mpdl.reader.dto.RecordDTO;
import de.mpg.mpdl.reader.dto.DownloadDTO;

import java.util.Arrays;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@RestController
public class EbooksController {

  @Autowired
  RestTemplate restTemplate;

  String thumbnailPrefix = "https://ebooks4-qa.mpdl.mpg.de/ebooks/Cover/Show?size=small&isbn=";

  @RequestMapping(value = "/search", method = RequestMethod.GET)
  public SearchResponseDTO searchEbooks(final String lookfor) {
    
    final HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
    final HttpEntity <String> entity = new HttpEntity<String>(headers);
    String url = "https://ebooks4-qa.mpdl.mpg.de/ebooks/api/v1/search";
    UriComponentsBuilder builder=  
      UriComponentsBuilder.fromUriString(url)
                          .queryParam("lookfor", lookfor)
                          .queryParam("type", "AllFields")
                          .queryParam("filter[]", "~prodcode_str_mv:Springer")
                          .queryParam("sort", "relevance")
                          .queryParam("page", "1")
                          .queryParam("limit", "20")
                          .queryParam("prettyPrint", "false")
                          .queryParam("lng", "en");
    
    // RestTemplate restTemplate1 = new RestTemplate(new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory()));
    // ArrayList<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
    // interceptors.add(new LoggingRequestInterceptor());
    // restTemplate1.setInterceptors(interceptors);

    final ResponseEntity<String> resp = restTemplate.exchange(builder.buildAndExpand().toUri(), HttpMethod.GET, entity, String.class);
    final Gson gson = new GsonBuilder().create();
    final SearchResponseDTO responseDTO = gson.fromJson(resp.getBody(), SearchResponseDTO.class);
    for (SearchItem searchItem: responseDTO.getRecords()) {
      if (!searchItem.getIsbns().isEmpty()) {
        searchItem.setThumbnail(thumbnailPrefix + searchItem.getIsbns().get(0));
      }
      if (searchItem.getAuthorsPrimary() == null) {
        searchItem.setAuthorsPrimary(searchItem.getAuthorsSecondary());
      }
    }
    return responseDTO;
  }

  @RequestMapping(value = "/record", method = RequestMethod.GET)
  public RecordResponseDTO getRecordById(final String id) {
    
    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
      HttpEntity <String> entity = new HttpEntity<String>(headers);
      String url = "https://ebooks4-qa.mpdl.mpg.de/ebooks/api/v1/record";
      UriComponentsBuilder builder=  
        UriComponentsBuilder.fromUriString(url)
                            .queryParam("id", id)
                            .queryParam("prettyPrint", "false")
                            .queryParam("lng", "en")
                            .queryParam("field[]", "abstract", "authorsPrimary", "authorsSecondary", "id", "isbns", "title", "urlPdf_str", "publicationDates", "publishers", "downloads");
      ResponseEntity<String> resp = restTemplate.exchange(builder.buildAndExpand().toUri(), HttpMethod.GET, entity, String.class);
      Gson gson = new GsonBuilder().create();
      RecordResponseDTO responseDTO = gson.fromJson(resp.getBody(), RecordResponseDTO.class);
      
      //todo: remove dummy data
      String[] urls = new String[] {"https://keeper.mpdl.mpg.de/f/6cd11bdbe4894c4c85f8/?dl=1", 
                                    "https://keeper.mpdl.mpg.de/f/7524353b2721433fae88/?dl=1",
                                    "https://keeper.mpdl.mpg.de/f/ce025a1d52b740e6aa11/?dl=1",
                                    "https://keeper.mpdl.mpg.de/f/723cd4a7d8884ddd9d04/?dl=1",
                                    "https://keeper.mpdl.mpg.de/f/1e0c737e4b164a829dc3/?dl=1",
                                    "https://keeper.mpdl.mpg.de/f/0ebd4edad15142d0b803/?dl=1",
                                    "https://keeper.mpdl.mpg.de/f/f672dffa8dc34c95842a/?dl=1",
                                    "https://keeper.mpdl.mpg.de/f/d14ff8ba4a3e4cda81e2/?dl=1"
                                  };
      //todo: remove dummy data
      boolean[] formats = new boolean[] {true, false};
      for (RecordDTO record: responseDTO.getRecords()) {
        int format = record.getTitle().length() % 2;
        int index = record.getTitle().length() % 8;
        record.setIsPdf(formats[format]);
        record.setDownloadUrl(urls[index]);
        if (!record.getIsbns().isEmpty()) {
          record.setThumbnail(thumbnailPrefix + record.getIsbns().get(0));
        }
        if (record.getAuthorsPrimary() == null) {
          record.setAuthorsPrimary(record.getAuthorsSecondary());
        }
      }
      
      /**
      for (RecordDTO record: responseDTO.getRecords()) {
        if (record.getDownloads() == null) {
          record.setIsPdf(true);
          record.setDownloadUrl("");
        } else {
          for (DownloadDTO downloadDTO: record.getDownloads()) {
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
          record.setThumbnail(thumbnailPrefix + record.getIsbns().get(0));
        }
        if (record.getAuthorsPrimary() == null) {
          record.setAuthorsPrimary(record.getAuthorsSecondary());
        }
      }
      */
      return responseDTO;
  }

}