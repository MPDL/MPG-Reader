package de.mpg.mpdl.reader.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @author shidenghui@gmail.com
 * @date 2020/11/9
 */
@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class EBook extends BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    @Column(name = "ebook_id", updatable = false, nullable = false)
    private Long eBookId;

    @Column(name = "bookId")
    private String bookId;

    @Column(name = "downloads")
    private int downloads;

    @Column(name = "reviews")
    private int reviews;

    //avg rating by all users
    @Column(name = "rating")
    private Double rating;

    //weighted score by all users
    @Column(name = "score")
    private Double score;

    public EBook(String bookId) {
        this.bookId = bookId;
    }
}
