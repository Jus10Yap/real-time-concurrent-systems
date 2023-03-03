/**
 * 
 */
package assignment1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/** ChefAgent.java
*
* This is a Java implementation of the classic producer/consumer/bounded buffer 
* program
* 
* @author justine yap
* 
* note that most code came from sample code provided for the class SYSC3303 C Winter 2023
*/
public class ChefAgent {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//agent thread and the three chef threads
        // Declare and initialize thread variables
		Thread agent, chefOne, chefTwo, chefThree;
		
		//buffer
	    TableQueue table = new TableQueue();
	    
        // Add the 3 ingredients to a list
	    List<String> ingredients = Arrays.asList("bread", "jam", "peanut butter");
	    
        // Create new threads and pass them a reference to the shared TableQueue object and the ingredients list
	    agent = new Thread(new Agent(table, ingredients),"Agent");
	    chefOne = new Thread(new Chef(table, ingredients.get(0)),"Chef One");
	    chefTwo = new Thread(new Chef(table, ingredients.get(1)),"Chef Two");
	    chefThree = new Thread(new Chef(table, ingredients.get(2)),"Chef Three");
	    
        // Start all threads
	    agent.start();
	    chefOne.start();
	    chefTwo.start();
	    chefThree.start();
	

	}

}
