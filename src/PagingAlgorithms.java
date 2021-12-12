import java.nio.IntBuffer;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Filename: PagingAlgorithms.java
 *
 * @author willfeighner
 * Date: 2021 12 10
 * Purpose: Simulate various demand paging algorithms
 */

public class PagingAlgorithms {

  private static final int NUMBEROFFRAMES = 5;
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

    // Create arrays for outputs
    displayArray = new String[pageBuffer.capacity()][NUMBEROFFRAMES];
    faultArray = new String[pageBuffer.capacity()];
    victimArray = new String[pageBuffer.capacity()];

    // initialize output arrays to empty spaces
    for (int i = 0; i < pageBuffer.capacity(); i++) {
      for (int j = 0; j < NUMBEROFFRAMES; j++) {
        displayArray[i][j] = " ";
      }
      faultArray[i] = " ";
      victimArray[i] = " ";
    }

    // Create queue for pages
    ArrayBlockingQueue<Integer> fifoQueue = new ArrayBlockingQueue<>(NUMBEROFFRAMES);

    int pageFaultIndex = -1;

    // For each page in the reference string
    for (int i = 0; i < pageBuffer.capacity(); i++) {

      // check to see if new page is already in queue
      if (fifoQueue.contains(pageBuffer.get(i))) {

        // If page hit
        faultArray[i] = " "; // no page fault
        victimArray[i] = " "; // no victim page
        displayArray[i] = displayArray[i - 1]; // array of pages stays same as prior
      } else {

        // page fault
        faultArray[i] = "F";
        pageFaults++;

        // if page queue is full
        if (fifoQueue.size() == NUMBEROFFRAMES) {

          // victim page from queue
          Integer victim = fifoQueue.remove();

          // find victim page frame number
          for (int j = 0; j < currentPages.length; j++) {
            if (currentPages[j].equals(victim)) {
              pageFaultIndex = j;
              break;
            }
          }

          // Victim page to victim array
          victimArray[i] = String.valueOf(victim);

          // Add new page vacated frame number
          currentPages[pageFaultIndex] = pageBuffer.get(i);

          // add page to end of queue
          try {
            fifoQueue.put(pageBuffer.get(i));
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        } else { // queue not full

          // add page to end of queue
          try {
            fifoQueue.put(pageBuffer.get(i));
          } catch (InterruptedException e) {
            e.printStackTrace();
          }

          // Add new page vacated frame number
          for (int j = 0; j < currentPages.length; j++) {
            if (currentPages[j] == null) {
              currentPages[j] = pageBuffer.get(i);
              break;
            }
          }
        }
        // copy current page frames to display array
        for (int j = 0; j < displayArray[i].length; j++) {
          displayArray[i][j] = String.valueOf(currentPages[j]);
        }
      }

      // Display output table
      printTable(displayArray, faultArray, victimArray);
      System.out.print("\nPress Enter key to continue");
      Scanner scanner = new Scanner(System.in);
      scanner.nextLine();
    }

    double hitRatio = (double) ((pageBuffer.capacity() - pageFaults) / pageBuffer.capacity()) * 100;

    System.out.println("Total Page Faults: " + pageFaults);
    System.out.println("Hit ratio: " + hitRatio + " %");
  }

  public void optimum() {

    // Create arrays for outputs
    displayArray = new String[pageBuffer.capacity()][NUMBEROFFRAMES];
    faultArray = new String[pageBuffer.capacity()];
    victimArray = new String[pageBuffer.capacity()];

    // initialize output arrays to empty spaces
    for (int i = 0; i < pageBuffer.capacity(); i++) {
      for (int j = 0; j < NUMBEROFFRAMES; j++) {
        displayArray[i][j] = " ";
      }
      faultArray[i] = " ";
      victimArray[i] = " ";
    }


  }

