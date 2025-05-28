package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    private static final ObjectMapper mapper = new ObjectMapper();
    
    public static void main(String[] args) {
        try {
            List<Property> properties = loadProperties("src/main/resources/properties.json");
            List<Client> clients = loadClients("src/main/resources/clients.jsonl");
            processRecommendations(properties, clients);
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static List<Property> loadProperties(String filePath) throws IOException {
        List<Property> properties = new ArrayList<>();
        Map<String, Object> propertiesData = mapper.readValue(new File(filePath), Map.class);
        Map<String, List<Map<String, Object>>> builds = 
            (Map<String, List<Map<String, Object>>>) propertiesData.get("builds");
        
        builds.forEach((type, buildings) -> {
            buildings.forEach(building -> {
                properties.add(new Property(type, building));
            });
        });
        
        return properties;
    }
    
    private static List<Client> loadClients(String filePath) throws IOException {
        List<Client> clients = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                Map<String, Object> clientData = mapper.readValue(line, Map.class);
                clients.add(new Client(clientData));
            }
        }
        return clients;
    }
    
    private static void processRecommendations(List<Property> properties, List<Client> clients) {
        clients.forEach(client -> {
            Map<String, Object> requirements = client.getRequirements();
            
            List<Property> recommendations = properties.stream()
                .filter(p -> p.meetsRequirements(requirements))
                .sorted(Comparator.comparingDouble(Property::getPrice))
                .limit(3)
                .collect(Collectors.toList());
            
            System.out.println("\nRecomendaciones para " + client.getFullName() + ":");
            recommendations.forEach(p -> 
                System.out.println("- " + p.getId() + " (" + p.getType() + "): Q" + p.getPrice())
            );
            
            if (!recommendations.isEmpty()) {
                Property selected = recommendations.get(0);
                System.out.println("\nContrato generado para " + selected.getId());
                System.out.println(Contract.generateContract(client, selected, selected.getPrice()));
            }
        });
    }
}