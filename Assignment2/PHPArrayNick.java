//Nicholas Tillmann
//Assignment #2 
//PURPOSE:
// To implement a hybrid Symbol table that provides hash table access, indexed integer access,
// and sequential access (using an iterator).
//Goal 1: To implement a linear probing hash table.
//Goal 2: To implement an iterator.
import java.util.*;
public class PHPArrayNick<V>implements Iterable<V>{ //PHPArray class will be a symbol table and will thus store data as (key, value) pairs,

    private PHPArrayIterator<V> iterator; 
    private int M;              //SIZE OF UNDERLYING HASH table 
    private int N = 0;          //KEY PAIR VALUE COUNT
    private Node<V> head;       //FIRST NODE INSERTED INTO BEGGINING OF LINKED CHAIN
    private Node<V> tail;       //LAST NODE HENCE END OF LINKED CHAIN
	private Node<V> [] entries;   //ACTUAL HASH table called entries
	
	@SuppressWarnings("unchecked")
	public PHPArrayNick(int capacity){
		this.entries = new Node[capacity];
        M = capacity;
    }
    public PHPArrayNick(){
        entries = new Node[10];
        M = 15; //default constructor size set as 10
    }
    public boolean checkEmpty() { //check if pair values are empty
        if(N == 0){
            return true;
        }
        else{
            return false;
        }
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
		System.out.println("\nHash entries Contents:");
		int s = entries.length;
		for(int i = 0;i< s;i++) {
			Node<V> node = entries[i];
			if(node != null)
				System.out.println("Key: "+ i + "\t" + node.pair.key+" Value: "+node.pair.value);
			else
				System.out.println("null");
		}
		
	}
 	//checkSize: CHECKS IF BOARD IS LESS THAN 50% FULL
    //Table resizing as required:
	@SuppressWarnings("unchecked")
	private void checkSize() {
		int s = entries.length;
		if(N < (s / 2)) return;
		System.out.println("\t\tSize: "+length()+" -- resizing array from "+s+" to "+2*s);
		
		entries = (Node<V>[])new Node[2*entries.length]; //Double the size of entries
		Node<V> node = head;
		while(node != null) {
			insert(node);
			node = node.next;
		}
	}
	//PUT
    //Hashes a given (key,value) pair into the table
    //if the key already exists, replace its value with the new value
    //else create newNode, resize, and insert
	public void put(String key, V value){
        Node <V> node = getValue(key);
        if(node != null){ // while not null 
            node.pair.value = value; 
            return;
        }
        node = new Node<V>(key,value); checkSize(); insert(node); //create new node, check size and if updated//less than 50% already insertNode
        if(N == 0){
            head = node; 
            iterator = new PHPArrayIterator(head);
        }
        else{
            tail.next = node; 
            node.previous = tail; 
        }
        tail = node; 
        N++; 
	}
	
