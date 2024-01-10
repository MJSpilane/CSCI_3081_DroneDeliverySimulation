import java.io.*;
import java.util.*;

/**
 * Object that processes the header information of an election file
 * @author Bek Allenson, Matthew Johnson
 */
public class HeaderProcessor {
    private File fp;
    private String votingSystem;
    private int numCandidates;
    private ArrayList<Candidate> candidates;
    private int numSeats;
    private int numBallots;

    /**
     * Creates a header processor object
     * @param fp file pointer to the election file
     */
    public HeaderProcessor(File fp) {
        this.fp = fp;
    }

    /**
     * parses through header to create Voting System depending on election type
     * @return VotingSystem object which performs the election processing
     * @throws FileNotFoundException
     */
    public VotingSystem parseHeader() throws FileNotFoundException {
        Scanner scanner = new Scanner(fp);

        votingSystem = scanner.nextLine();
        
        if(votingSystem.equals("MPO")) {
            numSeats = Integer.parseInt(scanner.nextLine());
            numCandidates = Integer.parseInt(scanner.nextLine());
        }
        else{
            numCandidates = Integer.parseInt(scanner.nextLine());
        }
        candidates = new ArrayList<>();
        makeCandidateList(scanner.nextLine());
        if(votingSystem.equals("OPL")) {
            numSeats = Integer.parseInt(scanner.nextLine());
        }
        numBallots = Integer.parseInt(scanner.nextLine());

        if(votingSystem.equals("IR")) {
            IRVotingSystem temp = new IRVotingSystem(candidates, numCandidates, numBallots, fp);
            return temp;
        } else if (votingSystem.equals("OPL")) {
            OPLVotingSystem temp = new OPLVotingSystem(candidates, numCandidates, numBallots, fp, numSeats);
            return temp;
        } else if (votingSystem.equals("MPO")) {
            MPOVotingSystem temp = new MPOVotingSystem(candidates, numCandidates, numBallots, fp, numSeats);
            return temp;
        }

        return null;
    }

    /**
     * Helper function which takes in one line from the election file and creates
     * a list of Candidate objects
     * @param data line in header with Candidate data
     */
    private void makeCandidateList(String data) {
        data = data.replace(" ", "");
        data = data.substring(1, data.length()-1);
        String[] candidateStrings;
        if(votingSystem.equals("MPO")) {
            candidateStrings = data.split("\\],\\[", numCandidates);
        }
        else {
            candidateStrings = data.split(",", numCandidates);
        }
        for(int i = 0; i < numCandidates; i++) {
            Candidate c = null;
            if(votingSystem.equals("IR")) {
                String[] candidateInfo = candidateStrings[i].split("[(]", 0);
                String name = candidateInfo[0];
                String party = candidateInfo[1].substring(0, 1);
                c = new IRCandidate(name, party, i);
            } else if (votingSystem.equals("OPL")) {
                String[] candidateInfo = candidateStrings[i].split("[(]", 0);
                String name = candidateInfo[0];
                String party = candidateInfo[1].substring(0, 1);
                c = new OPLCandidate(name, party, i);
            } else if (votingSystem.equals("MPO")) {
                String[] candidateInfo = candidateStrings[i].split(",", 0);
                String name = candidateInfo[0];
                String party = candidateInfo[1];
                c = new MPOCandidate(name,party,i);
            }
            candidates.add(c);
        }
    }

    /**
     * getter for String representing voting system
     * @return String with voting system
     */
    public String getVotingSystem() {
        return votingSystem;
    }

    /**
     * getter for num candidates
     * @return int representing number of candidates in the race
     */
    public int getNumCandidates() {
        return numCandidates;
    }

    /**
     * getter for candidate list
     * @return ArrayList with all Candidates in race
     */
    public ArrayList<Candidate> getCandidates() {
        return candidates;
    }

    /**
     * @return total number of ballots cast in the election as an int
     */
    public int getNumBallots() {
        return numBallots;
    }
}