import java.nio.IntBuffer;

/**
 * Filename: PagingAlgorithms.java
 *
 * @author willfeighner
 * Date: 2021 12 10
 * Purpose: Simulate various demand paging algorithms
 */

public class PagingAlgorithms {

  private static final int NUMBEROFFRAMES = 8;
  private final IntBuffer pageBuffer;
  int[][] displayArray;

  public PagingAlgorithms(IntBuffer pageBuffer) {
    this.pageBuffer = pageBuffer;
  }

  public void fifo() {
    System.out.println("FIFO");
    for (int i = 0; i < pageBuffer.capacity(); i++) {
      System.out.print(pageBuffer.get(i) + " ");
    }

    displayArray = new int[pageBuffer.capacity()][NUMBEROFFRAMES];

    for (int i = 0; i < pageBuffer.capacity(); i++) {

    }
  }

  public void optimum() {
    System.out.println("Optimum");
    for (int i = 0; i < pageBuffer.capacity(); i++) {
      System.out.print(pageBuffer.get(i) + " ");
    }
  }

  public void leastRecentlyUsed() {
    System.out.println("Least Recently Used");
    for (int i = 0; i < pageBuffer.capacity(); i++) {
      System.out.print(pageBuffer.get(i) + " ");
    }
  }

  public void leastFrequentlyUsed() {
    System.out.println("Least Frequently Used");
    for (int i = 0; i < pageBuffer.capacity(); i++) {
      System.out.print(pageBuffer.get(i) + " ");
    }
  }

  private void printTable(int[][] displayArray, String[] faultArray, int[] victimArray) {


    for (int i = 0; i < 50; i++) {
      System.out.println();
    }
  }
}
