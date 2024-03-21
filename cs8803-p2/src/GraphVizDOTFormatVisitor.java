import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.Stack;

public class GraphVizDOTFormatVisitor extends TigerBaseVisitor<String> {

    private int nodeCounter = 0;
    private StringBuilder buffer = new StringBuilder();
    private Stack<String> nodeStack = new Stack<>();

    @Override
    public String visitTerminal(TerminalNode node) {
        createNode(node.getSymbol(), nodeStack.peek());
        return null;
    }

    @Override
    public String visitTiger_program(TigerParser.Tiger_programContext ctx) {
        buffer.append("digraph G {")
                .append(System.lineSeparator());
        nodeStack.push(createRuleNode(ctx.getRuleIndex(), null));
        super.visitTiger_program(ctx);
        nodeStack.pop();
        buffer.append("}");
        return buffer.toString();
    }

    @Override
    public String visitDeclaration_segment(TigerParser.Declaration_segmentContext ctx) {
        nodeStack.push(createRuleNode(ctx.getRuleIndex(), nodeStack.peek()));
        super.visitDeclaration_segment(ctx);
        nodeStack.pop();
        return null;
    }

    @Override
    public String visitType_declaration_list(TigerParser.Type_declaration_listContext ctx) {
        nodeStack.push(createRuleNode(ctx.getRuleIndex(), nodeStack.peek()));
        super.visitType_declaration_list(ctx);
        nodeStack.pop();
        return null;
    }

    @Override
    public String visitVar_declaration_list(TigerParser.Var_declaration_listContext ctx) {
        nodeStack.push(createRuleNode(ctx.getRuleIndex(), nodeStack.peek()));
        super.visitVar_declaration_list(ctx);
        nodeStack.pop();
        return null;
    }

    @Override
    public String visitFunct_list(TigerParser.Funct_listContext ctx) {
        nodeStack.push(createRuleNode(ctx.getRuleIndex(), nodeStack.peek()));
        super.visitFunct_list(ctx);
        nodeStack.pop();
        return null;
    }

    @Override
    public String visitType_declaration(TigerParser.Type_declarationContext ctx) {
        nodeStack.push(createRuleNode(ctx.getRuleIndex(), nodeStack.peek()));
        super.visitType_declaration(ctx);
        nodeStack.pop();
        return null;
    }

    @Override
    public String visitType(TigerParser.TypeContext ctx) {
        nodeStack.push(createRuleNode(ctx.getRuleIndex(), nodeStack.peek()));
        super.visitType(ctx);
        nodeStack.pop();
        return null;
    }

    @Override
    public String visitBase_type(TigerParser.Base_typeContext ctx) {
        nodeStack.push(createRuleNode(ctx.getRuleIndex(), nodeStack.peek()));
        super.visitBase_type(ctx);
        nodeStack.pop();
        return null;
    }

    @Override
    public String visitVar_declaration(TigerParser.Var_declarationContext ctx) {
        nodeStack.push(createRuleNode(ctx.getRuleIndex(), nodeStack.peek()));
        super.visitVar_declaration(ctx);
        nodeStack.pop();
        return null;
    }

    @Override
    public String visitStorage_class(TigerParser.Storage_classContext ctx) {
        nodeStack.push(createRuleNode(ctx.getRuleIndex(), nodeStack.peek()));
        super.visitStorage_class(ctx);
        nodeStack.pop();
        return null;
    }

    @Override
    public String visitId_list(TigerParser.Id_listContext ctx) {
        nodeStack.push(createRuleNode(ctx.getRuleIndex(), nodeStack.peek()));
        super.visitId_list(ctx);
        nodeStack.pop();
        return null;
    }

    @Override
    public String visitOptional_init(TigerParser.Optional_initContext ctx) {
        nodeStack.push(createRuleNode(ctx.getRuleIndex(), nodeStack.peek()));
        super.visitOptional_init(ctx);
        nodeStack.pop();
        return null;
    }

    @Override
    public String visitFunct(TigerParser.FunctContext ctx) {
        nodeStack.push(createRuleNode(ctx.getRuleIndex(), nodeStack.peek()));
        super.visitFunct(ctx);
        nodeStack.pop();
        return null;
    }

    @Override
    public String visitParam_list(TigerParser.Param_listContext ctx) {
        nodeStack.push(createRuleNode(ctx.getRuleIndex(), nodeStack.peek()));
        super.visitParam_list(ctx);
        nodeStack.pop();
        return null;
    }

