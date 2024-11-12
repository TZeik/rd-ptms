package logic;

import java.io.Serializable;
import java.util.Random;

public class WeatherTrafficEvent implements Serializable {
    private static final long serialVersionUID = 1L;
    private EventType type;
    private double delayFactor;
    
    public enum EventType {
        NORMAL(1.0, "Normal"),
        LIGHT_RAIN(1.2, "Lluvia ligera"),
        HEAVY_RAIN(1.5, "Lluvia fuerte"),
        STORM(2.0, "Tormenta"),
        LIGHT_TRAFFIC(1.3, "Tráfico ligero"),
        HEAVY_TRAFFIC(1.8, "Tráfico pesado"),
        ACCIDENT(2.5, "Accidente"),
        CONSTRUCTION(1.6, "Construcción");
        
        private final double factor;
        private final String description;
        
        EventType(double factor, String description) {
            this.factor = factor;
            this.description = description;
        }
        
        public double getFactor() {
            return factor;
        }
        
        public String getDescription() {
            return description;
        }
    }
    
    public WeatherTrafficEvent() {
        generateRandomEvent();
    }
    
    public void generateRandomEvent() {
        Random rand = new Random();
        EventType[] events = EventType.values();
        // 20% de probabilidad de condiciones normales
        if (rand.nextDouble() < 0.2) {
            this.type = EventType.NORMAL;
        } else {
            // Para el resto, elegir aleatoriamente entre los otros eventos
            this.type = events[rand.nextInt(events.length)];
        }
        
        // Añadir una pequeña variación aleatoria al factor base
        double variation = 0.9 + (rand.nextDouble() * 0.2); // ±10% variación
        this.delayFactor = this.type.getFactor() * variation;
    }
    
    public double getDelayFactor() {
        return delayFactor;
    }
    
    public EventType getType() {
        return type;
    }
    
    @Override
    public String toString() {
        return String.format("%s (Factor de retraso: %.2fx)", 
            type.getDescription(), delayFactor);
    }
}
