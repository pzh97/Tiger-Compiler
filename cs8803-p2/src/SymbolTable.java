//package p2;
//
//import java.util.ArrayList;
//
//public class SymbolTable {
//
//    private SymbolTable parent;
//
//    private ArrayList<SymbolNode> symbolTable = new ArrayList<>();
//
//    public SymbolTable(SymbolTable parent) {
//        this.parent = parent;
//    }
//
//    public ArrayList<SymbolNode> getSymbolTable() {
//        return symbolTable;
//    }
//
//    public void addSymbolEntry(SymbolNode entry) {
//        symbolTable.add(entry);
//    }
//
//    public SymbolNode findSymbol(String symbolName, SymbolType symbolType) {
//        SymbolTable symbolTable = this;
//        while (symbolTable != null) {
//            SymbolNode entry = findSymbol(symbolTable, symbolName, symbolType);
//            if (entry != null) {
//                return entry;
//            }
//            symbolTable = parent;
//        }
//        return null;
//    }
//
//    public static SymbolNode findSymbol(SymbolTable symbolTable, String symbolName, SymbolType symbolType) {
//        for (SymbolNode entry : symbolTable.symbolTable) {
//            if (symbolName.equals(entry.getSymbolName()) && entry.getSymbolType() == symbolType) {
//                return entry;
//            }
//        }
//        return null;
//    }
//
//}
