package Model;

public class clientModelToRecomendations {
    private int reccomendationID;
    private int client_ID;
    private int product_ID;
    private int probability;
    public clientModelToRecomendations() {
    }

    public clientModelToRecomendations(int reccomendationID, int client_ID, int product_ID, int probability) {
        this.reccomendationID = reccomendationID;
        this.client_ID = client_ID;
        this.product_ID = product_ID;
        this.probability = probability;
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

    public int getProbability() {
        return probability;
    }

    public void setProbability(int probability) {
        this.probability = probability;
    }
}
