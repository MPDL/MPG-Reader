package de.mpg.mpdl.reader.service.impl;

import de.mpg.mpdl.reader.common.BasePageRequest;
import de.mpg.mpdl.reader.common.PageUtils;
import de.mpg.mpdl.reader.model.EBook;
import de.mpg.mpdl.reader.repository.EBookRepository;
import de.mpg.mpdl.reader.service.IEBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author shidenghui@gmail.com
 * @date 2020/11/9
 */
@Service
public class EBookServiceImpl implements IEBookService {
    @Autowired
    private EBookRepository eBookRepository;

    @Override
    @Transactional
    public EBook notifyDownloads(String bookId, String sn) {
        EBook eBook = eBookRepository.findByBookId(bookId);
        if(eBook == null){
            eBook = new EBook(bookId);
        }
        if(!eBook.getDownloadedBySn().contains(sn)) {
            eBook.setDownloads(eBook.getDownloads() + 1);
            eBook.getDownloadedBySn().add(sn);
        }
        eBookRepository.save(eBook);
        return eBook;
    }

    @Override
    public Page<EBook> getTopDownloadsBooks(BasePageRequest page) {
        Pageable pageable = PageUtils.createPageable(page.getPageNumber(), page.getPageSize(), Sort.Direction.DESC);
        return eBookRepository.findAllByOrderByDownloads(pageable);
    }

    @Override
    public Page<EBook> getTopRatedBooks(BasePageRequest page) {
        Pageable pageable = PageUtils.createPageable(page.getPageNumber(), page.getPageSize(), Sort.Direction.DESC);
        return eBookRepository.findAllByOrderByScore(pageable);
    }

    @Override
    public EBook getByBookId(String bookId) {
        return eBookRepository.findByBookId(bookId);
    }
}
