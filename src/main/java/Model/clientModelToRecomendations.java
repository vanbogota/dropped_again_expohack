package Model;

public class clientModelToRecomendations {
    private int reccomendationID;
    private int client_ID;
    private int product_ID;
    private String probability;
    private int recomendedTo;
    public clientModelToRecomendations() {
    }

    public clientModelToRecomendations(int reccomendationID, int client_ID, int product_ID, String probability, int recomendedTo) {
        this.reccomendationID = reccomendationID;
        this.client_ID = client_ID;
        this.product_ID = product_ID;
        this.probability = probability;
        this.recomendedTo = recomendedTo;
    }

    public int getReccomendationID() {
        return reccomendationID;
    }

    public void setReccomendationID(int reccomendationID) {
        this.reccomendationID = reccomendationID;
    }

    public int getClient_ID() {
        return client_ID;
    }

    public void setClient_ID(int client_ID) {
        this.client_ID = client_ID;
    }

    public int getProduct_ID() {
        return product_ID;
    }

    public void setProduct_ID(int product_ID) {
        this.product_ID = product_ID;
    }

    public String getProbability() {
        return probability;
    }

    public void setProbability(String probability) {
        this.probability = probability;
    }

    public int getRecomendedTo() {
        return recomendedTo;
    }

    public void setRecomendedTo(int recomendedTo) {
        this.recomendedTo = recomendedTo;
    }
}
