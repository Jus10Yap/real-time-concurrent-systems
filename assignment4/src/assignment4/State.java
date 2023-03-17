package assignment4;

public abstract class State {
	// Abstract method to handle the entry event??
    public abstract void onEntry(Context context);

	// Abstract method to handle the TIMEOUT event
    public abstract void onTimeout(Context context);
    
    // Abstract method to handle the PEDESTRIAN_WAITING event
    public abstract void onPedestrianWaiting(Context context);

}
