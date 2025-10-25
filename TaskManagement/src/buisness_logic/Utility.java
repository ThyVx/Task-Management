package buisness_logic;

import data_model.Employee;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Utility {
    public List<String> filterEmployeesByWorkDuration(TaskManagement tm) {
        List<EmployeeDuration> filteredEmployees = new ArrayList<>();

        for (Employee emp : tm.getEmployeeTasks().keySet()) {

            int duration = tm.calculateEmployeeWorkDuration(emp.getIdEmployee());
            if (duration > 10) {
                filteredEmployees.add(new EmployeeDuration(emp.getName(), duration));
            }
        }

        Collections.sort(filteredEmployees, new Comparator<EmployeeDuration>() {

            @Override
            public int compare(EmployeeDuration e1, EmployeeDuration e2) {
                return Integer.compare(e2.duration, e1.duration);
            }
        });

        List<String> employeeNames = new ArrayList<>();

        for (EmployeeDuration empDur : filteredEmployees) {
            employeeNames.add(empDur.name);
        }
        return employeeNames;
    }

    private static class EmployeeDuration {
        private String name;
        private int duration;

        public EmployeeDuration(String name, int duration) {
            this.name = name;
            this.duration = duration;
        }
    }
}