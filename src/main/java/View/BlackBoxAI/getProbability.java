package View.BlackBoxAI;

import DAO.DBOperationsForRecommendation;
import DAO.DBoperationsForProducts;
import Model.clientModel;
import Model.productModel;
import View.mainWindow.crmWindow;
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
    private DBOperationsForRecommendation operations2;
    @Autowired
    public void setOperations2(DBOperationsForRecommendation operations2) {
        this.operations2 = operations2;
    }

    @Autowired
    public void setOperations(DBoperationsForProducts operations) {
        this.operations = operations;
    }
    public void createWindow(clientModel client) {
        List<productModel> list = operations.allProducts();
        String systemMessage =
                "Ваша задача на сегодня состоит в том, чтобы определить то, насколько высока вероятность того, что идеальная система рекомендаций посоветует конкретный продукт конкретному клиенту. " +
                        "Вероятность может быть Низкой, Средней или Высокой. " +
                        "Указывай рядом с вероятностью ID продукта, который указан первым параметром. " +
                        "Пример входных данных (продукты): " +
                        "1. productId=29, partnerId=0, description='крутой байк', name='Велосипед', price='25 000', promotions='25%'; " +
                        "2. productId=30, partnerId=0, description='Кредит на разные услуги', name='Кредит', price='18% годовых', promotions='Если возьмете кредит прямо сейчас - снимем 1%'; " +
                        "3. productId=31, partnerId=0, description='Интересная книга для любителей почитать', name='Книга', price='250 рублей', promotions='10%'; " +
                        "ЭТО БЫЛ ВСЕГО ЛИШЬ ПРИМЕР ,ЕГО ТЫ НЕ ДОЛЖЕН ВООБЩЕ УЧИТЫВАТЬ.ТЫ ДОЛЖЕН ОПИРАТЬСЯ НА ПАРАМЕТРЫ КЛИЕНТА-ЕГО ИНТЕРЕСЫ И ПРЕДПОЧТЕНИЯ.ЭТИ 3 ПРИМЕРА ТЫ ВООБЩЕ НЕ БЕРЕШЬ В УЧЕТ,ПРОСТО ЩАБУДЬ О НИХ" +
                        "Не генерируйте продукты, используйте только те, которые переданы,а также ничего не нужно обосновывать,просто - id.вероятность.Пиши на русском языке.ОЧЕНЬ ВАЖНО ,ОТФАРМАТИРУЙ СТРОКУ СЛЕДУЮЩИМ ОБРАЗОМ (productId(простое число без текста))=(вероятность(возвращай вероятность словами 'Низкая' или 'Высокая' или 'Средняя' для всех продуктов)).САМЫЙ ВЫЖНЫЙ ПУНКТ-ВЫВОДИ ДАННЫЕ ВСЕГДА В ТАКОМ ФОРМАТЕ,ПРИМЕР:29=(Низкая).УЧИТЫВАЙ ВЕРОЯТНОСТИ ДЛЯ ВСЕХ ПРОДУКТОВ.ОЧЕНЬ ВАЖНО УЧИТЫВАТЬ ПРЕДПОЧТЕНИЯ КЛИЕНТА,К ПРИМЕРУ:если он не любит книги-не стоит ему их рекомендовать,а стоит порекомендовать фильм.Вот список продуктов ТЫ ДОЛЖЕН ВЫДАВАТЬ ВЕРОЯТНОСТИ ТОЛЬКО ДЛЯ ЭТИХ ПРОДУКТОВ: " ;
         for (int i = 0; i < list.size(); i++) {
            systemMessage += (i + 1) + " " + list.get(i) + "; ";
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

        String str = (generate(systemMessage, userMessage.toString()));
        System.out.println(str);

        HashMap<Integer, String> dataMap = new HashMap<>();

        StringBuilder number = new StringBuilder();
        StringBuilder value = new StringBuilder();

        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (Character.isDigit(c)) {
                number.append(c);
            } else if (c == '=') {
                int j = i + 1;
                if(str.charAt(j)=='(')
                    j++;
                while (j < str.length() && Character.isLetter(str.charAt(j))) {
                    value.append(str.charAt(j));
                    j++;
                }
                dataMap.put(Integer.parseInt(number.toString()), value.toString());
                number.setLength(0);
                value.setLength(0);
            }
        }
        System.out.println("----------");
      try{
          for (Integer key : dataMap.keySet()) {
              System.out.println(key+" "+dataMap.get(key));
              if(dataMap.get(key).equals("Средняя")||dataMap.get(key).equals("Высокая")){
                  productModel productModel=operations2.getProductById(key);
                  operations2.addToReccomendations(client,dataMap.get(key),productModel,(int)productModel.getPartnerId());
              }
          }
          JOptionPane.showMessageDialog(this, "Данные успешно обработаны!Для просмотре результата нажмите кнопку 'лидогенерация'.");
      }catch (Exception ex){
          JOptionPane.showMessageDialog(this, "Произошла непредвиденная ошибка.Загрузите файл повторно.");
      }
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