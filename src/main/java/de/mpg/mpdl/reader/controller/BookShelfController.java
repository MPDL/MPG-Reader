package de.mpg.mpdl.reader.controller;

import de.mpg.mpdl.reader.common.BaseResponseDTO;
import de.mpg.mpdl.reader.common.BeanUtils;
import de.mpg.mpdl.reader.common.ResponseBuilder;
import de.mpg.mpdl.reader.dto.folder.BookshelfDto;
import de.mpg.mpdl.reader.dto.folder.CreateFolderRQ;
import de.mpg.mpdl.reader.dto.folder.DeleteBooksRQ;
import de.mpg.mpdl.reader.dto.folder.FolderDetailsDto;
import de.mpg.mpdl.reader.dto.folder.FolderDto;
import de.mpg.mpdl.reader.dto.folder.MoveBooksRQ;
import de.mpg.mpdl.reader.dto.folder.MoveInBooksRQ;
import de.mpg.mpdl.reader.dto.folder.MoveOutBooksRQ;
import de.mpg.mpdl.reader.dto.folder.RenameFolderRQ;
import de.mpg.mpdl.reader.exception.ReaderException;
import de.mpg.mpdl.reader.model.Bookshelf;
import de.mpg.mpdl.reader.model.EBook;
import de.mpg.mpdl.reader.model.Folder;
import de.mpg.mpdl.reader.repository.BookshelfRepository;
import de.mpg.mpdl.reader.service.IEBookService;
import de.mpg.mpdl.reader.service.IFolderService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author denghui.shi@gmail.com
 * @date 2021/03/16
 */
@RestController
@RequestMapping("/rest/bookshelf")
public class BookShelfController {
    @Autowired
    private IFolderService folderService;

    @Autowired
    private IEBookService bookService;

    @Autowired
    private BookshelfRepository bookshelfRepository;

    // Show folder and book list with LRU order
    // Display the folders on the page “My Bookshelf” in the order of recently read (Mix with other books).
    // * e.g: Put the folder which contains the recently read book first.
    @ApiOperation("Display the folders on the page My Bookshelf")
    @GetMapping
    public BaseResponseDTO<BookshelfDto> getFolderAndBooks(@RequestHeader(name = "X-SN") String sn) {
        Bookshelf bookshelf = folderService.getBookshelf(sn);
        BookshelfDto bookshelfDto = folderService.convertBookShelf(bookshelf);
        return ResponseBuilder.buildSuccess(bookshelfDto);
    }

    @ApiOperation("Get Books in Folder")
    @PostMapping(value = "/folder/books")
    public BaseResponseDTO<FolderDetailsDto> getBooksInFolder(@RequestHeader(name = "X-SN") String sn, String folderName) {
        FolderDetailsDto folderDetailsDto = folderService.getBooksInFolder(sn, folderName);
        return ResponseBuilder.buildSuccess(folderDetailsDto);
    }

    @ApiOperation("add one downloaded book in bookshelf")
    @PostMapping(value = "/addBook")
    public BaseResponseDTO<BookshelfDto> downloadBooks(@RequestHeader(name = "X-SN") String sn,
                                                       String bookId) {
        Bookshelf bookshelf = folderService.getBookshelf(sn);
        EBook eBook = bookService.getByBookId(bookId);
        if(eBook == null){
            throw new ReaderException(ResponseBuilder.RetCode.ERROR_400004);
        }
        bookshelf.getBookIds().add(bookId);
        bookshelfRepository.save(bookshelf);
        BookshelfDto bookshelfDto = BeanUtils.convertObject(bookshelf, BookshelfDto.class);
        return ResponseBuilder.buildSuccess(bookshelfDto);
    }

    @ApiOperation("create en empty folder without books")
    @PostMapping(value = "/createFolder")
    public BaseResponseDTO<FolderDto> createFolder(@RequestHeader(name = "X-SN") String sn,
                                                   @Validated @RequestBody CreateFolderRQ createFolderRQ) {
        Folder folder = folderService.createFolderIfNotExists(sn, createFolderRQ);
        Bookshelf bookshelf = folderService.getBookshelf(sn);
        bookshelf.getFolderIds().add(folder.getFolderId());
        bookshelfRepository.save(bookshelf);
        FolderDto folderDto = BeanUtils.convertObject(folder, FolderDto.class);
        return ResponseBuilder.buildSuccess(folderDto);
    }

