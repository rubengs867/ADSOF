package labeledDataset; 

import dataset.Dataset;
import dataset.IFeaturizer;


public class LabeledDataset<T, L> extends Dataset<T> {

  private LabelProvider<T, L> labelProvider;

  // El constructor ahora pide las DOS herramientas
  public LabeledDataset(IFeaturizer<T> featurizer, LabelProvider<T, L> labelProvider) {
    super(featurizer); // 1º Pasamos el featurizer al padre para que monte las columnas
    this.labelProvider = labelProvider; // 2º Guardamos nuestro etiquetador
  }

  // Ahora devuelve el tipo L (ej. Boolean) en lugar de un Object genérico
  public L getLabel(T object) {
    return labelProvider.getLabel(object);
  }
  
  // Te recomiendo añadir este getter, ¡el algoritmo GreedyTreeLearner lo va a agradecer luego!
  public LabelProvider<T, L> getLabelProvider() {
      return this.labelProvider;
  }
  
}