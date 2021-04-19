Robot Android Programming Challenge

In order to be considered for the Robot Android position, you must complete the following steps.
Note: This task should take no longer than 2-3 hours at the most. However, you do not have a timeline 
and it is up to you to complete the challenge as soon as possible.

Tasks
1. Get to know the Pepper QiSDK (https://developer.softbankrobotics.com/pepper-qisdk/gettingstarted);
2. Give Pepper the ability to chat with a user by implementing a custom robot chatbot. In this regard, 
make a Topic file to:
o use a "welcoming" bookmark within a proposal for saying a message when the robot focus 
is gained;
o define a rule on the input "Hello Pepper".
The application should have a UI consisting of two buttons (button A and B) and one EditText. 
3. Give Pepper the ability to start a bow animation by pressing button A:
o extend the Topic file with a "goodbye" bookmark that says "It was a pleasure!";
o call the "goodbye" bookmark as soon as the animation starts;
o log a message in Logcat when the animation stops.
4. Give Pepper the ability to contact the world by pressing button B. It will connect to an external 
API, such as http://api.icndb.com/jokes/random:
o create an Android library;
o send a GET request to download a joke;o show the joke into the EditText.
5. Export to Zip file and send out to us.
Extra
1. Be sure to comment your code;
2. Generate a javadoc with dokka.
