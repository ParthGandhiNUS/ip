import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.util.InputMismatchException;


public class Kowalski {

    private static final String DIVIDING_LINE = "____________________________________________________________";

    public static List <Task> currentTask = new ArrayList<>();
    public static Scanner in = new Scanner (System.in);

    /**
     * Prints out the message introducing the functionalities of Kowalski Bot
     */
    public static void printIntro(){
        System.out.println(DIVIDING_LINE);
        System.out.println("Welcome Skipper! I'm Kowalski, reporting for Duty!" + System.lineSeparator() +
                "What can I do for you today?" );
        System.out.println(DIVIDING_LINE);
    }

    /**
     * Used to check if the user has accurately input the deadline by stating the "/by"
     * @param deadlineDetails : Contains the details of the deadline task
     * @throws KowalskiException In the event that the input has no "/by"
     */
    public static void checkDeadlineInput(String deadlineDetails) throws KowalskiException{
        if (!(deadlineDetails.contains("/by"))) {
            throw new KowalskiException();
        }
    }

    /**
     * Used to check if the user has accurately input the deadline by stating the "/from" and "/to"
     * @param eventDetails : Contains the details of the event task
     * @throws KowalskiException In the event that the input has no "/from" or "/to" or both
     */
    public static void checkEventInput(String eventDetails) throws KowalskiException{
        if (!((eventDetails.contains("/from")) && (eventDetails.contains("/to")))) {
            throw new KowalskiException();
        }
    }

    /**
     * Used to process the different variations of the users inputs
     * @param userInput : String which the user inputs
     * @return String which is in lowercase and clear of any unnecessary whitespace
     */
    public static String processInput(String userInput){
        return (userInput.trim()).toLowerCase();
    }

    /**
     * Prints out an accurate message for the number of tasks in the list.
     * @param number : represents the total current task count
     */
    public static void printCurrentTaskMessage(int number){
        switch (number){
        case 0:
            System.out.println("Now you have 0 tasks in the list.");
            break;
        case 1:
            System.out.println("Now you have 1 task in the list.");
            break;
        default:
            System.out.println("Now you have " + number + " tasks in the list.");
        }
    }

    /**
     * Used in the "list" command to print all the Current Tasks in the proper format
     */
    public static void printCurrentTaskItems(){
        for (int i = 1; i <= currentTask.size(); i++){
            System.out.println(i + "." + currentTask.get(i-1));
        }
    }

    /**
     * Cleans up the user input and forms new Deadline Task
     * @param deadlineDetails : User input for details of the deadline task
     * @return new deadline task created
     */
    private static Task getNewDeadlineTask(String deadlineDetails) {
        String[] deadlineArray = deadlineDetails.split("/by");
        for (int i = 0; i < deadlineArray.length; i++) {
            deadlineArray[i] = deadlineArray[i].trim();
        }


        return new Deadline(deadlineArray[0], deadlineArray[1]);
    }


    /**
     * Cleans up the user input and forms new event Task
     * @param eventDetails : User input for details of the event task
     * @return new event task created
     */
    private static Task getNewEventTask(String eventDetails) {
        String[] eventArray = eventDetails.split("/from");
        String eventInformation = eventArray[0].trim();
        String [] fromAndTo = eventArray[1].split("/to");
        String eventFrom = fromAndTo[0].trim();
        String eventTo = fromAndTo[1].trim();

        return new Event(eventInformation, eventFrom, eventTo);
    }

