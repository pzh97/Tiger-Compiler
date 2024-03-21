import org.antlr.v4.runtime.Token;

public class VariableDeclare extends SymbolNode {

    private StorageClass storageClass;
    private DataType dataType;

    public VariableDeclare(String symbolName, StorageClass storageClass, DataType dataType, Token token) {
        super(symbolName, SymbolType.VARIABLE_DECLARE, token);
        this.storageClass = storageClass;
        this.dataType = dataType;
    }

    public StorageClass getStorageClass() {
        return storageClass;
    }

    public DataType getDataType() {
        return dataType;
    }
}
