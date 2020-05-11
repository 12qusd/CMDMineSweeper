import java.util.Scanner;
/* Examples:
 * Scanner scanner = new Scanner(System.in);
 * String username = scanner.next();
 * int age = scanner.nextInt();
 *
 *
 */
public class MineWorld
{
     public static final String ANSI_RESET = "\u001B[0m";
     public static final String BLACK = "\u001B[30m";
     public static final String RED = "\u001B[31m";
     public static final String GREEN = "\u001B[32m";
     public static final String YELLOW = "\u001B[33m";
     public static final String BLUE = "\u001B[34m";
     public static final String PURPLE = "\u001B[35m";
     public static final String CYAN = "\u001B[36m";
     public static final String WHITE = "\u001B[37m";
     public static final String BG_BLACK = "\u001B[40m";
     public static final String BG_RED = "\u001B[41m";
     public static final String BG_GREEN = "\u001B[42m";
     public static final String BG_YELLOW = "\u001B[43m";
     public static final String BG_BLUE = "\u001B[44m";
     public static final String BG_PURPLE = "\u001B[45m";
     public static final String BG_CYAN = "\u001B[46m";
     public static final String BG_WHITE = "\u001B[47m";
     public static final String ANSI_CLS = "\u001b[2J";
     public static final String ANSI_HOME = "\u001b[H";
     //END OF ANSI CODES
     private static Scanner scanner = new Scanner(System.in);
     private static boolean[][] revealed;
     private static boolean[][] mineLocations;
     private static int[][] cellNumbers;
     private static String[][] currentBoard;
     private static boolean placedMine = false;
     private static int numMines = 0;
     //initialize(Board Size, Number of Mines)
     private static void initialize(int size, int numberOfMines)
     {
          numMines = numberOfMines;
          mineLocations = new boolean[size][size];
          cellNumbers = new int[size][size];
          revealed = new boolean[size][size];
          placeMines(numberOfMines);
          numberSquares();
     }
     private static String colorNum(int num)
     {
          if (num == 0)
                return "\u001B[36m";
          if (num >= 6)
                num -= 5;
          num--;
          return "\u001B[3" + num + "m";
     }
     private static String bgColorNum(int num)
     {
        if (num == 0)
                return "\u001B[45m";
        if (num >= 6)
                num -= 5;
          return "\u001B[4" + num + "m";
     }
     private static void printBoard()
     {
          writeNumbers(mineLocations.length);
          for (int i = 0; i < mineLocations.length; i++)
          {
               System.out.print("R" + i);
               System.out.print("|");
               for (int j = 0; j < mineLocations[i].length; j++)
               {
                    if (mineLocations[i][j] && revealed[i][j])
                    {
                         System.out.print(" X |");
                    }
                    else if (!mineLocations[i][j] && revealed[i][j])
                    {

                         System.out.print(" " + bgColorNum(cellNumbers[i][j]) + colorNum(cellNumbers[i][j]) + cellNumbers[i][j] + ANSI_RESET + " |");
                    }
                    else
                    {
                         System.out.print("\u001B[1m # |" + ANSI_RESET);
                    }
               }
               System.out.println();
          }
     }
     private static void printRevealedBoard()
     {
          // Prints the whole board
          writeNumbers(mineLocations.length);
          for (int i = 0; i < mineLocations.length; i++)
          {
               System.out.print("R" + i);
               System.out.print("|");
               for (int j = 0; j < mineLocations[i].length; j++)
               {
                    if (mineLocations[i][j])
                    {
                    System.out.print(" X |");
                    }
                    else
                    {
                     System.out.print(" " + bgColorNum(cellNumbers[i][j]) + colorNum(cellNumbers[i][j]) + cellNumbers[i][j] + ANSI_RESET + " |");
                    }
               }
               System.out.println();
          }
          // End of printing the board
     }
     public static void main(String args[])
     {
          System.out.println("Enter the number of mines < 100");
          int usrMines = scanner.nextInt();

          initialize(10, usrMines);
          System.out.print(ANSI_CLS + ANSI_HOME);
          System.out.flush();
          //printInstructions();
          printBoard();
          System.out.println();
          //printRevealedBoard();
          getInput();
     }
     private static void getInput()
     {
          //input is x,y
          boolean noExceptionCaught = true;
          System.out.println("Type in your click (format xy): ");
          String coords = scanner.next();
          int x = -1;
          int y = -1;
          if (coords.equals("reveal"))
          {
                printRevealedBoard();
                return;
          }
          try {
               x = Integer.parseInt(coords.substring(0,1));
               y = Integer.parseInt(coords.substring(1,2));
          } catch (NumberFormatException e) {
               noExceptionCaught = false;
          } catch (IndexOutOfBoundsException e) {
               noExceptionCaught = false;
          }
          if (noExceptionCaught) {
               if (check(y, x))
               {
                    System.out.print(ANSI_CLS + ANSI_HOME);
                    System.out.flush();
                    printBoard();
                    System.out.println("YOU LOSE!!!!");
                    return;
               }
               System.out.print(ANSI_CLS + ANSI_HOME);
               System.out.flush();
               printBoard();
          }
          else {
               noExceptionCaught = true;
               System.out.println("Please Enter a Valid Input");
          }
          if (!(checkWin()))
               getInput();
          else
               System.out.println("YOU WIN!!!!");
     }
     private static boolean checkWin() {
          int counter = 0;
          for (int i = 0; i < revealed.length; i++)
          {
               for (int j = 0; j < revealed[i].length; j++)
               {
                    if (revealed[i][j] == false)
                         counter++;
                    if (counter > numMines)
                         return false;
               }
          }
          if (counter == numMines)
               return true;
          else
               return false;
     }
     private static boolean check(int y, int x) {
          if (mineLocations[y][x])
          {
               revealed[y][x] = true;
               return true;
          }
          else {
               revealed[y][x] = true;
               if (cellNumbers[y][x] == 0)
                    reveal(y, x);
               return false;
          }
     }

