import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class Main {

    public static void main(String[] args) {

        String sourceFile = null;
        boolean parseSource = false;
        boolean scanSource = false;
        boolean outputSymbolTable = false;

        for (var i = 0; i < args.length; i++) {
            if (args[i].equals("-f")) {
                sourceFile = args[i + 1];
                i = i + 1;
            } else if (args[i].equals("-s")) {
                scanSource = true;
            } else if (args[i].equals("-t")) {
                outputSymbolTable = true;
            } else if (args[i].equals("-p")) {
                parseSource = true;
            } else {
                System.exit(1);
                return;
            }
        }

        if (args.length == 0 || sourceFile == null || !Files.exists(Paths.get(sourceFile))) {
            System.exit(1);
            return;
        }

        int exitCode = 0;
        if (scanSource) {
            exitCode = scanSource(sourceFile);
            if (exitCode != 0) {
                System.exit(exitCode);
            }
        }

        if (parseSource) {
            exitCode = parseSource(sourceFile);
        }

        if (outputSymbolTable) {
            createSymbolTableFile(sourceFile);
        }

        exitCode = doSemanticCheck(sourceFile);

        System.exit(exitCode);
    }

    public static int scanSource(String sourceFile) {
        String tokenStreamFile = sourceFile.substring(0, sourceFile.lastIndexOf('.')) + ".tokens";
        try {
            var lexer = createLexer(sourceFile);
            try (var out = new PrintWriter(Files.newOutputStream(Paths.get(tokenStreamFile)))) {
                while (true) {
                    var token = lexer.nextToken();
                    if (token.getType() == Token.EOF) break;
                    out.println("<" + TigerLexer.VOCABULARY.getSymbolicName(token.getType()) + ", \"" + token.getText() + "\">");
                }
            }

            //check lex error
            if (((SyntaxErrorListener) lexer.getErrorListeners().get(0)).getSyntaxErrors().size() > 0) {
                return 2;
            }
            return 0;
        } catch (IOException ex) {
            ex.printStackTrace();
            return 1;
        }
    }

    public static int parseSource(String sourceFile) {
        String gvFilename = sourceFile.substring(0, sourceFile.lastIndexOf('.')) + ".tree.gv";
        try {
            var lexer = createLexer(sourceFile);
            var parseErrorListener = new SyntaxErrorListener();
            var parser = new TigerParser(new CommonTokenStream(lexer));
            parser.removeErrorListeners();
            parser.addErrorListener(parseErrorListener);

            var listener = new GraphVizDOTFormatVisitor();
            var dotSource = listener.visitTiger_program(parser.tiger_program());
            //check lex error
            if (((SyntaxErrorListener) lexer.getErrorListeners().get(0)).getSyntaxErrors().size() > 0) {
                return 2;
            }
            //check parse error
            if (parseErrorListener.getSyntaxErrors().size() > 0) {
                return 3;
            }
            Files.writeString(Paths.get(gvFilename), dotSource, StandardOpenOption.CREATE);
            return 0;
        } catch (IOException ex) {
            ex.printStackTrace();
            return 1;
        }
    }

    public static String createSymbolTableFile(String sourceFile) {
        String filename = sourceFile.substring(0, sourceFile.lastIndexOf('.')) + ".st";
        try {
            var charStream = CharStreams.fromString(Files.readString(Paths.get(sourceFile)));
            var lexer = new TigerLexer(charStream);
            var parser = new TigerParser(new CommonTokenStream(lexer));
            var listener = new SymbolNodeVisitor();
            var symbolNode = (ScopeNode) listener.visitTiger_program(parser.tiger_program());
            var symbolTableText = new SymbolTableFormatter().format(symbolNode);
            Files.writeString(Paths.get(filename), symbolTableText, StandardOpenOption.CREATE);
            return filename;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static int doSemanticCheck(String sourceFile) {
        String filename = sourceFile.substring(0, sourceFile.lastIndexOf('.')) + ".st";
        try {
            var charStream = CharStreams.fromString(Files.readString(Paths.get(sourceFile)));
            var lexer = new TigerLexer(charStream);
            var parser = new TigerParser(new CommonTokenStream(lexer));
            var listener = new SemanticCheckVisitor();
            var symbolNode = (ScopeNode) listener.visitTiger_program(parser.tiger_program());
            var semanticErrors = new SemanticErrorCollector().getSemanticErrors(symbolNode);
            System.err.println(sourceFile);
            System.err.println(String.join(System.lineSeparator(), semanticErrors));
            if(semanticErrors.size() > 0) {
                return 4;
            } else {
                return 0;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            return 1;
        }
    }

    private static TigerLexer createLexer(String sourceFile) throws IOException {
        var charStream = CharStreams.fromString(Files.readString(Paths.get(sourceFile)));
        var lexErrorListener = new SyntaxErrorListener();
        var lexer = new TigerLexer(charStream);
        lexer.removeErrorListeners();
        lexer.addErrorListener(lexErrorListener);
        return lexer;
    }

}
