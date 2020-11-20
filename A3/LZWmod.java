/*************************************************************************
 *  Compilation:  javac LZW.java
 *  Execution:    java LZW - < input.txt   (compress)
 *  Execution:    java LZW + < input.txt   (expand)
 *  Dependencies: BinaryIn.java BinaryOut.java
 *
 *  Compress or expand binary input from standard input using LZW.
 *
 *
 *************************************************************************/
 //Nicholas Tillmann
 //1501 Sheriff Khattab
 /*
 PURPOSE: : The purpose of this assignment is to fully understand the LZW compression algorithm, its
performance and its implementation. We will improve the performance of the textbook's LZW
implementation when the input files are large.
Goal 1: Read the input file as a stream of bytes (i.e., byte by byte) instead of all at once (feel free to use
your lab code for this task).
Goal 2: Avoid the Theta(n) overhead of using String.substring() (feel free to use your lab code for this
task).
Goal 3: Allow the codebook size to increase beyond the 4096 entries in the textbook's implementation
using adaptive codeword width.
Goal 4: Allow LZW to learn new patterns after the codebook size is reached by giving the user the option
to reset the codebook.
*/

public class LZWmod {
    private static final int R = 256;        // number of input chars
    private static int L = 512;       // number of codewords = 2^W
    private static int W = 9;         // codeword width
    private static String letter = "n";
    
    public static void increaseParameters(){
        L*= 2;
        W+= 1;
    }
    public static void resetParameters(){
        L = 512;
        W = 9;
    }
    public static void compress() {  
        TSTmod<Integer> st = new TSTmod<Integer>();
        StringBuilder current = new StringBuilder();
        for(int i = 0; i < R; i++) {
            st.put(new StringBuilder("" + (char) i), i);
        }
        int code = R + 1; // R is codeword for EOF

        if(letter.equals("r")) 
            BinaryStdOut.write('r', W);     
        else if(letter.equals("n")) 
            BinaryStdOut.write('n', W);    
        
        /*
        LAB CODE
         while (!BinaryStdIn.isEmpty()) {
            codeword = st.get(current);
            char ch = BinaryStdIn.readChar();
            current.append(ch);
            if(!st.contains(current)){
              BinaryStdOut.write(codeword, W);
              if (code < L)    // Add to symbol table if not full
                  st.put(current, code++);
              current = new StringBuilder();
              current.append(ch);
            }
        }
        */

            while(!BinaryStdIn.isEmpty()) 
            {    
                char ch = BinaryStdIn.readChar();
                current.append(ch);   // append next char
                if(!st.contains(current)) 
                {   
                    current.deleteCharAt(current.length() - 1); //delete
                    BinaryStdOut.write( st.get(current), W ); 
                    current.append(ch); // append char
                    if(code < 65536) {   
                                if(code == L) {
                                    increaseParameters();   
                                }
                            st.put(current, code++);  
                        }
                else if(code == 16 && W == 65536 && letter.equals("r")) 
                {  
                            st = new TSTmod<Integer>();      
                            for(int i = 0; i < R; i++) { 
                                st.put(new StringBuilder("" + (char) i), i);
                            }
                            code = R + 1; 
                            resetParameters();
                    }
                    current.delete(0, current.length()-1);  
                }
            }
            BinaryStdOut.write(st.get(current), W);
            BinaryStdOut.write(R, W); 
            BinaryStdOut.close();
        }

        /*
        LAB CODE
        String[] st = new String[L];
        int i; // next available codeword value

        // initialize symbol table with all 1-character strings
        for (i = 0; i < R; i++)
            st[i] = "" + (char) i;
        st[i++] = "";                        // (unused) lookahead for EOF

        int codeword = BinaryStdIn.readInt(W);
        String val = st[codeword];

        while (true) {
            BinaryStdOut.write(val);
            codeword = BinaryStdIn.readInt(W);
            if (codeword == R) break;
            String s = st[codeword];
            if (i == codeword) s = val + val.charAt(0);   // special case hack
            if (i < L) st[i++] = val + s.charAt(0);
            val = s;

        }
        BinaryStdOut.close();
    }
    */
        public static void expand() {
            String[] st = new String[65536];
            int i; // next available codeword value
            // initialize symbol table with all 1-character strings
            for (i = 0; i < R; i++) {
                st[i] = "" + (char) i;
            }
            st[i++] = "";                        // (unused) lookahead for EOF
    
            int codeword = BinaryStdIn.readInt(W);
            String val;
            letter = st[codeword];
            codeword = BinaryStdIn.readInt(W);
            val= st[codeword];
            BinaryStdOut.write(val);
            
            while (true) {
                if(i == 65536 && W == 16 && letter.equals("r")) { // reset
                    st = new String[65536];
                    for(i = 0; i < R; i++) 
                        st[i] = "" + (char) i;
                    st[i++] = "";
                    

                    resetParameters();
    
                    codeword = BinaryStdIn.readInt(W);  
                    val = st[codeword];
                    BinaryStdOut.write(val);    
                }
                else if(i < 65536 && i == L) {   
                   increaseParameters();
                }
                codeword = BinaryStdIn.readInt(W);  
    
                if(codeword == R) break;    
    
                String s = st[codeword];
                if(i == codeword) s = val + val.charAt(0);  
                if(i < L) st[i++] = val + s.charAt(0);
    
                val = s;    
                BinaryStdOut.write(val);   
            }
            BinaryStdOut.close();
        }        public static void main(String[] args) {
            if(args.length > 1 && args[1].equals("r")){
                letter = "r";
            }
            if (args[0].equals("-")){
                compress();
            }
            else if (args[0].equals("+")) expand();
            else throw new RuntimeException("Illegal command line argument");
        }
}

