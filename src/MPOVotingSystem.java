import java.util.ArrayList;
import java.util.Random;
import java.io.*;

/**
 * MPOVotingSystem is a VotingSystem object for MPO election types, they are reponsible for running the election
 * algorithm, reporting to the audit file and printing final results to the terminal.
 * 
 * @author Perrie Gryniewicz, Logan Watters, Matthew Johnson
 */
public class MPOVotingSystem extends VotingSystem{
    private int seatsAvailable;
    private int numSeats;
    private int numSeatsAwarded = 1;
    private ArrayList<MPOCandidate> winners;
    private ArrayList<Candidate> allCandidates;

    /**
     * <p> Constructor for MPOVotingSystem class </p>
     * @param c ArrayList of candidates in the running in this election
     * @param numCandidates number of candidates in the running
     * @param numBallots number of ballots cast
     * @param fp file pointer to the .csv file containing ballot and election information
     * @param numSeats number of seats available to be won
     */
    public MPOVotingSystem(ArrayList<Candidate> c, int numCandidates, int numBallots, File fp, int numSeats) {
        super(c, numCandidates, numBallots, fp);
        this.seatsAvailable = numSeats;
        this.numSeats = numSeats;
        winners = new ArrayList<MPOCandidate>();
        allCandidates = new ArrayList<>(c);
    }

    /**
     * <p>Main driver method for the election algorithm, it is responsible for handing out all seats to candidates in order of 
     * poplarity. It is also responsible for calls to the audit methods and printing basic output to the terminal.</p>
     * @return true when complete
     */
    @Override
    public boolean runElection() {
        winners.clear();
        int maxVotes = 0;
        MPOCandidate tempWinner = new MPOCandidate("", "", 0);
        int seatsAllocated = 0; 
        ArrayList<MPOCandidate> highestCandidates = new ArrayList<MPOCandidate>();
        ArrayList<Candidate> candidatesNew =  new ArrayList<Candidate>();
        ArrayList<Candidate> candidateCopy = super.getCandidates();
        for(int i = 0; i<super.getNumberOfCandidates(); i++) {
            candidatesNew.add(candidateCopy.get(i));
        }

        while(seatsAllocated != seatsAvailable) {
            highestCandidates.clear();
            maxVotes = 0; 
            for (int i = 0; i<super.getNumberOfCandidates()-seatsAllocated; i++) {
                MPOCandidate curr = (MPOCandidate) candidatesNew.get(i);
                if (curr.getBallotCount() > maxVotes) {
                    maxVotes = curr.getBallotCount();
                    tempWinner = curr;
                    highestCandidates.clear();
                    highestCandidates.add(curr);
                }
                else if (curr.getBallotCount() == maxVotes) {
                    highestCandidates.add((curr));
                }
            }
            if(highestCandidates.size() != 0) {
                int rand = breakTie(highestCandidates.size());
                winners.add(highestCandidates.get(rand));
                seatsAllocated++;
                auditSeatAwarded(highestCandidates.get(rand));
                candidatesNew.remove(highestCandidates.get(rand));
                highestCandidates.clear();
            }
            else{
                winners.add(tempWinner);
                candidatesNew.remove(tempWinner);
                seatsAllocated++;
                highestCandidates.clear();
            }
        }
        audit.log("ELECTION RESULTS: \n");
        System.out.println("\nElection results:");
        System.out.println("---------------------------");
        printResults();
        return true; 
    }

    /**
     * <p>Helper function for runElection(), called when a seat is awarded, sends this info to audit object to be printed in audit file.</p>
     * @param can the candidate object that is awarded the seat.
     */
    public void auditSeatAwarded(Candidate can){
        audit.log("SEAT FILLED\n");
        audit.log("-------------------------\n");
        audit.log(can.getName() + " had the most votes with " + can.getBallotCount() + " and has been awarded seat " + numSeatsAwarded + ".\n\n");
    }

    /**
     * <p>This method is used by runElection to print end of election results to the terminal for the user to have quick results. It will print
     * the name of the candidate(s) that won the seat or seats, as well as the number of ballots and ballot percentage that candidate had.</p>
     */
    @Override
    public void printResults(){
        if (numSeats == 1){
            audit.log(winners.get(0).getName() + " has won the election with " + winners.get(0).getBallotCount() + " votes!\n");
            System.out.println(winners.get(0).getName() + " has won the election with " + winners.get(0).getBallotCount() + " votes!");
        }
        else if (numSeats == 2){
            audit.log(winners.get(0).getName() + " and " + winners.get(1).getName() + " each won seats with " + winners.get(0).getBallotCount() + " and " + winners.get(1).getBallotCount() + " votes respectively.\n");
            System.out.println(winners.get(0).getName() + " and " + winners.get(1).getName() + " each won seats with " + winners.get(0).getBallotCount() + " and " + winners.get(1).getBallotCount() + " votes respectively.");
        }
        else{
            for(int i = 0; i < numSeats; i++) {
                if(i == (numSeats -1)){
                    audit.log("and " + winners.get(i).getName() + " won seats");
                    System.out.println("and " + winners.get(i).getName() + " won seats");
                } else {
                    audit.log(winners.get(i).getName() + ", \n");
                    System.out.print(winners.get(i).getName() + ", ");
                }
            }
        }
        System.out.println("\nVote Breakdown:");
        System.out.println("---------------------------");
        for(int i = 0; i < allCandidates.size(); i++){
            double percentage = (((double) allCandidates.get(i).getBallotCount()) / (double) numberOfBallots) * 100;
            String stringPercent = String.format("%.1f", percentage);
            System.out.println(allCandidates.get(i).getName() + " had " + allCandidates.get(i).getBallotCount() + " votes, which is " + stringPercent + "% of the total votes.");
        }
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

    public String[] getWinners() {
        String[] winnersString = new String[winners.size()];
        for(int i = 0; i<winners.size(); i++) {
            winnersString[i] = winners.get(i).getName();
        }
        return winnersString;
    }
}