//Nicholas Tillmann
//Substitute.java
//Assignment 4
/*
This class must implement SymCipher and meet the following specifications:
It will have two constructors, one without any parameters and one that takes a byte
array. 
The parameterless constructor will create a random 256-byte array which is a
permutation of the 256 possible byte values and will serve as a map from bytes to their
substitution values. For example, if location 65 of the key array has the value 92, it
means that byte value 65 will map into byte value 92. 
*/
import java.lang.reflect.Array;
import java.util.*;
public class Substitute implements SymCipher {
    private byte[] keys;
    private byte[] decode;
    ArrayList<Byte> temp = new ArrayList<Byte>();

	public Substitute() {
        keys = new byte[256];
        decode = new byte[256];

		for (int i = 0; i < 256; i++) {
            temp.add((byte)i);
		}
        Collections.shuffle(temp);
        for (int i = 0; i < 256; i++) {
            //Write code here
            byte item = (byte)temp.get(i);	
            keys[i] = item;
            decode[item & 0xFF] = (byte)i;
        }
	}
	public Substitute(byte [] array) {
		keys = array;
		decode = new byte[256];
		for(int i = 0; i < keys.length;i++) {
			decode[0xFF & keys[i]] = (byte)i;
		}
	}
	@Override
	public byte[] getKey() {
		return keys;
	}
	@Override
	public byte[] encode(String input) {
		byte [] arrMessage = input.getBytes();	
		for(int i = 0; i < arrMessage.length; i++) {
			arrMessage[i] = keys[arrMessage[i] & 0xFF];
        }
		return arrMessage;
	}
	@Override
	public String decode(byte[] bytes) {
		for(int i = 0; i < bytes.length; i++) {
			bytes[i] = decode[bytes[i] & 0xFF];
		}
		return new String(bytes);
	}
}
