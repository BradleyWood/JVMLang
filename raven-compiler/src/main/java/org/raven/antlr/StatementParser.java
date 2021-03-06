package org.raven.antlr;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ConsoleErrorListener;
import org.raven.antlr.ast.Statement;
import org.raven.antlr.visitor.RavenFileVisitor;

import java.util.LinkedList;
import java.util.List;

public class StatementParser {

    public static RavenTree parseStatements(final String line) {
        RavenLexer lexer = new RavenLexer(CharStreams.fromString(line));
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        org.raven.antlr.RavenParser parser = new org.raven.antlr.RavenParser(tokenStream);
        parser.removeErrorListener(ConsoleErrorListener.INSTANCE);
        parser.addErrorListener(new RavenErrorListener("<stdin>"));

        RavenFileVisitor tfv = new RavenFileVisitor();

        RavenParser.RavenFileContext ctx = parser.ravenFile();

        if (parser.getNumberOfSyntaxErrors() > 0) {
            return null;
        }

        return tfv.visitRavenFile(ctx);
    }
}
