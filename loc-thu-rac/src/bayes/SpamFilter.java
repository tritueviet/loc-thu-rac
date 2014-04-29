package bayes;

import java.io.IOException;
import java.util.*;
import java.util.regex.*;

import readFile.ReadFile;

/**
 *
 * @author TRITUEVIET
 */
public class SpamFilter {

    HashMap words;

    public SpamFilter() {
        words = new HashMap();

    }

    public void trainSpam(String file) throws IOException {
        ReadFile fr = new ReadFile(file);
        String content = fr.getContent().substring(1);
        
        String[] tokens = content.split(" ");
        int spamTotal = 0;
        for (int i = 0; i < tokens.length; i++) {
            String word = tokens[i].toLowerCase();


            spamTotal++;
            if (words.containsKey(word)) {
                Word w = (Word) words.get(word);
                w.countBad();
            } else {
                Word w = new Word(word);
                w.countBad();
                words.put(word, w);
            }

        }

        Iterator iterator = words.values().iterator();
        while (iterator.hasNext()) {
            Word word = (Word) iterator.next();
            word.calcBadProb(spamTotal);
        }
    }

    public void trainGood(String file) throws IOException {
        ReadFile fr = new ReadFile(file);

        String content = fr.getContent().substring(1);
        
        System.out.println(""+content);
        
        String[] tokens = content.split(" ");
        int goodTotal = 0;

        for (int i = 0; i < tokens.length; i++) {
            String word = tokens[i].toLowerCase();

            goodTotal++;
            if (words.containsKey(word)) {
                Word w = (Word) words.get(word);
                w.countGood();
            } else {
                Word w = new Word(word);
                w.countGood();
                words.put(word, w);
            }

        }
        Iterator iterator = words.values().iterator();
        while (iterator.hasNext()) {
            Word word = (Word) iterator.next();
            word.calcGoodProb(goodTotal);
            
        }

    }

    public boolean analyze(String stuff) {
        ArrayList interesting = new ArrayList();
        
        String[] tokens = stuff.substring(1).split(" ");
        System.out.println(""+stuff);
        for (int i = 0; i < tokens.length; i++) {
            String s = tokens[i].toLowerCase();

            Word w;

            if (words.containsKey(s)) {
                w = (Word) words.get(s);
            } else {
                w = new Word(s);
                w.setPSpam(0.4f);
            }

            int limit = 15;
            if (interesting.isEmpty()) {
                interesting.add(w);
            } else {
                for (int j = 0; j < interesting.size(); j++) {
                    Word nw = (Word) interesting.get(j);
                    if (w.getWord().equals(nw.getWord())) {
                        break;
                    } else if (w.interesting() > nw.interesting()) {
                        interesting.add(j, w);
                        break;
                    } else if (j == interesting.size() - 1) {
                        interesting.add(w);
                    }
                }
            }

            while (interesting.size() > limit) {
                interesting.remove(interesting.size() - 1);
            }


        }

        float pposproduct = 1.0f;
        float pnegproduct = 1.0f;
        for (int i = 0; i < interesting.size(); i++) {
            Word w = (Word) interesting.get(i);
            pposproduct *= w.getPSpam();
            pnegproduct *= (1.0f - w.getPSpam());
        }

        float pspam = pposproduct / (pposproduct + pnegproduct);
        System.out.println("\nSpam rating: " + pspam);

        if (pspam > 0.7f) {
            return true;
        } else {
            return false;
        }

    }

    public void displayStats() {
        Iterator iterator = words.keySet().iterator();
        while (iterator.hasNext()) {
            String key = (String) iterator.next();
            Word word = (Word) words.get(key);
            if (word != null) {
                System.out.println(key + " " + word.getPSpam());
            }
        }
    }

    public void finalizeTraining() {
        Iterator iterator = words.values().iterator();
        while (iterator.hasNext()) {
            Word word = (Word) iterator.next();
            word.finalizeProb();
            System.out.println(""+word.getWord()+" "+ word.getPSpam());
        }
    }
}
