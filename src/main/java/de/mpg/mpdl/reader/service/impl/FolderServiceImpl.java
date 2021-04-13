package de.mpg.mpdl.reader.service.impl;

import de.mpg.mpdl.reader.common.BeanUtils;
import de.mpg.mpdl.reader.common.ResponseBuilder;
import de.mpg.mpdl.reader.dto.folder.BookshelfDto;
import de.mpg.mpdl.reader.dto.folder.CreateFolderRQ;
import de.mpg.mpdl.reader.dto.folder.FolderDetailsDto;
import de.mpg.mpdl.reader.dto.folder.MoveInBooksRQ;
import de.mpg.mpdl.reader.dto.folder.SimpleBookDto;
import de.mpg.mpdl.reader.exception.ReaderException;
import de.mpg.mpdl.reader.model.Bookshelf;
import de.mpg.mpdl.reader.model.EBook;
import de.mpg.mpdl.reader.model.Folder;
import de.mpg.mpdl.reader.repository.BookshelfRepository;
import de.mpg.mpdl.reader.repository.FolderRepository;
import de.mpg.mpdl.reader.service.IEBookService;
import de.mpg.mpdl.reader.service.IFolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author denghui.shi@safetytaxfree.com
 * @date 3/22/21
 * @desc
 */
@Service
public class FolderServiceImpl implements IFolderService {

    @Autowired
    private FolderRepository folderRepository;

    @Autowired
    private IEBookService eBookService;

    @Autowired
    private BookshelfRepository bookshelfRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Folder createFolderIfNotExists(String sn, CreateFolderRQ createFolderRQ) {
        Folder newFolder = new Folder();
        newFolder.setSn(sn);
        newFolder.setFolderName(createFolderRQ.getFolderName());
        return folderRepository.save(newFolder);
    }

    @Override
    public Folder validFolder(String sn, String folderName) {
        Folder srcFolder = folderRepository.findByFolderNameAndSn(folderName, sn);
        if (srcFolder == null) {
            throw new ReaderException(ResponseBuilder.RetCode.ERROR_400005);
        }
        return srcFolder;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Folder renameFolder(Folder folder, String folderName) {
        folder.setFolderName(folderName);
        folderRepository.save(folder);
        return folder;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void moveInBooksFromBookShelf(String sn, MoveInBooksRQ moveBooksRQ, Folder destFolder) {
        Bookshelf bookshelf = bookshelfRepository.findBySn(sn);
        if (bookshelf.getBookIds().containsAll(moveBooksRQ.getBookIds())) {
            bookshelf.getBookIds().removeAll(moveBooksRQ.getBookIds());
            bookshelfRepository.save(bookshelf);
        } else {
            throw new ReaderException(ResponseBuilder.RetCode.ERROR_400006);
        }
        moveIn(moveBooksRQ.getBookIds(), destFolder);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void moveOut(Set<String> bookIds, Folder srcFolder) {
        if (srcFolder.getBookIds().containsAll(bookIds)) {
            srcFolder.getBookIds().removeAll(bookIds);
            if (srcFolder.getBookIds().isEmpty()) {
                srcFolder.setIsEmpty(true);
            }
            folderRepository.save(srcFolder);
        } else {
            throw new ReaderException(ResponseBuilder.RetCode.ERROR_400006);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void moveIn(Set<String> bookIds, Folder destFolder) {
        destFolder.getBookIds().addAll(bookIds);
        destFolder.setIsEmpty(false);
        folderRepository.save(destFolder);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Folder removeFolder(String folderName, String sn) {
        Folder folder = folderRepository.findByFolderNameAndSn(folderName, sn);
        folder.setBookIds(null);
        folderRepository.delete(folder);
        return folder;
    }

    @Override
    @Transactional
    public Bookshelf getBookshelf(String sn) {
        Bookshelf bookshelf = bookshelfRepository.findBySn(sn);
        if (bookshelf == null) {
            bookshelf = new Bookshelf(sn);
            bookshelfRepository.save(bookshelf);
        }
        return bookshelf;
    }

    @Override
    public BookshelfDto convertBookShelf(Bookshelf bookshelf) {
        BookshelfDto bookshelfDto = BeanUtils.convertObject(bookshelf, BookshelfDto.class);
        List<String> folderNames = new LinkedList<>();
        for (Long folderId : bookshelf.getFolderIds()) {
            Optional<Folder> folder = folderRepository.findById(folderId);
            folder.ifPresent(theFolder -> folderNames.add(theFolder.getFolderName()));
        }
        bookshelfDto.setFolderNames(folderNames);
        return bookshelfDto;
    }

    @Override
    @Transactional
    public FolderDetailsDto getBooksInFolder(String sn, String folderName) {
        Folder folder = folderRepository.findByFolderNameAndSn(folderName, sn);
        if (folder == null) {
            throw new ReaderException(ResponseBuilder.RetCode.ERROR_400005);
        }
        List<SimpleBookDto> books = new LinkedList<>();
        for (String bookId : folder.getBookIds()) {
            EBook eBook = eBookService.getByBookId(bookId);
            books.add(BeanUtils.convertObject(eBook, SimpleBookDto.class));
        }
        FolderDetailsDto folderDetailsDto = new FolderDetailsDto();
        folderDetailsDto.setSn(sn);
        folderDetailsDto.setFolderName(folderName);
        folderDetailsDto.setBooks(books);
        return folderDetailsDto;
    }

}
