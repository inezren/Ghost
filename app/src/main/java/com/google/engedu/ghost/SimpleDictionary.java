
package com.google.engedu.ghost;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

public class SimpleDictionary implements GhostDictionary {
    private ArrayList<String> words;

    public SimpleDictionary(InputStream wordListStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
        words = new ArrayList<>();
        String line = null;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            if (word.length() >= MIN_WORD_LENGTH)
              words.add(line.trim());
        }
    }

    @Override
    public boolean isWord(String word) {
        return words.contains(word);
    }

    @Override
    public String getAnyWordStartingWith(String prefix) {
        Random random = new Random();
        if(prefix == "")
            return words.get(random.nextInt(words.size()));
        //binary search
        int wordIndex = binarySearch(words,prefix,0,words.size());
       // System.out.println(words.get(wordIndex));
        if(wordIndex == -1)
            return null;
        else
            return words.get(wordIndex);
    }

    @Override
    public String getGoodWordStartingWith(String prefix) {
        return null;
    }


    public int binarySearch(ArrayList word, String key, int min, int max){
        if (min > max)
            return -1;
        int mid = (max+min)/2;
        if((words.get(mid).startsWith(key)))
            return mid;
        else if((words.get(mid)).compareTo(key) > 0)
            return binarySearch(word,key,min, mid-1);
        else
            return binarySearch(word, key,mid+1,max);
    }
}
