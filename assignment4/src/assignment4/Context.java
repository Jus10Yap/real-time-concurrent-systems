package assignment4;

/*
 *  @author Justine Yap
 *  
 *  Simple implementation of a Pelican crossing system
 */

/*
 * Context class
 * 
 * represents the current state of the system
 */
public class Context implements Runnable {
	private State state; // current state
	private boolean isPedestrianWaiting; // flag to check if pedestrian is waiting
	private int pedestrianFlashCtr; // counter for pedestrian flashing

	// Constructor
	public Context() {
		// Set initial state to operational state
		state = new operational();
		isPedestrianWaiting = false;
		pedestrianFlashCtr = 0;

		// Start the context thread
		Thread c = new Thread(this);
		c.start();
	}

	// Method to start the TIMEOUT event "pull"
	public void timeout() {
		System.out.println("\n\n[Context] Current State: " + state.getClass().getSimpleName());
		// Trigger the TIMEOUT event in the current state
		state.onTimeout(this);
	}

	// Method to start the PEDESTRIAN_WAITING event
	public void pedestrianWaiting() {
		System.out.println("\n[Context] Pedestrian waiting in: " + state.getClass().getSimpleName());
		// Trigger the PEDESTRIAN_WAITING event in the current state
		state.onPedestrianWaiting(this);
	}

	// Method to set the current state
	public void setCurrentState(State state) {
		this.state = state;
	}

	// Method to check if a pedestrian is waiting
	public boolean isPedestrianWaiting() {
		return isPedestrianWaiting;
	}

	// Method to set if a pedestrian is waiting
	public void setPedestrianWaiting(boolean isPedestrianWaiting) {
		this.isPedestrianWaiting = isPedestrianWaiting;
	}

	// Method to get the pedestrian flash counter
	public int getPedestrianFlashCtr() {
		return pedestrianFlashCtr;
	}

	// Method to set the pedestrian flash counter
	public void setPedestrianFlashCtr(int pedestrianFlashCtr) {
		this.pedestrianFlashCtr = pedestrianFlashCtr;
	}

	// Run method for the Context thread
	@Override
	public void run() {
		while (true) {
			try {
				timeout();
			} catch (Exception e) {
				System.out.println("NOT RUNNING");
				e.printStackTrace();
				break;
			}

		}

	}

}

/*
 * 
 * State
 * 
 * Abstract class that represents a state in the system
 */
abstract class State {

// Method to signal pedestrians
	public void signalPedestrians(String signal) {
		System.out.println("[Pedestrian] " + signal);
	}

// Method to signal vehicles
	public void signalVehicles(String signal) {
		System.out.println("[Traffic Light] " + signal);
	}

// Abstract method to handle the TIMEOUT event
	public abstract void onTimeout(Context context);

// Abstract method to handle the PEDESTRIAN_WAITING event
	public abstract void onPedestrianWaiting(Context context);

// Abstract method to handle the entry into the state
	public abstract void onEntry();

}

/*
 * 
 * Operational state
 * 
 * The initial state of the system
 */
class operational extends State {

	// Implementation of the onTimeout() method
	public void onTimeout(Context context) {
		System.out.println("Transitioning from [operational] to [vehiclesEnabled]");
		// Transition to the next state
		context.setCurrentState(new vehiclesEnabled());
	}

	// Implementation of the onPedestrianWaiting() method
	public void onPedestrianWaiting(Context context) {
		context.setPedestrianWaiting(true);
	}

	// Implementation of the onEntry() method
	public void onEntry() {
		// Do nothing
	}

}

/*
 * 
 * vehiclesEnabled state
 * 
 * the state when the traffic lights are enabled for vehicles
 */
class vehiclesEnabled extends operational {

	// Implementation of the onTimeout() method
	public void onTimeout(Context context) {
		onEntry();
		System.out.println("Transitioning from [vehiclesEnabled] to [vehiclesGreen]");
		// Transition to the next state
		context.setCurrentState(new vehiclesGreen());
	}

	// Implementation of the onPedestrianWaiting() method
	public void onPedestrianWaiting(Context context) {
		context.setPedestrianWaiting(true);
	}

	// Implementation of the onEntry() method
	public void onEntry() {
		//tell pedestrians not to walk
		signalPedestrians("DONT_WALK");
	}

}

/*
 * 
 * vehiclesGreen state
 * 
 * the state when the traffic lights are green for vehicles
 */
class vehiclesGreen extends vehiclesEnabled {

	// Implementation of the onTimeout() method
	public void onTimeout(Context context) {
		onEntry();
		// if there is a pedestrian waiting, transition to yellow light
		if (context.isPedestrianWaiting() == true) {

			System.out.println("Transitioning from [vehiclesGreen] to [vehiclesYellow]");
			//context.setPedestrianWaiting(false);
			// Transition to the next state
			context.setCurrentState(new vehiclesYellow());
		} else { // else transition to vehiclesGreenInt state

			System.out.println("Transitioning from [vehiclesGreen] to [vehiclesGreenInt]");
			context.setCurrentState(new vehiclesGreenInt());
		}

	}

	// Implementation of the onPedestrianWaiting() method
	public void onPedestrianWaiting(Context context) {
		context.setPedestrianWaiting(true);
	}

