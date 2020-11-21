import java.util.*;
import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.math.BigInteger;

//Nicholas Tillmann
//SecureChatClient.java
//ImprovedChatClient.java
//Assignment 4
/*It opens a connection to the server via a Socket at the server's IP address and port. Use
8765 for the port. Have your client prompt the user for the server name. More than
likely you will always be using "localhost" for the server name, since you will be running
the server on your own machine.

1. It creates an ObjectOutputStream on the socket (for writing) and immediately calls the
flush() method (this technicality prevents deadlock).
2. It creates on ObjectInputStream on the socket (be sure you create this AFTER creating
the ObjectOutputStream).
3. It receives the server's public key, E, as a BigInteger object.
4. It receives the server's public mod value, N, as a BigInteger object
*/

public class SecureChatClient extends JFrame implements Runnable, ActionListener{

    public static final int PORT = 8765; //8765 port as specified in assignment

    ObjectInputStream myReader;
    ObjectOutputStream myWriter; 
    JTextArea outputArea;
    JLabel prompt;
    JTextField inputField;
    String myName;
    String serverName;
    Socket connection;
    BigInteger E;
    BigInteger N; 
    BigInteger key;
    BigInteger encryptedKey;
    SymCipher cipher;

    public SecureChatClient(){

        try{
            //retrieve user input for name and server values
            myName = JOptionPane.showInputDialog(this, "Enter your user name: ");
            serverName = JOptionPane.showInputDialog(this, "Enter the server name: ");

            InetAddress addr = InetAddress.getByName(serverName);
            connection = new Socket(addr, PORT);   // Connect to server with new socket
            
            myWriter = new ObjectOutputStream(connection.getOutputStream());
            myWriter.flush(); 
            myReader = new ObjectInputStream(connection.getInputStream()); 
            E = (BigInteger) myReader.readObject(); 
            N = (BigInteger) myReader.readObject();
            System.out.println("E: " + E.toString()); // Output the keys E and N received from the server to the console.
            System.out.println("N: " + N.toString());
            String encyptionType = (String) myReader.readObject();  
            System.out.println("Encryption type: " + encyptionType);

            if (encyptionType.equals("Sub")){
                cipher = new Substitute();
            } else if (encyptionType.equals("Add")){
                cipher = new Add128();

            }

            key = new BigInteger(1, cipher.getKey()); 
            System.out.println("Symmetric Key: " + key);
            encryptedKey = key.modPow(E, N); 


            myWriter.writeObject(encryptedKey);
            myWriter.flush(); 
            byte[] eName;
            eName = cipher.encode(myName);
            myWriter.writeObject(eName); 
            myWriter.flush();

            this.setTitle(myName);

            //ImprovedChatClient.java code
            Box b = Box.createHorizontalBox();
            outputArea = new JTextArea(8, 30);
            outputArea.setEditable(false);
            b.add(new JScrollPane(outputArea));

            outputArea.append("Welcome to the Chat Server, " + myName + "\n");
            inputField = new JTextField("");
            inputField.addActionListener(this);

            prompt = new JLabel("Type your messages: ");
            Container c = getContentPane();

            c.add(b, BorderLayout.NORTH);
            c.add(prompt, BorderLayout.CENTER);
            c.add(inputField, BorderLayout.SOUTH);

            Thread outputThread = new Thread(this);  
            outputThread.start();                    

            addWindowListener(
                    new WindowAdapter(){
                        public void windowClosing(WindowEvent e){
                            try{
                                myWriter.writeObject(cipher.encode("CLIENT CLOSING"));
                                myWriter.flush();
                            } catch (IOException io){
                                System.out.println("PROBLEM CLOSING CLIENT!");
                            }
                            System.exit(0);
                        }
                    }
            );
            setSize(500, 200);
            setVisible(true);
        } catch (Exception e){
            System.out.println("PROBLEM STARTING CLIENT!");
        }
    }
    public void run(){
        while (true){
            try{
                //read encrypted message, decode and write to output area
                byte[] encryptedMsg = (byte[]) myReader.readObject();
                String msg = cipher.decode(encryptedMsg); //decrypt
                outputArea.append(msg + "\n");
                    System.out.println("Received Array: " + Arrays.toString(encryptedMsg));
                    System.out.println("Decrypted Array: " + Arrays.toString(msg.getBytes()));
                    System.out.println("Matching String: " + msg);

            } catch (Exception e){
                System.out.println(e + ", CLOSING CLIENT!");
                break;
            }
        }
        System.exit(0);
    }

    public void actionPerformed(ActionEvent e){

        String currMsg = e.getActionCommand();    
        inputField.setText("");

        try{
            currMsg = myName + ": " + currMsg;
            byte[] msg = cipher.encode(currMsg);
            myWriter.writeObject(msg);
            myWriter.flush();
            System.out.println("Message Sent");
            System.out.println("Original Message: " + currMsg);
            System.out.println("Array Bytes: " + Arrays.toString(currMsg.getBytes()));
            System.out.println("Encrypted Array Bytes: " + Arrays.toString(msg));

        } catch (IOException io){
            System.err.println("ERROR: Message failed to send!");
        }
    }

    public static void main(String[] args){
        //create the new client and set the defualt close operation
        SecureChatClient JR = new SecureChatClient();
        JR.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

    }
}