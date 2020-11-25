package de.mpg.mpdl.reader.controller;

import de.mpg.mpdl.reader.common.BaseResponseDTO;
import de.mpg.mpdl.reader.common.ResponseBuilder;
import de.mpg.mpdl.reader.dto.RecordDTO;
import de.mpg.mpdl.reader.dto.SearchItem;
import de.mpg.mpdl.reader.service.IEBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class EBooksController {

    @Autowired
    private IEBookService bookService;

    @GetMapping(value = "/search")
    public BaseResponseDTO<List<SearchItem>> searchEBooks(final String keyword) {
        List<SearchItem> searchItems = bookService.searchRemoteBooks(keyword);
        return ResponseBuilder.buildSuccess(searchItems);
    }

    @GetMapping(value = "/record")
    public BaseResponseDTO<RecordDTO> getRecordById(final String bookId) {
        RecordDTO recordDTO = bookService.getRemoteBookById(bookId);
        return ResponseBuilder.buildSuccess(recordDTO);
    }

}