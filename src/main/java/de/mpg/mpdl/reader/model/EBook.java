package de.mpg.mpdl.reader.model;

import de.mpg.mpdl.reader.common.CommonUtils;
import de.mpg.mpdl.reader.common.Constants;
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

    @Column(name = "bookId", nullable = false, unique = true)
    private String bookId;

    @Column(name = "book_name")
    private String bookName;

    @Column(name = "book_cover_url")
    private String bookCoverURL;

    @Column(name = "downloads")
    private int downloads = 0;

    @ElementCollection
    @LazyCollection(LazyCollectionOption.FALSE)
    @CollectionTable(joinColumns = @JoinColumn(name = "ebook_id"))
    @Column(name = "downloaded_by_sn")
    private Set<String> downloadedBySn = new HashSet<>();

    @Column(name = "reviews")
    private int reviews = 0;

    //avg rating by all users
    @Column(name = "rating")
    private Double rating = 0.00;

    public EBook(String bookId) {
        this.bookId = bookId;
    }

    public void caculateRatingAndScore(Constants.Rating newRating) {
        int newReviewsCount = reviews + 1;
        this.rating = CommonUtils.round(((rating * reviews + newRating.getRate()) / newReviewsCount), 2);
        this.reviews = newReviewsCount;
    }
}
