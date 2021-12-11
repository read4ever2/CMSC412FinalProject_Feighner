import java.nio.IntBuffer;
import java.util.Random;
import java.util.Scanner;

/**
 * Filename: Main.java
 *
 * @author willfeighner
 * Date: 2021 12 10
 * Purpose: Simulate various demand paging algorithms
 */

public class Main {

  private static final int frameLimit = 10;
  public static boolean flag = true;
  static int menuChoice = 0;
  private static IntBuffer pageBuffer;

  public static void main(String[] args) {
    mainMenu();
  }

  private static void mainMenu() {
    Scanner scanner = new Scanner(System.in);
    PagingAlgorithms pager;

    while (flag) {
      System.out.println("\n*****************************************\n" +
                         "\n" +
                         "0 - Exit\n" +
                         "1 – Read reference string\n" +
                         "2 – Generate reference string\n" +
                         "3 – Display current reference string\n" +
                         "4 – Simulate FIFO\n" +
                         "5 – Simulate OPT\n" +
                         "6 – Simulate LRU\n" +
                         "7 – Simulate LFU\n" +
                         "Select Option:");

      try {
        menuChoice = Integer.parseInt(scanner.next());

        switch (menuChoice) {
          case 0:
            System.out.println("Goodbye");
            flag = false;
            break;
          case 1:
            System.out.println("1 entered. Enter Reference String");
            readString();
            break;
          case 2:
            System.out.println("2 entered. Generate reference String\n" + "Enter length of string: ");
            generateString(Integer.parseInt(scanner.next()));
            for (int i = 0; i < pageBuffer.capacity(); i++) {
              System.out.print(pageBuffer.get(i) + " ");
            }
            break;
          case 3:
            System.out.println("3 entered. Displaying Current reference string");
            for (int i = 0; i < pageBuffer.capacity(); i++) {
              System.out.print(pageBuffer.get(i) + " ");
            }
            break;
          case 4:
            System.out.println("4 entered. Simulating FIFO");
            pager = new PagingAlgorithms(pageBuffer);
            pager.fifo();
            break;
          case 5:
            System.out.println("5 entered. Simulating OPT");
            pager = new PagingAlgorithms(pageBuffer);
            pager.optimum();
            break;
          case 6:
            System.out.println("6 entered. Simulating LRU");
            pager = new PagingAlgorithms(pageBuffer);
            pager.leastRecentlyUsed();
            break;
          case 7:
            System.out.println("7 entered. Simulating LFU");
            pager = new PagingAlgorithms(pageBuffer);
            pager.leastFrequentlyUsed();
            break;
          default:
            System.out.println("Invalid selection. Please select 0-7");
            break;
        }
      } catch (NumberFormatException e) {
        System.out.println("Please enter a number between 0-7");
      }
    }
  }

  private static void readString() {
    Scanner scanner = new Scanner(System.in);

    String input = scanner.nextLine();

    if (pageBuffer != null) {
      pageBuffer.clear();
    }
    String[] strings = input.split(" ");
    int[] pages = new int[strings.length];
    for (int i = 0; i < strings.length; i++) {
      pages[i] = Integer.parseInt(strings[i]);
    }
    pageBuffer = IntBuffer.wrap(pages);
  }

  private static void generateString(int length) {
    if (pageBuffer != null) {
      pageBuffer.clear();
    }
    Random random = new Random();
    pageBuffer = IntBuffer.allocate(length);
    for (int i = 0; i < length; i++) {
      pageBuffer.put(random.nextInt(frameLimit));
    }
  }
}