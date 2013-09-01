package ro.minimul.romania1930.data;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class NaQuestion {
    public final String text;
    public final int answer;
    
    public NaQuestion(String text, int answer) {
        this.text = text;
        this.answer = answer;
    }
    
    public static NaQuestion[] loadNaQuestions(File file)
            throws FileNotFoundException {
        List<NaQuestion> ret = new ArrayList<NaQuestion>();
        
        Scanner in = new Scanner(file);
        
        String text;
        int correct;
        
        while (in.hasNextLine()) {
            text = in.nextLine();
            correct = Integer.parseInt(in.nextLine());
            ret.add(new NaQuestion(text, correct));
        }
        
        return ret.toArray(new NaQuestion[0]);
    }
}
