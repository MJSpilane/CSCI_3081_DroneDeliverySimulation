import java.io.File;
import java.io.FileNotFoundException;

/**
 * Election objects are created by main and are responsible for creating a header processor and a voting system based on the results from the header processor
 * 
 * @author Matthew Johnson
 */
public class Election{
    /**
     * File pointer to the .csv ballot file
     */
    private File fp;
    /**
     * HeaderProcessor used to call parseHeader on the .csv ballot file
     */
    private HeaderProcessor headerProcessor;
    /**
     * The VotingSystem object created to run the specified election type
     */
    private VotingSystem votingSystem;

    /**
     * <p>Constructor for election objects, takes in a file pointer from main and creates a headerProcessor object. It will then create a 
     * votingSystem object by calling parseHeader() on the headerProcessor object. Then it will call runElection on the votingSystem object.
     * @param fp file pointer pointed to the .csv ballot information file. This file should be tested valid by main before passed in
     * @throws FileNotFoundException if, incase the testing of validity in main lets a bad file descriptor through, this constructor will throw a FileNotFoundException
     */
    public Election(File fp) throws FileNotFoundException {
        this.fp = fp;
        this.headerProcessor = new HeaderProcessor(fp);
        this.votingSystem = headerProcessor.parseHeader();
        votingSystem.runElection();
    }

    // getters for testing
    /**
     * <p>Getter for the file pointer field</p>
     * @return the file pointer held in fp
     */
    public File getFp(){
        return fp;
    }

    /**
     * <p>Getter for the headerProcessor field</p>
     * @return the headerProcessor object held in headerProcessor
     */
    public HeaderProcessor getHeaderProcessor() {
        return headerProcessor;
    }

    /**
     * <p>Getter for the votingSystem field</p>
     * @return the votingSystem object held in votingSystem
     */
    public VotingSystem getVotingSystem() {
        return votingSystem;
    }
}