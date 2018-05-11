package Controllers;

public class ComboItemController {
    public final int key;
    public final String value;

    public ComboItemController(int key, String value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
    
    public int getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}