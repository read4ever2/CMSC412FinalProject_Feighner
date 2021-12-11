import java.nio.IntBuffer;
import java.util.Objects;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;

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
  String[][] displayArray;
  String[] faultArray;
  String[] victimArray;
  Integer[] currentPages = new Integer[NUMBEROFFRAMES];
  private int pageFaults = 0;

  public PagingAlgorithms(IntBuffer pageBuffer) {
    this.pageBuffer = pageBuffer;
  }

  public void fifo() {
    System.out.println("FIFO");
    for (int i = 0; i < pageBuffer.capacity(); i++) {
      System.out.print(pageBuffer.get(i) + " ");
    }

    displayArray = new String[pageBuffer.capacity()][NUMBEROFFRAMES];
    faultArray = new String[pageBuffer.capacity()];
    victimArray = new String[pageBuffer.capacity()];

    // initialize arrays to empty spaces
    for (int i = 0; i < pageBuffer.capacity(); i++) {
      for (int j = 0; j < NUMBEROFFRAMES; j++) {
        displayArray[i][j] = " ";
      }
      faultArray[i] = " ";
      victimArray[i] = " ";
    }


    ArrayBlockingQueue<Integer> fifoQueue = new ArrayBlockingQueue<>(NUMBEROFFRAMES);

    int pageFaultIndex = 0;

    for (int i = 0; i < pageBuffer.capacity(); i++) {

      if (fifoQueue.contains(pageBuffer.get(i))) {
        faultArray[i] = " ";
        victimArray[i] = " ";
      } else {
        faultArray[i] = "F";
        pageFaults++;


        if (fifoQueue.size() == NUMBEROFFRAMES) {
          for (int j = 0; j < currentPages.length; j++) {
            if (Objects.equals(fifoQueue.peek(), currentPages[j])) {
              pageFaultIndex = j;
              break;
            }
          }
          victimArray[i] = String.valueOf(currentPages[pageFaultIndex]);
          currentPages[pageFaultIndex] = pageBuffer.get(i);

          try {
            fifoQueue.put(pageBuffer.get(i));
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        } else {
          for (int j = 0; j < currentPages.length; j++) {
            if (currentPages[j] == null) {
              currentPages[j] = pageBuffer.get(i);
              break;
            }
          }
        }
        for (int j = 0; j < displayArray[i].length; j++) {
          displayArray[i][j] = String.valueOf(currentPages[j]);
        }
      }


      printTable(displayArray, faultArray, victimArray);
      System.out.print("\nPress Any key to Continue");
      Scanner scanner = new Scanner(System.in);
      scanner.nextLine();
    }
    System.out.println("Total Page Faults: " + pageFaults);

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

  private void printTable(String[][] displayArray, String[] faultArray, String[] victimArray) {

    for (int i = 0; i < 50; i++) {
      System.out.println();
    }

    System.out.print("Reference String\t");
    for (int i = 0; i < pageBuffer.capacity(); i++) {
      System.out.print(pageBuffer.get(i) + "\t");
    }
    System.out.println();

    for (int i = 0; i < NUMBEROFFRAMES; i++) {
      System.out.print("Physical Frame " + i + "\t");
      for (int j = 0; j < pageBuffer.capacity(); j++) {
        if (Objects.equals(displayArray[j][i], "null")) {
          System.out.print(" \t");
        } else {
          System.out.print(displayArray[j][i] + "\t");
        }
      }
      System.out.println();
    }

    System.out.print("Page Faults\t\t\t");
    for (int i = 0; i < pageBuffer.capacity(); i++) {
      System.out.print(faultArray[i] + "\t");
    }
    System.out.println();

    System.out.print("Victim Pages\t\t");
    for (int i = 0; i < pageBuffer.capacity(); i++) {
      System.out.print(victimArray[i] + "\t");
    }
    System.out.println();
  }
}
