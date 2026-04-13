package dataset;

import java.util.ArrayList;
import java.util.*;

public class Feature<T extends Comparable<T>> extends ArrayList<T> {

  public T min() {
    if (this.isEmpty())
      return null;
    T min = this.get(0);
    for (T element : this) {
      if (element.compareTo(min) < 0)
        min = element;
    }
    return min;
  }

  public T max(){
    if(this.isEmpty()){
      return null;
    }
    T max = this.get(0);
    for(T element : this){
      if(element.compareTo(max)> 0){
        max = element;
      }
    }
    return max;
  }

  public Map<T, Integer> distribution(){
    if(this.isEmpty()){
      return null;
    }
    Map<T, Integer> frecuencia= new HashMap<>();
    for(T element : this){
      frecuencia.compute(element, (k,v) -> (v == null) ? 1 : v + 1);
    }
    return frecuencia;
  }


}