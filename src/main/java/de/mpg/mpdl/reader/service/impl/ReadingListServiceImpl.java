package de.mpg.mpdl.reader.service.impl;

import de.mpg.mpdl.reader.common.ResponseBuilder;
import de.mpg.mpdl.reader.dto.ReadingListRemoveRQ;
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
    public ReadingList addBookingIntoReadingList(String booId, String sn) {
        ReadingList readingList = readingListRepository.getBySn(sn);
        if(readingList == null){
            readingList = new ReadingList(sn);
        }
        readingList.getBookIds().add(booId);
        readingListRepository.save(readingList);
        return readingList;
    }

    @Override
    public ReadingList getReadingByUser(String email) {
        return null;
    }

    @Override
    public ReadingList getReadingBySn(String sn) {
        return readingListRepository.getBySn(sn);
    }

    @Override
    @Transactional
    public ReadingList removeFromReadingList(ReadingListRemoveRQ removeRQ, String sn) {
        ReadingList readingList = readingListRepository.getBySn(sn);
        if(readingList == null || readingList.getBookIds().isEmpty() ||
                !readingList.getBookIds().containsAll(removeRQ.getBookIds())){
            throw new ReaderException(ResponseBuilder.RetCode.ERROR_400000);
        }
        readingList.getBookIds().removeAll(removeRQ.getBookIds());
        readingListRepository.save(readingList);
        return readingList;
    }
}
