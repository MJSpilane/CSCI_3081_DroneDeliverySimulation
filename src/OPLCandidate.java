/**
 * Object representing a Candidate in the OPL voting process
 * @author Perrie Gryniewicz, Bek Allenson
 */
class OPLCandidate extends Candidate {

    /**
     * Constructor
     * @param name_ of candidate
     * @param party_ of candidate
     * @param ballotIndex index in the List of candidates
     */
    public OPLCandidate(String name_, String party_, int ballotIndex) {
        super(name_, party_, ballotIndex);
    }

    /**
     * increases ballot count by one (how many votes the candidate has received)
     */
    public void addVote() {
        ballotCount++;
    }

    /**
    * <p> Retrieve the number of ballots currently allocated to the candidate. </p>
    * @return  ballotCount an int representing how many ballots the candidate currently has allocated to candidate.
    */
    public int getBallotCount(){
        return ballotCount;
    }
}
