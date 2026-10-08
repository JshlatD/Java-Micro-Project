import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import model.Unit;
import service.ConversionService;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class Server {

    public static void main(String[] args)
            throws IOException {

        ConversionService conversionService =
                new ConversionService();


        HttpServer server =
                HttpServer.create(
                        new InetSocketAddress(8080),
                        0
                );


        server.createContext(
                "/api/convert",
                exchange ->
                        convert(
                                exchange,
                                conversionService
                        )
        );


        server.createContext(
                "/api/units",
                exchange ->
                        getUnits(
                                exchange,
                                conversionService
                        )
        );


        server.createContext(
                "/",
                exchange -> {

                    String response = """
                            <h1>IndicMeasure</h1>
                            <p>Java server is running!</p>
                            """;


                    sendResponse(
                            exchange,
                            response,
                            "text/html"
                    );
                }
        );


        server.start();


        System.out.println(
                "IndicMeasure server started!"
        );


        System.out.println(
                "Open http://localhost:8080"
        );
    }


    private static void convert(
            HttpExchange exchange,
            ConversionService conversionService
    ) throws IOException {

        String query =
                exchange.getRequestURI()
                        .getQuery();


        double value = 0;

        String fromUnit = "";

        String toUnit = "";


        if (query != null) {

            String[] parameters =
                    query.split("&");


            for (String parameter :
                    parameters) {

                String[] parts =
                        parameter.split("=");


                if (parts.length == 2) {

                    if (
                            parts[0]
                                    .equals("value")
                    ) {

                        try {

                            value =
                                    Double.parseDouble(
                                            parts[1]
                                    );

                        } catch (
                                NumberFormatException e
                        ) {

                            sendResponse(
                                    exchange,
                                    "{\"error\":\"Invalid value\"}",
                                    "application/json"
                            );

                            return;
                        }
                    }


                    if (
                            parts[0]
                                    .equals("from")
                    ) {

                        fromUnit =
                                parts[1];
                    }


                    if (
                            parts[0]
                                    .equals("to")
                    ) {

                        toUnit =
                                parts[1];
                    }
                }
            }
        }


        if (
                fromUnit.isEmpty()
                ||
                toUnit.isEmpty()
        ) {

            sendResponse(
                    exchange,
                    "{\"error\":\"Please select both units\"}",
                    "application/json"
            );

            return;
        }


        try {

            String fromCategory =
                    conversionService.getCategory(
                            fromUnit
                    );


            String toCategory =
                    conversionService.getCategory(
                            toUnit
                    );


            if (
                    !fromCategory.equals(
                            toCategory
                    )
            ) {

                sendResponse(
                        exchange,
                        "{\"error\":\"Units must belong to the same category\"}",
                        "application/json"
                );

                return;
            }


            double baseValue =
                    conversionService.convertToBase(
                            value,
                            fromUnit
                    );


            double result =
                    conversionService.convertFromBase(
                            baseValue,
                            toUnit
                    );


            String response = """
                    {
                        "value": %.4f,
                        "fromUnit": "%s",
                        "toUnit": "%s",
                        "category": "%s",
                        "result": %.4f
                    }
                    """.formatted(
                            value,
                            fromUnit,
                            toUnit,
                            fromCategory,
                            result
                    );


            sendResponse(
                    exchange,
                    response,
                    "application/json"
            );


        } catch (
                IllegalArgumentException e
        ) {

            sendResponse(
                    exchange,
                    "{\"error\":\"Unknown unit\"}",
                    "application/json"
            );
        }
    }


    private static void getUnits(
            HttpExchange exchange,
            ConversionService conversionService
    ) throws IOException {

        List<Unit> units =
                conversionService.getAllUnits();


        StringBuilder json =
                new StringBuilder("[");


        for (
                int i = 0;
                i < units.size();
                i++
        ) {

            Unit unit =
                    units.get(i);


            json.append("{");


            json.append(
                    "\"name\":\""
            );


            json.append(
                    escapeJson(
                            unit.getName()
                    )
            );


            json.append("\",");


            json.append(
                    "\"category\":\""
            );


            json.append(
                    escapeJson(
                            unit.getCategory()
                    )
            );


            json.append("\",");


            json.append(
                    "\"system\":\""
            );


            json.append(
                    escapeJson(
                            unit.getSystem()
                    )
            );


            json.append("\",");


            json.append(
                    "\"description\":\""
            );


            json.append(
                    escapeJson(
                            unit.getDescription()
                    )
            );


            json.append("\",");


            json.append(
                    "\"region\":\""
            );


            json.append(
                    escapeJson(
                            unit.getRegion()
                    )
            );


            json.append("\"");


            json.append("}");


            if (
                    i < units.size() - 1
            ) {

                json.append(",");
            }
        }


        json.append("]");


        sendResponse(
                exchange,
                json.toString(),
                "application/json"
        );
    }


    private static String escapeJson(
            String text
    ) {

        if (text == null) {

            return "";
        }


        return text
                .replace(
                        "\\",
                        "\\\\"
                )
                .replace(
                        "\"",
                        "\\\""
                )
                .replace(
                        "\n",
                        "\\n"
                )
                .replace(
                        "\r",
                        "\\r"
                );
    }


    private static void sendResponse(
            HttpExchange exchange,
            String response,
            String contentType
    ) throws IOException {

        byte[] responseBytes =
                response.getBytes(
                        StandardCharsets.UTF_8
                );


        exchange.getResponseHeaders()
                .set(
                        "Content-Type",
                        contentType
                );


        exchange.getResponseHeaders()
                .set(
                        "Access-Control-Allow-Origin",
                        "*"
                );


        exchange.sendResponseHeaders(
                200,
                responseBytes.length
        );


        exchange.getResponseBody()
                .write(responseBytes);


        exchange.close();
    }
}