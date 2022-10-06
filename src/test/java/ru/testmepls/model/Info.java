package ru.testmepls.model;

import java.util.List;

public class Info {
    public String name;
    public String surname;
    public int age;
    public boolean isPioneer;
    public List<String> address;
    public Father father;

    public static class Father {
        public String name;
        public boolean isProsperous;
    }

}
