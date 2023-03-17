package assignment4;

public class operational extends State {

	private static final int GREEN_TO_YELLOW_TIMEOUT = 10000; // 10 seconds
	private static final int YELLOW_TO_RED_TIMEOUT = 3000; // 3 seconds
	private static final int WALK_TIMEOUT = 15000; // 15 seconds
	private static final int FLASH_TIMEOUT = 2000; // 2 seconds
	
	public void onEntry(Context context) {
		context = new vehiclesEnabled();
		context.onEntry();
    }

	// Implementation of the onTimeout() method
	public void onTimeout(Context context) {
		System.out.println("Transitioning from operational to vehiclesGreen");
		// Transition to the next state
		context.setCurrentState(new vehiclesGreen());
	}

	// Implementation of the onPedestrianWaiting() method
	public void onPedestrianWaiting(Context context) {
        // Do nothing

}
