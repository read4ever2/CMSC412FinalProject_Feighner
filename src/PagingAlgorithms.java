import java.nio.IntBuffer;

/**
 * Filename: PagingAlgorithms.java
 *
 * @author willfeighner
 * Date: 2021 12 10
 * Purpose: Simulate various demand paging algorithms
 */

public class PagingAlgorithms {

  private IntBuffer pageBuffer;

  public PagingAlgorithms(IntBuffer pageBuffer) {
    this.pageBuffer = pageBuffer;
  }

  public void fifo() {
    System.out.println("FIFO");
    System.out.println(pageBuffer.toString());
  }

  public void optimum() {
    System.out.println("Optimum");
    System.out.println(pageBuffer.toString());
  }
}
