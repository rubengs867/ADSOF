package apartado1;

public class Person {
  private String name;
  private int age;
  private int weight;
  private int height;
  private boolean gender;

  public Person(String name, int age, int weight, int height, boolean male) {
    this.name = name;
    this.age = age;
    this.weight = weight;
    this.height = height;
    this.gender = male;
  }

  public String getName() {
    return name;
  }

  public int getAge() {
    return age;
  }

  public int getWeight() {
    return weight;
  }

  public int getHeight() {
    return height;
  }

  public boolean isGender() {
    return gender;
  }

  @Override
    public String toString() {
        String genero = this.gender ? "male" : "female";
        return this.name + "(age: " + this.age + ", " + genero + ")";
    }
  
}
