package data_model;
import java.io.Serializable;

public sealed abstract class Task implements Serializable permits ComplexTask, SimpleTask {
    private int idTask;
    private String statusTask;

    public abstract int estimateDuration();

    public Task(int idTask) {
        this.idTask = idTask;
        this.statusTask = "Uncompleted";
    }

    public int getIdTask() {
        return idTask;
    }

    public void setIdTask(int idTask) {
        this.idTask = idTask;
    }

    public String getStatusTask() {
        return statusTask;
    }

    public void setStatusTask(String statusTask) {
        this.statusTask = statusTask;
    }

}
