package de.mpg.mpdl.reader.service;

import de.mpg.mpdl.reader.dto.folder.BookshelfDto;
import de.mpg.mpdl.reader.dto.folder.CreateFolderRQ;
import de.mpg.mpdl.reader.dto.folder.MoveInBooksRQ;
import de.mpg.mpdl.reader.model.Bookshelf;
import de.mpg.mpdl.reader.model.Folder;

import java.util.Set;

/**
 * @author shidenghui@gmail.com
 * @date 2021/03/17
 */
public interface IFolderService {
    Folder createFolderIfNotExists(String sn, CreateFolderRQ createFolderRQ);

    Folder validFolder(String sn, String folderName);

    Folder renameFolder(Folder folder, String folderName);

    void moveInBooksFromBookShelf(String sn, MoveInBooksRQ moveBooksRQ, Folder destFolder);

    void moveOut(Set<String> bookIds, Folder srcFolder);

    void moveIn(Set<String> bookIds, Folder destFolder);

    Folder removeFolder(String folderName, String sn);

    Bookshelf getBookshelf(String sn);

    BookshelfDto convertBookShelf(Bookshelf bookshelf);
}
