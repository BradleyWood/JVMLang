package org.raven.antlr.ast;

import org.raven.antlr.Node;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * A block of code that contains a set of statements
 * Should have its own scope
 */
public class Block extends Statement {

    private final LinkedList<Statement> statements = new LinkedList<>();

    /**
     * Initializes a block of code
     *
     * @param statements The list of statements in the block
     */
    public Block(final Statement... statements) {
        for (Statement statement : statements) {
            append(statement);
        }
    }

    /**
     * Append a statement to the end of this block
     *
     * @param statement The statement or expression
     */
    public void append(final Statement statement) {
        statements.add(statement);
    }

    /**
     * Add a statement to the beginning of this block
     *
     * @param statement The statement or expression
     */
    public void addBefore(final Statement statement) {
        statements.addFirst(statement);
    }

    /**
     * Get the list of statements in this block
     *
     * @return A list of statements
     */
    public List<Statement> getStatements() {
        return statements;
    }

    @Override
    public void accept(final TreeVisitor visitor) {
        visitor.visitBlock(this);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        statements.forEach(stmt -> builder.append(stmt.toString()).append(System.lineSeparator()));
        return "{" + System.lineSeparator() + builder.toString() + "}";
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Block block = (Block) o;
        return Objects.equals(statements, block.statements);
    }

    @Override
    public void setParent(final Node parent) {
        super.setParent(parent);
        statements.forEach(stmt -> stmt.setParent(parent));
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), statements);
    }
}
