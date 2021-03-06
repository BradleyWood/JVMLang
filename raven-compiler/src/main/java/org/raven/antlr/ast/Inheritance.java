package org.raven.antlr.ast;

import java.util.Arrays;
import java.util.Objects;

public class Inheritance {

    private final QualifiedName super_;
    private final Expression[] superParams;

    private final QualifiedName[] interfaces;

    public Inheritance(final QualifiedName superClass, final Expression[] superParams, final QualifiedName[] interfaces) {
        this.super_ = superClass;
        this.superParams = superParams;
        this.interfaces = interfaces;
    }

    public QualifiedName getSuperClass() {
        return super_;
    }

    public Expression[] getSuperParams() {
        return superParams;
    }

    public QualifiedName[] getInterfaces() {
        return interfaces;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Inheritance that = (Inheritance) o;
        return Objects.equals(super_, that.super_) &&
                Arrays.equals(superParams, that.superParams) &&
                Arrays.equals(interfaces, that.interfaces);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(super_);
        result = 31 * result + Arrays.hashCode(superParams);
        result = 31 * result + Arrays.hashCode(interfaces);
        return result;
    }
}
