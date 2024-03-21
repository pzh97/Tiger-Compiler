import org.antlr.v4.runtime.Token;

public class DataTypeDeclare extends SymbolNode {

    private DataType dataType;

    public DataTypeDeclare(String symbolName, DataType dataType, Token token) {
        super(symbolName, SymbolType.TYPE_DECLARE, token);
        this.dataType = dataType;
    }

    public DataType getDataType() {
        return dataType;
    }
}
