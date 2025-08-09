import com.google.gson.Gson;

import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * Clase de utilidad para realizar conversiones de moneda
 * usando ExchangeRate-API y Gson.
 */
public class ConversorMoneda {

    // 1) Se recomienda guardar la API key en variable de entorno EXCHANGE_API_KEY
    //    Para pruebas, puedes reemplazar el fallback por tu clave, pero no la subas a GitHub.
    private static final String CLAVE_API = "9823c3a96b4bee5cfd536808";
    private static final String URL_BASE_API = "https://v6.exchangerate-api.com/v6/";

    /**
     * Convierte un monto desde monedaOrigen hacia monedaDestino.
     * @param monedaOrigen  código de la moneda de origen (ej. "USD", "COP")
     * @param monedaDestino código de la moneda de destino (ej. "ARS", "EUR")
     * @param monto         cantidad a convertir
     * @return monto convertido; 0.0 si ocurre algún error
     */
    public static double convertir(String monedaOrigen, String monedaDestino, double monto) {
        try {
            // Usa la clave de entorno si existe; si no, usa un placeholder temporal
            String clave = (CLAVE_API != null && !CLAVE_API.isBlank())
                    ? CLAVE_API
                    : "9823c3a96b4bee5cfd536808";

            String origen = monedaOrigen.toUpperCase();
            String destino = monedaDestino.toUpperCase();

            // URL correcta del endpoint /latest/<ORIGEN>
            String urlCompleta = URL_BASE_API + clave + "/latest/" + origen;

            // 2) Abrir conexión HTTP
            URL url = new URL(urlCompleta);
            HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
            conexion.setRequestProperty("Accept", "application/json");
            conexion.setConnectTimeout(10000);
            conexion.setReadTimeout(10000);

            int codigoHttp = conexion.getResponseCode();
            if (codigoHttp != HttpURLConnection.HTTP_OK) {
                throw new RuntimeException("HTTP " + codigoHttp + " al consultar la API");
            }

            // 3) Deserializar el JSON de la respuesta directamente al record RespuestaTasas
            try (Reader lector = new InputStreamReader(conexion.getInputStream(), StandardCharsets.UTF_8)) {
                RespuestaTasas respuesta = new Gson().fromJson(lector, RespuestaTasas.class);

                if (respuesta == null || respuesta.conversion_rates() == null) {
                    throw new RuntimeException("Respuesta JSON inválida o incompleta");
                }
                if (!"success".equalsIgnoreCase(respuesta.result())) {
                    throw new RuntimeException("La API respondió con error (result=" + respuesta.result() + ")");
                }

                // 4) Buscar la tasa en el mapa "conversion_rates" usando el código de la moneda destino
                Double tasa = respuesta.conversion_rates().get(destino);
                if (tasa == null) {
                    throw new IllegalArgumentException("Moneda destino no válida: " + destino);
                }

                // 5) Multiplicar el monto original por la tasa obtenida
                return tasa * monto;
            }
        } catch (Exception e) {
            System.out.println("Error al convertir: " + e.getMessage());
            return 0.0; // Valor seguro en caso de error
        }
    }
}
