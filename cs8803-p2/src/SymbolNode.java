import org.antlr.v4.runtime.Token;

public abstract class SymbolNode {

    public SymbolNode(String symbolName, SymbolType symbolType, Token token) {
        this.symbolName = symbolName;
        this.symbolType = symbolType;
        this.token = token;
    }

    protected String symbolName;
    protected SymbolType symbolType;
    protected Token token;

    public String getSymbolName() {
        return symbolName;
    }

    public SymbolType getSymbolType() {
        return symbolType;
    }

    public Token getToken() {
        return token;
    }
}
