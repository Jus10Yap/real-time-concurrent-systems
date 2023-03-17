Justine Yap 
std #101180098
assignment 4

Questions
1. There is a defect in the design which will annoy law-abiding pedestrians
    Problem: in the hierarchical diagram, we are setting the pedestrians flash counter to 7 inside the pedestriansFlash state
    which is wrong because when we timeout in pedestriansFlash it will subtract by one(becomes 6 so signalPedestrians(BLANK)) 
    and re-enter the pedestriansFlash state and the pedestrians flash counter will reset to 7 again so the pedestrians flash 
    will forever be flashing

    Solution: set the pedestrian flash counter inside the pedestriansWalk state before transitioning to the pedestriansFlash state

2.  There is second error in the design.
    Problem: Setting isPedestrianWaiting to false and calling pedestrianWaiting() and setting it to true in the VehiclesGreen state. 
    If a pedestrian were to press the button while the lights are flashing, isPedestrianWaiting would get set to false after entering 
    the VehiclesGreen state when there is actually a pedestrian waiting. 

    Solution: isPedestrianWaiting should be set to false before transitioning from PedestriansWalk to PedestriansFlash. Additionally, 
    calling pedestrianWaiting() inside the VehiclesGreen function would always transition us into VehiclesYellow, making the cars 
    stop when there could be no pedestrian waiting. Instead, this function should be removed from VehiclesGreen and should be called from 
    a class using Context, in this case the TestHarness.

Important Files
    README.txt : Explaining the names of all important files, set up instructions, etc
    assignment4 -> src : Source folder that contains all java files and source code
        Context.java :  Implements the Runnable interface and is responsible for maintaining the current state of the traffic light system.
        TestCode.java : Test code that invokes the state machine

Classes/States
    Context: has a State object to represent the current state
    State: abstract class that defines the interface for each state in the traffic light system
    Operational: extends the State class and represents the initial state of the traffic light system
    VehiclesEnabled: extends the Operational class and represents the state when the traffic lights are enabled for vehicles
    VehiclesGreen: extends the VehiclesEnabled class and represents the state when the traffic lights are green for vehicles
    VehiclesGreenInt: extends the VehiclesEnabled class and represents the state when the traffic lights are green for vehicles and no pedestrian is waiting
    VehiclesYellow: extends the VehiclesEnabled class and represents the state when the traffic lights are yellow for vehicles and pedestrian is waiting
    pedestriansEnabled: the state where the pedestrian traffic light is enabled, and pedestrians can cross the road.
    pedestriansWalk: the state where the pedestrian traffic light is on, and pedestrians can cross the road.
    pedestriansFlash: state where the pedestrian traffic light is flashing, and pedestrians should finish crossing the road.

Setup
    1. open 'assignment4' folder on eclipse
    2. open 'TestCode.java' file located in 'src' folder
    3. run the file on eclipse as java application
        - or press shift + cmd + f11 if you're on mac
    4. all test outputs will be printed on the console