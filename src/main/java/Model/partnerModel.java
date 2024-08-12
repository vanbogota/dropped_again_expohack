package Model;

public class partnerModel {
    private int id;
    private String name;
    private String description;
    private String address;
    private String dateOfFoundations;
    private String password;
    private String question;
    public partnerModel() {
    }

    public partnerModel(int id, String name, String description, String address, String dateOfFoundations, String password, String question) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.address = address;
        this.dateOfFoundations = dateOfFoundations;
        this.password = password;
        this.question = question;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    public String getDateOfFoundations() {
        return dateOfFoundations;
    }

    public void setDateOfFoundations(String dateOfFoundations) {
        this.dateOfFoundations = dateOfFoundations;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }
}
