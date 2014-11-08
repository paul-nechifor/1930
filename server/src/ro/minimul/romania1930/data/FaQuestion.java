package ro.minimul.romania1930.data;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FaQuestion {
    public final String text;
    public final String[] answers;
    public final int correct;
    
    public FaQuestion(String text, String[] answers, int correct) {
        this.text = text;
        this.answers = answers;
        this.correct = correct;
    }
    
    public static FaQuestion[] loadFaQuestions(File file)
            throws FileNotFoundException {
        List<FaQuestion> ret = new ArrayList<FaQuestion>();
        
        Scanner in = new Scanner(file);
        
        String text;
        String[] answers = new String[4];
        int correct;
        
        while (in.hasNextLine()) {
            text = in.nextLine();
            for (int i = 0; i < answers.length; i++) {
                answers[i] = in.nextLine();
            }
            correct = Integer.parseInt(in.nextLine()) - 1;            
            ret.add(new FaQuestion(text, answers, correct));
        }
        
        return ret.toArray(new FaQuestion[0]);
    }
}
