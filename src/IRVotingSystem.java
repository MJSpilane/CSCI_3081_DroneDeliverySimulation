import java.util.ArrayList;
import java.util.Random;
import java.io.*;

/**
 * IRVotingSystem is the votingSystem object for IR election types, they are responsible for running the election algorithm, reporting to audit and printing final results to terminal.
 * 
 * @author Matthew Johnson
 */
public class IRVotingSystem extends VotingSystem {
    private IRCandidate winner; 
    private int originalNumberOfCandidates;
    /**
     * <p>Constructor for the IRVotingSystem class</p>
     * @param c ArrayList of candidates in the running in this election
     * @param numCandidates number of candidates running in this election
     * @param numBallots number of ballots cast in this election
     * @param fp file pointer to the .csv file containing ballot information
     */
    public IRVotingSystem(ArrayList<Candidate> c, int numCandidates, int numBallots, File fp) {
        super(c, numCandidates, numBallots, fp);
        this.originalNumberOfCandidates = numCandidates;
    }

    /**
     * <p>This sends the audit file logs to represent the state of the election at the start, it will write the type of voting, number
     * of candidates, number of ballots, and candidate information to the audit file </p>
     */
    public void auditIntialState() {
        audit.log("INITIAL STATE OF ELECTION\n");
        audit.log("-------------------------\n\n");
        audit.log("Type of voting: IR\n");
        audit.log("Number of candidates: " + getNumberOfCandidates() + "\n");
        audit.log("Number of ballots: " + getNumberOfBallots() + "\n");
        for (int i = 0; i < getNumberOfCandidates(); i++) {
            double percentage = ((double) candidates.get(i).getBallotCount() / (double) numberOfBallots) * 100;
            audit.log(candidates.get(i).getName() + " has " + candidates.get(i).getBallotCount() + " ballots, which is " + percentage + "%.\n");
        }
        audit.log("-------------------------\n\n");
    }

    /**
     * <p>This sends the audit file logs to report the elimiation of a candidate from the election, it will write which candidate was removed
     * and reports the number of ballots they had when they were eliminated. </p>
     * @param can the candidate object representing the candidate with the lowest ballot count and it being eliminated.
     */
    public void auditCandidateFallingOut(Candidate can) {
        audit.log("CANDIDATE REMOVED\n");
        audit.log("-------------------------\n\n");
        audit.log(can.getName() + " has the fewest votes with " + can.getBallotCount() + " ballots.\n");
        audit.log("Their ballots will be redistributed\n");
    }

    /**
     * <p>This sends the audit file information logs containing the baLlot count information of each candidate after a candidate has been eliminated.</p>
     */
    public void auditNewBallotTotals() {
        audit.log("BALLOT TOTALS AFTER REDISTRIBUTION\n");
        audit.log("-------------------------\n\n");
        for (int i = 0; i < originalNumberOfCandidates; i++) {
            IRCandidate candidate = (IRCandidate) candidates.get(i);
            if(candidate.getRunningStatus()){
                double percentage = (double) candidate.getBallotCount() / (double) numberOfBallots * 100;
                audit.log(candidates.get(i).getName() + " now has " + candidate.getBallotCount() + " ballots, which is " + percentage + "%.\n");
            }
            
        }
    }

    /**
     * <p>This sends the audit file information logs containing the winner and the number of ballots / percentage of ballots they won with</p>
     * @param can the candidate object that represents the winner of the election
     */
    public void auditEndState(Candidate can) {
        audit.log("ELECTION HAS ENDED");
        audit.log("-------------------------\n\n");
        double percentage = (double) can.getBallotCount() / (double) numberOfBallots * 100;
        audit.log(can.getName() + " has won with " + can.getBallotCount() + " votes, which is " + percentage + "%.\n");
    }

    /**
     * <p>Used by runElection to periodically check if any of the candidates have ballot totals over 50% of the total ballots, or if there
     * are less than 3 candidates left.</p>
     * @return true if a candidate has majority or if there are less than 3 candidates left in running. False if no candidates have reached majority and there are 3 or more candidates left
     */
    public boolean checkMajority() {
        for (int i = 0; i <  candidates.size(); i++) {
           double percentage = ((double) (candidates.get(i).getBallotCount())) / (double) numberOfBallots;
           if (percentage > 0.5){
                return true;
           }
        }
        return false; // Will fall through if none of the candidates have majority and there are more than 2 candidates remaining
    }

