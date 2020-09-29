import java.util.*;
import java.io.*;
public class CrosswordB
{
    private DictInterface words;
    private char [][] theBoard;
    //private StringBuilder currentSolution;
    private int dimension, bound;
    private StringBuilder[] colStr; 
    private StringBuilder[] rowStr; 
    public String highScoreBoard = "";
    public int highScore = 0;
    public static void main(final String [] args) throws IOException
    {
        new CrosswordB(args[0], args[1]);
    }
    public CrosswordB(String file, String dictType) throws IOException
    {
        //Read the dictionary
        final Scanner fileScan = new Scanner(new FileInputStream("dict8.txt"));
        String st;

        if (dictType.equals("DLB"))
        {
            words = new DLB();
        }
        else
        {
            words = new MyDictionary();
        }
        
        while (fileScan.hasNext())
        {
            st = fileScan.nextLine();
            words.add(st);
        }
        fileScan.close();
        // Parse input file of the Boggle theBoard to create 2-d grid of characters
        Scanner fReader = new Scanner(new FileInputStream(file));
        File fName;
        String fString = "";
        // Make sure the file name for the Boggle theBoard is valid
        
            dimension = fReader.nextInt(); //this fReader sets dimensions = to first value in file, hence reason name dimension
            bound = dimension - 1;  //used for checking bounds, 
            
            theBoard = new char[dimension][dimension];
            fReader.nextLine(); //WITHOUT THIS LINE ERROR, NEED TO SKIP LINE
            rowStr = new StringBuilder[dimension];  //dimension holds size of the theBoard
            colStr = new StringBuilder [dimension]; //create both StringBuilders with size Dimension
            //System.out.println(dimension);
            for (int i = 0; i < dimension ; i++ ) 
            {
                // Initialize the string builders
                rowStr[i] = new StringBuilder(dimension); //rowStr initialized
                colStr[i] = new StringBuilder(dimension); //colStr initialized
                String rowString = fReader.nextLine();
                for (int j = 0; j < rowString.length(); j++) //i picked the wrong major
                {
        
                    theBoard[i][j] = rowString.charAt(j);
                }
            }
            fReader.close();
        solve(0, 0); //call Solve at first position (0,0), this is ok because from now on solve will act was a recursive function passing in updates values appropriately
        }

       public boolean isValid(int row, int col, char letter, StringBuilder[] rowStr, StringBuilder[] colStr) 
        {
            
            //if its the current value of board is not equal to letter or character return false, we do not call isValid check on -
            // both if theres '-' or prexisting charcater is handed in solve
            if(theBoard[row][col] != letter && theBoard[row][col] != '+') { //board is not character or plus return false 
               return false;                                               //we dont check isValid for '-' this is accounted for
            }                                                              //if there is a filled in character in spot already which doesnt equal letter it returns false and recurses appropriately 
            if(row < dimension && col < dimension) { //initialize stringBuilders
                colStr[col].append(letter); //colStr initialized to letters
                rowStr[row].append(letter); //rowStr initialized to letters
            } 
            else{
                return false;
            }
            int rowStart = rowStr[row].lastIndexOf("-") +1; //variable for storing next Row right after '-' (directly one spot to right) will be used to pass into searchPrefix
            int colStart =  colStr[col].lastIndexOf("-") +1; //variable for storing next Col right after '-' (directly one spot to right) will be used to pass into searchPrefix
            int rowEnd = rowStr[row].length()-1;  //variable for storing end of Row | will be used to pass into searchPrefix since parameters are s, start, end)
            int colEnd = colStr[col].length()-1; // variable for storing end of Col| will be used to pass into searchPrefix since parameters are s, start, end)

            //if on last spot on board 
            if(col == bound && row == bound) //row and column must be words, otherwise return false
            {
                // call search prefix on both col and Row passing in variables initialized above to represent s, start,
                int colValue = words.searchPrefix(colStr[col], colStart, colEnd); int rowValue = words.searchPrefix(rowStr[row], rowStart, rowEnd); 
                if(colStr[col].length() != 0 && rowStr[row].length() != 0) //out of bounds error checks
                colStr[col].deleteCharAt(colEnd); rowStr[row].deleteCharAt(rowEnd);  //backtracing undoing
                
                if((rowValue == 2 || rowValue == 3) && (colValue == 2 || colValue == 3)) 
                    return true;
                return false;
            }
            //if on last col thats not an edge.
            if(col == bound || theBoard[row][col+1] == '-') // row needs to be word, and col needs to be prefix because either nextCol is '-'' or edge, otherwise return false
            {                                               
                int colValue = words.searchPrefix(colStr[col], colStart, colEnd); int rowValue = words.searchPrefix(rowStr[row], rowStart, rowEnd);  
                if(colStr[col].length() != 0 && rowStr[row].length() != 0) //out of bounds error check
                colStr[col].deleteCharAt(colEnd); rowStr[row].deleteCharAt(rowEnd); //backtracing undoing
                if((rowValue == 2 || rowValue == 3) && (colValue == 1 || colValue == 3)) //if row word and col prefix return true 
                    return true;

                return false;
            }
            //if on last row thats not edge.
            if(row == bound || theBoard[row+1][col] == '-') { // Col needs to be word, and row needs to be prefix because either nextRow is '-' or edge, otherwise return false
                int colValue = words.searchPrefix(colStr[col], colStart, colEnd); int rowValue = words.searchPrefix(rowStr[row], rowStart, rowEnd); 
                if(colStr[col].length() != 0 && rowStr[row].length() != 0) //out of bounds error check
                colStr[col].deleteCharAt(colEnd); rowStr[row].deleteCharAt(rowEnd);  //backtracing undoing
                if((colValue == 2 || colValue == 3) && (rowValue == 1 || rowValue == 3)) //if col word and row prefix return true
                    return true;

                return false;
            } 
            else { //were not at an edge so could be anywhere in middle of board, both row and col must be prefixes, otherwise return false
                
                int colValue = words.searchPrefix(colStr[col], colStart, colEnd); int rowValue = words.searchPrefix(rowStr[row], rowStart, rowEnd); 
                if(colStr[col].length() != 0 && rowStr[row].length() != 0) //out of bounds error check
                colStr[col].deleteCharAt(colEnd); rowStr[row].deleteCharAt(rowEnd);  //backtracing undoing
                
                if((rowValue == 1 || rowValue == 3) && (colValue == 1 || colValue == 3)) //if both prefixes return true
                    return true;

                return false;
            }

        }

