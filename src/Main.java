// This Suduko Solution Validator will check the given Suduko puzzle
// This puzzle will be a 9 x 9 grid with only number 1 to 9
// Each row, column, and a 3 x 3 grid contains number 1 to 9
// If there is no duplicate numbers between 1 and 9 in each row, column, and 3 x 3 grid
// The solution of this puzzle will consider valid.
// If there is duplicate numbers or any number are greater than 9 or less than 1
// The solution of this puzzle should consider not valid.
public class Main
{
    // The 9 x 9 suduko puzzle value will be contained in a 9x9 2D array
    // The validator will create 27 threads to validating each 9 rows, 9 columns and 9 3x3 grids
    // The threads of rows, columns, and 3x3 grids will be created into a threads array then join all the threads

    static int[][] puzzle =
            {
                    {6, 2, 4, 5, 3, 9, 1, 8, 7},
                    {5, 1, 9, 7, 2, 8, 6, 3, 4},
                    {8, 3, 7, 6, 1, 4, 2, 9, 5},
                    {1, 4, 3, 8, 6, 5, 7, 2, 9},
                    {9, 5, 8, 2, 4, 7, 3, 6, 1},
                    {7, 6, 2, 3, 9, 1, 4, 5, 8},
                    {3, 7, 1, 9, 5, 6, 8, 4, 2},
                    {4, 9, 6, 1, 8, 2, 5, 7, 3},
                    {2, 8, 5, 4, 7, 3, 9, 1, 6},
            };

    public static int numThreads = 27; // The total number of threads will be created
    public static boolean[] valid;  // This array will be containing the validating results from each part of the puzzle

    // 1. First create an object which only contains a row and a col
    // relevant to the threads will be creating.
    // This will be extending by the threads creating objects.
    public static class rowCol
    {
        int row;
        int col;

        public rowCol(int row, int col)
        {
            this.row = row;
            this.col = col;
        }
    }

    // 2. The column part threads object
    //    This will be implements from Runnable in order to create thread
    public static class checkCols extends rowCol implements Runnable
    {
        public checkCols(int row, int col)
        {
            super(row, col);
        }


        @Override
        public void run()
        {
            // The run method will be used to checking the numbers of each column of the puzzle
            // indicating every column only and number 1 to 9 once and no number are greater than 9 or less than 0.
            boolean[] colsValidity = new boolean[9];
            if(row != 0 || col > 8)
            {
                // If the row number is not 0 or column number is greater than 8
                // means that the data for validating is not from each column of the puzzle
                // Print out a message indicates this and return to escape.
                System.out.println("Detecting invalid row or column number for columns validating.");
                return;
            }

            // Create a loop to check numbers in the columns are valid
            for(int i = 0; i < 9; i++)
            {
                int number = puzzle[i][col];    // get each element in this column
                // first check if is the number greater than 9 or less than 0
                // or the current number is already existing in this column.
                if (number < 1 || number > 9 || colsValidity[number - 1])    return;
                else if (!colsValidity[number - 1])                          colsValidity[number - 1] = true;
            }

            // If the code runs to this point
            // update the true value to the "valid" Array indicates the current column has passed the validator.
            valid[col] = true;
        }
    }

    // 2. The column part threads object
    //    This will be implements from Runnable in order to create thread
    public static class checkRows extends rowCol implements Runnable
    {
        public checkRows(int row, int col)
        {
            super(row, col);
        }


        @Override
        public void run()
        {
            // The run method will be used to checking the numbers of each row of the puzzle
            // indicating every row only and number 1 to 9 once and no number are greater than 9 or less than 0.
            boolean[] rowsValidity = new boolean[9];
            if(row > 8 || col != 0)
            {
                // If the row number is greater than 8 or column number is not 0
                // means that the data for validating is not from each row of the puzzle
                // Print out a message indicates this and return to escape.
                System.out.println("Detecting invalid row or column number for columns validating.");
                return;
            }

            // Create a loop to check numbers in the rows are valid
            for(int i = 0; i < 9; i++)
            {
                int number = puzzle[row][i];    // get each element in this row
                // first check if is the number greater than 9 or less than 0
                // or the current number is already existing in this column.
                if (number < 1 || number > 9 || rowsValidity[number - 1])    return;
                else if (!rowsValidity[number - 1])                          rowsValidity[number - 1] = true;
            }

            // If the code runs to this point
            // update the true value to the "valid" Array indicates the current row has passed the validator.
            // will start after the last column's value.
            valid[9 + row] = true;
        }
    }

    public static class grid3x3 extends rowCol implements Runnable
    {
        public grid3x3(int row, int col)
        {
            super(row, col);
        }

        @Override
        public void run()
        {
            boolean[] gridValidity = new boolean[9];
            if(row > 6 || col > 6 || row % 3 != 0 || col % 3 != 0)
            {
                System.out.println("Invalid row or column for columns valid check!");
                return;
            }

            // Creating a nesting loop go through the 3 x 3 grid
            for(int i = row; i < row + 3; i++)
            {
                for(int j = col; j < col + 3; j++)
                {
                    // Get the elements' value
                    int number = puzzle[i][j];

                    // Validating the values
                    if (number < 1 || number > 9 || gridValidity[number - 1]) return;
                    else if (!gridValidity[number - 1])                       gridValidity[number - 1] = true;
                }
            }

            // update the true value to the "valid" Array indicates the current grid has passed the validator.
            // These will start after the last row value at 18.
            valid[18 + row + col/3] = true;
        }
    }

    public static void main(String[] args) {
        // The main method will: create the threads, start threads, join threads and check the final result
        Thread[] threads = new Thread[numThreads];
        valid = new boolean[numThreads];
        int threadsIdx = 0;

        for (int i = 0; i < puzzle.length; i++)
        {
            for (int j = 0; j < puzzle[i].length; j++)
            {
                // create the column, row, grid threads.
                if (i == 0)               threads[threadsIdx++] = new Thread(new checkCols(i,j));
                if (j == 0)               threads[threadsIdx++] = new Thread(new checkRows(i,j));
                if (i%3 == 0 && j%3 == 0) threads[threadsIdx++] = new Thread(new grid3x3(i,j));
            }
        }

        // Start all the threads.
        for (int i = 0; i < threads.length; i++)
        {
            threads[i].start();
        }

        // Join threads
        for (int i = 0; i < threads.length; i++)
        {
            try
            {
                threads[i].join();
            }
            catch (Exception e){e.printStackTrace();}
        }

        // Loop through the valid array which contains the results from all the threads
        // If any of the element's value is false
        // Print a message and stop the program, otherwise pass the loop go to next part of the code.
        for (int i = 0; i < valid.length; i++)
        {
            if(!valid[i])
            {
                System.out.println("The puzzle solution is not valid!");
                return;
            }
        }

        // If the code executed to this point, means all the result from the validator are passed
        // Print a message that indicates the solution is valid.
        System.out.println("The puzzle solution is valid!");
    }
}

