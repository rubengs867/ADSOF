package apartado4;

import model.ILabelProvider;

public class ShouldIPlayTennisToday implements ILabelProvider<Weather, Boolean> {
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