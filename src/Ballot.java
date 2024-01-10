/**
 * Object representing a ballot with a unique ID for audit purposes
 */

public class Ballot {
    private static int id = 0;

    /**
     * Constructor
     */
    public Ballot() {
        id += 1;
    }
}
