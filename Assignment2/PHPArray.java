//Nicholas Tillmann
//Assignment #2 
//PURPOSE:
// To implement a hybrid Symbol table that provides hash table access, indexed integer access,
// and sequential access (using an iterator).
//Goal 1: To implement a linear probing hash table.
//Goal 2: To implement an iterator.
import java.util.*;
public class PHPArray<V>implements Iterable<V>{ //PHPArray class will be a symbol table and will thus store data as (key, value) pairs,

    private PHPArrayIterator<V> iterator; 
    private int M;              //SIZE OF UNDERLYING HASH table 
    private int N = 0;          //KEY PAIR VALUE COUNT
    private Node<V> head;       //FIRST NODE INSERTED INTO BEGGINING OF LINKED CHAIN
    private Node<V> tail;       //LAST NODE HENCE END OF LINKED CHAIN
	private Node<V> [] entries;   //ACTUAL HASH table called entries
	
	@SuppressWarnings("unchecked")
	public PHPArray(int capacity){
		this.entries = new Node[capacity];
        M = capacity;
    }
    public PHPArray(){
        entries = new Node[10];
        M = 15; //default constructor size set as 10
    }
    public int length() { 
        return this.N; 
    }
    public void put(int key, V val) {
        put(Integer.toString(key), val);    // linear probing hash table O(1)   
    }
    public V get(int key) {
        return get(Integer.toString(key));  // linear probing hash table O(1)
    }
    public void unset(int key) {           
        unset(Integer.toString(key));       // linear probing hash table O(1)
    }
	public void showTable() {
		System.out.println("\tRaw Hash Table Contents:");
		Node<V> node; 
		int s = entries.length;
		for(int index = 0;index< s;index++) {
			System.out.print(index + ": ");
			node = entries[index];
			if(node != null)
				System.out.println("Key: " + node.pair.key+" Value: "+node.pair.value);
			else
				System.out.println("null");
		}
		
	}
 	//resize: CHECKS IF BOARD IS LESS THAN 50% FULL
    //Table resizing as required:
	@SuppressWarnings("unchecked")
	private void resize() {
		int s = entries.length;
		System.out.println("\t\tSize: "+length()+" -- resizing array from "+s+" to "+2*s);
		entries = (Node<V>[])new Node[2*entries.length]; //Double the size of entries
		Node<V> node = head;
		while(node != null) {
			add(node);
			node = node.next;
			}
		}

	//PUT
    //Hashes a given (key,value) pair into the table
    //if the key already exists, replace its value with the new value
    //else create newNode, resize, and insert
	public void put(String key, V value){
		Node <V> temp;
		int s = entries.length;
		temp = getHash(key);
        if(temp != null){ // if temp exists
            temp.pair.value = value; 
            return;
		}
		if (N >= s/2){
			resize(); 
		} 

		temp = new Node<V>(key,value); 
		add(temp); 
        if(N == 0){
            head = temp; 
            reset();
        }
        else{
            tail.next = temp; 
            temp.previous = tail; 
        }
        tail = temp; 
        N++; 
	}
	
	//GET
	public V get(String key) {
		Node <V> node;
		node = getHash(key);
		if(node == null)
			return null;
		return node.pair.value;
	}
	//ADD (PUT HELPER METHOD)
	public void add(Node<V> node) {
		int s = entries.length;
		int linearProbe; 
		int hashVal = node.pair.key.hashCode() & 0x7fffffff;
		for(int i = 0; i<s; i++) {
			linearProbe = (hashVal+i) % s;
			if(entries[linearProbe] == null) { //if hashTable(linearProbe) is null (aka free)
				entries[linearProbe] = node; //replace position with Node
				return;		
			}
		}
	}
	//GET-VALUE 
	private Node<V> getHash(String key){ 
		if(N == 0) return null;
		int s = entries.length;
		int linearProbe; 
		int hashVal = key.hashCode() & 0x7fffffff;
		for(int index = 0; index< s; index++) //iterate through hash table 
		{
			linearProbe = (hashVal+index) % s; //linearProbe value
			if(entries[linearProbe] == null)  //value is null stop searching
				break;
			if(entries[linearProbe].pair.key.equals(key)) //value is key and has not been deleted
				return entries[linearProbe];
		}
		return null;
	}

	private class PHPArrayIterator<V> implements Iterator<V> {
        private Node<V> current;
        
        public PHPArrayIterator(Node<V> head) {
            current = head;
        }
        public boolean hasNext()  { 
			return current != null; 
		}

        public V next() {
            return nextPair().value;
        }
        public Pair<V> nextPair(){
        	  if (!hasNext()) return null;
              Pair<V> pair = current.pair;
              current = current.next; 
              return pair;
        }
	}
	
