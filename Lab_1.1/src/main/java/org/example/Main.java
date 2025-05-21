package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        String filePath = "src/main/resources/challenge.jsonl";
        ObjectMapper mapper = new ObjectMapper();

        //Leer cada JSON del archivo JSONL
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                Map<String, Object> dictionary = mapper.readValue(line, Map.class);

                //Se separa en listas los input1 e input2
                List<Map<String, Boolean>> input1 = (List<Map<String, Boolean>>) dictionary.get("input1");
                List<String> input2 = (List<String>) dictionary.get("input2");

                //Map<String, Object> input1 = (Map<String, Object>) dictionary.get("input1");
                //Map<String, Object> input2 = (Map<String, Object>) dictionary.get("input2");

//                Map<String, Boolean> services = (Map<String, Boolean>) input1.get("services");
//                Map<String, List<Map<String, Object>>> builds = (Map<String, List<Map<String, Object>>>) input1.get("builds");
//
//                List<String> requiredServices = (List<String>) input2.get("requiredServices");
//                String typeBuilder = (String) input2.get("typeBuilder");
//                String minDanger = (String) input2.get("minDanger");
//                Boolean wannaPetFriendly = (Boolean) input2.get("wannaPetFriendly");
//                String commercialActivity = (String) input2.get("commercialActivity");
//                Double budget = (Double) input2.get("budget");

                //Lista de mejores apartamentos
                List<Integer> bestApartments = findRecommendations(input1, input2);

                System.out.println(bestApartments);


            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo: " + e.getMessage());
        }
    }

    //Funcion para encontrar las recomendaciones de apartamentos
    private static List<Integer> findRecommendations(List<Map<String, Boolean>> input1, List<String> input2) {
        List<Integer> recommendations = new ArrayList<>();
        int minTotalDistance = Integer.MAX_VALUE;
        int minMaxDistance = Integer.MAX_VALUE;
        int n = input1.size();

        //Ciclo para recorrer cada apartamento en la lista
        for (int i = 0; i < n; i++) {
            int totalDistance = 0;
            int maxDistance = 0;
            boolean validApartment = true;
            Map<String, Boolean> apartment = input1.get(i);

            //Ciclo para recorrer los requerimientos
            for (String requirement : input2) {
                //Verificacion de presencia del requerimiento
                if (!apartment.getOrDefault(requirement, false))
                {
                    //Se busca la distancia al apartamento mas cercano
                    int distance = findDistance(input1, i, requirement, n);
                    //Si no se encuentra, el apartamento no es valido y se termina la busqueda de req
                    if (distance == -1)
                    {
                        validApartment = false;
                        break;
                    }
                    //Suma de la distancia total
                    totalDistance += distance;
                    //Calculo de la distancia maxima entre apartamentos
                    maxDistance = Math.max(maxDistance, distance);
                }
            }

            //Si el apartamento es valido
            if (validApartment)
            {
                //Si la distancia total es menor a la distancia minima o son iguales y la distancia maxima es menor a la distancia maxima minima
                if (totalDistance < minTotalDistance || (totalDistance == minTotalDistance && maxDistance < minMaxDistance))
                {
                    //Se actualizan los valores de distancias minimas
                    minTotalDistance = totalDistance;
                    minMaxDistance = maxDistance;

                    //Se eliminan las recomendaciones agregadas y se agrega la nueva mejor opción
                    recommendations.clear();
                    recommendations.add(i);
                }
                //Si las distancias son iguales a las distancias minimas
                else if (totalDistance == minTotalDistance && maxDistance == minMaxDistance)
                {
                    //Se agrega una nueva recomendacion
                    recommendations.add(i);
                }
            }
        }
        return recommendations;
    }

    private static int findDistance(List<Map<String, Boolean>> input1, int startIndex, String requirement, int n)
    {
        for (int i = 1; i < n; i++) {
            //Recorrer hacia adelante del apartamento actual
            int index = startIndex + i;
            if (index < n && input1.get(index).getOrDefault(requirement, false)) {
                return i;
            }

            //Recorrer hacia atras del apartamento actual
            index = startIndex - i;
            if (index>= 0 && input1.get(index).getOrDefault(requirement, false)) {
                return i;
            }
        }

        //Si no se encuentra ningun apartamento con el requerimiento
        return -1;
    }
}