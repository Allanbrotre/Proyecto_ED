package org.example;

import java.security.MessageDigest;
import java.util.Base64;
import java.util.Date;

public class Contract {
    public static String generateContract(Client client, Property property, double amount) {
        String contractDetails = String.format(
            "Contrato de alquiler entre %s y propiedad %s\n" +
            "Precio acordado: Q%.2f\n" +
            "Fecha: %s\n" +
            "Términos y condiciones aplicables...",
            client.getFullName(),
            property.getId(),
            amount,
            new Date()
        );
        
        String signature = generateSignature(client, property, amount);
        
        return contractDetails + "\nFirma digital: " + signature;
    }
    
    private static String generateSignature(Client client, Property property, double amount) {
        try {
            String data = client.getDpi() + property.getId() + amount;
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(data.getBytes());
            return Base64.getEncoder().encodeToString(hash);
        } catch (Exception e) {
            throw new RuntimeException("Error generando firma", e);
        }
    }
}