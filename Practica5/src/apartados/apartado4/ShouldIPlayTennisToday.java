package apartados.apartado4;

import model.ILabelProvider;

/**
 * Proveedor de etiquetas booleanas que determina si se debería
 * jugar al tenis según las condiciones meteorológicas.
 */
public class ShouldIPlayTennisToday implements ILabelProvider<Weather, Boolean> {

  /**
   * Obtiene la etiqueta asociada a una condición meteorológica.
   *
   * @param w condición meteorológica evaluada
   * @return {@code true} si se recomienda jugar; {@code false} en caso contrario
   */
  @Override
  public Boolean getLabel(Weather w) {
    if (w.getCondition() == WeatherCondition.SUNNY
        && w.getTemperature() == Temperature.HOT) {
      return true;
    }

    if (w.getCondition() == WeatherCondition.OVERCAST) {
      return true;
    }

    return false;
  }
}