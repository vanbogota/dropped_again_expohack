package config;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class DatasLoader {
    private final Properties properties = new Properties();

    public DatasLoader(String filePath) {
        try (FileInputStream input = new FileInputStream(filePath)) {
            properties.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void save(String filePath) {
        try (FileOutputStream output = new FileOutputStream(filePath)) {
            properties.store(output, null); // null - это комментарий
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public String getUserName() {
        return properties.getProperty("user.name");
    }

    public String getUserPassword() {
        return properties.getProperty("user.password");
    }
    public String getUserId(){
        return properties.getProperty("user.id");
    }

    public void setUserName(String userName) {
        properties.setProperty("user.name", userName);
    }
    public void setUserPassword(String password) {
        properties.setProperty("user.password", password);
    }
    public void setUserId(int id){
        properties.setProperty("user.id", String.valueOf(id));
    }
}