	//ARRAYSORTER USED AS HELPER FUNCTION FOR KEYS,VALUES,SORT,ASORT
	public ArrayList<Pair<V>> arraySorter(){ //Pairs sorter
		ArrayList<Pair<V>> temp = new ArrayList<Pair<V>>();
		PHPArrayIterator<V> iteratorr = new PHPArrayIterator(head);
		while(iteratorr.hasNext()) {
			temp.add(iteratorr.nextPair());
		}
		return temp;
	}
	//KEYS (SAME METHOD AS VALUES)
	public ArrayList<String> keys(){
		ArrayList<String> temp = new ArrayList<String>();
		PHPArrayIterator<V> iteratorr = new PHPArrayIterator(head);
		while(iteratorr.hasNext()) {
			temp.add(iteratorr.nextPair().key);
		}
		return temp;
	}
	//VALUES (SAME METHOD AS KEYS)
	public ArrayList<V> values(){
		ArrayList<V> temp = new ArrayList<V>();
		PHPArrayIterator<V> iteratorr = new PHPArrayIterator(head);
		while(iteratorr.hasNext()) {
			temp.add(iteratorr.nextPair().value);
		}
		return temp;
		
	}
	//SORT
	public void sort() throws ClassCastException {
		ArrayList<Pair<V>> temp = (ArrayList<Pair<V>>)arraySorter(); //create new arrayList temp referencing arraySorter
		int s = temp.size(); int z = entries.length;
		Collections.sort(temp); 
		entries = (Node<V>[])new Node[z];
		head = tail = null;
		N = 0;
		for(int index = 0; index< s; index++) {
				this.put(index,temp.get(index).value);
			}
	  	}
	//ASORT
	public void asort() {
		ArrayList<Pair<V>> temp = (ArrayList<Pair<V>>)arraySorter(); //create new arrayList temp referencing arraySorter
		int s = temp.size(); int z = entries.length;
		Collections.sort(temp); 
		entries = (Node<V>[])new Node[z];
		head = tail = null;
		N = 0;
		for(int index = 0;index < s;index++) {
			this.put(temp.get(index).key,temp.get(index).value);
		}
	}
	//UNSET
	public void unset(String key) {
		if(N == 0) return;
		int hashKey = key.hashCode() & 0x7fffffff;
		Node<V> nodeDel;
		int s = entries.length;
		for(int i = 0; i<s; i++) 
		{
			int linearProbe = (hashKey+i) % entries.length;
			if(entries[linearProbe] == null) return;
			//If we're here that means we found the node that has not been deleted
			//We need to delete the node now
			if(entries[linearProbe].pair.key.equals(key)) 
			{
				nodeDel = entries[linearProbe]; //get value of linear probe
				
				if(nodeDel.previous != null) { 
					nodeDel.previous.next = nodeDel.next; 
				}
				else { 
					head = nodeDel.next; 
				}
				if(nodeDel.next != null) { 
					nodeDel.next.previous = nodeDel.previous; 
				}
				else { 
					tail = nodeDel.previous; 
				}
				//Rehash cluster
				entries[linearProbe] = null;
				N--;
				rehash(hashKey+i,1); 				
				return;
	  	}
	}
}
	private void rehash(int deleteIndex,int i) {
		int linearProbe = (deleteIndex + i) % entries.length;
		Node<V> cluster = entries[linearProbe];
		if(cluster == null) return;
		entries[linearProbe] = null;
		System.out.println("\t\tKey "+cluster.pair.key+" rehashed...\n");
		add(cluster);
		rehash(deleteIndex,i+1);
		
	}
	public PHPArray<String> array_flip() throws ClassCastException{
		iterator = new PHPArrayIterator<>(head);
		int s = entries.length; Pair<V> p; 
		PHPArray<String> temp = new PHPArray<String>(s);
		while((p = this.each()) != null) {
			temp.put((String)p.value, p.key);
		}
		return temp;
	}
	public Pair<V> each(){
		if(iterator == null) return null; 
		return iterator.nextPair();
	}
	public void reset() {
		iterator = new PHPArrayIterator<>(head);
	}
	public Iterator<V> iterator() {
		return new PHPArrayIterator<>(head);
	}
	public static class Pair<V> implements Comparable<Pair<V>>{
		public String key;
		public V value;
		public Pair(String key,V value) {
			this.key = key;
			this.value = value;
		}
		@Override
		public int compareTo(Pair<V> temp) throws ClassCastException{
			 return ((Comparable) this.value).compareTo((Comparable) temp.value);
		}
	}
	private class Node<V>{
		private Pair<V> pair;
		private Node<V> next; 
		private Node<V> previous; 
		
		private Node(String key,V value) {
			this.pair = new Pair(key,value);
		}	
	}
}
