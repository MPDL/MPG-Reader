package de.mpg.mpdl.reader.common;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author shidenghui@gmail.com
 * @date 2020/11/25
 */
public class CommonUtils {
    public static double round(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
