import java.util.ArrayList;
import java.util.Collections;

/**
 * Object representing a Party in the OPL voting system
 * @author Perrie Gryniewicz, Bek Allenson
 */
class OPLParty implements Comparable<OPLParty> {
    private String name;
    private int totalVotes;
    private int votesLeft;
    private int nextInLine;
    private ArrayList<OPLCandidate> candidates;
    private int numSeats;

    /**
     * Constructor
     * @param name_ name of the party as given in election file header
     */
    public OPLParty(String name_) {
        this.name = name_;
        this.totalVotes = 0;
        this.votesLeft = 0;
        this.nextInLine = 0;
        this.candidates = new ArrayList<>();
        this.numSeats = 0;
    }

    /**
     * adds given candidate to the party
     * @param newCandidate candidate to be added
     */
    public void addCandidate(OPLCandidate newCandidate) {
        candidates.add(newCandidate);
        totalVotes += newCandidate.getBallotCount();
    }

    /**
     * sort candidates in descending order by their total vote count
     */
    public void sortCandidates() {
        Collections.sort(candidates, Collections.reverseOrder());
    }

    /**
     * based on how many seats the party wins in the first round,
     * seat the top candidates into the winners
     * @param quota represents how many votes it takes to win one seat in the first round
     * @param winners list to add winners to
     * @param audit allows us to log changes to audit file
     */
    public void seatFirstRound(int quota, ArrayList<OPLCandidate> winners, Audit audit) {
        int numToSeat = totalVotes / quota;
        votesLeft = totalVotes % quota;
        for(nextInLine = 0; nextInLine < numToSeat; nextInLine++) {
            OPLCandidate c = candidates.get(nextInLine);
            winners.add(c);
            audit.log(c.getName() + " has been seated for the " + name + " party.\n");
            numSeats++;
        }
    }

    /**
     * based on how many votes are remaining, seat another candidate from the party
     * @param winners list for candidate to be added to
     * @param audit allows us to log activity to audit file
     */
    public void seatSecondRound(ArrayList<OPLCandidate> winners, Audit audit) {
        OPLCandidate c = candidates.get(nextInLine);
        winners.add(c);
        audit.log(c.getName() + " was seated for the " + name + " party with their " + votesLeft + " remaining votes.\n");
    }

    /**
     * allows us to compare OPLParty objects based on votes left
     * @param o OPLParty to compare to
     * @return int representing the outcome of the comparison
     */
    @Override
    public int compareTo(OPLParty o) {
        if (this.votesLeft > o.votesLeft) {
            // if current object is greater,then return 1
            return 1;
        }
        else if (this.votesLeft < o.votesLeft) {
            // if current object is greater,then return -1
            return -1;
        }
        else {
            // if current object is equal to o,then return 0
            return 0;
        }
    }

    public String getName() {
        return name;
    }

    public int getTotalVotes() {
        return totalVotes;
    }

    public int getVotesLeft() {
        return votesLeft;
    }

    public int getNumSeats() {
        return numSeats;
    }
}


