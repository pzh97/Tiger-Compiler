import org.antlr.v4.runtime.Token;

import java.util.ArrayList;
import java.util.List;

public class SymbolNodeVisitor extends TigerBaseVisitor<Object> {

    protected ScopeNode currentScope;

    @Override
    public Object visitTiger_program(TigerParser.Tiger_programContext ctx) {
        var global = new ScopeNode(null, SymbolType.SCOPE, null, ctx.PROGRAM().getSymbol());
        currentScope = global;
        super.visitTiger_program(ctx);
        return global;
    }

    @Override
    public Object visitType_declaration(TigerParser.Type_declarationContext ctx) {
        currentScope.addSymbolNode(new DataTypeDeclare(ctx.ID().getText(), createDataType(ctx.type()), ctx.TYPE().getSymbol()));
        return null;
    }

    protected DataType createDataType(TigerParser.TypeContext ctx) {
        if (ctx.ARRAY() != null) {
            return new ArrayType(createBaseType(ctx.base_type()), Integer.parseInt(ctx.INTLIT().getText()));
        } else if (ctx.ID() != null) {
            return new DataType(ctx.ID().getText());
        } else {
            return createBaseType(ctx.base_type());
        }
    }

    private DataType createBaseType(TigerParser.Base_typeContext ctx) {
        if (ctx.INT() != null) {
            return DataType.INT;
        } else {
            return DataType.FLOAT;
        }
    }

    @Override
    public Object visitVar_declaration(TigerParser.Var_declarationContext ctx) {
        StorageClass storageClass = null;
        if (ctx.storage_class().VAR() != null) {
            storageClass = StorageClass.VAR;
        } else {
            storageClass = StorageClass.STATIC;
        }
        for (var token : getIdList(ctx.id_list())) {
            currentScope.addSymbolNode(new VariableDeclare(token.getText(), storageClass, createDataType(ctx.type()), token));
        }
        return null;
    }

    private List<Token> getIdList(TigerParser.Id_listContext ctx) {
        ArrayList<Token> idList = new ArrayList<>();
        idList.add(ctx.ID().getSymbol());
        if (ctx.id_list() != null) {
            idList.addAll(getIdList(ctx.id_list()));
        }
        return idList;
    }

    @Override
    public Object visitFunct(TigerParser.FunctContext ctx) {
        var retType = ctx.ret_type().type() == null ? DataType.VOID : createDataType(ctx.ret_type().type());
        var funcDecl = new FunctionDeclare(ctx.ID().getText(), retType, ctx.FUNCTION().getSymbol());
        if (ctx.param_list() != null) {
            funcDecl.addParameter(createParamList(ctx.param_list()));
        }
        currentScope.addSymbolNode(funcDecl);
        super.visitFunct(ctx);
        return null;
    }

    protected List<VariableDeclare> createParamList(TigerParser.Param_listContext ctx) {
        ArrayList<VariableDeclare> paramList = new ArrayList<>();
        if (ctx.param() != null) {
            paramList.add(createParam(ctx.param()));
            if (ctx.param_list_tail() != null) {
                paramList.addAll(createParamListTail(ctx.param_list_tail()));
            }
        }
        return paramList;
    }

    protected List<VariableDeclare> createParamListTail(TigerParser.Param_list_tailContext ctx) {
        ArrayList<VariableDeclare> paramList = new ArrayList<>();
        if (ctx.param() != null) {
            paramList.add(createParam(ctx.param()));
        }
        if (ctx.param_list_tail() != null) {
            paramList.addAll(createParamListTail(ctx.param_list_tail()));
        }
        return paramList;
    }

    private VariableDeclare createParam(TigerParser.ParamContext ctx) {
        return new VariableDeclare(ctx.ID().getText(), null, createDataType(ctx.type()), ctx.ID().getSymbol());
    }

    @Override
    public Object visitStat(TigerParser.StatContext ctx) {
        if (ctx.LET() != null) {
            var parent = currentScope;
            currentScope = new ScopeNode(null, SymbolType.SCOPE, parent, ctx.LET().getSymbol());
            parent.addSymbolNode(currentScope);
            super.visitStat(ctx);
            currentScope = parent;
        } else {
            super.visitStat(ctx);
        }
        return null;
    }
}