    @ApiOperation("move books from Bookshelf to existing destFolder")
    @PostMapping(value = "/moveIn")
    public BaseResponseDTO<FolderDto> moveInBooks(@RequestHeader(name = "X-SN") String sn,
                                                  @Validated @RequestBody MoveInBooksRQ moveBooksRQ) {
        Folder destFolder = folderService.validFolder(sn, moveBooksRQ.getDestFolderName());
        folderService.moveInBooksFromBookShelf(sn, moveBooksRQ, destFolder);
        FolderDto folderDto = BeanUtils.convertObject(destFolder, FolderDto.class);
        return ResponseBuilder.buildSuccess(folderDto);
    }

    @ApiOperation("move books from existing srcFolder to Bookshelf")
    @PostMapping(value = "/folder/moveOut")
    @Transactional
    public BaseResponseDTO<FolderDto> moveOutBooks(@RequestHeader(name = "X-SN") String sn,
                                                   @Validated @RequestBody MoveOutBooksRQ moveBooksRQ) {
        Folder srcFolder = folderService.validFolder(sn, moveBooksRQ.getSrcFolderName());
        folderService.moveOut(moveBooksRQ.getBookIds(), srcFolder);
        Bookshelf bookshelf = folderService.getBookshelf(sn);
        bookshelf.getBookIds().addAll(moveBooksRQ.getBookIds());
        bookshelfRepository.save(bookshelf);
        FolderDto folderDto = BeanUtils.convertObject(srcFolder, FolderDto.class);
        return ResponseBuilder.buildSuccess(folderDto);
    }


    @ApiOperation("move books from existing srcFolder to existing destFolder")
    @PostMapping(value = "/folder/moveBetween")
    public BaseResponseDTO<FolderDto> moveBooks(@RequestHeader(name = "X-SN") String sn,
                                                @Validated @RequestBody MoveBooksRQ moveBooksRQ) {
        Folder srcFolder = folderService.validFolder(sn, moveBooksRQ.getSrcFolderName());
        Folder destFolder = folderService.validFolder(sn, moveBooksRQ.getDestFolderName());
        folderService.moveOut(moveBooksRQ.getBookIds(), srcFolder);
        folderService.moveIn(moveBooksRQ.getBookIds(), destFolder);
        FolderDto folderDto = BeanUtils.convertObject(destFolder, FolderDto.class);
        return ResponseBuilder.buildSuccess(folderDto);
    }

    @ApiOperation("rename a folder")
    @PostMapping(value = "/folder/rename")
    @Transactional
    public BaseResponseDTO<FolderDto> renameFolder(@RequestHeader(name = "X-SN") String sn,
                                                   @Validated @RequestBody RenameFolderRQ renameFolderRQ) {
        Folder folder = folderService.validFolder(sn, renameFolderRQ.getFolderName());
        folderService.renameFolder(folder, renameFolderRQ.getFolderNameNew());
        FolderDto folderDto = BeanUtils.convertObject(folder, FolderDto.class);
        return ResponseBuilder.buildSuccess(folderDto);
    }

    @ApiOperation("remove a folder, all items in the folder would be deleted as well")
    @PostMapping(value = "/folder/remove")
    @Transactional
    public BaseResponseDTO<FolderDto> removeFolder(@RequestHeader(name = "X-SN") String sn, String folderName) {
        folderService.validFolder(sn, folderName);
        Folder folder = folderService.removeFolder(folderName, sn);

        Bookshelf bookshelf = folderService.getBookshelf(sn);
        bookshelf.getFolderIds().remove(folder.getFolderId());
        bookshelfRepository.save(bookshelf);
        FolderDto folderDto = BeanUtils.convertObject(folder, FolderDto.class);
        return ResponseBuilder.buildSuccess(folderDto);
    }

    @ApiOperation("remove books in given folder")
    @PostMapping(value = "/folder/removeBooks")
    @Transactional
    public BaseResponseDTO<FolderDto> removeFolder(@RequestHeader(name = "X-SN") String sn,
                                                   @Validated @RequestBody DeleteBooksRQ deleteBooksRQ) {
        Folder folder = folderService.validFolder(sn, deleteBooksRQ.getFolderName());
        folderService.moveOut(deleteBooksRQ.getBookIds(), folder);
        FolderDto folderDto = BeanUtils.convertObject(folder, FolderDto.class);
        return ResponseBuilder.buildSuccess(folderDto);
    }


}