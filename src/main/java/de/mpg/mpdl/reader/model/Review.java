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
public class Review extends BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    @Column(name = "review_id", updatable = false, nullable = false)
    private Long reviewId;

    @Column(name = "sn")
    private String sn;

    @Column(name = "bookId")
    private String bookId;

    @Column(name = "rating")
    private int rating;

    //max length?
    @Column(name = "comment")
    private String comment;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "organization")
    private String organization;
}
