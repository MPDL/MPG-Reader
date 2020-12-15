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

    public static boolean needUpdate(String v1, String v2) {
        //return v2 > v1;
        String[] current = v1.split("\\.");
        String[] latest = v2.split("\\.");

        int commonLength = Math.min(current.length, latest.length);
        int x1, x2;
        for (int i = 0; i < commonLength; i++) {
            x1 = Integer.parseInt(current[i]);
            x2 = Integer.parseInt(latest[i]);
            if(x1 == x2){
                continue;
            }
            return x2 > x1;
        }

        String[] extraPart = current.length > latest.length ? current : latest;
        for (int i = commonLength; i < extraPart.length; i++) {
            if (Integer.parseInt(extraPart[i]) > 0){
                return latest.length > current.length;
            }
        }
        return false;
    }
}
