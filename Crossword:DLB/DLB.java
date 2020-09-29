public class DLB implements DictInterface { //DLB Class Implementation of Dict Interface using under 100 lines
    Node root;
    public DLB() {
        root = new Node();
    }    
    // Searches the dictionary for the sequence of chars (letter)
    public int searchPrefix(StringBuilder s, int start, int end) {
        Node temp = root; int charIndex = 0;  //create reference to the root node Dict
        while (temp != null && charIndex < s.length()){ //while root not null and greater than 0
            temp = getChild(temp, s.charAt(charIndex)); //pass in each charcater to getChild()
            charIndex++;
        } //all characters passed in break from loop
        if (temp != null) { //if temp node is not null means something was found
            temp = getChild(temp, '^'); //find node that ends with ^
            if (temp == null)   //if temp  null, no dollar sign was found and its a prefix ONLY
                return 1; //return 1 for prefix ONLY
            if (temp.silb == null) // if current is not null but sibling is, then dollar sign bu tno sibling, word ONLY
                return 2; //return 2 for word ONLY
            return 3;  //if temp not null and we find ^ with sibling, return 3 for word and prefix 
        } else {
            return 0; //else its neither a word or prefix, return 0
        }
    }
    //add ^ to end of word 's' chat using concat
    public boolean add(String s) {
        String word = s.concat("^"); //^ to end of word string Str
        Node temp = root;    //create reference to the root node Dict
        for (int i = 0; i < word.length(); i++) 
            temp = insertChild(temp, word.charAt(i)); //iterates over the string and pass one char at a time to insertChild()
        return temp.constructed; //boolean for determining whether it has been created or already existed prior
    }
    public int searchPrefix(StringBuilder s) {
        return searchPrefix(s, 0, s.length()-1);
    }
    private Node insertChild(Node temp, char letter) {
        if (temp.child != null)  //if node child is not null
            return insertSibling(temp.child, letter); //If current has a child, the node and value are passed to the insertSibling method
        else { //Else currents child reference is null, it adds a child node and sets value to letter.
            temp.child = new Node(letter); 
            temp.child.constructed = true; // node created set to true
            return temp.child; //return
        }
    }
    private Node getChild(Node temp, char letter) {
        if (temp.child != null && temp.child.data == letter) { //if child of first node is not null and value is letter
            return temp.child; //return that child node
        }
        return getSilbling(temp.child, letter); //Otherwise pass the child of current and the value letter to getSibling
    }
    // insertSibling is called within insertChild and receives the same root reference and letter reference
    private Node insertSibling(Node temp, char letter) {

        while (temp.silb != null && temp.data != letter) // traverse sibling node 
            temp = temp.silb;

        if (temp.data == letter) {  //if temp data matches our char letter, then created is false and we return the found node.
            temp.constructed = false; //set to false
            return temp; //return node found
        }
        temp.silb = new Node(letter); //else we create a new node, set created to true and return the created node.
        temp.silb.constructed = true; //constructed is now True
        return temp.silb; //return new node
    }
     //getSibling through siblings while temp node is not null and the value of the current node does not match char letter. 
     // after loop itll check if current letter ise equal to the temp node, or temp is null and not found (null)
    private Node getSilbling(Node temp, char letter) {
        while (temp != null && temp.data != letter) {
            temp = temp.silb;
        }
        return temp;
    }
    public class Node {
        Node child; //child node
        Node silb; //sibling node
        Node root; //root node
        char data; //data 
        boolean constructed; // boolean for checking if the node has been more recently been created or did it already exist

        public Node() {
            this.root = root;
        }
        public Node(char letter) {
            this.data = letter; //
            this.child = null;
            this.silb = null;
            this.constructed = false; //initially set to false
        }
    }
}