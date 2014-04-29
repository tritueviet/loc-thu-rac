package bayes;

import readFile.*;

/**
 *
 * @author TRITUEVIET
 */
public class Bayesian {

    public static void main(String args[]) {
        try {
            SpamFilter filter = new SpamFilter();
            filter.trainSpam("spam.txt");
            filter.trainGood("good.txt");
            filter.finalizeTraining();
            ReadFile fr = new ReadFile("messages/mail1.txt");
            String stuff = fr.getContent();
            boolean spam = filter.analyze(stuff);
            if (spam) {
                System.out.println("spam");
            } else {
                System.out.println("không phải");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