    public int calculateScore(){
        int score = 0;
        for(int outer= 0; outer < colStr.length; outer++)
        {
            for(int inner=0; inner < colStr[outer].length(); inner++)
            {
                char c = colStr[outer].charAt(inner);
                if(c=='d' || c=='g') 
                    score+= 2;
                else if(c=='b' || c=='c' || c=='m' || c=='p') 
                    score+= 3;
                else if(c=='f' || c=='h' || c=='v' || c=='w' || c=='y') 
                    score+= 4;
                else if(c=='k') 
                    score+= 5;
                else if(c=='j' || c=='x') 
                    score+= 8;
                else if(c=='q' || c=='z') 
                    score+= 10;
                else score+= 1;
            }
        }
        return score;
    }
    
    public void printBoard() {
        for(int i = 0; i < colStr.length; i++) {
            System.out.println(colStr[i].toString());
        }
    }

        public void solve(int row, int col)
        {
            if(theBoard[row][col] == '-')
            {
                rowStr[row].append('-'); colStr[col].append('-'); int rowEnd = rowStr[row].length()-1; int colEnd = colStr[col].length()-1; 
                if(col < bound)
                {
                    solve(row, col+1); //recurse on nextCol
                }
                else
                {
                    solve(row+1, 0); //recurse on nextRow
                }
                if(colStr[col].length() != 0 && rowStr[row].length() != 0)
                {
                    colStr[col].deleteCharAt(colEnd);
                    rowStr[row].deleteCharAt(rowEnd);

                }
            }
            for(char letter = 'a'; letter <= 'z'; letter++)
            {
                if(isValid(row, col, letter, rowStr, colStr))
                {
                    rowStr[row].append(letter); colStr[col].append(letter);
    
                    if(col == bound && row == bound)
                    {
                        System.out.println("Solution found!");
                        printBoard();
                        System.exit(0);
                     }
                    if(col < bound)
                        {
                            solve(row, col+1);  //recurse on nextCol
                        }
                        else
                        {
                            solve(row+1, 0);    //recurse on nextRow
                        }
                        if(colStr[col].length() != 0 && rowStr[row].length() != 0)
                          {  colStr[col].deleteCharAt(colStr[col].length()-1); rowStr[row].deleteCharAt(rowStr[row].length()-1);} //undo backtracing
    
                }    
            }
        }
}