/**
 * 
 */
package assignment1;

/** Chef.java
*
* Chef is the class for the consumer thread.
* 
* @author justine yap
* 
* note that most code came from sample code provided for the class SYSC3303 C Winter 2023
*/
public class Chef implements Runnable {

	//buffer
	private TableQueue table;
	
	//chef's infinite supply of one ingredient
	private String ingredient; 

	//constructor
	public Chef(TableQueue t, String item)
	{
		table = t;
		ingredient = item;
	}

	//run function
	public void run()
	{
		//while sandwiches are less than 20
		while (table.getSize()<20) {
			//get table ingredient
			table.get(ingredient);
			System.out.println(Thread.currentThread().getName() + " with ingredient "+ ingredient + " made and ate sandwich " + table.getSize() + "\n");
			
			//error handling
			try {
	            Thread.sleep(5000);
	        } catch (InterruptedException e) {
				System.err.println(e);

	        }
		}
		//finish thread
		System.out.println(Thread.currentThread().getName() + " thread finished.");

	}

}
