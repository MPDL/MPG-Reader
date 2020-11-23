package de.mpg.mpdl.reader.common;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

/**
 * @author shidenghui@gmail.com
 * @date 2020/11/18
 */
public class PageUtils {
    /**
     * 分页查询参数构建
     *
     * @param page     页码，从 0 开始
     * @param pageSize 页面大小，默认20
     * @param sort     排序方向，正序还是倒序
     * @param orderBy  排序依据属性
     * @return Pageable
     */
    public static Pageable createPageable(int page, int pageSize, Sort.Direction sort, String orderBy) {
        return PageRequest.of(page, pageSize, sort, orderBy);
    }

    public static Pageable createPageable(int page, int pageSize, Sort.Direction sort) {
        return PageRequest.of(page, pageSize, sort, "createDate");
    }

    public static Pageable createPageable(BasePageRequest pageRequest) {
        return createPageable(pageRequest.getPageNumber(), pageRequest.getPageSize(), Sort.Direction.DESC, "createDate");
    }

    public static <SrcClazz, TarClazz> Page<TarClazz> adapterPage(Page<SrcClazz> page, Class<TarClazz> clazz) {
        List<TarClazz> retList = BeanUtils.convertList(page.getContent(), clazz);
        return new PageImpl<>(retList, page.getPageable(), page.getTotalElements());
    }

    public static <SrcClazz> Page<SrcClazz> adapterPage(List<SrcClazz> list, Pageable pageable, long totalElements) {
        return new PageImpl<>(list, pageable, totalElements);
    }
}
