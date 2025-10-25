package graphic_user_interface;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

public class TaskManagementView extends JFrame {
    private JTable employeeTable;
    private JTable unassignedTaskTable;
    private JButton addEmpBtn, addTaskBtn, assignTaskBtn, modifyStatusBtn, addSubtaskBtn;
    private JButton filterEmployeesBtn;

    public TaskManagementView() {
        Color black = new Color(28, 37, 38);
        Color darkPurple = new Color(75, 0, 130);
        Color lightPurple = new Color(230, 230, 250);
        Color grayishPurple = new Color(47, 47, 79);

        setTitle("Task Management");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 700);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(black);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBackground(black);
        buttonPanel.setBorder(new CompoundBorder(
                new LineBorder(darkPurple, 2, true),
                new EmptyBorder(10, 10, 10, 10)
        ));

        addEmpBtn = createThemedButton("Add Employee", darkPurple, lightPurple);
        addTaskBtn = createThemedButton("Add Task", darkPurple, lightPurple);
        assignTaskBtn = createThemedButton("Assign Task", darkPurple, lightPurple);
        modifyStatusBtn = createThemedButton("Modify Status", darkPurple, lightPurple);
        addSubtaskBtn = createThemedButton("Add Subtask", darkPurple, lightPurple);
        filterEmployeesBtn = createThemedButton("Filter Employees", darkPurple, lightPurple);

        buttonPanel.add(addEmpBtn);
        buttonPanel.add(Box.createVerticalStrut(15));
        buttonPanel.add(addTaskBtn);
        buttonPanel.add(Box.createVerticalStrut(15));
        buttonPanel.add(assignTaskBtn);
        buttonPanel.add(Box.createVerticalStrut(15));
        buttonPanel.add(modifyStatusBtn);
        buttonPanel.add(Box.createVerticalStrut(15));
        buttonPanel.add(addSubtaskBtn);
        buttonPanel.add(Box.createVerticalStrut(15));
        buttonPanel.add(filterEmployeesBtn);

        String[] empColumnNames = {"Employee ID", "Name", "Total Duration", "Task ID", "Type", "Duration", "Status"};
        employeeTable = new JTable(new Object[][]{}, empColumnNames);
        styleTable(employeeTable, black, grayishPurple, lightPurple, darkPurple);
        JScrollPane empScrollPane = new JScrollPane(employeeTable);
        empScrollPane.setBackground(black);
        empScrollPane.setBorder(new CompoundBorder(
                new LineBorder(darkPurple, 2, true),
                new EmptyBorder(5, 5, 5, 5)
        ));

        empScrollPane.setViewportBorder(new LineBorder(darkPurple, 1));
        empScrollPane.getViewport().setBackground(black);

        String[] taskColumnNames = {"Task ID", "Type", "Duration", "Status"};
        unassignedTaskTable = new JTable(new Object[][]{}, taskColumnNames);
        styleTable(unassignedTaskTable, black, grayishPurple, lightPurple, darkPurple);
        JScrollPane taskScrollPane = new JScrollPane(unassignedTaskTable);
        taskScrollPane.setBackground(black);
        taskScrollPane.setBorder(new CompoundBorder(
                new LineBorder(darkPurple, 2, true),
                new EmptyBorder(5, 5, 5, 5)
        ));

        taskScrollPane.setViewportBorder(new LineBorder(darkPurple, 1));
        taskScrollPane.getViewport().setBackground(black);
        taskScrollPane.setPreferredSize(new Dimension(0, 150));

        add(buttonPanel, BorderLayout.WEST);
        add(empScrollPane, BorderLayout.CENTER);
        add(taskScrollPane, BorderLayout.SOUTH);

        setVisible(true);
    }

    private JButton createThemedButton(String text, Color bgColor, Color fgColor) {

        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBorder(new CompoundBorder(
                new LineBorder(bgColor.darker(), 1, true),
                new EmptyBorder(8, 15, 8, 15)
        ));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.brighter());
                button.setForeground(Color.WHITE);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
                button.setForeground(fgColor);
            }
        });
        return button;
    }

    private void styleTable(JTable table, Color bgColor, Color altRowColor, Color fgColor, Color gridColor) {
        table.setBackground(bgColor);
        table.setForeground(fgColor);
        table.setRowHeight(25);
        table.setShowGrid(true);
        table.setGridColor(gridColor);
        table.setFont(new Font("Arial", Font.PLAIN, 12));
        table.getTableHeader().setBackground(bgColor);
        table.getTableHeader().setForeground(fgColor);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        table.setSelectionBackground(altRowColor);
        table.setSelectionForeground(Color.WHITE);
    }

    public JTable getEmployeeTable() { return employeeTable; }
    public JTable getUnassignedTaskTable() { return unassignedTaskTable; }
    public JButton getAddEmpBtn() { return addEmpBtn; }
    public JButton getAddTaskBtn() { return addTaskBtn; }
    public JButton getAssignTaskBtn() { return assignTaskBtn; }
    public JButton getModifyStatusBtn() { return modifyStatusBtn; }
    public JButton getAddSubtaskBtn() { return addSubtaskBtn; }
    public JButton getFilterEmployeesBtn() { return filterEmployeesBtn; }
}