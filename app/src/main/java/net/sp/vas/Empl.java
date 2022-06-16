package net.sp.vas;


public class Empl {
    int id;
    String name, d, joiningDate;
    double s;

    public Empl(int id, String name, String d, String joiningDate, double s) {
        this.id = id;
        this.name = name;
        this.d = d;
        this.joiningDate = joiningDate;
        this.s = s;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getD() {
        return d;
    }

    public String getJoiningDate() {
        return joiningDate;
    }

    public double getS() {
        return s;
    }
}
