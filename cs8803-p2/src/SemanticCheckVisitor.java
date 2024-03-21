import java.util.ArrayList;
import java.util.stream.Collectors;

public class SemanticCheckVisitor extends SymbolNodeVisitor {

    private boolean insideLoop;

    @Override
    public Object visitStat(TigerParser.StatContext ctx) {

        // condition type checking
        if (ctx.IF() != null || ctx.WHILE() != null) {
            var conditionDataType = currentScope.findBaseDataType(visitExpr(ctx.expr(0)));
            if (conditionDataType != DataType.INT) {
                var node = ctx.IF();
                if (node == null) node = ctx.WHILE();
                currentScope.addSemanticError(node.getSymbol(), "Invalid condition expression");
            }
        }

        // assign type checking
        if (ctx.value() != null) {
            var left = (VariableDeclare) currentScope.searchSymbol(ctx.value().ID().getText(), SymbolType.VARIABLE_DECLARE);
            if (left != null) {
                var leftType = currentScope.findBaseDataType(left.getDataType());
                var rightType = currentScope.findBaseDataType(visitExpr(ctx.expr(0)));
                if (leftType == DataType.INT && rightType != DataType.INT) {
                    currentScope.addSemanticError(ctx.value().getStart(), "Incompatible assign");
                }
                var arrayIndexExpr = ctx.value().value_tail().expr();
                if (arrayIndexExpr != null) {
                    var arrayIndexType = currentScope.findBaseDataType(visitExpr(arrayIndexExpr));
                    if (arrayIndexType != DataType.INT) {
                        currentScope.addSemanticError(arrayIndexExpr.getStart(), "Invalid array index");
                    }
                }
            }
            else {
                currentScope.addSemanticError(ctx.value().ID().getSymbol(), "Variable undefined");
            }
        }

        if (ctx.FOR() != null) {
            var endType = currentScope.findBaseDataType(visitExpr(ctx.expr(1)));
            if (endType != DataType.INT) {
                currentScope.addSemanticError(ctx.FOR().getSymbol(), "Invalid loop index");
            }
        }

        if (ctx.RETURN() != null) {
            var entry = currentScope.getSymbolTable().get(currentScope.getSymbolTable().size() - 1);
            if (entry instanceof FunctionDeclare) {
                var funcReturnType = currentScope.findBaseDataType(((FunctionDeclare) entry).getReturnType());
                var returnType = DataType.VOID;
                if (ctx.optreturn().expr() != null) {
                    returnType = currentScope.findBaseDataType(visitExpr(ctx.optreturn().expr()));
                }

                if (funcReturnType == DataType.INT && returnType != DataType.INT) {
                    currentScope.addSemanticError(ctx.optreturn().getStart(), "Incompatible return type");
                }
            }
        }

        if (ctx.optprefix() != null) {
            var funcDecl = (FunctionDeclare) currentScope.searchSymbol(ctx.ID().getText(), SymbolType.FUNCTION_DECLARE);
            if (funcDecl != null) {
                var exprList = createExprList(ctx.expr_list());
                if (funcDecl.getParameters().size() != exprList.size()) {
                    currentScope.addSemanticError(ctx.optprefix().getStart(), "Incorrect number of parameters");
                } else {
                    for (var i = 0; i < funcDecl.getParameters().size(); i++) {
                        var funcParam = funcDecl.getParameters().get(i);
                        var declType = currentScope.findBaseDataType(funcParam.getDataType());
                        var expr = exprList.get(i);
                        var exprType = currentScope.findBaseDataType(visitExpr(expr));
                        if (declType == DataType.INT && exprType != DataType.INT) {
                            currentScope.addSemanticError(expr.getStart(), "Incompatible argument");
                        }
                    }
                }
            } else {
                currentScope.addSemanticError(ctx.optprefix().getStart(), "Function undefined");
            }
        }

        if (ctx.FOR() != null || ctx.WHILE() != null) {
            insideLoop = true;
            super.visitStat(ctx);
            insideLoop = false;
        } else {
            super.visitStat(ctx);
        }

        if (ctx.BREAK() != null && !insideLoop) {
            currentScope.addSemanticError(ctx.BREAK().getSymbol(), "break outside loop.");
        }
        return null;
    }

    @Override
    public Object visitFunct(TigerParser.FunctContext ctx) {
        ArrayList<VariableDeclare> paramList = new ArrayList<>();
        for (var p : createParamList(ctx.param_list())) {
            if (paramList.stream().anyMatch(fp -> fp.getSymbolName().equals(p.getSymbolName()))) {
                currentScope.addSemanticError(p.getToken(), "redefine parameter");
            }
            paramList.add(p);
        }
        super.visitFunct(ctx);
        return null;
    }


    @Override
    public DataType visitExpr(TigerParser.ExprContext ctx) {
        if (ctx.const_() != null) {
            if (ctx.const_().INTLIT() != null) {
                return DataType.INT;
            } else {
                return DataType.FLOAT;
            }
        } else if (ctx.value() != null) {
            var varDecl = (VariableDeclare) currentScope.searchSymbol(ctx.value().ID().getText(), SymbolType.VARIABLE_DECLARE);
            if (varDecl == null) {
                currentScope.addSemanticError(ctx.value().getStart(), "Variable undefined");
                return DataType.ANY;
            } else {
                return currentScope.findBaseDataType(varDecl.getDataType());
            }
        } else if (ctx.binary_operator != null) {
            var left = currentScope.findBaseDataType((DataType) super.visit(ctx.expr(0)));
            var right = currentScope.findBaseDataType((DataType) super.visit(ctx.expr(1)));
            switch (ctx.binary_operator.getType()) {
                case TigerLexer.ADD:
                case TigerLexer.SUB:
                case TigerLexer.MUL:
                case TigerLexer.DIV:
                    if (left == DataType.FLOAT || right == DataType.FLOAT) {
                        return DataType.FLOAT;
                    } else {
                        return DataType.INT;
                    }
                case TigerLexer.POW:
                    if (right != DataType.INT) {
                        currentScope.addSemanticError(ctx.expr(0).getStart(), "Invalid right operand, must be an integer.");
                    }
                    return left;
                case TigerLexer.EQ:
                case TigerLexer.NE:
                case TigerLexer.LT:
                case TigerLexer.GT:
                case TigerLexer.LE:
                case TigerLexer.GE:
                case TigerLexer.AND:
                case TigerLexer.OR:
                default:
                    if (left != right) {
                        currentScope.addSemanticError(ctx.expr(0).getStart(), "Binary expression operand mismatch.");
                    }
                    return DataType.INT;
            }
        } else {
            return currentScope.findBaseDataType((DataType) super.visit(ctx.expr(0)));
        }
    }

    private ArrayList<TigerParser.ExprContext> createExprList(TigerParser.Expr_listContext ctx) {
        ArrayList<TigerParser.ExprContext> exprList = new ArrayList<>();
        exprList.add(ctx.expr());
        if (ctx.expr_list_tail() != null) {
            exprList.addAll(createExprList(ctx.expr_list_tail()));
        }
        return exprList;
    }

    private ArrayList<TigerParser.ExprContext> createExprList(TigerParser.Expr_list_tailContext ctx) {
        ArrayList<TigerParser.ExprContext> exprList = new ArrayList<>();
        if (ctx.expr() != null)
            exprList.add(ctx.expr());
        if (ctx.expr_list_tail() != null)
            exprList.addAll(createExprList(ctx.expr_list_tail()));
        return exprList;
    }
}
