package de.mpg.mpdl.reader.service.impl;

import de.mpg.mpdl.reader.common.ResponseBuilder;
import de.mpg.mpdl.reader.exception.ReaderException;
import de.mpg.mpdl.reader.model.ReadingList;
import de.mpg.mpdl.reader.repository.ReadingListRepository;
import de.mpg.mpdl.reader.service.IReadingListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author shidenghui@gmail.com
 * @date 2020/11/9
 */
@Service
public class ReadingListServiceImpl implements IReadingListService {

    @Autowired
    private ReadingListRepository readingListRepository;

    @Override
    @Transactional
    public ReadingList addBookingIntoReadingList(String booId, String email) {
        ReadingList readingList = readingListRepository.getByEmail(email);
        if(readingList == null){
            readingList = new ReadingList(email);
        }
        readingList.getBookIds().add(booId);
        readingListRepository.save(readingList);
        return readingList;
    }

    @Override
    public ReadingList getReadingByUser(String email) {
        return readingListRepository.getByEmail(email);
    }

    @Override
    @Transactional
    public ReadingList removeFromReadingList(String bookId, String email) throws Exception {
        ReadingList readingList = readingListRepository.getByEmail(email);
        if(readingList == null || readingList.getBookIds().isEmpty() || !readingList.getBookIds().contains(bookId)){
            throw new ReaderException(ResponseBuilder.RetCode.ERROR_400000);
        }
        readingList.getBookIds().remove(bookId);
        readingListRepository.save(readingList);
        return readingList;
    }
}
