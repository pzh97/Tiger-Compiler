import java.util.stream.Collectors;

public class SymbolTableFormatter {

    private StringBuilder buffer = new StringBuilder();
    private int ident;
    private int scopeCounter;

    public SymbolTableFormatter() {
    }

    public String format(ScopeNode scopeNode) {
        ident = 0;
        formatSymbolTable(scopeNode);
        return buffer.toString();
    }

    private void formatSymbolTable(SymbolNode symbolNode) {

        buffer.append(" ".repeat(ident * 4));
        switch (symbolNode.getSymbolType()) {
            case SCOPE:
                scopeCounter = scopeCounter + 1;
                buffer.append("scope ").append(scopeCounter).append(':')
                        .append(System.lineSeparator());
                ident = ident + 1;
                for (var node : ((ScopeNode) symbolNode).getSymbolTable()) {
                    formatSymbolTable(node);
                }
                ident = ident - 1;
                break;
            case TYPE_DECLARE:
                var typeDecl = (DataTypeDeclare) symbolNode;
                buffer.append(typeDecl.getSymbolName() + ", type, " + typeDecl.getDataType().getTypeName());
                if (typeDecl.getDataType() instanceof ArrayType) {
                    buffer.append(", " + ((ArrayType) typeDecl.getDataType()).getArrayLength());
                }
                buffer.append(System.lineSeparator());
                break;
            case VARIABLE_DECLARE:
                var varDecl = (VariableDeclare) symbolNode;
                buffer.append(varDecl.getSymbolName() + ", " + varDecl.getStorageClass().toString().toLowerCase() + ", " + varDecl.getDataType().getTypeName())
                        .append(System.lineSeparator());
                break;
            case FUNCTION_DECLARE:
                var funcDecl = (FunctionDeclare) symbolNode;
                buffer.append(funcDecl.getSymbolName() + ", function, [");
                buffer.append(String.join(", ", funcDecl.getParameters().stream().map(p -> p.getDataType().getTypeName()).collect(Collectors.toList())))
                        .append("], ")
                        .append(funcDecl.getReturnType().getTypeName())
                        .append(System.lineSeparator());
                break;
        }
    }
}