  public void leastRecentlyUsed() {
    // Create arrays for outputs
    displayArray = new String[pageBuffer.capacity()][NUMBEROFFRAMES];
    faultArray = new String[pageBuffer.capacity()];
    victimArray = new String[pageBuffer.capacity()];

    // initialize output arrays to empty spaces
    for (int i = 0; i < pageBuffer.capacity(); i++) {
      for (int j = 0; j < NUMBEROFFRAMES; j++) {
        displayArray[i][j] = " ";
      }
      faultArray[i] = " ";
      victimArray[i] = " ";
    }

    // Create queue for pages
    LinkedList<Integer> LRUQueue = new LinkedList<>();

    int pageFaultIndex = -1;

    // For each page in the reference string
    for (int i = 0; i < pageBuffer.capacity(); i++) {

      // check to see if new page is already in queue
      if (LRUQueue.contains(pageBuffer.get(i))) {

        // If page hit
        faultArray[i] = " "; // no page fault
        victimArray[i] = " "; // no victim page
        displayArray[i] = displayArray[i - 1]; // array of pages stays same as prior

        int pageHitIndex = -1;

        // find page hit queue position
        for (int j = 0; j < LRUQueue.size(); j++) {
          if (LRUQueue.get(j).equals(pageBuffer.get(i))) {
            pageHitIndex = j;
          }
        }

        // remove page from queue position and add to end
        Integer requeue = LRUQueue.remove(pageHitIndex);
        LRUQueue.add(requeue);

      } else {

        // page fault
        faultArray[i] = "F";
        pageFaults++;

        // if page queue is full
        if (LRUQueue.size() == NUMBEROFFRAMES) {

          // victim page from queue
          Integer victim = LRUQueue.remove();

          // find victim page frame number
          for (int j = 0; j < currentPages.length; j++) {
            if (currentPages[j].equals(victim)) {
              pageFaultIndex = j;
              break;
            }
          }

          // Victim page to victim array
          victimArray[i] = String.valueOf(victim);

          // Add new page vacated frame number
          currentPages[pageFaultIndex] = pageBuffer.get(i);

          // add page to end of queue
          LRUQueue.add(pageBuffer.get(i));

        } else { // queue not full

          // add page to end of queue
          LRUQueue.add(pageBuffer.get(i));

          // Add new page vacated frame number
          for (int j = 0; j < currentPages.length; j++) {
            if (currentPages[j] == null) {
              currentPages[j] = pageBuffer.get(i);
              break;
            }
          }
        }
        // copy current page frames to display array
        for (int j = 0; j < displayArray[i].length; j++) {
          displayArray[i][j] = String.valueOf(currentPages[j]);
        }
      }

      // Display output table
      printTable(displayArray, faultArray, victimArray);
      System.out.print("\nPress Enter key to continue");
      Scanner scanner = new Scanner(System.in);
      scanner.nextLine();
    }

    double hitRatio = (((double) (pageBuffer.capacity() - pageFaults) / (double) pageBuffer.capacity()) * 100);

    System.out.println("Total Page Faults: " + pageFaults);
    System.out.println("Hit ratio: " + hitRatio + " %");
  }

  public void leastFrequentlyUsed() {
    // Create arrays for outputs
    displayArray = new String[pageBuffer.capacity()][NUMBEROFFRAMES];
    faultArray = new String[pageBuffer.capacity()];
    victimArray = new String[pageBuffer.capacity()];

    // initialize output arrays to empty spaces
    for (int i = 0; i < pageBuffer.capacity(); i++) {
      for (int j = 0; j < NUMBEROFFRAMES; j++) {
        displayArray[i][j] = " ";
      }
      faultArray[i] = " ";
      victimArray[i] = " ";
    }

    HashSet<Integer> currentPageSet = new HashSet<>(NUMBEROFFRAMES);
    // Initialize map to keep track of page use count
    int mapSize = 10;
    HashMap<Integer, Integer> pageCount = new HashMap<>(mapSize, 1);

    for (int i = 0; i < mapSize; i++) {
      pageCount.put(i, 0);
    }

    // For each page in the reference string
    for (int i = 0; i < pageBuffer.capacity(); i++) {
      // Increase page frequency for each page when called
      int previousCount = pageCount.get(i);
      pageCount.replace(i, previousCount + 1);

      // Check to see if new page matches any current pages
      if (currentPageSet.contains(pageBuffer.get(i))) {
        // if page hit
        faultArray[i] = " "; // no page fault
        victimArray[i] = " "; // no victim page
        displayArray[i] = displayArray[i - 1]; // array of pages stays same as prior

      } else { // Page Fault
        faultArray[i] = "F";
        pageFaults++;
        // If all frames are full
        if (currentPageSet.size() == NUMBEROFFRAMES) {

        } else { // Frame not full
          for (int j = 0; j < currentPages.length; j++) {
            if (currentPages[j] == null) {
              currentPages[j] = pageBuffer.get(i);
              break;
            }
          }
        }
        // copy current page frames to display array
        for (int j = 0; j < displayArray[i].length; j++) {
          displayArray[i][j] = String.valueOf(currentPages[j]);
        }
      }
      // Display output table
      printTable(displayArray, faultArray, victimArray);
      System.out.print("\nPress Enter key to continue");
      Scanner scanner = new Scanner(System.in);
      scanner.nextLine();
    }
    double hitRatio = (((double) (pageBuffer.capacity() - pageFaults) / (double) pageBuffer.capacity()) * 100);

    System.out.println("Total Page Faults: " + pageFaults);
    System.out.println("Hit ratio: " + hitRatio + " %");
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

    System.out.print("--------------------");
    for (int i = 0; i < pageBuffer.capacity(); i++) {
      System.out.print("----");
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