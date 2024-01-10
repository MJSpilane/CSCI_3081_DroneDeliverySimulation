import java.util.*;
/**
* Abstract class representing a candidate. Holds the commonalities between IR and OPL Candidates. 
* @author Logan Watters
*/ 
public abstract class Candidate implements Comparable<Candidate> {
    protected String name; 
    protected String party; 
    protected int ballotCount;
    protected int ballotIndex;
    protected int currentArraySize;  
    /**
    * <p> Constructor. Constructs the Candidate object. </p>
    * @param name        String object representing the name of the candidate. 
    * @param party       String object representing the party of the candidate. 
    * @param ballotIndex int representing which index corresponds to the candidate on the ballots.
    */
    public Candidate(String name, String party, int ballotIndex) {
        this.name = name; 
        this.party = party;
        this.ballotIndex = ballotIndex;
        currentArraySize = 50;
        ballotCount = 0; 
    }
    /** 
    * <p> Getter for the ballot count of the candidate. </p>
    * @return  int representing the number of ballots allocated to the candidate.
    */ 
    public int getBallotCount() {
        return ballotCount; 
    }
    
    /** 
    * <p> Getter for the ballot index of the candidate. </p>
    * @return  int representing the index of the candidate in ballots.
    */ 
    public int getBallotIndex() {
        return ballotIndex; 
    }

    /** 
    * <p> Getter for the name of the candidate. </p>
    * @return  String representing the name of the candidate.
    */ 
    public String getName() {
        return name;
    }
   
    /** 
    * <p> Getter for the party of the candidate. </p>
    * @return  String representing the party of the candidate.
    */ 
    public String getParty() {
        return party; 
    }

    @Override
    public int compareTo(Candidate o) {
        if (this.ballotCount > o.ballotCount) {
            // if current object is greater,then return 1
            return 1;
        }
        else if (this.ballotCount < o.ballotCount) {
            // if current object is greater,then return -1
            return -1;
        }
        else {
            // if current object is equal to o,then return 0
            return 0;
        }
    }
}
