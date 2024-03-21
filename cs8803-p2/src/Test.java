import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Test {

    public static void main(String[] args) {

//        System.out.println("------------------------------ SCANNER TESTING ------------------------------");
//        try {
//            Files.list(Paths.get("phase1_testfiles_v1"))
//                    .forEach(f -> {
//                        if (f.toFile().getName().endsWith(".tiger")) {
//                            int exitCode = Main.scanSource(f.toFile().getAbsolutePath());
//                            System.out.println(f.toFile().getAbsolutePath() + " -> " + exitCode);
//                        }
//                    });
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        System.out.println("------------------------------ PARSER TESTING ------------------------------");
//        try {
//            Files.list(Paths.get("phase1_testfiles_v1"))
//                    .forEach(f -> {
//                        if (f.toFile().getName().endsWith(".tiger")) {
//                            int exitCode = Main.parseSource(f.toFile().getAbsolutePath());
//                            System.out.println(f.toFile().getAbsolutePath() + " -> " + exitCode);
//                        }
//                    });
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

//        System.out.println("------------------------------ SYMBOL TABLE TESTING ------------------------------");
//        try {
//            Files.list(Paths.get("phase2_testfiles_v1/symboltable"))
//                    .forEach(f -> {
//                        if (f.toFile().getName().endsWith(".tiger")) {
//                            Main.createSymbolTableFile(f.toFile().getAbsolutePath());
//                        }
//                    });
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        System.out.println("------------------------------ SEMANTIC CHECKING ------------------------------");
        try {
            Files.list(Paths.get("phase2_testfiles_v1/semantic"))
                    .forEach(f -> {
                        if (f.toFile().getName().endsWith(".tiger")) {
                            Main.doSemanticCheck(f.toFile().getAbsolutePath());
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