    @Override
    public String visitParam_list_tail(TigerParser.Param_list_tailContext ctx) {
        nodeStack.push(createRuleNode(ctx.getRuleIndex(), nodeStack.peek()));
        super.visitParam_list_tail(ctx);
        nodeStack.pop();
        return null;
    }

    @Override
    public String visitRet_type(TigerParser.Ret_typeContext ctx) {
        nodeStack.push(createRuleNode(ctx.getRuleIndex(), nodeStack.peek()));
        super.visitRet_type(ctx);
        nodeStack.pop();
        return null;
    }

    @Override
    public String visitParam(TigerParser.ParamContext ctx) {
        nodeStack.push(createRuleNode(ctx.getRuleIndex(), nodeStack.peek()));
        super.visitParam(ctx);
        nodeStack.pop();
        return null;
    }

    @Override
    public String visitStat_seq(TigerParser.Stat_seqContext ctx) {
        nodeStack.push(createRuleNode(ctx.getRuleIndex(), nodeStack.peek()));
        super.visitStat_seq(ctx);
        nodeStack.pop();
        return null;
    }

    @Override
    public String visitStat(TigerParser.StatContext ctx) {
        nodeStack.push(createRuleNode(ctx.getRuleIndex(), nodeStack.peek()));
        super.visitStat(ctx);
        nodeStack.pop();
        return null;
    }

    @Override
    public String visitOptreturn(TigerParser.OptreturnContext ctx) {
        nodeStack.push(createRuleNode(ctx.getRuleIndex(), nodeStack.peek()));
        super.visitOptreturn(ctx);
        nodeStack.pop();
        return null;
    }

    @Override
    public String visitOptprefix(TigerParser.OptprefixContext ctx) {
        nodeStack.push(createRuleNode(ctx.getRuleIndex(), nodeStack.peek()));
        super.visitOptprefix(ctx);
        nodeStack.pop();
        return null;
    }

    @Override
    public String visitExpr(TigerParser.ExprContext ctx) {
        nodeStack.push(createRuleNode(ctx.getRuleIndex(), nodeStack.peek()));
        super.visitExpr(ctx);
        nodeStack.pop();
        return null;
    }

    @Override
    public String visitConst(TigerParser.ConstContext ctx) {
        nodeStack.push(createRuleNode(ctx.getRuleIndex(), nodeStack.peek()));
        super.visitConst(ctx);
        nodeStack.pop();
        return null;
    }

    @Override
    public String visitExpr_list(TigerParser.Expr_listContext ctx) {
        nodeStack.push(createRuleNode(ctx.getRuleIndex(), nodeStack.peek()));
        super.visitExpr_list(ctx);
        nodeStack.pop();
        return null;
    }

    @Override
    public String visitExpr_list_tail(TigerParser.Expr_list_tailContext ctx) {
        nodeStack.push(createRuleNode(ctx.getRuleIndex(), nodeStack.peek()));
        super.visitExpr_list_tail(ctx);
        nodeStack.pop();
        return null;
    }

    @Override
    public String visitValue(TigerParser.ValueContext ctx) {
        nodeStack.push(createRuleNode(ctx.getRuleIndex(), nodeStack.peek()));
        super.visitValue(ctx);
        nodeStack.pop();
        return null;
    }

    @Override
    public String visitValue_tail(TigerParser.Value_tailContext ctx) {
        nodeStack.push(createRuleNode(ctx.getRuleIndex(), nodeStack.peek()));
        super.visitValue_tail(ctx);
        nodeStack.pop();
        return null;
    }

    private String createNode(String label) {
        nodeCounter = nodeCounter + 1;
        var nodeId = "n" + nodeCounter;
        buffer.append(nodeId + " [label=\"" + label + "\"]");
        buffer.append(System.lineSeparator());
        return nodeId;
    }

    private void connectNode(String parentNodeId, String childNodeId) {
        buffer.append(parentNodeId + " -> " + childNodeId);
        buffer.append(System.lineSeparator());
    }

    private String createRuleNode(int ruleIndex, String parentNodeId) {
        var childNodeId = createNode(TigerParser.ruleNames[ruleIndex]);
        if (parentNodeId != null) {
            connectNode(parentNodeId, childNodeId);
        }
        return childNodeId;
    }

    private String createNode(Token token, String parentNodeId) {
        var label = TigerLexer.VOCABULARY.getSymbolicName(token.getType());
        if (token.getType() == TigerLexer.ID) {
            label = label + ":" + token.getText();
        }
        var childNodeId = createNode(label);
        if (parentNodeId != null) {
            connectNode(parentNodeId, childNodeId);
        }
        return childNodeId;
    }

}
