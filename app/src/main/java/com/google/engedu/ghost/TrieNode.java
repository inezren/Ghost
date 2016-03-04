
package com.google.engedu.ghost;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;


public class TrieNode extends HashMap<Character, TrieNode> {
    private HashMap<Character, TrieNode> children;
    private boolean isWord;

    public TrieNode() {
        children = new HashMap<>();
        isWord = false;
    }

    public void add(String word) {
        HashMap<Character, TrieNode> child = children;

        for(int i=0; i<word.length(); i++){
            char c = word.charAt(i);

            TrieNode t;
            if(child.containsKey(c)){
                t = child.get(c);
            }else{
                t = new TrieNode();
                child.put(c, t);
            }

            child = t.children;

            //set leaf node
            if(i==word.length()-1)
                t.isWord = true;
        }
    }

    public boolean isWord(String word) {
        TrieNode t = searchNode(word);

        if(t != null && t.isWord)
            return true;
        else
            return false;
    }
    public TrieNode searchNode(String str){
        HashMap<Character, TrieNode> child =children;
        TrieNode t = null;
        for(int i=0; i<str.length(); i++){
            char c = str.charAt(i);
            if(child.containsKey(c)){
                t = child.get(c);
                child = t.children;
            }else{
                return null;
            }
        }

        return t;
    }

    public String getAnyWordStartingWith(String s) {
        TrieNode t = searchNode(s);
        if (t == null)
            return null;
        String word = s;
        HashMap<Character, TrieNode> follow = t.children;
        while (!t.isWord) {
            Object[] characters = follow.keySet().toArray();
            Object c = characters[new Random().nextInt(characters.length)];
            word += String.valueOf(c);

            t = follow.get(c);
            follow = t.children;
        }
        return word;
    }

    public String getGoodWordStartingWith(String s) {
        TrieNode t = searchNode(s);
        if (t == null)
            return null;
        String word = s;
        HashMap<Character, TrieNode> follow = t.children;
        HashMap<Character,Boolean> incomplete = new HashMap<>();
        Set completeWords = follow.keySet();

        for (Object c : completeWords) {
           if(!follow.get(c).isWord)
               incomplete.put((Character)c, false);
        }
        //randomly pick next incomplete next word to return
        Character c;
        if(incomplete.isEmpty()){
            Object[] characters = follow.keySet().toArray();
            c = (Character) characters[new Random().nextInt(characters.length)];
        }else {
            Object[] incomp = incomplete.keySet().toArray();
            c = (Character) incomp[new Random().nextInt(incomp.length)];
        }
        //go down that trie
        String words = s+c;
        return getAnyWordStartingWith(words);
    }
}
