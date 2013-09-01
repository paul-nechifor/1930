package ro.minimul.romania1930.data;

import java.io.File;
import java.io.FileNotFoundException;
import ro.minimul.romania1930.util.Util;

public class QuestionSet {
    public final FaQuestion[] faQuestions;
    public final NaQuestion[] naQuestions;
    
    private QuestionSet(FaQuestion[] faQuestions, NaQuestion[] naQuestions) {
        this.faQuestions = faQuestions;
        this.naQuestions = naQuestions;
    }
    
    public static QuestionSet load(Config config) throws FileNotFoundException {
        File faFile = Util.getJarRelative(config.faFile);
        File naFile = Util.getJarRelative(config.naFile);
        
        FaQuestion[] faQuestions = FaQuestion.loadFaQuestions(faFile);
        NaQuestion[] naQuestions = NaQuestion.loadNaQuestions(naFile);
        
        return new QuestionSet(faQuestions, naQuestions);
    }
}
