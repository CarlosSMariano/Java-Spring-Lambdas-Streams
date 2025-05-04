package com.br.desafio.fipe.principal;

import com.br.desafio.fipe.models.Description;
import com.br.desafio.fipe.models.Modelos;
import com.br.desafio.fipe.models.Vehicle;
import org.springframework.boot.Banner;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Principal {
    private static final String BASE_URL = "https://parallelum.com.br/fipe/api/v1/";
    private static final Scanner input = new Scanner(System.in);
    private static final Conversor conversor = new Conversor();

    private static String firstLetterUpper(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return Character.toUpperCase(str.charAt(0)) + str.substring(1);
    }

    private static String getUserOption() {
        System.out.print("""
                Carros
                Motos
                Caminhões
                Digite abaixo qual gostaria de consultar:
                ->""");
        String option = input.nextLine().toLowerCase().replace(" ", "");

        return switch (option) {
            case "car", "carr", "carro", "carros" -> "carros";
            case "mot", "moto", "motos" -> "motos";
            case "caminhao", "caminhoes", "caminhão", "caminhões" -> "caminhoes";
            default -> throw new IllegalArgumentException("Opção inválida: " + option);
        };
    }

    private static List<Description> getBrands(String url) {
        return conversor.translateListJson(url, Description.class);
    }

    private static void displayTopBrands(List<Description> brands) {
        if (brands == null || brands.isEmpty()) {
            System.out.println("Nenhuma marca disponível");
            return;
        }

        brands.stream()
                .sorted(Comparator.comparing(Description::name))
                .limit(10)
                .forEach(m -> System.out.printf("Código: %s Nome: %s%n", m.code(), m.name()));

    }

    private static String getBrandsOption(String vehicleType, String url) {
        System.out.printf("\nDigite o código da marca para consultar %s \n->", firstLetterUpper(vehicleType));
        return input.nextLine();
    }

    private static Modelos getModels(String brandsUrl, String brandsOptional) {
        var modelUrl = "%s%s/modelos/".formatted(brandsUrl, brandsOptional);
        Modelos models = conversor.translateJson(modelUrl, Modelos.class);

        models.modelos().forEach(m -> System.out.printf("Nome: %s%n", m.name()));

        return models;
    }

    private static List<Description> filterModels(Modelos modelos) {
        System.out.print("\nDigite um trecho do produto que deseja\n->");
        var product = input.nextLine().toLowerCase();

        return modelos.modelos().stream()
                .filter(m -> m.name().toLowerCase().contains(product))
                .peek(m -> System.out.printf("Código: %s Descrição: %s%n", m.code(), m.name()))
                .toList();
    }

    private static String getProduct() {
        System.out.print("Digite o código do modelo para consultar valores\n->");
        return input.nextLine();
    }

    private static List<Vehicle> getVehiclesDetails(String modelUrl, String productCode) {
        var productYearURL = "%s%s/anos/".formatted(modelUrl, productCode);
        List<Description> years = conversor.translateListJson(productYearURL, Description.class);

        return years.stream()
                .map(
                        year -> {
                            var vehicleValues = "%s%s".formatted(productYearURL, year.code());
                            return conversor.translateJson(vehicleValues, Vehicle.class);
                        }
                )
                .toList();
    }

    private static void displayVehicles(List<Vehicle> vehicle) {
        System.out.println("Todos os veículos filtratos por anos:");
        vehicle.stream()
                .sorted(Comparator.comparing(Vehicle::year))
                .forEach(
                        v -> System.out.printf("Moldelo: %s | Marca: %s | Ano: %s |  " +
                                        "Combustivel: %s | Valor: %s| %n",
                                v.model(), v.brand(), v.year(), v.fuel(), v.value())
                );
    }

    public void menu() {
        var vehicleType = getUserOption();
        var brandsUrl = "%s%s/marcas/".formatted(BASE_URL, vehicleType);

        List<Description> brands = getBrands(brandsUrl);
        displayTopBrands(brands);

        var brandsOptional = getBrandsOption(vehicleType, brandsUrl);

        Modelos models = getModels(brandsUrl, brandsOptional);

        List<Description> description = filterModels(models);

        var productCode = getProduct();

        List<Vehicle> vehicles = getVehiclesDetails(
                "%s%s/modelos/".formatted(brandsUrl, brandsOptional),
                productCode);

        displayVehicles(vehicles);
    }
}
