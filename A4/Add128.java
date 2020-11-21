import java.util.*;
import java.io.*;

/*
Nicholas Tillmann
Add128.java
Assignment 4
This class must implement SymCipher and meet the following specifications:
o It will have two constructors, one without any parameters and one that takes a byte
array. 
The parameterless constructor will create a random 128-byte additive key and
store it in an array of bytes. The other constructor will use the byte array parameter as
its key. The SecureChatClient will call the parameterless constructor and the
SecureChatServer calls the version with a parameter

*/

public class Add128 implements SymCipher {
  private byte [] keys; 

  public Add128() {
    keys = new byte[128];
    Random rand = new Random();
    rand.nextBytes(keys); 
  }

  public Add128(byte [] arr) {
    keys = arr;
  }

  public byte [] getKey() {
    return keys;
  }

  public byte [] encode(String input) {
          byte[] arrMessage = input.getBytes(); 
          for(int i = 0; i < arrMessage.length; i++){  
               arrMessage[i] =  (byte) (arrMessage[i] + keys[i % keys.length]);
          }
          return arrMessage.clone();
    }

  public String decode(byte [] bytes) {
    byte[] arrMessage = bytes.clone(); 

    for(int i = 0; i < arrMessage.length; i++){
        arrMessage[i] = (byte) (arrMessage[i] - keys[i % keys.length]);
    }
    return new String(arrMessage);
    }
}   