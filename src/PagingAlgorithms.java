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

  private final IntBuffer pageBuffer;
  String[][] displayArray;
  String[] faultArray;
  String[] victimArray;
  Integer[] currentPages;
  private int numberOfFrames;
  private int pageFaults = 0;

  public PagingAlgorithms(IntBuffer pageBuffer) {
    this.pageBuffer = pageBuffer;
  }

  public void setNumberOfFrames() {
    System.out.println("How many physical frames? (1-8): ");
    Scanner scanner = new Scanner(System.in);

    this.numberOfFrames = Integer.parseInt(scanner.next());
    this.currentPages = new Integer[numberOfFrames];
  }

  public void fifo() {

    setNumberOfFrames();

    // Create arrays for outputs
    displayArray = new String[pageBuffer.capacity()][numberOfFrames];
    faultArray = new String[pageBuffer.capacity()];
    victimArray = new String[pageBuffer.capacity()];

    // initialize output arrays to empty spaces
    for (int i = 0; i < pageBuffer.capacity(); i++) {
      for (int j = 0; j < numberOfFrames; j++) {
        displayArray[i][j] = " ";
      }
      faultArray[i] = " ";
      victimArray[i] = " ";
    }

    // Create queue for pages
    ArrayBlockingQueue<Integer> fifoQueue = new ArrayBlockingQueue<>(numberOfFrames);

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
        if (fifoQueue.size() == numberOfFrames) {

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
    setNumberOfFrames();
    // Create arrays for outputs
    displayArray = new String[pageBuffer.capacity()][numberOfFrames];
    faultArray = new String[pageBuffer.capacity()];
    victimArray = new String[pageBuffer.capacity()];

    // initialize output arrays to empty spaces
    for (int i = 0; i < pageBuffer.capacity(); i++) {
      for (int j = 0; j < numberOfFrames; j++) {
        displayArray[i][j] = " ";
      }
      faultArray[i] = " ";
      victimArray[i] = " ";
    }


    HashSet<Integer> currentPageSet = new HashSet<>(numberOfFrames);

    // For each page in the reference string
    for (int i = 0; i < pageBuffer.capacity(); i++) {
      // Check to see if new page matches any current pages
      if (currentPageSet.contains(pageBuffer.get(i))) {
        // if page hit
        faultArray[i] = " "; // no page fault
        victimArray[i] = " "; // no victim page
        displayArray[i] = displayArray[i - 1]; // array of pages stays same as prior
      } else { // page fault
        faultArray[i] = "F";
        pageFaults++;
        // if all frame are full
        if (currentPageSet.size() == numberOfFrames) {

          // find optimum page to replace, longest until next use
          int victim = -1, farthest = i + 1, victimFrame = -1;
          for (int j = 0; j < currentPages.length; j++) {
            for (int k = i; k < pageBuffer.capacity(); k++) {
              if (currentPages[j] == pageBuffer.get(k)) {
                if (k > farthest) {
                  farthest = k;
                  victim = pageBuffer.get(k);
                  victimFrame = j;
                }
                break;
              }
            }
          }
          victimArray[i] = String.valueOf(victim);

          // remove victim frame from fifo tracking list
          currentPageSet.remove(victim);
          currentPageSet.add(pageBuffer.get(i));

          currentPages[victimFrame] = pageBuffer.get(i);

        } else { // Frames not full
          currentPageSet.add(pageBuffer.get(i));
          // place page in first free frame
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

  public void leastRecentlyUsed() {
    setNumberOfFrames();
    // Create arrays for outputs
    displayArray = new String[pageBuffer.capacity()][numberOfFrames];
    faultArray = new String[pageBuffer.capacity()];
    victimArray = new String[pageBuffer.capacity()];

    // initialize output arrays to empty spaces
    for (int i = 0; i < pageBuffer.capacity(); i++) {
      for (int j = 0; j < numberOfFrames; j++) {
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
        if (LRUQueue.size() == numberOfFrames) {

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
    setNumberOfFrames();
    // Create arrays for outputs
    displayArray = new String[pageBuffer.capacity()][numberOfFrames];
    faultArray = new String[pageBuffer.capacity()];
    victimArray = new String[pageBuffer.capacity()];

    // initialize output arrays to empty spaces
    for (int i = 0; i < pageBuffer.capacity(); i++) {
      for (int j = 0; j < numberOfFrames; j++) {
        displayArray[i][j] = " ";
      }
      faultArray[i] = " ";
      victimArray[i] = " ";
    }

    // keep track of fifo order
    LinkedList<Integer> currentPageAge = new LinkedList<>();
    // Initialize map to keep track of page use count
    int mapSize = 10;
    HashMap<Integer, Integer> pageCount = new HashMap<>(mapSize, 1);

    for (int i = 0; i < mapSize; i++) {
      pageCount.put(i, 0);
    }

    // For each page in the reference string
    for (int i = 0; i < pageBuffer.capacity(); i++) {
      // Increase page frequency for each page when called
      int previousCount = pageCount.get(pageBuffer.get(i));
      pageCount.replace(pageBuffer.get(i), previousCount + 1);

      // Check to see if new page matches any current pages
      if (currentPageAge.contains(pageBuffer.get(i))) {
        // if page hit
        faultArray[i] = " "; // no page fault
        victimArray[i] = " "; // no victim page
        displayArray[i] = displayArray[i - 1]; // array of pages stays same as prior

      } else { // Page Fault
        faultArray[i] = "F";
        pageFaults++;
        // If all frames are full
        if (currentPageAge.size() == numberOfFrames) {
          // find least frequently used frame, and fifo list position among ties
          int lru = Integer.MAX_VALUE, value = Integer.MIN_VALUE;

          int lruListPos = -1;
          for (int j = 0; j < currentPageAge.size(); j++) {
            int testPage = currentPageAge.get(j);
            if (pageCount.get(testPage) < lru) {
              lru = pageCount.get(testPage);
              value = testPage;
              lruListPos = j;
            }
          }
          // Victim page to victim array
          victimArray[i] = String.valueOf(value);

          // replace victim page with new page in frame
          int pageFaultFrameNum = -1;
          for (int j = 0; j < currentPages.length; j++) {
            if (currentPages[j] == value) {
              pageFaultFrameNum = j;
            }
          }
          // remove victim frame from fifo tracking list
          currentPageAge.remove(lruListPos);
          currentPageAge.add(pageBuffer.get(i));

          // Add new page vacated frame number
          currentPages[pageFaultFrameNum] = pageBuffer.get(i);
        } else { // Frame not full
          // add to hash set
          currentPageAge.add(pageBuffer.get(i));
          // place page in first free frame
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

    for (int i = 0; i < numberOfFrames; i++) {
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