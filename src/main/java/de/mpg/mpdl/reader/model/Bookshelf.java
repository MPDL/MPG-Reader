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
 * @author denghui.shi@safetytaxfree.com
 * @date 3/22/21
 * @desc
 */
@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Bookshelf extends BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    @Column(name = "shelf_id", updatable = false, nullable = false)
    private Long shelfId;

    @Column(name = "sn", unique = true)
    private String sn;

    @ElementCollection
    @LazyCollection(LazyCollectionOption.FALSE)
    @CollectionTable(joinColumns = @JoinColumn(name = "shelf_id"))
    @Column(name = "folders")
    private Set<Long> folderIds = new HashSet<>();

    @ElementCollection
    @LazyCollection(LazyCollectionOption.FALSE)
    @CollectionTable(joinColumns = @JoinColumn(name = "shelf_id"))
    @Column(name = "books")
    private Set<String> bookIds = new HashSet<>();

    public Bookshelf(String sn) {
        this.sn = sn;
        folderIds = new HashSet<>();
        bookIds = new HashSet<>();
    }
}
