// FeatureTest.java
package model;

import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class FeatureTest {

  private Feature<Integer> emptyFeature;
  private Feature<Integer> populatedFeature;
  private Feature<String> stringFeature;

  @Before
  public void setUp() {
    emptyFeature = new Feature<>();

    populatedFeature = new Feature<>();
    populatedFeature.add(5);
    populatedFeature.add(1);
    populatedFeature.add(10);
    populatedFeature.add(5);

    stringFeature = new Feature<>();
    stringFeature.add("Apple");
    stringFeature.add("Zebra");
    stringFeature.add("Banana");
  }

  @Test
  public void shouldReturnNullWhenMinCalledOnEmptyFeature() {
    assertNull(emptyFeature.min());
  }

  @Test
  public void shouldReturnMinimumValueWhenFeatureContainsElements() {
    assertEquals(Integer.valueOf(1), populatedFeature.min());
    assertEquals("Apple", stringFeature.min());
  }

  @Test
  public void shouldReturnNullWhenMaxCalledOnEmptyFeature() {
    assertNull(emptyFeature.max());
  }

  @Test
  public void shouldReturnMaximumValueWhenFeatureContainsElements() {
    assertEquals(Integer.valueOf(10), populatedFeature.max());
    assertEquals("Zebra", stringFeature.max());
  }

  @Test
  public void shouldReturnNullWhenDistributionCalledOnEmptyFeature() {
    assertNull(emptyFeature.distribution());
  }

  @Test
  public void shouldReturnCorrectDistributionMapWhenFeatureContainsElements() {
    Map<Integer, Integer> dist = populatedFeature.distribution();

    assertNotNull(dist);
    assertEquals(3, dist.size());
    assertEquals(Integer.valueOf(2), dist.get(5));
    assertEquals(Integer.valueOf(1), dist.get(1));
    assertEquals(Integer.valueOf(1), dist.get(10));
  }

  @Test
  public void shouldAllowStandardListOperations() {
    emptyFeature.add(100);
    assertEquals(1, emptyFeature.size());
    assertTrue(emptyFeature.contains(100));

    emptyFeature.remove(Integer.valueOf(100));
    assertTrue(emptyFeature.isEmpty());
  }
}