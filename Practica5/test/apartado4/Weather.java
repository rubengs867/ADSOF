package apartado4;

public class Weather {
    private WeatherCondition condition;
    private Temperature temperature;

    public Weather(WeatherCondition condition, Temperature temperature) {
        this.condition = condition;
        this.temperature = temperature;
    }

    public WeatherCondition getCondition() { return condition; }
    public Temperature getTemperature() { return temperature; }

    @Override
    public String toString() {
        return "Weather(" + condition + ", " + temperature + ")";
    }
}