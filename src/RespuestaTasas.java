import java.util.Map;

public record RespuestaTasas(
        String result,                      // "success" o "error". Útil para validar antes de convertir
        String base_code,                   // Moneda base (ej. "USD")
        Map<String, Double> conversion_rates // Mapa de "CODIGO_MONEDA" -> tasa (ej. "COP" -> 4067.09)
) {}
