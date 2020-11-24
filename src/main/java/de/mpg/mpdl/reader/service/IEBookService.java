package de.mpg.mpdl.reader.service;

import de.mpg.mpdl.reader.common.BasePageRequest;
import de.mpg.mpdl.reader.model.EBook;
import org.springframework.data.domain.Page;

/**
 * @author shidenghui@gmail.com
 * @date 2020/11/9
 */
public interface IEBookService {
    EBook notifyDownloads(String bookId, String sn);

    Page<EBook> getTopDownloadsBooks(BasePageRequest pageRequest);

    Page<EBook> getTopRatedBooks(BasePageRequest pageRequest);

    EBook getByBookId(String bookId);
}
