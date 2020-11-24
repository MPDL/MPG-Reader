package de.mpg.mpdl.reader.service;

import de.mpg.mpdl.reader.dto.ReadingListRemoveRQ;
import de.mpg.mpdl.reader.model.ReadingList;

/**
 * @author shidenghui@gmail.com
 * @date 2020/11/9
 */
public interface IReadingListService {
    ReadingList addBookingIntoReadingList(String booId, String sn);
    ReadingList getReadingByUser(String email);
    ReadingList getReadingBySn(String sn);
    ReadingList removeFromReadingList(ReadingListRemoveRQ removeRQ, String sn) throws Exception;
}
