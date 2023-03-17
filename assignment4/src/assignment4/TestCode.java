package assignment4;

public class TestCode {

	public static void main(String[] args) {
		
		// create a new context
		Context context = new Context();
		
		
		 // Send pedestrian waiting event
        System.out.println("\n[Test] Sending PEDESTRIAN_WAITING event");
        context.pedestrianWaiting();

        // Send pedestrian waiting event again before previous event is handled
        System.out.println("\n[Test] Sending another PEDESTRIAN_WAITING event before previous event is handled");
        context.pedestrianWaiting();

        // Wait for some time to allow the state machine to handle the events
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Send pedestrian waiting event after vehicles have green light
        System.out.println("\n[Test] Sending PEDESTRIAN_WAITING event after vehicles have green light");
        context.pedestrianWaiting();

        // Wait for some time to allow the state machine to handle the events
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Send pedestrian waiting event after vehicles have yellow light
        System.out.println("\n[Test] Sending PEDESTRIAN_WAITING event after vehicles have yellow light");
        context.pedestrianWaiting();
        
     // Wait for some time to allow the state machine to handle the events
        try {
            Thread.sleep(15000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Send pedestrian waiting event before pedestrian lights are flashing
        System.out.println("\n[Test] Sending PEDESTRIAN_WAITING event before pedestrian lights are flashing");
        context.pedestrianWaiting();
        

    }

}