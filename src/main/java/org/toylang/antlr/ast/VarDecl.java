package org.toylang.antlr.ast;

import org.toylang.antlr.Modifier;

public class VarDecl extends Statement {


    private final QualifiedName name;
    private final Expression initialValue;
    private final Modifier[] modifiers;

    public VarDecl(QualifiedName name, Expression initialValue, Modifier... modifiers) {
        this.name = name;
        this.initialValue = initialValue;
        this.modifiers = modifiers;
    }
    public QualifiedName getName() {
        return name;
    }
    public Modifier[] getModifiers() {
        return modifiers;
    }
    public int modifiers() {
        int mod = 0;
        for (Modifier modifier : getModifiers()) {
            mod += modifier.getModifier();
        }
        return mod;
    }
    public Expression getInitialValue() {
        return initialValue;
    }
    @Override
    public void accept(TreeVisitor visitor) {
        visitor.visitVarDecl(this);
    }

    @Override
    public String toString() {
        return "var " + name + " = " + initialValue;
    }
}
