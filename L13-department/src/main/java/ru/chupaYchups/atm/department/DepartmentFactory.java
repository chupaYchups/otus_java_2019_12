package ru.chupaYchups.atm.department;

public class DepartmentFactory {

    public static Department createDepartment() {
        return new ClassicDepartment();
    }
}
