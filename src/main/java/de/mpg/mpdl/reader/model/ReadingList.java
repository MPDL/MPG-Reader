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
import java.util.Set;
import java.util.TreeSet;

/**
 * @author shidenghui@gmail.com
 * @date 2020/11/9
 */
@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class ReadingList extends BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    @Column(name = "reading_list_id", updatable = false, nullable = false)
    private Long readingListId;

    @Column(name = "sn", unique = true)
    private String sn;

    @ElementCollection
    @LazyCollection(LazyCollectionOption.FALSE)
    @CollectionTable(joinColumns = @JoinColumn(name = "reading_list_id"))
    @Column(name = "bookIds")
    private Set<String> bookIds = new TreeSet<>();

    public ReadingList(String sn) {
        this.sn = sn;
        this.bookIds = new TreeSet<>();
    }
}
