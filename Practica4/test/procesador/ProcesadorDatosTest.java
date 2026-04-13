package procesador;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import conversor.ConversorCelsiusKelvin;
import unidad.UnidadTemperatura;

public class ProcesadorDatosTest {

  private ProcesadorDatos procesador;
  private LocalDateTime ahora;


  private static final double DELTA = 0.001;

  @Before
  public void setUp() {

    procesador = new ProcesadorDatos(UnidadTemperatura.CELSIUS);
    ahora = LocalDateTime.now();
  }


  @Test
  public void testEstaVacioInicialmente() {
    assertTrue("El procesador debe nacer vacío", procesador.estaVacio());

    Collection<Double> valores = procesador.getValores();
    assertTrue("La colección de valores debe estar vacía", valores.isEmpty());
  }

  // 2. Prueba de almacenamiento y cálculos matemáticos base
  @Test
  public void testProcesarLecturaYEstadisticasBasicas() {

    procesador.procesarLectura(ahora, 10.0);
    procesador.procesarLectura(ahora.plusMinutes(1), 20.0);
    procesador.procesarLectura(ahora.plusMinutes(2), 30.0);

    assertFalse("El procesador ya no debe estar vacío tras las lecturas", procesador.estaVacio());

    assertEquals("El mínimo debe ser 10.0", 10.0, procesador.getMinimo(), DELTA);
    assertEquals("El máximo debe ser 30.0", 30.0, procesador.getMaximo(), DELTA);
    assertEquals("La media debe ser 20.0", 20.0, procesador.getMedia(), DELTA);

    Collection<Double> valores = procesador.getValores();
    assertEquals("Debe haber guardado 3 valores", 3, valores.size());
  }

  @Test
  public void testEstadisticasConConversorAplicado() throws Exception {
    

    ConversorCelsiusKelvin conversor = new ConversorCelsiusKelvin();
    procesador.setConversor(conversor);

    procesador.procesarLectura(ahora, 0.0);
    procesador.procesarLectura(ahora.plusMinutes(1), 100.0);

    assertEquals("El mínimo guardado debe ser 273.15K", 273.15, procesador.getMinimo(), DELTA);
    assertEquals("El máximo guardado debe ser 373.15K", 373.15, procesador.getMaximo(), DELTA);
    assertEquals("La media guardada debe ser 323.15K", 323.15, procesador.getMedia(), DELTA);
  }


  @Test
  public void testEstadisticasVaciasNoFallan() {

    assertEquals(0.0, procesador.getMedia(), DELTA);
    assertEquals(0.0, procesador.getMinimo(), DELTA);
    assertEquals(0.0, procesador.getMaximo(), DELTA);
  }
}