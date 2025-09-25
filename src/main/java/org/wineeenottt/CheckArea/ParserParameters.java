package org.wineeenottt.CheckArea;

import org.wineeenottt.Exception.InvalidParametersException;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class ParserParameters {

    public static Map<String, String> parseParams(String params) throws InvalidParametersException {
        Map<String, String> map = new HashMap<>();
        String[] pairs = params.split("&");

        if (pairs.length != 3) {
            throw new InvalidParametersException("Параметры отсутствуют (необходимо 3 параметра)");
        }

        for (String pair : pairs) {
            String[] keyValue = pair.split("=", 2);
            if (keyValue.length == 2) {
                map.put(keyValue[0], keyValue[1]);
            }
        }
        return map;
    }

    public static BigDecimal getX(Map<String, String> params) throws InvalidParametersException {
        if (!params.containsKey("x")) {
            throw new InvalidParametersException("Параметр X отсутствует");
        }

        BigDecimal x;
        try {
            x = new BigDecimal(params.get("x"));
        } catch (NumberFormatException e) {
            throw new InvalidParametersException("Параметр X должен быть числом");
        }

        if (!Validation.checkX(x)) {
            throw new InvalidParametersException("Параметр X вне допустимого диапазона [-4, 4]");
        }

        return x;
    }

    public static BigDecimal getY(Map<String, String> params) throws InvalidParametersException {
        if (!params.containsKey("y")) {
            throw new InvalidParametersException("Параметр Y отсутствует");
        }

        BigDecimal y;
        try {
            y = new BigDecimal(params.get("y"));
        } catch (NumberFormatException e) {
            throw new InvalidParametersException("Параметр Y должен быть числом");
        }

        if (!Validation.checkY(y)) {
            throw new InvalidParametersException("Параметр Y вне допустимого диапазона [-3, 3]");
        }

        return y;
    }

    public static BigDecimal getR(Map<String, String> params) throws InvalidParametersException {
        if (!params.containsKey("r")) {
            throw new InvalidParametersException("Параметр R отсутствует");
        }

        BigDecimal r;
        try {
            r = new BigDecimal(params.get("r"));
        } catch (NumberFormatException e) {
            throw new InvalidParametersException("Параметр R должен быть числом");
        }

        if (!Validation.checkR(r)) {
            throw new InvalidParametersException("Параметр R вне допустимого диапазона [1, 5]");
        }

        return r;
    }
}
