package de.mpg.mpdl.reader.repository;

import de.mpg.mpdl.reader.model.Folder;
import org.springframework.stereotype.Repository;

/**
 * @author denghui.shi@safetytaxfree.com
 * @date 3/22/21
 * @desc
 */
@Repository
public interface FolderRepository extends BaseRepository<Folder, Long> {
    Folder findByFolderNameAndSn(String name, String sn);
}
