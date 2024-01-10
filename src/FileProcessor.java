import java.io.*;
import java.math.RoundingMode;
import java.lang.reflect.Array;
import java.util.*;

/**
* FileProcessor is the object that processes the ballots in a file, creates the ballots, and assigns the ballots to each candidate.
* @author Logan Watters
*/

public class FileProcessor {
    private ArrayList<Ballot> ballots; 
    private File electionFile; 
    private Scanner reader; 
    private ArrayList<Candidate> candidates;
    
    public ArrayList<Ballot> getBallots() {
        return ballots; 
    }

    /**
    * <p> Initializes the attributes of the FileProcessor object when a new FileProcessor object is created. </p>
    * @param  fp         a File object for the election csv file. 
    * @param  candidates an ArrayList of the candidates for the current election from the HeaderProcessor object.
    */
    public FileProcessor(File fp, ArrayList<Candidate> candidates) { // should change fileName to be a file pointer, as we already have one
        electionFile = fp;
        try {
            reader = new Scanner(electionFile);
        }
        catch (IOException e) {
            System.out.println("Error - no file");
        }
        ballots = new ArrayList<Ballot>();
        this.candidates = candidates; // since all we need from voting system is the candidates, we should just pass those in
        processFile();
    }

    /**
    * <p> Reads through each line of the election file that is not the header and extracts the information for each
    * ballot. Creates the new ballot depending on the type of election and continues until the end of the file. </p>
    */
    public void processFile() {
        String currentData = "";
        try {
            currentData = reader.nextLine();
        }
        catch (NoSuchElementException e) {
            System.out.println("Error - file is empty");
            return; 
        }
        int numberOfCandidates = candidates.size(); 
        if(currentData.equals("OPL")) {
            for(int i = 0; i<4; i++) {
                currentData = reader.nextLine(); 
            }
            int candidateIndex; 
            while(reader.hasNextLine()) {
                candidateIndex = 0; 
                currentData = reader.nextLine();
                int currentDataLength = currentData.length();
                for(int j = 0; j<currentDataLength; j++) {
                    if(currentData.charAt(j) == ',') {
                        candidateIndex++; 
                    } else {
                        break;
                    }
                }
                OPLCandidate candidate = (OPLCandidate) candidates.get(candidateIndex);
                candidate.addVote();
            }
        }
        else if (currentData.equals("MPO")) {
            for(int i = 0; i < 4; i++){
                currentData = reader.nextLine();
            }
            int candidateIndex;
            while(reader.hasNextLine()){
                candidateIndex = 0;
                currentData = reader.nextLine();
                int currentDataLength = currentData.length();
                for(int j = 0; j <currentDataLength; j++) {
                    if(currentData.charAt(j) == ',') {
                        candidateIndex++;
                    } else {
                        break;
                    }
                }
                MPOCandidate candidate = (MPOCandidate) candidates.get(candidateIndex);
                candidate.addVote();
            }
        }
        else {
            File invalidIRFile = new File("invalidated.csv");
            OutputStream stream = null;
            try {
                stream = new FileOutputStream(invalidIRFile, true);
            } catch (IOException execpt) {
                System.out.println("ERROR: Unable to open file for writing correctly.");
            }
            double preprocessedThreshold = 0.5 * (double) numberOfCandidates;
            long threshold = java.lang.Math.round(preprocessedThreshold);
            for(int i = 0; i<3; i++) {
                currentData = reader.nextLine(); 
            }
            int candidateIndex; 
            int rank; 
            while(reader.hasNextLine()) {
                int candidatesRanked = 0; 
                ArrayList<Candidate> currentRankings = new ArrayList<Candidate>();
                for(int i = 0; i<numberOfCandidates; i++){
                    currentRankings.add(null);
                }
                candidateIndex = 0; 
                currentData = reader.nextLine();
                int currentDataLength = currentData.length(); 
                for(int j = 0; j<currentDataLength; j++) {
                    if(currentData.charAt(j) == ',') {
                        candidateIndex++;
                        continue; 
                    } else {
                        candidatesRanked++;
                        rank = Integer.parseInt(String.valueOf(currentData.charAt(j)));
                        currentRankings.set(rank-1, candidates.get(candidateIndex));
                    }
                }
                
                if(candidatesRanked < threshold) {
                    try {
                        stream.write(currentData.getBytes());
                        String newLine = "\n";
                        stream.write(newLine.getBytes());
                    } catch (IOException execp) {
                        System.out.println("ERROR: Unable to write information passed to audit file.");
                    }
                }
                else {
                    IRBallot newBallot = new IRBallot(currentRankings);
                    ballots.add((Ballot) newBallot);
                }
                
            }
        }
    }
}