package apartado4;

import labeledDataset.LabelProvider; // Ajusta el import a tu paquete real

public class ShouldIPlayTennisToday implements LabelProvider<Weather, Boolean> {
    @Override
    public Boolean getLabel(Weather w) {
        // Reglas inventadas 
        // Si está soleado y hace calor -> true (jugamos)
        if (w.getCondition() == WeatherCondition.SUNNY && w.getTemperature() == Temperature.HOT)
            return true;
        // Si está nublado (OVERCAST) -> siempre true
        if (w.getCondition() == WeatherCondition.OVERCAST)
            return true;
        // En cualquier otro caso (ej. lloviendo o hace frío) -> false
        return false;
    }
}