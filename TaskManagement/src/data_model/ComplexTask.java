package data_model;

import java.util.ArrayList;
import java.util.List;

public final class ComplexTask extends Task {
    private List<Task> tasks;      //where i ll store the SubTasks

    public ComplexTask(int idTask) {
        super(idTask);
        this.tasks = new ArrayList<>();
    }

    public void addTask(Task t) {
        this.tasks.add(t);
    }

    public void removeTask(int idTask) {
        tasks.removeIf(t->t.getIdTask()==idTask);
    }

    @Override
    public int estimateDuration() {
        int total = 0;
        for (Task task : tasks) {
            total += task.estimateDuration();
        }
        return total;
    }

    public List<Task> getTasks() {
        return tasks;
    }
    
}
