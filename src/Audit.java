import java.io.*;
import java.util.*; 
/**
* Object giving streamlined abilities to write to an audit file for the election. 
* @author Logan Watters
*/

public class Audit {
    private File auditFile = null; 
    private OutputStream stream = null; 
    /**
    * <p> Initializes the attributes of the Audit object when a new Audit object is created, creating a new file for the audit. </p>
    */
    public Audit() {
        try {
            auditFile = new File("auditFile.txt");
            auditFile.createNewFile();
        } catch (IOException execpt) {
            System.out.println("ERROR: Unable to create file.");
        }
        try {
            stream = new FileOutputStream(auditFile, true);
        } catch (IOException execpt) {
            System.out.println("ERROR: Unable to open file for writing correctly.");
        }
    }
    /**
    * <p> Writes the string provided to the audit file created in the constructor. </p>
    * @param   info  String object representing the information to be written to the file.
    */
    public void log(String info) {
        try {
            stream.write(info.getBytes());
        } catch (IOException execp) {
            System.out.println("ERROR: Unable to write information passed to audit file.");
        }
    }
}
