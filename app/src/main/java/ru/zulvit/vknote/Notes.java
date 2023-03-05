package ru.zulvit.vknote;

public class Notes {
    String heading;
    String description;

    public Notes(String heading, String titleImage) {
        this.heading = heading;
        this.description = titleImage;
    }

    @Override
    public String toString() {
        return "Notes{" +
                "heading='" + heading + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