	//GET
	public V get(String key) {
		Node<V> node = getValue(key);
		if(node == null)
			return null;
		return node.pair.value;
	}
	//INSERT (PUT HELPER METHOD)
	public void insert(Node<V> node) {
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
	//GET-VALUE (GET HELPER FUNCITON)
	private Node<V> getValue(String key){ //get helper function
		if(N == 0) return null;
		int s = entries.length;
		int linearProbe; 
		int hashVal = key.hashCode() & 0x7fffffff;
		for(int i = 0; i< s; i++) //iterate through hash table 
		{
			linearProbe = (hashVal+i) % s; //linearProbe value
			if(entries[linearProbe] == null)  //value is null stop searching
				break;
			if(entries[linearProbe].pair.key.equals(key)) //value is key and has not been deleted
				return entries[linearProbe];
		}
		return null;
	}
	//reset entries table
	private void resetEntries() {
		int s = entries.length;
		entries = (Node<V>[])new Node[s];
		head = tail = null;
		N = 0;
	
	}
	//ARRAYSORTER USED AS HELPER FUNCTION FOR KEYS,VALUES,SORT, ASORT
	public ArrayList<Pair<V>> arraySorter(){ //Pairs sorter
		ArrayList<Pair<V>> temp = new ArrayList<Pair<V>>();
		PHPArrayIterator<V> kvIterator = new PHPArrayIterator(head);
		while(kvIterator.hasNext()) {
			temp.add(kvIterator.nextPair());
		}
		return temp;
	}
	//KEYS (SAME METHOD AS VALUES)
	public ArrayList<String> keys(){
		ArrayList<String> temp = new ArrayList<String>();
		PHPArrayIterator<V> kvIterator = new PHPArrayIterator(head);
		while(kvIterator.hasNext()) {
			temp.add(kvIterator.nextPair().key);
		}
		return temp;
	}
	//VALUES (SAME METHOD AS KEYS)
	public ArrayList<V> values(){
		ArrayList<V> temp = new ArrayList<V>();
		PHPArrayIterator<V> kvIterator = new PHPArrayIterator(head);
		while(kvIterator.hasNext()) {
			temp.add(kvIterator.nextPair().value);
		}
		return temp;
		
	}
	//SORT
	public void sort() throws ClassCastException {
		ArrayList<Pair<V>> temp = (ArrayList<Pair<V>>)arraySorter(); //create new arrayList temp referencing arraySorter
		int s = temp.size(); Collections.sort(temp); resetEntries();
		//REBUILD
		for(int i = 0;i<s;i++) {
				this.put(i,temp.get(i).value);
			}
	  	}
	//ASORT
	public void asort() {
		ArrayList<Pair<V>> temp = (ArrayList<Pair<V>>)arraySorter(); //create new arrayList temp referencing arraySorter
		int s = temp.size(); Collections.sort(temp); resetEntries();
		//REBUILD
		for(int i = 0;i<s;i++) {
			this.put(temp.get(i).key,temp.get(i).value);
		}
	}
	//UNSET
	public void unset(String key) {
		if(checkEmpty()){
			return;
		}
		Node<V> nodeToDelete = null;
		int s = entries.length;
		int hashVal = key.hashCode() & 0x7fffffff;
		for(int i = 0; i<s; i++) 
		{
			int linearProbe = (hashVal+i) % s;
			if(entries[linearProbe] == null) 
				return;
			//theres a node found that has not been deleted yet
			if(entries[linearProbe].pair.key.equals(key)) 
			{
				nodeToDelete = entries[linearProbe];
				if(nodeToDelete.next != null) nodeToDelete.next.previous = nodeToDelete.previous; 
					tail = nodeToDelete.previous; 
				if(nodeToDelete.previous != null) nodeToDelete.previous.next = nodeToDelete.next; 
					head = nodeToDelete.next; 
				//else rehash the rest of the cluster
				entries[linearProbe] = null;
				N--;
				rehash(hashVal+i,1); 				
				return;
			}	
	  	}
	}
	private void rehash(int deleteIndex,int i) {
		int s = entries.length;
		int linearProbe = (deleteIndex + i) % s;
		Node<V> cluster = entries[linearProbe];
		if(cluster == null) return;
		entries[linearProbe] = null;
		System.out.println("\t\tKey "+cluster.pair.key+" rehashed...\n");
		insert(cluster);
		rehash(deleteIndex,i+1);
		
	}
	public PHPArray<String> array_flip() throws ClassCastException{
		reset(); int s = entries.length; Pair<V> currPair; PHPArray<String> temp = new PHPArray<String>(s);
		while((currPair = this.each()) != null) {
			temp.put((String)currPair.value, currPair.key);
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
		public int compareTo(Pair<V> p) throws ClassCastException{
			 return ((Comparable) this.value).compareTo((Comparable) p.value);
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
	private class PHPArrayIterator<V> implements Iterator<V> {
        private Node<V> current;
        
        public PHPArrayIterator(Node<V> head) {
            current = head;
        }
        public boolean hasNext()  { return current != null; }

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
}
