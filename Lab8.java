import java.util.*;

public class Lab8 
{
    public static void main(String[] args) 
    {
        try (Scanner sc = new Scanner(System.in)) 
        {
            System.out.print("Enter your sentence: ");
            String text = sc.nextLine().toLowerCase(); // Convert text to lowercase so any input can be read
            
            Map<Character, Integer> frequencyMap = countFrequency(text);
            
            System.out.println("Frequency of each letter:");
            printFrequency(frequencyMap);
            
            PriorityQueue<Tree> forest = createForest(frequencyMap);
            
            Tree huffmanTree = buildHuffmanTree(forest);
            
            Map<Character, String> encoding = huffManEncoding(huffmanTree);
            
            System.out.println("\nHuffman encoding:");
            for (Map.Entry<Character, String> entry : encoding.entrySet()) 
            {
                System.out.println("'" + entry.getKey() + "' : " + entry.getValue());
            }
        }
    }
    
    public static class Tree implements Comparable<Tree> 
    {
        public Node root;            
        public int frequency = 0; 
        
        public Tree() 
        {
            root = null;
        }
        
        // Compare trees based on their frequency
        public int compareTo(Tree object) 
        {
            return frequency - object.frequency;
        }
    }

    public static class Node 
    {
        public char letter;       
        public Node leftChild;    
        public Node rightChild;   
    }
    
    // Count the frequency of each letter in the input text
    public static Map<Character, Integer> countFrequency(String text) 
    {
        Map<Character, Integer> frequencyMap = new HashMap<>();
        for (char c : text.toCharArray()) 
        {
            if (Character.isLetter(c) || c == ' ') 
            {
                // Get the frequency of each letter by adding one if it is there 
                frequencyMap.put(c, frequencyMap.getOrDefault(c, 0) + 1);
            }
        }
        return frequencyMap;
    }
    
    // Make a tree for each letter and add them to a priority queue
    public static PriorityQueue<Tree> createForest(Map<Character, Integer> frequencyMap) 
    {
        PriorityQueue<Tree> forest = new PriorityQueue<>();
        for (Map.Entry<Character, Integer> entry : frequencyMap.entrySet()) 
        {
            Tree tree = new Tree();
            tree.root = new Node();
            tree.root.letter = entry.getKey();
            tree.frequency = entry.getValue();
            forest.add(tree);
        }
        return forest;
    }
    
    // Build the Huffman tree by combining trees in the priority queue
    public static Tree buildHuffmanTree(PriorityQueue<Tree> forest) 
    {
        while (forest.size() > 1) 
        {
            Tree left = forest.poll();
            Tree right = forest.poll();
            
            Tree mergedTree = new Tree();
            mergedTree.frequency = left.frequency + right.frequency;
            mergedTree.root = new Node();
            mergedTree.root.leftChild = left.root;
            mergedTree.root.rightChild = right.root;
            
            forest.add(mergedTree);
        }
        return forest.poll();
    }
    
    // Generate huffman encoding for each letter
    public static Map<Character, String> huffManEncoding(Tree huffmanTree) 
    {
        Map<Character, String> encodingMap = new HashMap<>();
        generateEncoding(huffmanTree.root, "", encodingMap);
        return encodingMap;
    }
    
    // Helper method to recursively generate Huffman encoding for each letter
    private static void generateEncoding(Node node, String encode, Map<Character, String> encodingMap) 
    {
        if (node != null) 
        {
            if (node.leftChild == null && node.rightChild == null) 
            {
                encodingMap.put(node.letter, encode);
            } 
            else 
            {
                generateEncoding(node.leftChild, encode + "0", encodingMap);
                generateEncoding(node.rightChild, encode + "1", encodingMap);
            }
        }
    }
    
    // Method to print the frequency of each letter 
    public static void printFrequency(Map<Character, Integer> frequencyMap) 
    {
        List<Map.Entry<Character, Integer>> entries = new ArrayList<>(frequencyMap.entrySet());

        // Sort the entries based on their frequencies 
        entries.sort((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()));

        // Print the frequencies starting from the highest frequency
        for (Map.Entry<Character, Integer> entry : entries) 
        {
            if (entry.getKey() == ' ') 
            {
                System.out.println("' ' has a frequency of " + entry.getValue());
            } 
            else 
            {
                System.out.println("'" + entry.getKey() + "' has a frequency of " + entry.getValue());
            }
        }
    }
}