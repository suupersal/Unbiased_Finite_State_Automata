/*
 * CS 311
 * Project #1
 * Salvador Gutierrez
 */
import java.io.IOException;

/**
 *
 * @author Salvador
 */
public class proj1 {

    /**
     * @param args the command line arguments
     */
    
   
    public static void main(String[] args) throws IOException { 
       UFSA mainUFSA = new UFSA();
       //keep runing until there are no more programas to complete
       int counter=1;
       while(!mainUFSA.fileComplete){
       
       mainUFSA.initialize();
           System.out.println("\n Final State Automaton #"+counter);
       mainUFSA.run();
       counter++;
       }
    }
}
