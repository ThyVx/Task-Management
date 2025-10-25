package data_access;

import buisness_logic.TaskManagement;
import data_model.Task;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DataPersistence {
    private static final String TASK_FILE = "task_management_data.ser";
    private static final String UNASSIGNED_TASK_FILE = "unassigned_tasks_data.ser";

    public void saveData(TaskManagement tm, List<Task> unassignedTasks) {
        try {
            FileOutputStream taskFileOut = new FileOutputStream(TASK_FILE);
            ObjectOutputStream taskObjectOut = new ObjectOutputStream(taskFileOut);
            taskObjectOut.writeObject(tm);
            taskObjectOut.close();
            taskFileOut.close();
            System.out.println("Tasks saved: " + TASK_FILE);

            //now thw unassigned tasks
            FileOutputStream unassignedFileOut = new FileOutputStream(UNASSIGNED_TASK_FILE);
            ObjectOutputStream unassignedObjectOut = new ObjectOutputStream(unassignedFileOut);
            unassignedObjectOut.writeObject(unassignedTasks);
            unassignedObjectOut.close();
            unassignedFileOut.close();
            System.out.println("Unassigned tasks saved: " + UNASSIGNED_TASK_FILE);
        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }

    public TaskManagement loadData() {
        TaskManagement tm = null;
        try {
            FileInputStream fileIn = new FileInputStream(TASK_FILE);
            ObjectInputStream objectIn = new ObjectInputStream(fileIn);
            tm = (TaskManagement) objectIn.readObject();
            objectIn.close();
            fileIn.close();
            System.out.println("Tasks loaded: " + TASK_FILE);
        } catch (FileNotFoundException e) {
            System.out.println("No saved tasks found, beginning..");
            tm = new TaskManagement();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading tasks: " + e.getMessage());
            tm = new TaskManagement();
        }
        return tm;
    }

    public List<Task> loadUnassignedTasks() {
        List<Task> unassignedTasks = null;
        try {
            FileInputStream fileIn = new FileInputStream(UNASSIGNED_TASK_FILE);
            ObjectInputStream objectIn = new ObjectInputStream(fileIn);
            unassignedTasks = (List<Task>) objectIn.readObject();
            objectIn.close();
            fileIn.close();
            System.out.println("Unassigned tasks loaded: " + UNASSIGNED_TASK_FILE);
        } catch (FileNotFoundException e) {
            System.out.println("No saved unassigned tasks found, beginning...");
            unassignedTasks = new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading unassigned tasks: " + e.getMessage());
            unassignedTasks = new ArrayList<>();
        }
        return unassignedTasks;
    }
}