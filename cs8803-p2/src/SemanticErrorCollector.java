import java.util.ArrayList;
import java.util.List;

public class SemanticErrorCollector {

    private ArrayList<String> errors = new ArrayList<>();

    public SemanticErrorCollector() {
    }

    public List<String> getSemanticErrors(ScopeNode scopeNode) {
        formatSymbolTable(scopeNode);
        return errors;
    }

    private void formatSymbolTable(ScopeNode symbolNode) {
        errors.addAll(symbolNode.getSemanticErrors());
        for (var node : symbolNode.getSymbolTable()) {
            if (node instanceof ScopeNode) {
                formatSymbolTable((ScopeNode) node);
            }
        }
    }
}
