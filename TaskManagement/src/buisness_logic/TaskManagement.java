package buisness_logic;

import data_model.Employee;
import data_model.Task;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class TaskManagement implements Serializable {
    private Map<Employee, List<Task>> employeeTasks;

    public TaskManagement() {
        this.employeeTasks = new HashMap<>();
    }

    public void assignTaskToEmployee(int idEmployee, Task task) {
        Employee foundEmployee = null;
        for(Employee emp : employeeTasks.keySet()) {
            if(emp.getIdEmployee()==idEmployee) {
                foundEmployee = emp;
                break;
            }
        }

        if(foundEmployee==null) {
            throw new IllegalArgumentException("Employee not found");
        }

        List<Task> foundTasks = employeeTasks.get(foundEmployee);
        if(foundTasks==null) {
            foundTasks = new ArrayList<>();
            employeeTasks.put(foundEmployee, foundTasks);
        }
        foundTasks.add(task);
    }

    public int calculateEmployeeWorkDuration(int idEmployee) {
        Employee foundEmployee = null;

        for(Employee emp : employeeTasks.keySet()) {
            if(emp.getIdEmployee()== idEmployee) {
                foundEmployee = emp;
                break;
            }
        }

        if(foundEmployee==null) {
            throw new IllegalArgumentException("Employee not found");
        }

        List<Task> foundTasks = employeeTasks.get(foundEmployee);
        if(foundTasks==null) {
            return 0;
        }

        int totalWorkDuration = 0;
        for(Task task : foundTasks) {
            if(task.getStatusTask().equals("Completed")) {
                totalWorkDuration += task.estimateDuration();
            }
        }
        return totalWorkDuration;
    }

    public void modifyTaskStatus(int idEmployee, int idTask, String newStatus) {
        Employee foundEmployee = null;

        for(Employee emp : employeeTasks.keySet()) {
            if(emp.getIdEmployee()==idEmployee) {
                foundEmployee = emp;
                break;
            }
        }

        if(foundEmployee==null) {
            throw new IllegalArgumentException("Employee not found");
        }

        List<Task> tasks = employeeTasks.get(foundEmployee);

        if(tasks==null) {
            throw new IllegalArgumentException("No tasks found for emp");
        }

        Task foundTask = null;

        for(Task task : tasks) {
            if(task.getIdTask()==idTask) {
                foundTask = task;
                break;
            }
        }

        if(foundTask==null) {
            throw new IllegalArgumentException("Task not found");
        }
        foundTask.setStatusTask(newStatus);
    }

    public void addEmployee(Employee employee) {
        if(!employeeTasks.containsKey(employee)) {
            employeeTasks.put(employee, new ArrayList<>());
        }
    }

    public void removeEmployee(int idEmployee) {
        Employee foundEmployee = null;
        for(Employee emp : employeeTasks.keySet()) {
            if(emp.getIdEmployee()==idEmployee) {
                foundEmployee = emp;
                break;
            }
        }
        if(foundEmployee==null) {
            throw new IllegalArgumentException("Employee not found");
        }

        employeeTasks.remove(foundEmployee);
    }

    public Map<Employee, List<Task>> getEmployeeTasks() {
        return employeeTasks;
    }
}
