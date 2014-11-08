package ro.minimul.romania1930.logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AnswerZone {
    private static class Diff implements Comparable<Diff> {
        final AnswerZone az;
        final int answer;
        final int time;
        
        public Diff(AnswerZone az, int answer, int time) {
            this.az = az;
            this.answer = answer;
            this.time = time;
        }

        @Override
        public int compareTo(Diff o) {
            if (answer != o.answer) {
                return answer - o.answer;
            } else {
                return time - o.time;
            }
        }
    }
    
    public final OwnedZone ownedZone;
    
    /**
     * The index of the answer or -1 for not answered.
     */
    public int faAnswer = -1;
    
    /**
     * The number of milliseconds the player took to answer the question.
     */
    public int faTime = -1;
    
    /**
     * If the player can answer the NA question.
     */
    public boolean canAnswerNa = false;
    
    /**
     * The integral answer or -1 for not answered.
     */
    public int naAnswer = -1;
    
    /**
     * The number of milliseconds the player took to answer the question.
     */
    public int naTime = -1;
    
    AnswerZone(OwnedZone ownedZone) {
        this.ownedZone = ownedZone;
    }
    
    public static AnswerZone getWinner(List<AnswerZone> list, int correct) {
        List<Diff> diffs = new ArrayList<Diff>();
        
        for (AnswerZone az : list) {
            diffs.add(new Diff(az, Math.abs(az.naAnswer - correct), az.naTime));
        }
        
        Collections.sort(diffs);
        
        return diffs.get(0).az;
    }
}
