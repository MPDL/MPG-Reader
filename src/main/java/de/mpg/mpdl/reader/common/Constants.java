package de.mpg.mpdl.reader.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author shidenghui@gmail.com
 * @date 2020/11/3
 */
public class Constants {

    @AllArgsConstructor
    public enum Rating {
        /*rating one star*/
        one(1, "one star"),
        /*rating two stars*/
        two(2, "two stars"),
        /*rating three stars*/
        three(3, "three stars"),
        /*rating four stars*/
        four(4, "four stars"),
        /*rating five stars*/
        five(5, "five stars");

        @Getter
        private int rate;

        @Getter
        private String name;
    }
}
