package org.wineeenottt;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class AreaShape {

    public static boolean areaRectangle(BigDecimal x, BigDecimal r, BigDecimal y) {
        //( x <= 0 ) && ( x >= -r/2 ) && ( y >= 0  ) && (y <= r);
        return x.compareTo(BigDecimal.ZERO) <= 0 &&
                x.compareTo(r.divide(BigDecimal.valueOf(2), 10, RoundingMode.HALF_UP).negate()) >= 0 &&
                y.compareTo(BigDecimal.ZERO) >= 0 &&
                y.compareTo(r) <= 0;
    }

    public static boolean areaCircle(BigDecimal x, BigDecimal r, BigDecimal y) {
        //(x >= 0 && y <= 0) && (Math.pow(x, 2) + Math.pow(y,2) <= Math.pow(r/2, 2));
        if (x.compareTo(BigDecimal.ZERO) >= 0 && y.compareTo(BigDecimal.ZERO) <= 0) {
            BigDecimal radius = r.divide(BigDecimal.valueOf(2), 10, RoundingMode.HALF_UP);
            BigDecimal xSquared = x.pow(2);
            BigDecimal ySquared = y.pow(2);
            BigDecimal sumSquared = xSquared.add(ySquared);

            return sumSquared.compareTo(radius.pow(2)) <= 0;
        }
        return false;
    }

    public static boolean areaTriangle(BigDecimal x, BigDecimal r, BigDecimal y) {
        // (x >= 0 && y >= 0) && ( x + y <= (double) r /2);
        if (x.compareTo(BigDecimal.ZERO) >= 0 && y.compareTo(BigDecimal.ZERO) >= 0) {
            BigDecimal halfR = r.divide(BigDecimal.valueOf(2), 10, RoundingMode.HALF_UP);
            BigDecimal sum = x.add(y);

            return sum.compareTo(halfR) <= 0;
        }
        return false;
    }

    public static boolean areaCheck(BigDecimal x, BigDecimal r, BigDecimal y) {
        return areaRectangle(x, r, y) || areaCircle(x, r, y) || areaTriangle(x, r, y);
    }
}