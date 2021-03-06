package org.raven.antlr.ast;

import org.raven.core.wrappers.TObject;

import java.util.Objects;

/**
 * Holds a literal value such as a string, number, boolean value, null etc
 */
public class Literal extends Expression {

    private final TObject value;

    /**
     * Initializes a literal with a value
     * @param value The literal value
     */
    public Literal(final TObject value) {
        this.value = value;
    }

    /**
     * Get the literal value
     * @return The value as a wrapped object
     */
    public TObject getValue() {
        return value;
    }

    @Override
    public void accept(final TreeVisitor visitor) {
        visitor.visitLiteral(this);
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Literal literal = (Literal) o;
        return Objects.equals(value, literal.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
