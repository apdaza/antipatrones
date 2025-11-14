// ANTIEJEMPLO EDUCATIVO: contiene antipatrones a propósito

import java.util.*;
import java.nio.file.*;
import java.io.*;

public class GlobalManager {
    private Path dbPath = Paths.get("users.csv");

    public List<Map<String, String>> loadUsers() { 
        List<Map<String, String>> list = new ArrayList<>();
        if (!Files.exists(dbPath)) return list;
        try (BufferedReader br = Files.newBufferedReader(dbPath)) {
            String line;
            while ((line = br.readLine()) != null) {
                // CSV: id;name;tier
                String[] parts = line.split(";");
                Map<String,String> u = new HashMap<>();
                u.put("id", parts.length > 0 ? parts[0] : "");
                u.put("name", parts.length > 1 ? parts[1] : "");
                u.put("tier", parts.length > 2 ? parts[2] : "");
                list.add(u);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void printUser(Map<String, String> u) { 
        System.out.printf("[%s] %s - tier=%s%n", u.get("id"), u.get("name"), u.get("tier"));
    }

    public double discountForOrder(Map<String, String> user, double total) {
        String tier = user.getOrDefault("tier", "");
        if ("gold".equals(tier) && total > 200) return total * 0.20;
        if ("silver".equals(tier) && total > 50) return total * 0.10;
        return 0.0;
    }

    public double shipCostDomestic(double weight, double distanceKm) {
        // Magic Numbers: 6, 0.3, 250, 2
        double base = 6;
        double variable = weight * 0.3 + (distanceKm / 250);
        if (weight > 15) variable += 2;
        return base + variable;
    }

    public double shipCostInternational(double weight, double distanceKm) {
        double base = 8; // distinto
        double variable = weight * 0.3 + (distanceKm / 250);
        if (weight >= 15) variable += 3; // >= y +3 en vez de +2
        return base + variable;
    }

    public void run() {
        List<Map<String,String>> users = loadUsers();
        for (Map<String,String> u : users) {
            printUser(u);
            double total = 199.99; // Magic number
            double d = discountForOrder(u, total);
            System.out.println("Descuento: " + d);
            System.out.println("Envío nacional: " + shipCostDomestic(12, 900));
            System.out.println("Envío internacional: " + shipCostInternational(12, 900));
        }
    }

    public static void main(String[] args) {
        new GlobalManager().run();
    }
}
