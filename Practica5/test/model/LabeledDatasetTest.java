package model;

import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

/**
 * Clase de pruebas unitarias para {@link LabeledDataset}.
 * Valida la correcta asociación de etiquetas a objetos y la funcionalidad de
 * creación de subconjuntos manteniendo la consistencia de los proveedores.
 */
public class LabeledDatasetTest {

  /**
   * Entidad de dominio simplificada para pruebas de etiquetado.
   */
  static class Product {
    String id;
    double price;

    /**
     * Constructor de producto.
     * 
     * @param id    Identificador único.
     * @param price Precio unitario.
     */
    Product(String id, double price) {
      this.id = id;
      this.price = price;
    }
  }

  /**
   * Implementación de featurizer para extraer propiedades de Product.
   */
  static class ProductFeaturizer implements IFeaturizer<Product> {

    /**
     * @return Lista de características extraíbles.
     */
    @Override
    public List<String> featureDeInteres() {
      return Arrays.asList("id", "price");
    }

    /**
     * Obtiene el valor de una característica por nombre.
     * 
     * @param object      Producto origen.
     * @param featureName Nombre de la característica.
     * @return Valor comparable de la propiedad.
     */
    @Override
    public Comparable<?> datoDeInteres(Product object, String featureName) {
      switch (featureName) {
        case "id":
          return object.id;
        case "price":
          return object.price;
        default:
          throw new IllegalArgumentException();
      }
    }
  }

  /**
   * Proveedor de etiquetas basado en reglas lógicas de negocio (precio).
   */
  static class ProductLabelProvider implements ILabelProvider<Product, String> {

    /**
     * Clasifica un producto según su precio.
     * 
     * @param object Producto a etiquetar.
     * @return "EXPENSIVE" si precio > 100, "CHEAP" en caso contrario.
     */
    @Override
    public String getLabel(Product object) {
      return object.price > 100.0 ? "EXPENSIVE" : "CHEAP";
    }
  }

  private LabeledDataset<Product, String> labeledDataset;
  private ProductFeaturizer featurizer;
  private ProductLabelProvider labelProvider;

  /**
   * Inicialización del entorno de pruebas.
   */
  @Before
  public void setUp() {
    featurizer = new ProductFeaturizer();
    labelProvider = new ProductLabelProvider();
    labeledDataset = new LabeledDataset<>(featurizer, labelProvider);
  }

  /**
   * Verifica la correcta asignación de componentes mediante los getters.
   */
  @Test
  public void shouldInitializeWithFeaturizerAndLabelProvider() {
    assertSame(featurizer, labeledDataset.getFeaturizer());
    assertSame(labelProvider, labeledDataset.getLabelProvider());
  }

  /**
   * Valida la lógica de etiquetado delegada al labelProvider.
   */
  @Test
  public void shouldReturnLabelForGivenObject() {
    assertEquals("CHEAP", labeledDataset.getLabel(new Product("P1", 50.0)));
    assertEquals("EXPENSIVE", labeledDataset.getLabel(new Product("P2", 150.0)));
  }

  /**
   * Evalúa la capacidad de crear un subconjunto de datos filtrando tanto por
   * filas (instancias) como por columnas (features).
   */
  @Test
  public void shouldCreateSubsetWithSelectedFeaturesAndData() {
    Product p1 = new Product("P1", 50.0);
    Product p2 = new Product("P2", 200.0);

    labeledDataset.addAll(new Product[] { p1, p2 });

    // Seleccionamos solo p2 y solo la característica "price"
    LabeledDataset<Product, String> subset = labeledDataset.subset(Arrays.asList(p2), Arrays.asList("price"));

    assertNotNull(subset);
    assertNotSame(labeledDataset, subset);
    assertSame(labelProvider, subset.getLabelProvider());

    // Verificación de columnas filtradas
    assertEquals(1, subset.getFeatures().size());
    assertTrue(subset.getFeatures().containsKey("price"));
    assertFalse(subset.getFeatures().containsKey("id"));

    // Verificación de filas filtradas
    assertEquals(1, subset.getData().size());
    assertTrue(subset.getData().contains(p2));

    Feature<Double> priceFeature = subset.feature("price");
    assertEquals(1, priceFeature.size());
    assertEquals(Double.valueOf(200.0), priceFeature.get(0));
  }

  /**
   * Verifica el comportamiento del sistema cuando se solicita un subconjunto sin
   * características.
   */
  @Test
  public void shouldHandleEmptySubsetSelection() {
    Product p1 = new Product("P1", 50.0);

    LabeledDataset<Product, String> subset = labeledDataset.subset(Arrays.asList(p1), Collections.<String>emptyList());

    assertTrue(subset.getFeatures().isEmpty());
    assertEquals(1, subset.getData().size());
  }
}