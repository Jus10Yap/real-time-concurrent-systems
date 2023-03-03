package assignment1;

import java.util.ArrayList;
import java.util.List;

/** Agent.java
*
* Agent is the class for the producer thread.
* 
* @author justine yap
* 
* note that most code came from sample code provided for the class SYSC3303 C Winter 2023
*/


		
public class Agent implements Runnable {

    // buffer
    private TableQueue table;

    // list of infinite supply of all 3 ingredients
    private List<String> ingredients = new ArrayList<String>();

    // constructor
    public Agent(TableQueue t, List<String> items) {
        table = t;
        ingredients = items;
    }

    // run function
    public void run() {
        for (int i = 0; i < 20; i++) {
            // choosing 1 ingredient out of the 3
            int random = (int) (Math.random() * ingredients.size());

            //put the 2 remaining ingredients that was not chosen by randomizer on the table
            List<String> available = new ArrayList<String>();
            available.add(ingredients.get((random + 1) % 3));
            available.add(ingredients.get((random + 2) % 3));

            System.out.println(" ");
            System.out.println(Thread.currentThread().getName() + " produced " + available.get(0) + " and " + available.get(1));
            System.out.println("sandwich needs: " + ingredients.get(random) + "\n");
            
            // puts out another two of the three ingredients
            table.put(available);

            // error handling
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                System.err.println(e);
            }
        }
        // end of thread
        System.out.println(Thread.currentThread().getName() + " thread finished.");
    }
}
