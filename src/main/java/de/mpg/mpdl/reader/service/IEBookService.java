package de.mpg.mpdl.reader.service;

import de.mpg.mpdl.reader.common.BasePageRequest;
import de.mpg.mpdl.reader.common.Constants;
import de.mpg.mpdl.reader.dto.CitationRS;
import de.mpg.mpdl.reader.dto.RecordDTO;
import de.mpg.mpdl.reader.dto.SearchItem;
import de.mpg.mpdl.reader.model.EBook;
import org.springframework.data.domain.Page;

import java.io.IOException;
import java.util.List;

/**
 * @author shidenghui@gmail.com
 * @date 2020/11/9
 */
public interface IEBookService {
    List<SearchItem> searchRemoteBooks(String keyword);

    RecordDTO getRemoteBookById(String bookId);

    EBook createEBookIfNotExists(String bookId);

    EBook notifyDownloads(String bookId, String sn);

    Page<EBook> getTopDownloadsBooks(BasePageRequest pageRequest);

    Page<EBook> getTopRatedBooks(BasePageRequest pageRequest);

    EBook getByBookId(String bookId);

    void updateScore(String booId, Constants.Rating rating);

    CitationRS fetchCitation(String bookId) throws IOException;
}
