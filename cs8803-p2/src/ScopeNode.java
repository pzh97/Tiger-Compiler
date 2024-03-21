import org.antlr.v4.runtime.Token;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ScopeNode extends SymbolNode {

    private ArrayList<String> semanticErrors = new ArrayList<>();

    private ScopeNode parent;

    private ArrayList<SymbolNode> symbolTable = new ArrayList<>();

    public ScopeNode(String symbolName, SymbolType symbolType, ScopeNode parent, Token token) {
        super(symbolName, symbolType, token);
        this.parent = parent;
    }

    public ArrayList<SymbolNode> getSymbolTable() {
        return symbolTable;
    }

    public ArrayList<String> getSemanticErrors() {
        return semanticErrors;
    }

    public void addSymbolNode(SymbolNode node) {
        if (node instanceof ScopeNode) {
            ((ScopeNode) node).parent = this;
        }
        switch (node.getSymbolType()) {
            case FUNCTION_DECLARE:
                if (searchSymbol(this, node.getSymbolName(), SymbolType.FUNCTION_DECLARE) != null) {
                    addSemanticError(node.token, "redefine function");
                }
                break;
            case VARIABLE_DECLARE:
                if (searchSymbol(this, node.getSymbolName(), SymbolType.VARIABLE_DECLARE) != null) {
                    addSemanticError(node.token, "redefine variable");
                }
                break;
            case TYPE_DECLARE:
                if (searchSymbol(this, node.getSymbolName(), SymbolType.TYPE_DECLARE) != null) {
                    addSemanticError(node.token, "redefine type");
                }
                break;
        }
        symbolTable.add(node);
    }

    /**
     * search symbol name in current scope
     *
     * @param symbolName
     * @return
     */
    public SymbolNode findSymbol(String symbolName) {
        for (SymbolNode entry : symbolTable) {
            if (symbolName.equals(entry.getSymbolName())) {
                return entry;
            }
            if (entry.getSymbolType() == SymbolType.FUNCTION_DECLARE) {
                var funcDecl = (FunctionDeclare) entry;
                for (var paramDecl : funcDecl.getParameters()) {
                    if (paramDecl.getSymbolName().equals(symbolName)) {
                        return paramDecl;
                    }
                }
            }
        }
        return null;
    }

    public DataType findBaseDataType(DataType dataType) {
        if (dataType == DataType.INT || dataType == DataType.FLOAT || dataType == DataType.ANY || dataType == DataType.VOID) {
            return dataType;
        } else if (dataType instanceof ArrayType) {
            return ((ArrayType) dataType).getBaseType();
        } else {
            var typeDecl = (DataTypeDeclare) searchSymbol(this, dataType.getTypeName(), SymbolType.TYPE_DECLARE);
            return typeDecl == null ? DataType.ANY : findBaseDataType(typeDecl.getDataType());
        }
    }

    public SymbolNode searchSymbol(String symbolName, SymbolType symbolType) {
        ScopeNode symbolTable = this;
        while (symbolTable != null) {
            SymbolNode entry = searchSymbol(symbolTable, symbolName, symbolType);
            if (entry != null) {
                return entry;
            } else {
                if (symbolType == SymbolType.VARIABLE_DECLARE &&
                        this.symbolTable.get(this.symbolTable.size() - 1).getSymbolType() == SymbolType.FUNCTION_DECLARE) {
                    var funDecl = (FunctionDeclare) this.symbolTable.get(this.symbolTable.size() - 1);
                    entry = funDecl.findParameter(symbolName);
                    if (entry != null) {
                        return entry;
                    }
                }
            }
            symbolTable = symbolTable.parent;
        }
        return null;
    }

    public static SymbolNode searchSymbol(ScopeNode symbolTable, String symbolName, SymbolType symbolType) {
        for (SymbolNode entry : symbolTable.symbolTable) {
            if (symbolName.equals(entry.getSymbolName()) && entry.getSymbolType() == symbolType) {
                return entry;
            }
        }
        return null;
    }

    public void addSemanticError(Token token, String errorDescription) {
        var msg = "line " + token.getLine() + ":" + token.getCharPositionInLine() + " " + errorDescription;
        if(!semanticErrors.contains(msg)) {
            semanticErrors.add(msg);
        }
    }

}