	// Implementation of the onEntry() method
	public void onEntry() {
		try {
			// stop light = green, pedestrians = dont walk
			signalVehicles("GREEN");
			signalPedestrians("DONT_WALK");
			// wait 10 seconds before transitioning to yellow
			Thread.sleep(10000);
		} catch (InterruptedException e) {

			e.printStackTrace();
		}
	}

}

/*
 * 
 * vehiclesGreenInt state
 * 
 * The state when the traffic lights are green for vehicles and no pedestrian is waiting
 */
class vehiclesGreenInt extends vehiclesEnabled {

	// Implementation of the onTimeout() method
	public void onTimeout(Context context) {
		//"wait" until a pedestrian is waiting
		synchronized (context) {
			while (!context.isPedestrianWaiting()) {
				try {
					context.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			System.out.println("Transitioning from [vehiclesGreenInt] to [vehiclesYellow]");
			// Transition to the next state
			context.setCurrentState(new vehiclesYellow());
		}
	}

	// Implementation of the onPedestrianWaiting() method
	public void onPedestrianWaiting(Context context) {
		context.setPedestrianWaiting(true);
	}

	// Implementation of the onEntry() method
	public void onEntry() {
		// Do nothing
	}

}

/*
 * 
 * vehiclesYellow state
 * 
 * The state when the traffic lights are yellow for vehicles and pedestrian is waiting
 */
class vehiclesYellow extends vehiclesEnabled {

	// Implementation of the onTimeout() method
	public void onTimeout(Context context) {
		onEntry();

		System.out.println("Transitioning from [vehiclesYellow] to [pedestriansEnabled]");
		// Transition to the next state
		context.setCurrentState(new pedestriansEnabled());

	}

	// Implementation of the onPedestrianWaiting() method
	public void onPedestrianWaiting(Context context) {
		context.setPedestrianWaiting(true);
	}

	// Implementation of the onEntry() method
	public void onEntry() {
		try {
			//change light to yellow and wait 3 seconds before turning red
			signalVehicles("YELLOW");
			Thread.sleep(3000);

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}

/*
 * 
 * pedestriansEnabled state
 * 
 * The initial state where the pedestrian traffic light is enabled, and pedestrians can cross the road
 */
class pedestriansEnabled extends operational {
	// Implementation of the onTimeout() method
	public void onTimeout(Context context) {
		onEntry();

		System.out.println("Transitioning from [pedestriansEnabled] to [pedestriansWalk]");
		// Transition to the next state
		context.setCurrentState(new pedestriansWalk());

	}

	// Implementation of the onPedestrianWaiting() method
	public void onPedestrianWaiting(Context context) {
		context.setPedestrianWaiting(true);
	}

	// Implementation of the onEntry() method
	public void onEntry() {
		//change light to red
		signalVehicles("RED");
	}

}

/*
 * 
 * pedestriansWalk state
 * 
 * The state where the pedestrian traffic light is on, and pedestrians can cross the road.
 */
class pedestriansWalk extends pedestriansEnabled {
	// Implementation of the onTimeout() method
	public void onTimeout(Context context) {
		onEntry();

		System.out.println("Transitioning from [pedestriansWalk] to [pedestriansFlash]");
		// resolved that the waiting pedestrian has walked
		context.setPedestrianWaiting(false);
		//initialize flash counter here
		context.setPedestrianFlashCtr(7);
		// Transition to the next state
		context.setCurrentState(new pedestriansFlash());

	}

	// Implementation of the onPedestrianWaiting() method
	public void onPedestrianWaiting(Context context) {
		context.setPedestrianWaiting(true);
	}

	// Implementation of the onEntry() method
	public void onEntry() {
		try {
			//light = red, pedestrian = walk
			signalVehicles("RED");
			signalPedestrians("WALK");
			// wait 15 seconds before begin flashing
			Thread.sleep(15000);

		} catch (InterruptedException e) {
			
			e.printStackTrace();
		}
	}
}

/*
 * 
 * pedestriansFlash state
 * 
 * The flashing state where the pedestrian traffic light is flashing, and pedestrians should finish crossing the road
 */
class pedestriansFlash extends pedestriansEnabled {
	// Implementation of the onTimeout() method
	public void onTimeout(Context context) {
		onEntry();
		//print flash counter number
		System.out.println("[FLASH COUNTER] " + context.getPedestrianFlashCtr());
		context.setPedestrianFlashCtr(context.getPedestrianFlashCtr() - 1);
		
		// if flash counter reaches 0 then transition to vehiclesEnabled state
		if (context.getPedestrianFlashCtr() == 0) {
			System.out.println("Transitioning from [pedestriansFlash] to [vehiclesEnabled]\n");
			// Transition to the next state
			context.setCurrentState(new vehiclesEnabled());
			
		} else if ((context.getPedestrianFlashCtr() & 1) == 0) { //flashing between "BLANK" and "DONT_WALK"
			signalPedestrians("DONT_WALK");
		} else {

			signalPedestrians("BLANK");
		}

	}

	// Implementation of the onPedestrianWaiting() method
	public void onPedestrianWaiting(Context context) {
		context.setPedestrianWaiting(true);
	}

	// Implementation of the onEntry() method
	public void onEntry() {

		try {
			// red light
			signalVehicles("RED");
			//wait for 1 second intervals for counter
			Thread.sleep(1);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
