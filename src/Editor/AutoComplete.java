package Editor;

import java.util.List;

public interface AutoComplete {
    public List<String> predictCompletions(String prefix, int numOfCompletions);
}
