public class ArrayType extends DataType {

    private int arrayLength;
    private DataType baseType;

    public ArrayType(DataType baseType, int arrayLength) {
        super("array");
        this.arrayLength = arrayLength;
        this.baseType = baseType;
    }

    public int getArrayLength() {
        return arrayLength;
    }

    public DataType getBaseType() {
        return baseType;
    }
}
