package View.BlackBoxAI;

import DAO.DBoperationsForProducts;
import Model.clientModel;
import Model.productModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.swing.*;
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

@Component("blackBoxAsk")
@Scope("prototype")
public class getProbability extends JFrame {
    private DBoperationsForProducts operations;

    @Autowired
    public void setOperations(DBoperationsForProducts operations) {
        this.operations = operations;
    }

    public void createWindow(clientModel client) {
        List<productModel>list=operations.allProducts();

        String systemMessage = "Ты - менеджер партнёрского сообщества из множества компаний, у каждой компании есть продукт, который она может предложить. Твоя цель состоит в том, чтобы понять, какие предложения тебе нужно порекомендовать клиенту. " +
                "Рекомендовать продукт на основе его популярности не нужно, нужно рекомендовать только то, что подойдёт конкретному клиенту. Рекомендацию отправь коротким сообщением типа '1, 3, 6'. Вот список предложений: ";
        for (int i = 0; i < list.size(); i++) {
            systemMessage += (i + 1)+ " " + list.get(i) + "; ";
            System.out.println(systemMessage);
        }
        systemMessage = systemMessage.substring(0, systemMessage.length() - 2);



        Map<String, String> user = new HashMap<>();
        user.put("name", client.getClientName());
        user.put("birthdate", client.getBirthDate());
        user.put("gender", client.getGender());
        user.put("address", client.getAddress());
        user.put("income", client.getWorkplace_income_amount());
        user.put("registration_date", client.getRegistration_date());
        user.put("interests", client.getInterests());
        user.put("preferences", client.getPreferences());
        user.put("communication_history", client.getCommunication_history());
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        user.put("today", today.format(formatter));


        StringBuilder userMessage = new StringBuilder();
        user.forEach((prop, val) -> userMessage.append(prop).append(": ").append(val).append(", "));
        userMessage.delete(userMessage.length() - 2, userMessage.length());

        System.out.println(generate(systemMessage, userMessage.toString()));

    }

    public static String generate(String userPrompt) {
        return generate("", userPrompt, -1, 0);
    }

    public static String generate(String systemPrompt, String userPrompt) {
        return generate(systemPrompt, userPrompt, -1, 0);
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
                    """ + (temperature == -1 ? "" : ",\n \"temperature\": " + temperature) + """
                    """ + (maxTokens == 0 ? "" : ",\n \"max_tokens\": " + maxTokens) + """
                    }
                    """,systemPrompt, userPrompt);
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