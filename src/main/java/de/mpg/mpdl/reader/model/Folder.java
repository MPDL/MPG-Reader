package de.mpg.mpdl.reader.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import java.util.HashSet;
import java.util.Set;

/**
 * @author denghui.shi@gmail.com
 * @date 2021/03/16
 * @desc
 */
@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Folder extends BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    @Column(name = "folder_id", updatable = false, nullable = false)
    private Long folderId;

    @Column(name = "sn")
    private String sn;

    @Column(name = "folder_name", unique = true)
    private String folderName;

    private Boolean isEmpty = true;

    @ElementCollection
    @LazyCollection(LazyCollectionOption.FALSE)
    @CollectionTable(joinColumns = @JoinColumn(name = "folder_id"))
    @Column(name = "books")
    private Set<String> bookIds = new HashSet<>();
}
