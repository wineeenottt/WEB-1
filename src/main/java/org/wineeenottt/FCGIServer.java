package org.wineeenottt;

import com.fastcgi.FCGIInterface;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Map;

public class FCGIServer {

    private final FCGIInterface fcgi;

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    private static final String HTTP_SUCCESS = """
            Status: 200 OK
            Content-Type: application/json
            Content-Length: %d
            
            %s
            """;

    private static final String HTTP_ERROR = """
            Status: %d %s
            Content-Type: application/json
            Content-Length: %d
            
            %s
            """;

    private static final String RESULT = """
            {
                "x": %s,
                "y": %s,
                "r": %s,
                "execTime": %.2f,
                "currentTime": "%s",
                "hit": %b
            }
            """;

    private static final String ERROR = """
            {
                "error": "%s",
                "currentTime": "%s"
            }
            """;

    public FCGIServer() {
        this.fcgi = new FCGIInterface();
    }

    public void start() {
        while (fcgi.FCGIaccept() >= 0) {
            try {
                var startTime = System.nanoTime();
                var method = FCGIInterface.request.params.getProperty("REQUEST_METHOD");
                if (!"GET".equals(method)) {
                    throw new Exception("Неверный метод: " + method);
                }

                var queryString = FCGIInterface.request.params.getProperty("QUERY_STRING");

                if (queryString == null || queryString.isEmpty()) {
                    throw new InvalidParametersException("Параметры отсутствуют");
                }

                Map<String, String> map = ParserParameters.parseParams(queryString);
                BigDecimal x = ParserParameters.getX(map);
                BigDecimal y = ParserParameters.getY(map);
                BigDecimal r = ParserParameters.getR(map);


                boolean isHit = AreaShape.areaCheck(x, r, y);

                var endTime = System.nanoTime();
                double executionTime = (endTime - startTime) / 1_000_000.0;
                LocalTime curTime = LocalTime.now();
                String nowTime = curTime.format(TIME_FORMATTER);

                String json = String.format(Locale.US, RESULT, x.toString(), y.toString(), r.toString(), executionTime, nowTime, isHit);
                String response = String.format(HTTP_SUCCESS, json.getBytes(StandardCharsets.UTF_8).length, json);
                System.out.println(response);

            } catch (InvalidParametersException e) {
                sendError(400, "Bad Request", e.getMessage());
            } catch (Exception e) {
                sendError(500, "Internal Server Error", "Внутренняя ошибка сервера: " + e.getMessage());
            }
        }
    }

    private void sendError(int statusCode, String errorMessage, String responseBody) {
        LocalTime currentTime = LocalTime.now();
        String time = currentTime.format(TIME_FORMATTER);
        String json = String.format(ERROR, responseBody, time);
        String response = String.format(HTTP_ERROR, statusCode, errorMessage, json.getBytes(StandardCharsets.UTF_8).length, json);
        System.out.println(response);
    }
}