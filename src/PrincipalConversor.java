import java.util.Locale;
import java.util.Scanner;

/**
 * Punto de entrada de la aplicación de consola.
 * Muestra un menú, pide datos y utiliza ConversorMoneda para realizar la conversión.
 */
public class PrincipalConversor {
    public static void main(String[] args) {
        Scanner entrada = new Scanner(System.in).useLocale(Locale.US);
        int opcion = 0;

        do {
            System.out.println("""
                Bienvenido/a al conversor de Monedas

                1) Dólar (USD)  ==> Peso Argentino (ARS)
                2) Peso Argentino (ARS) ==> Dólar (USD)
                3) Dólar (USD)  ==> Real Brasileño (BRL)
                4) Real Brasileño (BRL) ==> Dólar (USD)
                5) Dólar (USD)  ==> Peso Colombiano (COP)
                6) Peso Colombiano (COP) ==> Dólar (USD)
                7) Salir
                *****************************************
                Elija una opción:""");

            // Leer opción como String y parsear para evitar el clásico problema de nextInt/nextLine
            String opcionTexto = entrada.nextLine().trim();
            try {
                opcion = Integer.parseInt(opcionTexto);
            } catch (NumberFormatException e) {
                System.out.println("Opción inválida.\n");
                continue;
            }

            String monedaOrigen = "";
            String monedaDestino = "";

            switch (opcion) {
                case 1 -> { monedaOrigen = "USD"; monedaDestino = "ARS"; }
                case 2 -> { monedaOrigen = "ARS"; monedaDestino = "USD"; }
                case 3 -> { monedaOrigen = "USD"; monedaDestino = "BRL"; }
                case 4 -> { monedaOrigen = "BRL"; monedaDestino = "USD"; }
                case 5 -> { monedaOrigen = "USD"; monedaDestino = "COP"; }
                case 6 -> { monedaOrigen = "COP"; monedaDestino = "USD"; }
                case 7 -> {
                    System.out.println("Gracias por usar el conversor de monedas.");
                    break;
                }
                default -> {
                    System.out.println("Opción no válida.\n");
                    continue;
                }
            }

            if (opcion >= 1 && opcion <= 6) {
                System.out.print("Ingrese la cantidad que desea convertir: ");
                String montoTexto = entrada.nextLine().trim();

                double monto;
                try {
                    monto = Double.parseDouble(montoTexto);
                } catch (NumberFormatException e) {
                    System.out.println("Monto inválido.\n");
                    continue;
                }

                double resultado = ConversorMoneda.convertir(monedaOrigen, monedaDestino, monto);
                System.out.printf(Locale.US,
                        "El valor %.2f %s corresponde al valor final de ===> %.2f %s%n%n",
                        monto, monedaOrigen, resultado, monedaDestino);
                System.out.println("=================================================================");
            }

        } while (opcion != 7);

        entrada.close();
    }
}
