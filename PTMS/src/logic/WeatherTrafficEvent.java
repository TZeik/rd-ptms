package logic;

import java.io.Serializable;
import java.util.Random;

public class WeatherTrafficEvent implements Serializable {
    private static final long serialVersionUID = 1L;
    private EventType type;
    private double delayFactor;
    
    public enum EventType {
        NORMAL(1.0, "Normal", 20),
        LIGHT_RAIN(1.2, "Lluvia ligera", 15),
        HEAVY_RAIN(1.5, "Lluvia fuerte", 15),
        STORM(2.0, "Tormenta", 10),
        LIGHT_TRAFFIC(1.3, "Tráfico ligero", 15),
        HEAVY_TRAFFIC(1.8, "Tráfico pesado", 10),
        ACCIDENT(2.5, "Accidente", 5),
        CONSTRUCTION(1.6, "Construcción", 10);
        
        private final double factor;
        private final String description;
        private final int probability;
        
        EventType(double factor, String description, int probability) {
            this.factor = factor;
            this.description = description;
            this.probability = probability;
        }
        
        public double getFactor() {
            return factor;
        }
        
        public String getDescription() {
            return description;
        }
        
        public int getProbability() {
            return probability;
        }
    }
    
    public WeatherTrafficEvent() {
        generateRandomEvent();
    }
    
    public void generateRandomEvent() {
        Random rand = new Random();
        int randomNum = rand.nextInt(100) + 1; // Número entre 1 y 100
        int accumulator = 0;
        
        for (EventType event : EventType.values()) {
            accumulator += event.getProbability();
            if (randomNum <= accumulator) {
                this.type = event;
                break;
            }
        }
        
        // Si por alguna razón no se asignó un tipo, usar NORMAL como fallback
        if (this.type == null) {
            this.type = EventType.NORMAL;
        }
        
        // Añadir una pequeña variación aleatoria al factor base (±10%)
        double variation = 0.9 + (rand.nextDouble() * 0.2);
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