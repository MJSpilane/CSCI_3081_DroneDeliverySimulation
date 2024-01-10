import java.util.*;


/**
 * IRBallot represents ballot objects to be used in IR elections
 * 
 * @author Matthew Johnson
 */
class IRBallot extends Ballot {
    /**
     * An arrayList of candidate objects representing the votes cast on the ballot object
     */
    private ArrayList<Candidate> votes;

    /**
     * An integer that keeps track on which candidate in the votes array the ballot is currently assigned to.
     */
    private int rank;
    
    /**
     * A boolean value that represents if the ballot object is valid (if it is assigned to a candidate currently or has valid candidates left to go to in its votes array)
     */
    private boolean valid;

    /**
     * <p>Constructor for IRBallot objects, assigns values to rank, votes and valid fields. Also gives itself to the first candidate in the votes array.</p>
     * @param votes
     */
    public IRBallot(ArrayList<Candidate> votes) {
        this.rank = 0;
        this.votes = votes;
        valid = true;
        IRCandidate firstCandidate = (IRCandidate) votes.get(0);
        firstCandidate.giveBallot(this);
    }

    /**
     * <p>getCandidate returns the candidate object of which this ballot is currently assigned to.</p>
     * @return if the current ballot is valid, returns the candidate object that this ballot is assigned to. If this ballot is not valid, returns null
     */
    public Candidate getCandidate() {
        if(this.valid == false) {
            return null;
        }
        return votes.get(rank);
    }


   
    /**
     * <p>getNextCandidate will iterate into the votes array with the rank variable and find the next valid candidate, if there is a valid candidate left in the votes array, it will
     * assign itself to this candidate object. If there are no valid candidates left in the votes array, then it will change this.valid to false and return false
     * @return returns true if a valid candidate is found in the votes array, returns false if not and this ballot is not invalid.
     */
    public boolean getNextCandidate() {
        this.rank +=1;
        while(rank < votes.size()) {
            IRCandidate current = (IRCandidate) votes.get(rank);
            if(current != null) {
                if(current.getRunningStatus()){
                    current.giveBallot(this);
                    return true;
                }
                else {
                    this.rank+=1;
                }
            }
            else{
                this.valid = false;
                return false;
            }
        }
        if(rank >= votes.size()) {
            this.valid = false;
            return false;
        }
        return true;
    }

    /**
     * <p>Getter for valid field (for use in testing)</p>
     * @return boolean value held in valid field
     */
    public boolean getValid() {
        return this.valid;
    }

    /**
     * <p>Getter for rank field (for use in testing)</p>
     * @return integer value held in rank field
     */
    public int getRank() {
        return this.rank;
    }

    /**
     * <p>Setter for valid field (for use in testing)</p>
     * @param b boolean value to set valid field to
     */
    public void setValid(boolean b) {
        this.valid = b;
    }

    /**
     * <p>Setter for rank field (for use in testing)</p>
     * @param i integer value to set rank field to
     */
    public void setRank(int i) {
        this.rank = i;
    }

    /**
     * <p>Getter for votes field (for use in testing)</p>
     * @return ArrayList of candidate objects that is held in votes field
     */
    public ArrayList<Candidate> getVotes() {
        return this.votes;
    }
}
