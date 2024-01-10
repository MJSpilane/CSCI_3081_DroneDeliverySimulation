import java.util.*;

/**
* IRCandidate represents a candidate in the IR voting style.
* @author Logan Watters
*/
public class IRCandidate extends Candidate {
   
    private boolean inRunning = true; 
    protected ArrayList<IRBallot> ballots; 
    private int ballotCount;

    /**
    * <p> Initializes the attributes of the IRCandidate object when a new IRCandidate object is created. </p>
    * @param  name        a String object representing the name of the candidate. 
    * @param  party       a String object representing the party of the candidate. 
    * @param  ballotIndex an int representing the position in which the candidate is in each ballot of the CSV file.
    */
    public IRCandidate (String name, String party, int ballotIndex) {
        super(name, party, ballotIndex);
        ballots = new ArrayList<IRBallot>();
    }
    
    /**
    * <p> Retrieve the running status of the candidate. </p>
    * @return  inRunning a boolean representing whether or not the candidate is runnning.
    */
    public boolean getRunningStatus() {
        return inRunning; 
    }
       
    /**
    * <p> Retrieve the number of ballots currently allocated to the candidate. </p>
    * @return  ballotCount an int representing how many ballots the candidate currently has allocated to candidate.
    */
    public int getBallotCount(){
        return ballotCount;
    }
    
    /**
    * <p> Give each ballot currently held by the candidate to the next ranked candidate in their ballot. </p>
    * @return  numInvalid an int representing how many ballots did not have a next ranked candidate. 
    */
    public int removeCandidate() {
        int numInvalid = 0; // tracker to report back to voting system how many ballots are invalid
        for(int i = 0; i<ballotCount; i++) {
            IRBallot currentBallot = ballots.get(i);
            boolean ballotStillValid = currentBallot.getNextCandidate();
            if(!ballotStillValid){
                numInvalid +=1;
            }
        }
        inRunning = false; 
        return numInvalid;
    }

    /**
    * <p> Add a ballot to the candidate's ArrayList of ballots. </p>
    * @param   ballotToGive an initialized IRBallot that is to be given to the candidate.
    */
    public void giveBallot(IRBallot ballotToGive) {
        if(ballotToGive == null) {
        }
        else{
            ballots.add(ballotToGive);
            ballotCount++; 
        }
    }

    /**
    * <p> Set a candidate's running status. </p>
    * @param   b    a boolean representing the desired running status.
    */
    
    public void setInRunning(boolean b) {
        this.inRunning = b;
    }
}
