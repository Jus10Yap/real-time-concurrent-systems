package assignment4;

public class Context {
    
	 private State state;
	    private boolean isPedestrianWaiting;
	    private int pedestrianFlashCtr;
	    
	    // Constructor
	    public Context() {
	        // Set initial state
	    	state = new operational();
	    	
	        isPedestrianWaiting = false;
	        pedestrianFlashCtr = 0;
	    }
	    
	    // Setter method for current state
	    public void setCurrentState(State state) {
	    	this.state = state;
	    }
	    
	    // Method to start the TIMEOUT event
	    public void timeout() {
	        System.out.println("[Context] Timeout occurred in " + state.getClass().getSimpleName());
	        // Trigger the TIMEOUT event in the current state
	        state.onTimeout(this);
	    }
	    
	    // Method to start the PEDESTRIAN_WAITING event
	    public void pedestrianWaiting() {
	        System.out.println("[Context] Pedestrian waiting in " + state.getClass().getSimpleName());
	        // Trigger the PEDESTRIAN_WAITING event in the current state
	        state.onPedestrianWaiting(this);
	    }
	    
	    // Method to update the pedestrian flashing counter
	    public void updatePedestrianFlashCtr() {
	        pedestrianFlashCtr++;
	    }
	    
	    // Getter method for the pedestrian flashing counter
	    public int getPedestrianFlashCtr() {
	        return pedestrianFlashCtr;
	    }

		
}

