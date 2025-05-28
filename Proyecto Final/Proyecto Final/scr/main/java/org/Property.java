package org.example;

import java.util.List;
import java.util.Map;

public class Property {
    private String id;
    private String type;
    private double price;
    private boolean isPetFriendly;
    private String zoneDangerous;
    private List<String> services;
    private List<String> commercialActivities;
    
    public Property(String type, Map<String, Object> data) {
        this.id = (String) data.get("id");
        this.type = type;
        this.price = ((Number) data.getOrDefault("price", 0)).doubleValue();
        
        if (type.equals("Apartments")) {
            this.isPetFriendly = (boolean) data.getOrDefault("isPetFriendly", false);
            this.services = (List<String>) data.getOrDefault("services", List.of());
        } 
        else if (type.equals("Houses")) {
            this.zoneDangerous = (String) data.getOrDefault("zoneDangerous", "Unknown");
        }
        else if (type.equals("Premises")) {
            this.commercialActivities = (List<String>) data.getOrDefault("commercialActivities", List.of());
        }
    }
    
    public boolean meetsRequirements(Map<String, Object> requirements) {
        if (price > ((Number) requirements.get("budget")).doubleValue()) {
            return false;
        }
        
        if (requirements.containsKey("wannaPetFriendly") && !isPetFriendly && requirements.get("wannaPetFriendly").equals(true)) {
            return false;
        }
        
        if (requirements.containsKey("minDanger") && type.equals("Houses")) {
            String minDanger = (String) requirements.get("minDanger");
            Map<String, List<String>> securityLevels = Map.of(
                "Red", List.of("Red"),
                "Orange", List.of("Red", "Orange"),
                "Yellow", List.of("Red", "Orange", "Yellow"),
                "Green", List.of("Red", "Orange", "Yellow", "Green")
            );
            if (!securityLevels.get(minDanger).contains(zoneDangerous)) {
                return false;
            }
        }
        
        return true;
    }

    public String getId() { return id; }
    public String getType() { return type; }
    public double getPrice() { return price; }
    public boolean isPetFriendly() { return isPetFriendly; }
    public String getZoneDangerous() { return zoneDangerous; }
    public List<String> getServices() { return services; }
    public List<String> getCommercialActivities() { return commercialActivities; }
}