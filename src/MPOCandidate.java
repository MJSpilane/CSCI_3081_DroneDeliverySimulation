public class MPOCandidate extends Candidate{
    private int ballotCount; 

    public MPOCandidate(String name_, String party_, int ballotIndex) {
        super(name_, party_, ballotIndex);
        ballotCount = 0; 
    }

    /**
     * increases ballot count by one (how many votes the candidate has received)
     */
    public void addVote() {
        ballotCount++;
    }

    public String getName() {
        return super.name;
    }

    public String getParty(){
        return super.party;
    }
    
    public int getBallotCount(){
        return ballotCount;
    }
}
