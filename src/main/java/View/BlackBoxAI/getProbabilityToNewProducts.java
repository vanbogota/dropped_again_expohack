package View.BlackBoxAI;

import DAO.DBOperationsForRecommendation;
import DAO.DBoperationsForClient;
import DAO.DBoperationsForProducts;
import Model.clientModel;
import Model.productModel;
import View.mainWindow.createIdeasToBuisness;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.DatasLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.xml.crypto.Data;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component("blackBoxAsk2")
@Scope("prototype")
public class getProbabilityToNewProducts extends JFrame {
    private DBoperationsForProducts operations;
    private DBoperationsForClient operations2;
    private ApplicationContext context;
    @Autowired
    public void setOperations(DBoperationsForProducts operations) {
        this.operations = operations;
    }
    @Autowired
    public void setOperations2(DBoperationsForClient operations2) {
        this.operations2 = operations2;
    }

    public void setContext(ApplicationContext context) {
        this.context = context;
        createWindow();
    }


    public void createWindow() {
        DatasLoader datasLoader = new DatasLoader("src/main/java/config/datas.properties");
        List<productModel> productList = operations.allProducts(Integer.parseInt(datasLoader.getUserId()));
        List<clientModel> clientList = operations2.allClients(1);

        StringBuilder systemMessage = new StringBuilder("Твоя задача на сегодняшний день побыть бизнес аналитиком и сгенерировать мне " +
                "интересные продукты для бизнеса. ТЫ ДОЛЖЕН ПРОАНАЛИЗИРОВАТЬ КЛИЕНТОВ, А ТАКЖЕ ПРОДУКТЫ КОТОРЫЕ УЖЕ ЕСТЬ СЕЙЧАС. " +
                "ТЫ ДОЛЖЕН УЧИТЫВАТЬ, ЧТО ЕСЛИ КОМПАНИЯ ВЫДАЕТ КРЕДИТЫ, ТО ОНА НЕ БУДЕТ ПРОИЗВОДИТЬ ВЕЛОСИПЕДЫ. " +
                "МОЖЕШЬ ТАКЖЕ РЕКОМЕНДОВАТЬ АКЦИИ И ТП ДЛЯ ЛИДОГЕНЕРАЦИИ. ТЫ ДОЛЖЕН ВОЗВРАЩАТЬ МНЕ ДАННЫЕ В ФОРМАТЕ СПИСКА, " +
                "ТОЕСТЬ (номер идеи)(идея).ПИШИ ТОЛЬКО НА РУССКОМ ЯЗЫКЕ,ВАЛЮТА В ЦЕННИКАХ-РУССКИЕ РУБЛИ.ЕЩЕ РАЗ,ТВОЯ ИДЕЯ НЕ ПРОАНАЛИЗИРОВАТЬ КЛИЕНТОВ,А ПРИДУМАТЬ НОВЫЕ ПРОДУКТЫ ДЛЯ ПРЕДПРИЯТИЯ." +
                "ОЧЕНЬ ВАЖНО ,ЧТОБЫ ТЫ В ПРИМЕР НЕ ПРИВОДИЛ НИКАКИХ ЛЮДЕЙ.ЕЩЕ РАЗ:ТВОЯ ЗАДАЧА ПРИДУМАТЬ НОВЫЕ ПРОДУКТЫ ДЛЯ ПРЕДПРИЯТИЯ" +
                "И ВЫВЕСТИ ИХ ПРОСТЫМ СПИСОКМ,НЕ ОБОСНОВЫВАЯ НИЧЕГО.ВЫВОДИ ИДЕИ В ТАКОМ ФОРМАТЕ:Идеи для новых продуктов:" +
                "1. (1) Спортивный пакет - набор спортивных принадлежностей для спортсменов и фанатов спорта." +
                "* Цена: 10 000 рублей" +
                "* Акция: 5% скидка на первую покупку" +
                "2. (2) Книжный подарок - набор книг для любителей чтения, включая бестселлеры и классику." +
                "* Цена: 3 000 рублей" +
                "* Акция: бесплатная доставка на все книги в пакете" +
                "3. (3) Хлебный набор - набор различных сортов хлеба для любителей хлеба." +
                "* Цена: 100 рублей" +
                "* Акция: скидка 5% на повторную покупку" +
                "ВОТ ТВОИ ВХОДНЫЕ ДАННЫЕ:");

        // Добавляем продукты к сообщению
        for (int i = 0; i < productList.size(); i++) {
            systemMessage.append(i + 1).append(" ").append(productList.get(i).toString()).append("; ");
        }

        // Добавляем клиентов к сообщению
        systemMessage.append("Клиенты:");
        for (int i = 0; i < clientList.size(); i++) {
            systemMessage.append(i + 1).append(" ").append(clientList.get(i).toString()).append("; ");
        }

        // Удаляем последний символ "; " если он есть
        if (systemMessage.length() > 2) {
            systemMessage.setLength(systemMessage.length() - 2);
        }

        // Генерируем финальный промпт
        String finalPrompt = systemMessage.toString();
        System.out.println("-------------");
        System.out.println(finalPrompt); // Выводим финальный промпт для проверки

        // Обращаемся к нейросети с финальным промптом
        String response = generate("Твоя задача на сегодняшний день побыть бизнес аналитиком и сгенерировать мне " +
                "интересные продукты для бизнеса", finalPrompt); // Используем метод с пустым systemPrompt и финальным userPrompt
        createIdeasToBuisness createIdeasToBuisness=context.getBean("ideasToBuisness",View.mainWindow.createIdeasToBuisness.class);
        createIdeasToBuisness.setIdeasString(response);
    }

