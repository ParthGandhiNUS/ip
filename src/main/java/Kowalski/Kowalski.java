package Kowalski;

import Kowalski.UI.Ui;
import Kowalski.commands.KowalskiException;
import Kowalski.tasks.Deadline;
import Kowalski.tasks.Event;
import Kowalski.tasks.Task;
import Kowalski.tasks.Todo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.util.InputMismatchException;


public class Kowalski {

    private static final String TEXT_FILE_FOLDER = "data";
    private static final String FULL_FILE_PATH = "data/Kowalski.txt";


    public static List <Task> currentTask = new ArrayList<>();
    public static Scanner in = new Scanner (System.in);



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

        switch (UserCommand){
        case "bye":
            break;

        case "list":
            printCurrentTaskItems();
            Ui.printDivider();
            break;

        case "delete":
            try {
                taskNumber = in.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Skipper input a god damn number! I am now gonna add the text which you inputted into our list!");
                break;
            }

            try {
                System.out.println( "Damn Skipper, you're got some courage removing this task:");
                System.out.println("  " + currentTask.get(taskNumber - 1));
                Ui.printCurrentTaskMessage(currentTask.size()-1);
                currentTask.remove(taskNumber-1);
                writeText();
            } catch (IndexOutOfBoundsException e){
                System.out.println("Invalid Task Number! Skipper stop acting like Private!");
            }
            Ui.printDivider();
            break;

        case "unmark":
            try {
                taskNumber = in.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Skipper input a god damn number! I am now gonna add the text which you inputted into our list!");
                Ui.printDivider();
                break;
            }

            try{
                currentTask.get(taskNumber - 1).markAsNotDone();
                System.out.println( "C'mon Skipper, you're much better than that! I've marked this task as undone:");
                System.out.println("  " + currentTask.get(taskNumber - 1));
                writeText();
            } catch (IndexOutOfBoundsException e) {
                System.out.println("Invalid Task Number! Skipper stop acting like Private!");
                Ui.printDivider();
                break;
            }
            break;

        case "mark":
            try {
                taskNumber = in.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Skipper input a god damn number! I am now gonna add the text which you inputted into our list!");
                Ui.printDivider();
                break;
            }

            try{
                currentTask.get(taskNumber - 1).markAsDone();
                System.out.println( "Way to go Skipper! I've marked this task as done:");
                System.out.println("  " + currentTask.get(taskNumber - 1));
                writeText();
            } catch (IndexOutOfBoundsException e) {
                System.out.println("Invalid Task Number! Skipper stop acting like Private!");
                Ui.printDivider();
                break;
            }
            Ui.printDivider();
            break;

        case "todo":
            String toDoDetails = in.nextLine();

            Task newToDoTask = new Todo(toDoDetails.trim());
            currentTask.add(newToDoTask);
            lastTaskIndex = currentTask.size() - 1;

            System.out.println("Skipper you've got this work to do:");
            System.out.println("  " + currentTask.get( lastTaskIndex));
            Ui.printCurrentTaskMessage(currentTask.size());
            writeText();
            Ui.printDivider();
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
                Ui.printCurrentTaskMessage(currentTask.size());
                writeText();
                Ui.printDivider();
            } catch (KowalskiException e){
                System.out.println("Skipper your inputs are wrong! Try again!");
                Ui.printDivider();
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
                Ui.printCurrentTaskMessage(currentTask.size());
                writeText();
                Ui.printDivider();
            } catch (KowalskiException e) {
                System.out.println("Skipper your inputs are wrong! Try again!");
                Ui.printDivider();
            }
            break;