    /**
     * <p>Iterates through the list of candidates that are still in the running and returns the candidate object with the fewest votes.
     * Also breaks ties if there are multiple candidates tied for last place. </p>
     * @return candidate object representing the candidate with the fewest ballots
     */
    public Candidate getLowest(){
        int lowestValue = Integer.MAX_VALUE;
        ArrayList<Candidate> lowestCandidates = new ArrayList<>();
        for (int i = 0; i < candidates.size(); i++) {
            IRCandidate current = (IRCandidate) candidates.get(i);
            if ((candidates.get(i).getBallotCount()) < lowestValue && current.getRunningStatus()) { // new lowest value is found
                lowestValue = candidates.get(i).getBallotCount();
                lowestCandidates.clear(); // If there were any ties but there is another candidate that is lower, all candidates are cleared
                lowestCandidates.add(candidates.get(i));
            }
            else if ((candidates.get(i).getBallotCount()) == lowestValue){ //tie for lowest
                lowestCandidates.add(candidates.get(i));
            }
        }
        numberOfCandidates--;
        if ((lowestCandidates.size()) != 1) {
            int rand = breakTie(lowestCandidates.size()); //randomly choose a number in range of how many candidates tied
            return lowestCandidates.get(rand);
        }
        else{
            return lowestCandidates.get(0);
        }
        
    }
    /**
     * <p>Main driver method for the election algorithm, it is responsible for keeping a loop of removing the lowest candidate until a candidate 
     * reaches majority or until there are fewer than 3 candidates remaining, and then finding the winner of the election.
     * It is also responsible for calls to the audit methods and printing basic output to the terminal.</p>
     * @return true when complete
     */
    @Override
    public boolean runElection() {
        IRCandidate lowestCandidate;

        //send audit log initial state of election
        auditIntialState();

        while((!checkMajority()) && (numberOfCandidates > 2)){
            lowestCandidate = (IRCandidate) getLowest();
            lowestCandidate.removeCandidate();
            auditCandidateFallingOut(lowestCandidate);
            auditNewBallotTotals();
        }
        int highestValue = Integer.MIN_VALUE;
        ArrayList<Candidate> highestCandidates = new ArrayList<>();
        for (int i = 0; i < candidates.size(); i++) {
            if ((candidates.get(i).getBallotCount()) > highestValue) { //follows same logic as getLowest()
                highestValue = candidates.get(i).getBallotCount();
                highestCandidates.clear();
                highestCandidates.add(candidates.get(i));
            }
            else if ((candidates.get(i).getBallotCount()) == highestValue) {
                highestCandidates.add(candidates.get(i));
            }
        }
        if ((highestCandidates.size()) != 1) { //tie
            int rand = breakTie(highestCandidates.size());
            winner = (IRCandidate) highestCandidates.get(rand);
        } 
        else {
            winner = (IRCandidate) highestCandidates.get(0);
        }
        auditEndState(winner);
        printResults();

        return true;
    }

    /**
     * <p>This method is responsible for breaking ties between candidates with the same number of ballots. It will generate a random number from 0 to 
     * numOfCandidates - 1. This random number will be used to decide between the tied candidates.</p>
     * @param numOfCandidates An integer that represnts the number of candidates that are currently tied.
     * @return An integer that represents the candidate that has won the tie breaker.
     */
    public int breakTie(int numOfCandidates) {
        // Pick a random number within the range 0 to numOfCandidates
        Random rand = new Random();
        return rand.nextInt(numOfCandidates);
    }

    /**
     * <p>This method is used by runElection to print end of election results to the terminal for the user to have quick results. It will print
     * the name of the candidate that won the election as well as the number of ballots and ballot percentage that candidate has.
     */
    @Override
    public void printResults() {
        float result = (float) winner.getBallotCount() / numberOfBallots;
        System.out.println(winner.getName() + " won with " + winner.getBallotCount() + " votes (" + result + "%)");
    }
}
