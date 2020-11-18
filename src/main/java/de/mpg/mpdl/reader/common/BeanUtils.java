package de.mpg.mpdl.reader.common;

import org.dozer.DozerBeanMapperBuilder;
import org.dozer.Mapper;

import java.util.ArrayList;
import java.util.List;

/**
 * @author shidenghui@gmail.com
 * @date 2020/11/9
 */
public class BeanUtils {
    /**
     * default MAPPER without config
     */
    private static final Mapper MAPPER = DozerBeanMapperBuilder.buildDefault();

    /***
     * @param listSrc Source object collection
     * @param clazz   Target object class type
     */
    @SuppressWarnings("unchecked")
    public static <TContent> List<TContent> convertList(List listSrc,
                                                        Class<TContent> clazz) {
        List<TContent> listDes = new ArrayList<>();
        if (listSrc.isEmpty()) {
            return listDes;
        }
        listSrc.forEach(objSrc -> {
            listDes.add(convertObject(objSrc, clazz));
        });
        return listDes;
    }

    /**
     * Single object conversion
     *
     * @param content Source object
     * @param clazz   Target object class type
     */
    public static <TContent> TContent convertObject(Object content,
                                                    Class<TContent> clazz) {
        if (content == null) {
            return null;
        }
        return MAPPER.map(content, clazz);
    }
}
