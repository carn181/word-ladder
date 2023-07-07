import java.io.*;
import java.util.*;

public class Solution{
  // Array of possible transformations
  private static ArrayList<ArrayList<String>> results;
  // Adjacency List for DFS
  private static Map<String, ArrayList<String>> adj;
  
  // Chooses shortest transformation from results
  public static ArrayList<String> findShortestTransformation(String beginWord, String endWord, ArrayList<String> wordList)
  {
    if(wordList==null || !wordList.contains(endWord)){
      return null;
    }
    System.out.println("ARGUMENTS- beginWord: "+beginWord+" endWord: "+endWord+" wordList[]: "+wordList);

    results = new ArrayList<ArrayList<String>>(); 
    findLadders(beginWord, endWord, wordList);
    
    if(results.isEmpty())
      return new ArrayList<String>();

    ArrayList<String> shortest = results.stream()
                                      .min(Comparator.comparing(ArrayList<String>::size))
                                      .get();
    return shortest;
  }
  
  // Fills results with possible transformation
  // BFS+DFS
  // Works by first bfs and making adjacency list for dfs
  // Then dfs traverses graph till reaches endWord and adds to results
  private static void findLadders(String beginWord, String endWord, ArrayList<String> wordList){
    adj = new HashMap<String, ArrayList<String>>();
    Queue<String> queue = new LinkedList<String>();
    
    wordList.add(beginWord);
    String curr = beginWord;
    queue.add(beginWord);
    
    // Starting breadth first traversal
    while(!queue.isEmpty()){
      // Traversing through all elements of queue
      for(int j=0; j<queue.size(); j++){
        String curr_word=queue.poll();
        wordList.remove(curr_word);
        adj.put(curr_word, new ArrayList<String>());
        
        char[] word_chars = curr_word.toCharArray();
        // Checking all Possible Combinations of Patterns
        for(int i =0; i<curr_word.length(); i++){
          char original_char = word_chars[i];
          for(char c = 'a'; c <= 'z'; c++){
            // Avoid if it comes to same word as current
            if(c==original_char){continue;}

            char[] test_word = curr_word.toCharArray();
            test_word[i]=c;
            String new_word = String.valueOf(test_word);

            // If new word/combination == endWord, add to adjacency list and exit loop
            if(new_word.equals(endWord)){
              adj.get(curr_word).add(new_word);
              continue;
            }
            // If new word/combination is in the WordList, add to queue and adjacency list
            if(wordList.contains(new_word)&&!queue.contains(new_word)){
              queue.add(new_word);
              adj.get(curr_word).add(new_word);
            }
          }
        }
      }
    }
    
    System.out.println("ADJACENCY LIST: "+adj);

    Map<String, Boolean> visited = new HashMap<String,Boolean>();
    ArrayList<String> path = new ArrayList<String>();
    dfs(beginWord, endWord, visited,path);
  }

  // DFS through graph by adjacency list
  // Traverses till meets endWord, adds it to results then
  private static void dfs(String currWord, String endWord, Map<String, Boolean> visited, ArrayList<String> path){
    // Multiple possible transformations, so endWord should be able to be visited multiple times
    if(!currWord.equals(endWord))
      visited.put(currWord, true);
    
    // Reaching endWord and adding to results
    if(currWord.equals(endWord)){
      results.add(new ArrayList<String>(path));
      return;
    }
    
    // Leaf
    if(adj.get(currWord).isEmpty()){
      return;
    }

    for(int i=0; i<adj.get(currWord).size();i++){
      if(adj.get(currWord).isEmpty())
        continue;
     
      String neighbour=adj.get(currWord).get(i);
      
      // If not traversed
      if(visited.get(neighbour)==null){
        path.add(neighbour);
        dfs(neighbour, endWord, visited,new ArrayList<String>(path));
      }
    }
  }
}

class main {
  public static void main(String args[]){
    ArrayList<String> test1 = new ArrayList<String>(Arrays.asList("hot", "cot", "dot", "dog", "lot", "log", "cog"));
    // hit -> cog
    // [hot, cot, dot, dog, lot, log, cog]
    // should return [hot, cot, cog]
    System.out.println("TEST 1: "+Solution.findShortestTransformation("hit", "cog", test1));
    
    // hit -> dog
    // [hot, dog]
    ArrayList<String> test2 = new ArrayList<String>(Arrays.asList("hot", "dog"));
    System.out.println("TEST 2: "+Solution.findShortestTransformation("hot", "dog", test2));
    
    ArrayList<String> test3 = new ArrayList<String>(Arrays.asList("a","b","c"));
    System.out.println("TEST 3: "+Solution.findShortestTransformation("a", "c", test3));
  }
}
