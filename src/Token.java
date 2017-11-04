import java.util.ArrayList;
import java.util.List;

public class Token {
    private String name;
    private String pattern;
    private int priority;
    private String action;
    private List<String> attributes;

    public Token(String name, String pattern, int priority, String action) {
        this.name = name;
        this.pattern = pattern;
        this.priority = priority;
        this.action = action;
        attributes = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
