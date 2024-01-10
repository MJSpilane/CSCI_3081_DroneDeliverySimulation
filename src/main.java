import java.awt.Desktop;
import java.io.*;
import java.util.Scanner;

/**
 * Main is the starting point for the votingSystem program.
 * 
 * @author Matthew Johnson
 */
public class main{
    /**
     * <p>main will prompt the user to input the filename of the .csv ballot file. It will then test the filename. If the filename is not valid
     * it will prompt the user to try again, if the filename is valid, it will create a file pointer pointing to that file and pass that file pointer
     * to an election object that it creates.</p>
     */
    public static void main(String args[]) throws FileNotFoundException {

        Scanner s = new Scanner(System.in);
        System.out.println("Please Enter Filename: ");
        String fileName = s.nextLine();

        File fp;
        //test for valid filename
        while(true) {
            try {
                fp = new File(fileName);
                break; //break if the name is valid and calling File() doesn't throw an exception
            
            } catch (Exception e) {
                System.out.println("Invalid File Name, please retry.");
                fileName = s.nextLine();
            }
        }
        s.close();
        Election election = new Election(fp); //create election object with file pointer passed in
    }
}