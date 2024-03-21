public class DataType {

    public static final DataType INT = new DataType("int");
    public static final DataType FLOAT = new DataType("float");
    public static final DataType VOID = new DataType("void");
    public static final DataType ANY = new DataType("any");
    private String typeName;

    public DataType(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeName() {
        return typeName;
    }
}