        default:
            System.out.println("Skipper pull up your socks!"
                    + System.lineSeparator());
            Ui.printHelpCommands();
            Ui.printDivider();
            break;
        }
    }

    /**
     * This function takes in the lines of code from the Kowalski.txt and processes them to
     * list out all the previous tasks which we had saved.
     * @param fileInput: String containing each line of input from Kowalski.txt
     */
    public static void restoreTaskList(String fileInput){
        String [] inputArray = fileInput.split("\\s*\\|\\s*");
        switch (inputArray[0].trim()){
        case "T":
            Task newToDoTask = new Todo(inputArray[2].trim());
            if (inputArray[1].trim().equals("X")) {
                newToDoTask.markAsDone();
            } else {
                newToDoTask.markAsNotDone();
            }
            currentTask.add(newToDoTask);
            break;

        case "D":
            Task newDeadlineTask = new Deadline(inputArray[2].trim(), inputArray[3].trim());
            if (inputArray[1].trim().equals("X")) {
                newDeadlineTask.markAsDone();
            } else {
                newDeadlineTask.markAsNotDone();
            }
            currentTask.add(newDeadlineTask);
            break;
        case "E":
            String [] fromAndTo = inputArray[3].trim().split(" - ");
            Task newEventTask = new Event(inputArray[2].trim(), fromAndTo[0].trim(), fromAndTo[1].trim());
            if (inputArray[1].trim().equals("X")) {
                newEventTask.markAsDone();
            } else {
                newEventTask.markAsNotDone();
            }
            currentTask.add(newEventTask);
            break;
        default:
            System.out.println("Kowalski Analysis Error: Text File Corrupted!");
            break;
        }
    }

    /**
     * Read text file accesses Kowalski.txt and calls the restoreTaskList function
     * @throws IOException when unable to get the Kowalski file or has any input errors
     */
    public static void readTextFile() throws IOException{
        try {
            createTextFileFolder(Paths.get(TEXT_FILE_FOLDER));
            Path filePath = Paths.get(FULL_FILE_PATH);
            if (!Files.exists(filePath)){
                System.out.println("Creating new Kowalski.txt file");
                Files.createFile(filePath);
            }
            FileReader fileReader = new FileReader(FULL_FILE_PATH);
            BufferedReader line = new BufferedReader(fileReader);
            System.out.println("Kowalski retrieving previous data...");
            while (line.ready()) {
                restoreTaskList(line.readLine());
            }
            System.out.println("Kowalski Data Retrieval Complete!");
            Ui.printDivider();
        } catch (IOException e){
            System.out.println("Kowalski Data Retrieval Failed!");
            throw e;
        }
    }

    /**
     * Function which is called to generate an arrayList "lines" which updates according to the users' inputs.
     * Calls the writeTextFile function to update Kowalski.txt
     */
    public static void writeText(){
        List <String> lines = new ArrayList<>();
        for (Task task:currentTask){
            lines.add(task.textFileInputString());
        }
        Ui.printDivider();
        System.out.println("Kowalski analysing inputs...");
        writeTextFile(lines);
    }

    /**
     * Accesses the Kowalski.txt and updates it in the correct format.
     * @param lines: Arraylist containing the processed current tasks in the CurrentTask
     */
    public static void writeTextFile(List <String> lines) {
        try {
            Path parentPath = Paths.get(TEXT_FILE_FOLDER);
            createTextFileFolder(parentPath);

            FileWriter writer = new FileWriter(FULL_FILE_PATH);
            for (String line : lines) {
                writer.write(line + "\n");

            }
            System.out.println("Task recorded in the Text file!");
            writer.close();
        } catch (IOException e){
            System.out.println("Kowalski Analysis failed - Issue with directory/text file!");
        }
    }

    /**
     * Used to create the data folder to store the Kowalski.txt file
     * @param parentPath: Path file containing the path we intend to make Kowalski.txt in
     * @throws IOException whenever the input for the path or creation of the directory is improper
     */
    public static void createTextFileFolder(Path parentPath ) throws IOException{
        try {
            Files.createDirectories(parentPath);
        } catch (FileAlreadyExistsException ignored){
            //Ignore this error if file exists
        } catch (IOException e){
            System.out.println("Skipper, I am unable to create the data directory for you!");
            throw e;
        }
    }

    /**
     * Prints out the message to end conversation with the user
     */
    public static void printEndConversation(){
        System.out.println("Bye Skipper! Hope to serve you again for your next mission!");
        Ui.printDivider();
    }

    public static void main(String[] args) throws IOException {
        Ui.printIntro();
        readTextFile();
        String userCommand = processInput(in.next());

        while (!(userCommand.equals("bye"))){
            parseUserCommand(processInput(userCommand));
            userCommand = in.next();
        }

        Ui.printEndConversation();
    }
}
