package de.mpg.mpdl.reader.common;

import lombok.Data;

import javax.persistence.MappedSuperclass;
import java.util.Date;

/**
 * @author shidenghui@gmail.com
 * @date 2020/11/17
 */
@MappedSuperclass
@Data
public abstract class BaseRS {

    private String uuid;

    private Date createDate;

    private Date modifyDate;
}