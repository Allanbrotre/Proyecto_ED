package org.example;

import java.util.Map;

public class Client {
    private String dpi;
    private String firstName;
    private String lastName;
    private double budget;
    private boolean wannaPetFriendly;
    private String minDanger;
    private String commercialActivity;
    
    public Client(Map<String, Object> clientData) {
        this.dpi = (String) clientData.get("dpi");
        this.firstName = (String) clientData.get("firstName");
        this.lastName = (String) clientData.get("lastName");
        this.budget = ((Number) clientData.get("budget")).doubleValue();
        this.wannaPetFriendly = (boolean) clientData.getOrDefault("wannaPetFriendly", false);
        this.minDanger = (String) clientData.getOrDefault("minDanger", "Green");
        this.commercialActivity = (String) clientData.getOrDefault("commercialActivity", "");
    }
    
    public Map<String, Object> getRequirements() {
        return Map.of(
            "budget", budget,
            "wannaPetFriendly", wannaPetFriendly,
            "minDanger", minDanger,
            "commercialActivity", commercialActivity
        );
    }
    
    public String getFullName() {
        return firstName + " " + lastName;
    }
    
    public String getDpi() { return dpi; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public double getBudget() { return budget; }
    public boolean isWannaPetFriendly() { return wannaPetFriendly; }
    public String getMinDanger() { return minDanger; }
    public String getCommercialActivity() { return commercialActivity; }
}