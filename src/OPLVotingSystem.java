import java.awt.*;
import java.io.*;
import java.lang.reflect.Array;
import java.util.*;

/**
 * Object representing an OPLVotingSystem that determines the winners of the election
 * @author Perrie Gryniewicz, Bek Allenson
 */
class OPLVotingSystem extends VotingSystem {
    private int numSeatsAvailable;
    private HashMap<String, OPLParty> parties;
    private ArrayList<OPLCandidate> winners;
    private Audit audit;

    /**
     * Constructor
     * @param c list of candidates
     * @param numCandidates how many candidates in the election
     * @param numBallots how many ballots in election
     * @param fp file pointer to election file
     * @param numSeats how many seats are to be allocated
     */
    public OPLVotingSystem(ArrayList<Candidate> c, int numCandidates, int numBallots, File fp, int numSeats) {
        super(c, numCandidates, numBallots, fp);
        this.numSeatsAvailable = numSeats;
        this.winners = new ArrayList<>();
        this.parties = new HashMap<>();
        this.audit = new Audit();
    }

    /**
     * runs election, called by Election class
     * @return true if no errors, false if there were errors
     */
    public boolean runElection() {
        if(numberOfBallots != 0) {
            int quota = numberOfBallots / numSeatsAvailable;

            for(Candidate c : candidates) {
                String partyName = c.getParty();
                OPLParty p = null;
                if(parties.containsKey(partyName)) {
                    p = parties.get(partyName);
                } else {
                    p = new OPLParty(partyName);
                    parties.put(partyName, p);
                }
                p.addCandidate((OPLCandidate) c);
            }

            auditInitialState();

            ArrayList<OPLParty> partiesList = new ArrayList<>();
            for(OPLParty p : parties.values()) {
                p.sortCandidates();
                p.seatFirstRound(quota, winners, audit);
                partiesList.add(p);
            }
            audit.log("\n");

            int seatsLeft = numSeatsAvailable - winners.size();
            Collections.sort(partiesList, Collections.reverseOrder());

            auditAfterFirstRound(seatsLeft);

            auditFinalResults();
            ArrayList<OPLParty> tiedParties = new ArrayList<>();
            tiedParties.add(partiesList.get(0));
            partiesList.remove(0);
            while(seatsLeft > 0) {
                if(partiesList.isEmpty() || partiesList.get(0).compareTo(tiedParties.get(0)) != 0) {
                    OPLParty randomParty = selectRandomParty(tiedParties);
                    randomParty.seatSecondRound(winners, audit);
                    seatsLeft--;
                }
                if(!partiesList.isEmpty()) {
                    if(tiedParties.isEmpty() || partiesList.get(0).compareTo(tiedParties.get(0)) == 0) {
                        tiedParties.add(partiesList.get(0));
                        partiesList.remove(0);
                    }
                }
            }

            printResults();
            return true;
        }
        else{
            System.out.println("No ballots. Therefore, no results.");
            return true;
        }
    }

    public OPLParty selectRandomParty(ArrayList<OPLParty> tiedParties) {
        Random rand = new Random();
        int index = rand.nextInt(tiedParties.size());
        OPLParty party = tiedParties.get(index);
        tiedParties.remove(index);
        return party;
    }

    /**
     * prints out results of the election to the terminal
     */
    public void printResults() {
        for (OPLCandidate c : winners) {
            System.out.println(c.getName() + " was seated for the " + c.getParty() + " party with " + c.getBallotCount() + " votes.");
        }
    }

    /**
     * <p>This sends the audit file logs to represent the state of the election at the start, it will write the type of voting, number
     * of candidates, number of ballots, and candidate information to the audit file </p>
     */
    private void auditInitialState() {
        audit.log("INITIAL STATE OF ELECTION\n");
        audit.log("-------------------------\n\n");
        audit.log("Type of voting: OPL\n");
        audit.log("Number of candidates: " + numberOfCandidates + "\n");
        audit.log("Number of ballots: " + numberOfBallots + "\n");
        audit.log("-------------------------\n");
        audit.log("Candidate totals: \n");
        for (Candidate c : candidates) {
            audit.log(c.getParty() + ": "+ c.getName() + " with " + c.getBallotCount() + " votes\n");
        }
        audit.log("-------------------------\n");
        audit.log("Party totals: \n");
        for (OPLParty p : parties.values()) {
            audit.log(p.getName() + " has " + p.getTotalVotes() + " total votes.\n");
        }
        audit.log("-------------------------\n\n");
    }

    /**
     * writes first round info to audit file
     * @param numLeft how many seats are left to allocate after first round
     */
    private void auditAfterFirstRound(int numLeft) {
        audit.log("FIRST ROUND RESULTS\n");
        audit.log("-------------------------\n\n");
        for (OPLParty p : parties.values()) {
            audit.log(p.getName() + " has been allocated " + p.getNumSeats() + " seat(s) in the first round.\n");
        }
        audit.log("There are " + numLeft + " seat(s) left to allocate\n");
        audit.log("-------------------------\n\n");
    }

    /**
     * writes final results to audit file
     */
    private void auditFinalResults() {
        audit.log("FINAL RESULTS\n");
        audit.log("-------------------------\n\n");
    }
}


