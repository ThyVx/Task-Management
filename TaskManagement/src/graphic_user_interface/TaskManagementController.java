package graphic_user_interface;

import buisness_logic.TaskManagement;
import buisness_logic.Utility;
import data_access.DataPersistence;
import data_model.Employee;
import data_model.SimpleTask;
import data_model.ComplexTask;
import data_model.Task;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TaskManagementController {
    private TaskManagementView view;
    private TaskManagement tm;
    private DataPersistence dp;
    private List<Task> unassignedTasks;
    private Utility utility;

    public TaskManagementController(TaskManagementView view) {
        this.view = view;
        this.tm = new TaskManagement();
        this.dp = new DataPersistence();
        this.unassignedTasks = new ArrayList<>();
        this.utility = new Utility();
        tm = dp.loadData();
        unassignedTasks = dp.loadUnassignedTasks();
        updateEmployeeTable();
        updateTaskTable();

        view.getAddEmpBtn().addActionListener(e -> addEmployee());
        view.getAddTaskBtn().addActionListener(e -> addTask());
        view.getAssignTaskBtn().addActionListener(e -> assignTask());
        view.getModifyStatusBtn().addActionListener(e -> modifyStatus());
        view.getAddSubtaskBtn().addActionListener(e -> addSubtask());
        view.getFilterEmployeesBtn().addActionListener(e -> filterEmployees());

        view.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dp.saveData(tm, unassignedTasks);
            }
        });
    }

    private void filterEmployees() {
        List<String> filteredEmployees = utility.filterEmployeesByWorkDuration(tm);
        String message = filteredEmployees.isEmpty() ?
                "No employees with work duration > 40 hours" :
                "Employees with work duration > 40 hours:\n" + String.join("\n", filteredEmployees);
        showThemedMessage(message);
    }

    private void addEmployee() {
        JPanel panel = createThemedPanel();
        panel.add(createThemedLabel("Employee ID:"));
        JTextField idField = createThemedTextField();
        panel.add(idField);
        panel.add(createThemedLabel("Employee Name:"));
        JTextField nameField = createThemedTextField();
        panel.add(nameField);

        int result = showThemedDialog(panel, "Add Employee");
        if (result == JOptionPane.OK_OPTION) {
            try {
                int id = Integer.parseInt(idField.getText().trim());
                String name = nameField.getText().trim();
                if (name.isEmpty()) throw new IllegalArgumentException("Name can't be empty");
                tm.addEmployee(new Employee(id, name));
                showThemedMessage("Employee added: " + name);
                updateEmployeeTable();
            } catch (NumberFormatException e) {
                showThemedMessage("Invalid Employee ID");
            } catch (Exception e) {
                showThemedMessage(e.getMessage());
            }
        }
    }

    private void addTask() {
        JPanel panel = createThemedPanel();
        panel.add(createThemedLabel("Task ID:"));
        JTextField taskIdField = createThemedTextField();
        panel.add(taskIdField);
        panel.add(createThemedLabel("Task Type:"));
        JComboBox<String> typeCombo = createThemedComboBox(new String[]{"Simple", "Complex"});
        panel.add(typeCombo);
        panel.add(createThemedLabel("Start Hour (if Simple):"));
        JTextField startHourField = createThemedTextField();
        panel.add(startHourField);
        panel.add(createThemedLabel("End Hour (if Simple):"));
        JTextField endHourField = createThemedTextField();
        panel.add(endHourField);

        int result = showThemedDialog(panel, "Add Task");
        if (result == JOptionPane.OK_OPTION) {
            try {
                int taskId = Integer.parseInt(taskIdField.getText().trim());
                String type = (String) typeCombo.getSelectedItem();
                Task task;
                if ("Simple".equals(type)) {
                    int start = Integer.parseInt(startHourField.getText().trim());
                    int end = Integer.parseInt(endHourField.getText().trim());
                    if (start < 0 || end <= start || end > 24) {
                        throw new IllegalArgumentException("Invalid hours");
                    }
                    task = new SimpleTask(taskId, start, end);
                } else {
                    task = new ComplexTask(taskId);
                }
                unassignedTasks.add(task);
                showThemedMessage("Task " + taskId + " created");
                updateTaskTable();
            } catch (NumberFormatException e) {
                showThemedMessage("Invalid input");
            } catch (Exception e) {
                showThemedMessage(e.getMessage());
            }
        }
    }

    private void addSubtask() {
        JPanel panel = createThemedPanel();
        panel.add(createThemedLabel("Subtask ID:"));
        JTextField taskIdField = createThemedTextField();
        panel.add(taskIdField);
        panel.add(createThemedLabel("Task Type:"));
        JComboBox<String> typeCombo = createThemedComboBox(new String[]{"Simple", "Complex"});
        panel.add(typeCombo);
        panel.add(createThemedLabel("Start Hour (if Simple):"));
        JTextField startHourField = createThemedTextField();
        panel.add(startHourField);
        panel.add(createThemedLabel("End Hour (if Simple):"));
        JTextField endHourField = createThemedTextField();
        panel.add(endHourField);
        panel.add(createThemedLabel("Parent Task ID:"));
        JTextField parentIdField = createThemedTextField();
        panel.add(parentIdField);

        int result = showThemedDialog(panel, "Add Subtask");
        if (result == JOptionPane.OK_OPTION) {
            try {
                int taskId = Integer.parseInt(taskIdField.getText().trim());
                String type = (String) typeCombo.getSelectedItem();
                int parentId = Integer.parseInt(parentIdField.getText().trim());
                Task task;
                if ("Simple".equals(type)) {
                    int start = Integer.parseInt(startHourField.getText().trim());
                    int end = Integer.parseInt(endHourField.getText().trim());
                    if (start < 0 || end <= start || end > 24) {
                        throw new IllegalArgumentException("Invalid hours");
                    }
                    task = new SimpleTask(taskId, start, end);
                } else {
                    task = new ComplexTask(taskId);
                }
                Task parentTask = findTaskById(parentId);
                if (parentTask == null || !(parentTask instanceof ComplexTask)) {
                    throw new IllegalArgumentException("Parent task not found or not a ComplexTask");
                }
                ((ComplexTask) parentTask).addTask(task);
                showThemedMessage("Subtask " + taskId + " added to complex task " + parentId);
                updateEmployeeTable();
                updateTaskTable();
            } catch (NumberFormatException e) {
                showThemedMessage("Invalid input");
            } catch (Exception e) {
                showThemedMessage(e.getMessage());
            }
        }
    }

    private void assignTask() {
        JPanel panel = createThemedPanel();
        panel.add(createThemedLabel("Employee ID:"));
        JTextField empIdField = createThemedTextField();
        panel.add(empIdField);
        panel.add(createThemedLabel("Task ID:"));
        JTextField taskIdField = createThemedTextField();
        panel.add(taskIdField);

        int result = showThemedDialog(panel, "Assign Task to Employee");
        if (result == JOptionPane.OK_OPTION) {
            try {
                int empId = Integer.parseInt(empIdField.getText().trim());
                int taskId = Integer.parseInt(taskIdField.getText().trim());
                Task task = findUnassignedTask(taskId);
                if (task == null) {
                    throw new IllegalArgumentException("Task " + taskId + " not found in unassigned tasks.");
                }
                tm.assignTaskToEmployee(empId, task);
                unassignedTasks.remove(task);
                showThemedMessage("Task " + taskId + " assigned to employee " + empId);
                updateEmployeeTable();
                updateTaskTable();
            } catch (NumberFormatException e) {
                showThemedMessage("Invalid ID");
            } catch (Exception e) {
                showThemedMessage(e.getMessage());
            }
        }
    }

    private void modifyStatus() {
        JPanel panel = createThemedPanel();
        panel.add(createThemedLabel("Employee ID:"));
        JTextField empIdField = createThemedTextField();
        panel.add(empIdField);
        panel.add(createThemedLabel("Task ID:"));
        JTextField taskIdField = createThemedTextField();
        panel.add(taskIdField);
        panel.add(createThemedLabel("New Status (Completed/Uncompleted):"));
        JTextField statusField = createThemedTextField();
        panel.add(statusField);

        int result = showThemedDialog(panel, "Modify Task Status");
        if (result == JOptionPane.OK_OPTION) {
            try {
                int empId = Integer.parseInt(empIdField.getText().trim());
                int taskId = Integer.parseInt(taskIdField.getText().trim());
                String newStatus = statusField.getText().trim();
                if (!newStatus.equalsIgnoreCase("Completed") && !newStatus.equalsIgnoreCase("Uncompleted")) {
                    throw new IllegalArgumentException("Status has to be 'Completed' or 'Uncompleted'.");
                }
                tm.modifyTaskStatus(empId, taskId, newStatus);
                showThemedMessage("Status updated for task " + taskId);
                updateEmployeeTable();
            } catch (NumberFormatException e) {
                showThemedMessage("Invalid ID");
            } catch (Exception e) {
                showThemedMessage(e.getMessage());
            }
        }
    }

    private JPanel createThemedPanel() {
        Color black = new Color(28, 37, 38);
        Color darkPurple = new Color(75, 0, 130);
        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
        panel.setBackground(black);
        panel.setBorder(new CompoundBorder(
                new LineBorder(darkPurple, 2, true),
                new EmptyBorder(10, 10, 10, 10)
        ));
        return panel;
    }

    private JLabel createThemedLabel(String text) {
        Color lightPurple = new Color(230, 230, 250);
        JLabel label = new JLabel(text);
        label.setForeground(lightPurple);
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        return label;
    }

    private JTextField createThemedTextField() {
        Color black = new Color(28, 37, 38);
        Color lightPurple = new Color(230, 230, 250);
        Color grayishPurple = new Color(47, 47, 79);
        JTextField textField = new JTextField(10);
        textField.setBackground(grayishPurple);
        textField.setForeground(lightPurple);
        textField.setCaretColor(lightPurple);
        textField.setBorder(new LineBorder(black, 1, true));
        textField.setFont(new Font("Arial", Font.PLAIN, 14));
        return textField;
    }

    private JComboBox<String> createThemedComboBox(String[] items) {
        Color black = new Color(28, 37, 38);
        Color lightPurple = new Color(230, 230, 250);
        Color grayishPurple = new Color(47, 47, 79);
        JComboBox<String> comboBox = new JComboBox<>(items);
        comboBox.setBackground(grayishPurple);
        comboBox.setForeground(lightPurple);
        comboBox.setBorder(new LineBorder(black, 1, true));
        comboBox.setFont(new Font("Arial", Font.PLAIN, 14));
        return comboBox;
    }

    private int showThemedDialog(JPanel panel, String title) {
        Color black = new Color(28, 37, 38);
        UIManager.put("OptionPane.background", black);
        UIManager.put("Panel.background", black);
        UIManager.put("Button.background", new Color(75, 0, 130));
        UIManager.put("Button.foreground", new Color(230, 230, 250));
        UIManager.put("Button.font", new Font("Arial", Font.BOLD, 14));
        return JOptionPane.showConfirmDialog(view, panel, title, JOptionPane.OK_CANCEL_OPTION);
    }

    private void showThemedMessage(String message) {
        Color black = new Color(28, 37, 38);
        UIManager.put("OptionPane.background", black);
        UIManager.put("Panel.background", black);
        UIManager.put("Button.background", new Color(75, 0, 130));
        UIManager.put("Button.foreground", new Color(230, 230, 250));
        UIManager.put("Button.font", new Font("Arial", Font.BOLD, 14));
        JOptionPane.showMessageDialog(view, message);
    }

    private Task findUnassignedTask(int taskId) {
        for (Task t : unassignedTasks) {
            if (t.getIdTask() == taskId) return t;
        }
        return null;
    }

    private Task findTaskById(int id) {
        for (Task t : unassignedTasks) {
            Task found = findTaskByIdInTask(t, id);
            if (found != null) return found;
        }
        for (List<Task> tasks : tm.getEmployeeTasks().values()) {
            for (Task t : tasks) {
                Task found = findTaskByIdInTask(t, id);
                if (found != null) return found;
            }
        }
        return null;
    }

    private Task findTaskByIdInTask(Task task, int id) {
        if (task.getIdTask() == id) return task;
        if (task instanceof ComplexTask) {
            for (Task subTask : ((ComplexTask) task).getTasks()) {
                Task found = findTaskByIdInTask(subTask, id);
                if (found != null) return found;
            }
        }
        return null;
    }

    private void updateEmployeeTable() {
        Map<Employee, List<Task>> employeeTasks = tm.getEmployeeTasks();
        List<Object[]> data = new ArrayList<>();
        for (Map.Entry<Employee, List<Task>> entry : employeeTasks.entrySet()) {
            Employee emp = entry.getKey();
            List<Task> tasks = entry.getValue();
            int totalDuration = tm.calculateEmployeeWorkDuration(emp.getIdEmployee());
            // Add employee row
            data.add(new Object[]{emp.getIdEmployee(), emp.getName(), totalDuration, "", "", "", ""});
            // Add tasks with hierarchy
            for (Task t : tasks) {
                addTaskToTable(data, t, 0);
            }
        }
        String[] columnNames = {"Employee ID", "Name", "Total Duration", "Task ID", "Type", "Duration", "Status"};
        Object[][] dataArray = data.toArray(new Object[0][]);
        view.getEmployeeTable().setModel(new javax.swing.table.DefaultTableModel(dataArray, columnNames));
        view.getEmployeeTable().setDefaultRenderer(Object.class, new CustomTableCellRenderer());
    }

    private void addTaskToTable(List<Object[]> data, Task task, int level) {
        String indent = "-".repeat(level); //adds - for each level of complex tasks
        String type = (task instanceof SimpleTask) ? "Simple" : "Complex";
        String duration = String.valueOf(task.estimateDuration());
        String status = task.getStatusTask() != null ? task.getStatusTask() : "Uncompleted";

        data.add(new Object[]{"", "", "", indent + task.getIdTask(), type, duration, status});

        if (task instanceof ComplexTask) {
            for (Task subTask : ((ComplexTask) task).getTasks()) {
                addTaskToTable(data, subTask, level + 1);
            }
        }
    }

    private void updateTaskTable() {
        List<Object[]> data = new ArrayList<>();
        for (Task t : unassignedTasks) {
            String type = (t instanceof SimpleTask) ? "Simple" : "Complex";
            String duration = String.valueOf(t.estimateDuration());
            String status = t.getStatusTask() != null ? t.getStatusTask() : "Uncompleted";
            data.add(new Object[]{t.getIdTask(), type, duration, status});
        }

        String[] columnNames = {"Task ID", "Type", "Duration", "Status"};
        Object[][] dataArray = data.toArray(new Object[0][]);
        view.getUnassignedTaskTable().setModel(new javax.swing.table.DefaultTableModel(dataArray, columnNames));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TaskManagementView view = new TaskManagementView();
            new TaskManagementController(view);
        });
    }
}


class CustomTableCellRenderer extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        Color lightPurple = new Color(230, 230, 250);
        Color grayishPurple = new Color(47, 47, 79);
        if (table.getValueAt(row, 0) != null && !table.getValueAt(row, 0).toString().isEmpty()) {
            c.setFont(c.getFont().deriveFont(Font.BOLD));
            c.setBackground(grayishPurple.brighter());
            c.setForeground(lightPurple);
        } else {
            c.setFont(c.getFont().deriveFont(Font.PLAIN));
            c.setBackground(row % 2 == 0 ? grayishPurple : grayishPurple.darker());
            c.setForeground(lightPurple);
        }
        return c;
    }
}