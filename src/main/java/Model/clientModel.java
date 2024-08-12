package Model;

public class clientModel {
    private long id;
    private String clientName;
    private String birthDate;
    private String gender;
    private String clientType;
    private String income;
    private String mobile_phone;
    private String email;
    private String address;
    private String workplace_income_amount;
    private String communication_history;
    private String interests;
    private String preferences;
    private String registration_date;
    private String status;
    private String dialogue;

    public clientModel() {
    }

    public clientModel(long id, String clientName, String birthDate, String gender, String clientType, String income, String mobile_phone, String email, String address, String workplace_income_amount, String communication_history, String interests, String preferences, String registration_date, String status, String dialogue) {
        this.id = id;
        this.clientName = clientName;
        this.birthDate = birthDate;
        this.gender = gender;
        this.clientType = clientType;
        this.income = income;
        this.mobile_phone = mobile_phone;
        this.email = email;
        this.address = address;
        this.workplace_income_amount = workplace_income_amount;
        this.communication_history = communication_history;
        this.interests = interests;
        this.preferences = preferences;
        this.registration_date = registration_date;
        this.status = status;
        this.dialogue = dialogue;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    public String getIncome() {
        return income;
    }

    public void setIncome(String income) {
        this.income = income;
    }

    public String getMobile_phone() {
        return mobile_phone;
    }

    public void setMobile_phone(String mobile_phone) {
        this.mobile_phone = mobile_phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getWorkplace_income_amount() {
        return workplace_income_amount;
    }

    public void setWorkplace_income_amount(String workplace_income_amount) {
        this.workplace_income_amount = workplace_income_amount;
    }

    public String getCommunication_history() {
        return communication_history;
    }

    public void setCommunication_history(String communication_history) {
        this.communication_history = communication_history;
    }

    public String getInterests() {
        return interests;
    }

    public void setInterests(String interests) {
        this.interests = interests;
    }

    public String getPreferences() {
        return preferences;
    }

    public void setPreferences(String preferences) {
        this.preferences = preferences;
    }

    public String getRegistration_date() {
        return registration_date;
    }

    public void setRegistration_date(String registration_date) {
        this.registration_date = registration_date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDialogue() {
        return dialogue;
    }

    public void setDialogue(String dialogue) {
        this.dialogue = dialogue;
    }
}
