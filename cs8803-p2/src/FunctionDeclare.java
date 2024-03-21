import org.antlr.v4.runtime.Token;

import java.util.ArrayList;
import java.util.List;

public class FunctionDeclare extends SymbolNode {

    private DataType returnType;

    private ArrayList<VariableDeclare> parameters = new ArrayList<>();

    public FunctionDeclare(String symbolName, DataType returnType, Token token) {
        super(symbolName, SymbolType.FUNCTION_DECLARE, token);
        this.returnType = returnType;
    }

    public DataType getReturnType() {
        return returnType;
    }

    public void addParameter(VariableDeclare param) {
        parameters.add(param);
    }

    public void addParameter(List<VariableDeclare> params) {
        parameters.addAll(params);
    }

    public ArrayList<VariableDeclare> getParameters() {
        return parameters;
    }

    public VariableDeclare findParameter(String parameterName) {
        return parameters.stream().filter(p -> p.getSymbolName().equals(parameterName)).findFirst().orElse(null);
    }
}
