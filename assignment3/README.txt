Justine Yap 
std #101180098
assignment 3

Questions
1. Why did I suggest that you use more than one thread for the implementation of the Intermediate
task?
    - the Host can handle multiple client requests concurrently, which can improve the overall throughput and response time of the system.
2.  Is it necessary to use synchronized in the intermediate task? Explain.
    - Yes, it is necessary to use synchronized in the intermediate task to ensure thread safety and prevent race conditions. 
    - The intermediate task is responsible for receiving requests from the client and forwarding them to the server, as well as receiving responses from the server and forwarding them to the client. 
    - Since there are multiple threads involved in this process, there is a possibility that multiple threads may try to access and modify shared resources (such as the packet buffer or the output stream) concurrently, leading to synchronization issues.

Important Files
README.txt : explaining the names of all important files, set up instructions, etc
assignment3 -> src : source folder that contains all java files and source code
    Client.java : class for Client that sends requests/responses to the intermediate host
    Server.java : class for Server that sends requests/responses to the intermediate host
    Host.java : class for Host that handles requests and sends acknowledgements on to the server/client
    UDPEntity.java: class for UDPEntity that provides functionality for sending and receiving UDP packets.

Setup
1. open 'assignment3' folder on eclipse
2. open 'Server.java' file located in 'src' folder
3. run the file on eclipse as java application
    - or press shift + cmd + f11 if you're on mac
4. open 'Host.java' file located in 'src' folder
5. run the file on eclipse as java application
    - or press shift + cmd + f11 if you're on mac
6. open 'Client.java' file located in 'src' folder
7. run the file on eclipse as java application
    - or press shift + cmd + f11 if you're on mac
8. all thread outputs will be printed on the console