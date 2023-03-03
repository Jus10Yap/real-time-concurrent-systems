/**
 * 
 */
package assignment1;

import java.util.ArrayList;
import java.util.List;

/** TableQueue.java
*
* The basic TableQueue class to show mutual exclusion and 
* condition synchronization.
* 
* @author justine yap
* 
* note that most code came from sample code provided for the class SYSC3303 C Winter 2023
*/
public class TableQueue {

	// a simple ring buffer is used to hold the data

		// buffer capacity
		private static final int SIZE = 2;
		
		//ingredients on the table
		private List<String> tableIngredients = new ArrayList<String>();


		private int size = 0; // number of sandwiches
		
	    private boolean empty = true; // empty?
	    
	    //getter
	    public int getSize() {
	    	return size;
	    }
	    
	    //setter
	    public void setSize(int n) {
	    	size = n;
	    }
	    
	    /**
	     * Puts ingredients on the table. 
	     * 
	     * @param ingredients The ingredients to be put on the table.
	     */
	    public synchronized void put(List<String> items) {
	        while (!empty) {
	            try {
	                wait();
	            } catch (InterruptedException e) {
					System.err.println(e);

	                return;
	            }
	        }
	        //table no longer empty
	        empty = false;
	        System.out.println(Thread.currentThread().getName() + " put ingredients " + items.get(0) + " and " + items.get(1) + " on the table\n");
	        tableIngredients = items;
			
			notifyAll();
	    }
	    
	    /**
	     * Gets an ingredients from the table.  This method returns once the
	     * ingredients have been removed from the table.
	     * 
	     * @return The ingredients taken from the table.
	     */
	    public synchronized List<String> get(String item) {
	    	
	    	//ingredients holder
	    	List<String> items = new ArrayList<String>();
			
	    	//while sandwiches made are less than 20 and table is empty OR table ingredients has specified ingredient
			while(size < 20 && empty || tableIngredients.contains(item)){
				try{
					wait();
				} catch (InterruptedException e) {
					System.err.println(e);
					return null;
				}
			}
			
			//too many sandwiches were made
			if(size == 20) {
				return null;
			} else {
				//increase sandwiches made
				size++;
				items = tableIngredients;
				
				// removing everything on the table
				items.clear();
				empty = true;
				
				notifyAll();
				return items;
			}
	    }

}
