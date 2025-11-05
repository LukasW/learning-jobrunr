package ch.css.learning.jobrunr;

public class SimpleJobParameters {
    private String name;
    private String description;

    public SimpleJobParameters() {
    }

    public SimpleJobParameters(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
