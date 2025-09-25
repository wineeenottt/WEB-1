package org.wineeenottt.CheckArea;

import java.math.BigDecimal;

public class Validation {

    public static boolean checkX(BigDecimal x) {
        return x.compareTo(BigDecimal.valueOf(-4)) >= 0 &&
                x.compareTo(BigDecimal.valueOf(4)) <= 0;
    }

    public static boolean checkY(BigDecimal y) {
        return y.compareTo(BigDecimal.valueOf(-3)) >= 0 &&
                y.compareTo(BigDecimal.valueOf(3)) <= 0;
    }

    public static boolean checkR(BigDecimal r) {
        return r.compareTo(BigDecimal.ONE) >= 0 &&
                r.compareTo(BigDecimal.valueOf(5)) <= 0;
    }
}

