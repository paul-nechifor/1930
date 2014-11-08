package ro.minimul.romania1930.logic;

import java.util.ArrayList;
import java.util.List;
import ro.minimul.romania1930.data.FaQuestion;
import ro.minimul.romania1930.data.NaQuestion;
import ro.minimul.romania1930.data.QuestionSet;
import ro.minimul.romania1930.util.Util;

public class Attack {
    private static int NEW_ID = 1;
    
    public static final int FA_QUESTION = 1;
    public static final int NA_QUESTION = 2;
    
    public final int id;
    public final AnswerZone victim;
    public final List<AnswerZone> aggressors = new ArrayList<AnswerZone>();
    public final int secondsToAnswer;
    public final long deadline;
    public final FaQuestion faQuestion;
    public final NaQuestion naQuestion;
    
    public int state = FA_QUESTION;
    
    private Attack(int id, AnswerZone victim, AnswerZone aggressor,
            int secondsToAnswer, long deadline, FaQuestion faQuestion,
            NaQuestion naQuestion) {
        this.id = id;
        this.victim = victim;
        this.aggressors.add(aggressor);
        this.secondsToAnswer = secondsToAnswer;
        this.deadline = deadline;
        this.faQuestion = faQuestion;
        this.naQuestion = naQuestion;
    }
    
    public static Attack create(OwnedZone victim, OwnedZone aggressor,
            int secondsToAnswer, QuestionSet questionSet) {
        int id = NEW_ID;
        NEW_ID++;
        
        Attack ret = new Attack(
            id,
            new AnswerZone(victim),
            new AnswerZone(aggressor),
            secondsToAnswer,
            System.nanoTime() + secondsToAnswer * Util.NANO,
            questionSet.getRandomFaQuestion(),
            questionSet.getRandomNaQuestion()
        );
        
        return ret;
    }
    
    public void addAggressor(OwnedZone aggressor) {
        aggressors.add(new AnswerZone(aggressor));
    }
    
//    public AttackSubMsg getSubMsg() {
//        AttackSubMsg ret = new AttackSubMsg();
//        
//        ret.froms = new int[otherAggressors.size() + 1];
//        ret.froms[0] = firstAggressor.ownedZone.zone.id;
//        int i = 1;
//        for (AnswerZone z : otherAggressors) {
//            ret.froms[i] = z.ownedZone.zone.id;
//            i++;
//        }
//        
//        ret.to = victim.ownedZone.zone.id;
//        
//        ret.ms = (int) ((deadline - System.nanoTime()) / 1000000);
//        
//        return ret;
//    }
}
