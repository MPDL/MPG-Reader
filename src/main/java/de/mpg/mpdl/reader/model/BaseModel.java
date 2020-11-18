package de.mpg.mpdl.reader.model;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import java.util.Date;
import java.util.UUID;

/**
 * @author shidenghui@gmail.com
 * @date 2020/11/9
 */
@MappedSuperclass
@Data
public abstract class BaseModel {

    @Column(name = "uuid", unique = true, nullable = false)
    private String uuid;

    @CreationTimestamp
    @Column(name = "create_date", nullable = false)
    private Date createDate;

    @UpdateTimestamp
    @Column(name = "modify_date")
    private Date modifyDate;

    @Version
    @Column(name = "version")
    private long version;

    public BaseModel() {
        this.uuid = UUID.randomUUID().toString();
    }
}