     private static void reveal(int y, int x)
     {
          //check left and up (y-1, x-1)
          if (y-1 >= 0 && x-1 >= 0)
          {
               if (!(mineLocations[y-1][x-1]) && cellNumbers[y-1][x-1] == 0 && revealed[y-1][x-1] == false)
               {
                    revealed[y-1][x-1] = true;
                    reveal(y-1, x-1);
               }
               else if (!(mineLocations[y-1][x-1]) && cellNumbers[y-1][x-1] != 0)
               {
                    revealed[y-1][x-1] = true;
               }
          }
          //check above (y-1, x)
          if (y-1 >= 0)
          {
               if (!(mineLocations[y-1][x]) && cellNumbers[y-1][x] == 0 && revealed[y-1][x] == false)
               {
                    revealed[y-1][x] = true;
                    reveal(y-1, x);
               }
               else if (!(mineLocations[y-1][x]) && cellNumbers[y-1][x] != 0)
               {
                    revealed[y-1][x] = true;
               }
          }
          //check up 1 and right 1 (y-1, x+1)
          if (y-1 >= 0 && x+1 < mineLocations.length)
          {
               if (!(mineLocations[y-1][x+1]) && cellNumbers[y-1][x+1] == 0 && revealed[y-1][x+1] == false)
               {
                    revealed[y-1][x+1] = true;
                    reveal(y-1, x+1);
               }
               else if (!(mineLocations[y-1][x+1]) && cellNumbers[y-1][x+1] != 0)
               {
                    revealed[y-1][x+1] = true;
               }
          }
          //check back 1 (y, x-1)
          if (x-1 >= 0)
          {
               if (!(mineLocations[y][x-1]) && cellNumbers[y][x-1] == 0 && revealed[y][x-1] == false)
               {
                    revealed[y][x-1] = true;
                    reveal(y, x-1);
               }
               else if (!(mineLocations[y][x-1]) && cellNumbers[y][x-1] != 0)
               {
                    revealed[y][x-1] = true;
               }
          }
          //check down 1 (y, x+1)
          if (y+1 < mineLocations.length)
          {
               if (!(mineLocations[y+1][x]) && cellNumbers[y+1][x] == 0 && revealed[y+1][x] == false)
               {
                    revealed[y+1][x] = true;
                    reveal(y+1, x);
               }
               else if (!(mineLocations[y+1][x]) && cellNumbers[y+1][x] != 0)
               {
                    revealed[y+1][x] = true;
               }
          }
          //check forward 1
          if (x+1 < mineLocations.length)
          {
               if (!(mineLocations[y][x+1]) && cellNumbers[y][x+1] == 0 && revealed[y][x+1] == false)
               {
                    revealed[y][x+1] = true;
                    reveal(y, x+1);
               }
               else if (!(mineLocations[y][x+1]) && cellNumbers[y][x+1] != 0)
               {
                    revealed[y][x+1] = true;
               }
          }
          //check down 1 left 1 (y+1, x-1)
          if (x-1 >= 0 && y+1 < mineLocations.length)
          {
               if (!(mineLocations[y+1][x-1]) && cellNumbers[y+1][x-1] == 0 && revealed[y+1][x-1] == false)
               {
                    revealed[y+1][x-1] = true;
                    reveal(y+1, x-1);
               }
               else if (!(mineLocations[y+1][x-1]) && cellNumbers[y+1][x-1] != 0)
               {
                    revealed[y+1][x-1] = true;
               }
          }
          //check down 1 right 1 (y+1, x+1)
          if (x+1 < mineLocations.length && y+1 < mineLocations.length)
          {
               if (!(mineLocations[y+1][x+1]) && cellNumbers[y+1][x+1] == 0 && revealed[y+1][x+1] == false)
               {
                    revealed[y+1][x+1] = true;
                    reveal(y+1, x+1);
               }
               else if (!(mineLocations[y+1][x+1]) && cellNumbers[y+1][x+1] != 0)
               {
                    revealed[y+1][x+1] = true;
               }
          }
     }



