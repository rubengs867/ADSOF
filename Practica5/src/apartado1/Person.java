package apartado1;

/**
 * Clase que representa una persona, utilizado para probar el código en
 * distintos main de prueba.
 */
public class Person {
  /** nombre de la persona */
  private String name;
  /** edad de la persona */
  private int age;
  /** peso de la persona */
  private int weight;
  /** altura de la persona */
  private int height;
  /** género de la persona */
  private boolean gender;

  /**
   * Constructor.
   * 
   * @param name   nombre
   * @param age    edad
   * @param weight peso
   * @param height altura
   * @param male   género
   */
  public Person(String name, int age, int weight, int height, boolean male) {
    this.name = name;
    this.age = age;
    this.weight = weight;
    this.height = height;
    this.gender = male;
  }

  /**
   * Getter del nombre
   * 
   * @return cadena de caracteres con el nombre
   */
  public String getName() {
    return name;
  }

  /**
   * Getter para la edad de la persona
   * 
   * @return edad como entero
   */
  public int getAge() {
    return age;
  }

  /**
   * Getter para el peso de una persona
   * 
   * @return peso como entero
   */
  public int getWeight() {
    return weight;
  }

  /**
   * Getter para la altura de una persona
   * 
   * @return altura como un entero
   */
  public int getHeight() {
    return height;
  }

  /**
   * Comprueba si la persona en hombre o mujer.
   * 
   * @return {@code true} es hombre;
   *         {@code false} es mujer
   */
  public boolean isMale() {
    return gender;
  }

  /**
   * Representación textual de una persona.
   */
  @Override
  public String toString() {
    String genero = this.gender ? "male" : "female";
    return this.name + "(age: " + this.age + ", " + genero + ")";
  }

}