    public static String generate(String userPrompt) {
        return generate("", userPrompt, 0.000000001, 0);
    }

    public static String generate(String systemPrompt, String userPrompt) {
        return generate(systemPrompt, userPrompt, 0.000000001, 0);
    }

    public static String generate(String userPrompt, double temperature) {
        return generate("", userPrompt, temperature, 0);
    }

    public static String generate(String userPrompt, int maxTokens) {
        return generate("", userPrompt, -1, maxTokens);
    }

    public static String generate(String userPrompt, double temperature, int maxTokens) {
        return generate("", userPrompt, temperature, maxTokens);
    }

    public static String generate(String systemPrompt, String userPrompt, double temperature) {
        return generate(systemPrompt, userPrompt, temperature, 0);
    }

    public static String generate(String systemPrompt, String userPrompt, int maxTokens) {
        return generate(systemPrompt, userPrompt, -1, maxTokens);
    }

    public static String generate(String systemPrompt, String userPrompt, double temperature, int maxTokens) {
        HttpURLConnection conn = null;
        StringBuilder response = new StringBuilder();
        try {
            URL url = new URL("http://localhost:8889/v1/chat/completions");

            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);


            String jsonInputString = String.format("""
                    {
                    "messages": [
                      { "role": "system", "content": "%s" },
                      { "role": "user", "content": "%s" }
                    ]
                    """ + (temperature == 0.000000001 ? "" : ",\n \"temperature\": " + temperature) + """
                    """ + (maxTokens == 0 ? "" : ",\n \"max_tokens\": " + maxTokens) + """
                    }
                    """, systemPrompt, userPrompt);
            System.out.println(jsonInputString);

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
                os.flush();
            } catch (IOException e) {
                throw new IOException("Error writing request body: " + e.getMessage(), e);
            }

            int responseCode = conn.getResponseCode();
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(
                            responseCode >= 400 ? conn.getErrorStream() : conn.getInputStream(),
                            StandardCharsets.UTF_8))) {
                String line;
                while ((line = br.readLine()) != null) {
                    response.append(line.trim());
                }
            } catch (IOException e) {
                throw new IOException("Error reading response: " + e.getMessage(), e);
            }

        } catch (MalformedURLException e) {
            System.err.println("Invalid URL: " + e.getMessage());
        } catch (SocketTimeoutException e) {
            System.err.println("Connection timed out: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("I/O error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        return getMessageFromResponse(response.toString());
    }

    private static String getMessageFromResponse(String response) {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> map;
        try {
            map = objectMapper.readValue(response.toString(), new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Неверный формат Json");
        }
        String message = ((ArrayList<LinkedHashMap>) map.get("choices")).get(0).get("message").toString();

        return message.substring(message.indexOf("content=") + 8, message.length() - 1);
    }
}