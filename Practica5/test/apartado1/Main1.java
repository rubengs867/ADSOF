package apartado1;

import java.util.Collections;
import java.util.Map;

import model.*;

/**
 * Clase principal de pruebas funcionales exhaustivas para el Apartado 1.
 * Evalúa el comportamiento de Dataset, Feature y PersonFeaturizer.
 */
public class Main1 {

  /**
   * Punto de entrada de ejecución.
   *
   * @param args argumentos de línea de comandos
   */
  public static void main(String[] args) {

    testOperacionesBasicasOriginales();
    testAgregacionYDistribucion();
    testCasosExtremosDuplicados();
    testDatasetVacio();
  }

  /**
   * Verifica el funcionamiento original y el tipado genérico seguro.
   */
  public static void testOperacionesBasicasOriginales() {
    System.out.println(" PRUEBA 1: Operaciones Básicas y Tipado Genérico ");
    Dataset<Person> dataSet = buildDataSet();
    System.out.println("Dataset inicial: \n" + dataSet);

    // Comprobamos la extracción segura de tipos distintos (Integer y String)
    Feature<Integer> ages = dataSet.feature("age");
    Feature<Integer> weights = dataSet.feature("weight");
    Feature<String> genders = dataSet.feature("gender");

    System.out.println("\nExtracción de Features:");
    System.out.println("- Edades (Integer): " + ages);
    System.out.println("- Pesos (Integer): " + weights);
    System.out.println("- Géneros (String): " + genders);
  }

  /**
   * Verifica las funciones matemáticas y de conteo de la clase Feature.
   */
  public static void testAgregacionYDistribucion() {
    System.out.println("\n PRUEBA 2: Min, Max y Distribución ");
    Dataset<Person> dataSet = buildDataSet();

    Feature<Integer> ages = dataSet.feature("age");
    Feature<String> genders = dataSet.feature("gender");

    System.out.println("Mínima edad: " + ages.min());
    System.out.println("Máxima edad: " + ages.max());

    // Test de distribución con Strings
    Map<String, Integer> distGeneros = genders.distribution();
    System.out.println("Distribución de géneros: " + distGeneros);

    // Verificamos el comportamiento tras ordenar
    Collections.sort(ages);
    System.out.println("Edades ordenadas independientemente: " + ages);
  }

  /**
   * Prueba de estrés para el método removeDuplicates.
   */
  public static void testCasosExtremosDuplicados() {
    System.out.println("\n PRUEBA 3: Duplicación");
    Dataset<Person> dataSet = buildDataSet();

    // Añadimos explícitamente a personas idénticas a las ya existentes
    Person clonPedro = new Person("PedroClon", 66, 75, 180, true);
    Person clonAna = new Person("AnaClon", 47, 54, 158, false);

    // Añadimos a alguien nuevo
    Person marta = new Person("Marta", 22, 60, 170, false);

    dataSet.addAll(new Person[] { clonPedro, clonAna, marta });

    int sizeAntes = dataSet.feature("age").size();
    System.out.println("Tamaño antes de limpiar duplicados: " + sizeAntes);

    boolean eliminoAlgo = dataSet.removeDuplicates();
    int sizeDespues = dataSet.feature("age").size();

    System.out.println("¿Se eliminaron duplicados? " + eliminoAlgo);
    System.out.println("Tamaño tras limpiar duplicados: " + sizeDespues);

    // Si intentamos eliminar de nuevo, debería devolver false
    boolean eliminoDeNuevo = dataSet.removeDuplicates();
    System.out.println("¿Elimina algo en una segunda pasada? " + eliminoDeNuevo);
  }

  /**
   * Casos Frontera. Dataset vacío.
   */
  public static void testDatasetVacio() {
    System.out.println("\n PRUEBA 4: Manejo de Dataset Vacío");
    Dataset<Person> emptySet = new Dataset<>(new PersonFeaturizer());

    Feature<Integer> edadesVacias = emptySet.feature("age");

    System.out.println("Feature vacía: " + edadesVacias);
    System.out.println("Min en feature vacía: " + edadesVacias.min());
    System.out.println("Max en feature vacía: " + edadesVacias.max());
    System.out.println("Distribución en feature vacía: " + edadesVacias.distribution());

    boolean eliminaVacios = emptySet.removeDuplicates();
    System.out.println("Llamada a removeDuplicates en set vacío: " + eliminaVacios);
  }

  /**
   * Método de construcción de datos
   */
  public static Dataset<Person> buildDataSet() {
    Person people[] = {
        new Person("Pedro", 66, 75, 180, true),
        new Person("Ana", 47, 54, 158, false),
        new Person("Luis", 34, 75, 176, true),
        new Person("Rosa", 47, 54, 158, false)
    };

    // Featurizer para Person
    Dataset<Person> dataSet = new Dataset<>(new PersonFeaturizer());
    dataSet.addAll(people);
    return dataSet;
  }
}