    /**
     * Processes all the inputs from the user and categorises the User Commands
     * @param UserCommand: The first word input by the user
     */
    public static void parseUserCommand(String UserCommand) {

        int taskNumber;
        int lastTaskIndex;
        String remainingCommand;

        switch (UserCommand){
        case "bye":
            break;

        case "list":

            printCurrentTaskItems();
            System.out.println(DIVIDING_LINE);
            break;

        case "unmark":
            try {
                taskNumber = in.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Skipper input a god damn number! I am now gonna add the text which you inputted into our list!");
                break;
            }

            try{
                currentTask.get(taskNumber - 1).markAsNotDone();
                System.out.println( "C'mon Skipper, you're much better than that! I've marked this task as undone:");
                System.out.println("  " + currentTask.get(taskNumber - 1));
            } catch (IndexOutOfBoundsException e) {
                System.out.println("Invalid Task Number! Skipper stop acting like Private!");
            }
            System.out.println(DIVIDING_LINE);
            break;

        case "mark":
            try {
                taskNumber = in.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Skipper input a god damn number! I am now gonna add the text which you inputted into our list!");
                break;
            }

            try{
                currentTask.get(taskNumber - 1).markAsDone();
                System.out.println( "Way to go Skipper! I've marked this task as done:");
                System.out.println("  " + currentTask.get(taskNumber - 1));
            } catch (IndexOutOfBoundsException e) {
                System.out.println("Invalid Task Number! Skipper stop acting like Private!");
            }
            System.out.println(DIVIDING_LINE);
            break;

        case "todo":
            String toDoDetails = in.nextLine();

            Task newToDoTask = new Todo(toDoDetails.trim());
            currentTask.add(newToDoTask);
            lastTaskIndex = currentTask.size() - 1;

            System.out.println("Skipper you've got this work to do:");
            System.out.println("  " + currentTask.get( lastTaskIndex));
            printCurrentTaskMessage(currentTask.size());
            System.out.println(DIVIDING_LINE);
            break;

        case "deadline":
            String deadlineDetails = in.nextLine();

            try{
                checkDeadlineInput(deadlineDetails);

                //Adding the new deadline task into currentTask List after processing and cleaning inputs
                Task newDeadlineTask = getNewDeadlineTask(deadlineDetails);
                currentTask.add(newDeadlineTask);
                lastTaskIndex = currentTask.size() - 1;

                //Printing the appropriate information for the User
                System.out.println("Skipper, I have recorded this deadline:");
                System.out.println("  " + currentTask.get( lastTaskIndex));
                printCurrentTaskMessage(currentTask.size());
                System.out.println(DIVIDING_LINE);
            } catch (KowalskiException e){
                System.out.println("Skipper your inputs are wrong! Try again!");
                System.out.println(DIVIDING_LINE);
            }
            break;

        case "event":
            String eventDetails = in.nextLine();

            try{
                checkEventInput(eventDetails);

                //Adding the new event task into currentTask List after processing and cleaning inputs
                Task newEventTask = getNewEventTask(eventDetails);
                currentTask.add(newEventTask);
                lastTaskIndex = currentTask.size() - 1;

                //Printing the appropriate information for the User
                System.out.println("Skipper I've noted this event in my calendar:");
                System.out.println("  " + currentTask.get(lastTaskIndex));
                printCurrentTaskMessage(currentTask.size());
                System.out.println(DIVIDING_LINE);
            } catch (KowalskiException e) {
                System.out.println("Skipper your inputs are wrong! Try again!");
                System.out.println(DIVIDING_LINE);
            }
            break;


        default:
            remainingCommand = in.nextLine();
            Task newTask = new Task(UserCommand + remainingCommand);
            currentTask.add(newTask);
            System.out.println("added: " + UserCommand + remainingCommand);
            System.out.println(DIVIDING_LINE);
            break;
        }
    }

    /**
     * Prints out the message to end conversation with the user
     */
    public static void printEndConversation(){
        System.out.println("Bye Skipper! Hope to serve you again for your next mission!");
        System.out.println(DIVIDING_LINE);
    }

    public static void main(String[] args){
        printIntro();
        String userCommand = processInput(in.next());

        while (!(userCommand.equals("bye"))){
            parseUserCommand(processInput(userCommand));
            userCommand = in.next();
        }

        printEndConversation();
    }
}