     // checks if all locations are filled.
     private static boolean checkIfFilled()
     {
           for (int i = 0; i < mineLocations.length; i++)
          {
               for (int j = 0; j < mineLocations[i].length; j++)
               {
                    if (!mineLocations[i][j])
                    return false;
               }
          }
           return true;
     }
     // places one mine
     private static void placeOneMine()
     {
          if (!checkIfFilled())
          {
               while(true)
               {
                    int randomRow = (int)Math.floor(Math.random()*mineLocations.length);
                    int randomCol = (int)Math.floor(Math.random()*mineLocations.length);
                    if (mineLocations[randomRow][randomCol] == false)
                    {
                         mineLocations[randomRow][randomCol] = true;
                         break;
                    }
               }
          }
     }
     // places the number of mines needed
     private static void placeMines(int numberNeeded)
     {
          for (int i = 0; i < numberNeeded; i++)
          {
               placeOneMine();
          }
     }
     private static void numberSquares()
     {
         for (int i = 0; i < cellNumbers.length; i++)
        {
            for (int j = 0; j < cellNumbers[i].length; j++)
            {
                int currentNumber = 0;
                //check diagonal (up 1, left 1) (i-1,j-1)
                if (i-1 >= 0 && j-1 >= 0 && mineLocations[i-1][j-1] == true)
                currentNumber++;
                //check above (up 1, same column) (i-1, j)
                if (i-1 >= 0 && mineLocations[i-1][j] == true)
                currentNumber++;
                //check diagonal (up 1, right 1) (i-1, j+1)
                if (i-1 >= 0 && j+1 <= cellNumbers[i].length - 1 && mineLocations[i-1][j+1] == true)
                currentNumber++;
                //check left (i, j-1)
                if (j-1 >= 0 && mineLocations[i][j-1] == true)
                currentNumber++;
                //check right (i, j+1)
                if (j+1 <= cellNumbers[i].length - 1 && mineLocations[i][j+1] == true)
                currentNumber++;
                //check diagonal (down 1, left 1) (i+1, j-1)
                if (i+1 <= cellNumbers.length - 1 && j-1 >= 0 && mineLocations[i+1][j-1])
                currentNumber++;
                //check below (i+1, j)
                if (i+1 <= cellNumbers.length - 1 && mineLocations[i+1][j])
                currentNumber++;
                // check diagonal (down 1, right 1) (i+1, j+1)
                if (i+1 <= cellNumbers.length - 1 && j+1 <= cellNumbers[i].length - 1 && mineLocations[i+1][j+1] == true)
                currentNumber++;
                //set number
                cellNumbers[i][j] = currentNumber;
            }
        }
     }
     private static void printInstructions()
     {
          System.out.println("E = hidden. X = Mine. Numbers tell you how many mines are touching that square. Type in where you would want to click in the format x,y (column, row). Row and Column numbers are provided.");
     }
     //Writes numbers at the top
     private static void writeNumbers(int size)
     {
          System.out.print("  ");
          System.out.print("|");
          for (int i = 0; i < size; i++)
          {
               System.out.print(" C" + i + "|");
          }
          System.out.println();
     }
}
