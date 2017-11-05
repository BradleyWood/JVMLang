package org.toylang.antlr.ast;

import org.toylang.antlr.Modifier;
import org.toylang.antlr.Operator;
import org.toylang.antlr.ToyTree;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class ClassDef extends Statement {

    private final Modifier[] modifiers;
    private final QualifiedName name;
    private final QualifiedName super_;
    private final QualifiedName[] interfaces;
    private QualifiedName package_;
    private final List<Statement> statements;
    private Expression[] superParams = null;
    private List<Constructor> constructors = null;
    private List<VarDecl> varParams = new LinkedList<>();
    private ToyTree sourceTree = null;

    public ClassDef(Modifier[] modifiers, String name, Inheritance inh, List<Statement> statements) {
        this.statements = statements;
        this.modifiers = modifiers;
        this.interfaces = inh.getInterfaces();
        this.superParams = inh.getSuperParams();
        this.name = new QualifiedName(name);
        this.superParams = inh.getSuperParams();
        this.super_ = inh.getSuperClass();
    }

    public ClassDef(Modifier[] modifiers, QualifiedName package_, String name, QualifiedName super_, QualifiedName[] interfaces, List<Statement> statements) {
        this(modifiers, name, new Inheritance(super_, null, interfaces), statements);
        this.package_ = package_;
    }

    public boolean hasVarParams() {
        return varParams.size() > 0;
    }

    public void addVarParam(VarDecl decl) {
        varParams.add(decl);
    }

    public void setVarParams(List<VarDecl> varParams) {
        this.varParams = varParams;
    }

    public List<VarDecl> getVarParams() {
        return varParams;
    }

    public ToyTree getSourceTree() {
        return sourceTree;
    }

    public void setSourceTree(ToyTree sourceTree) {
        this.sourceTree = sourceTree;
    }

    @Override
    public void accept(TreeVisitor visitor) {
        visitor.visitClassDef(this);
    }

    public QualifiedName getPackage() {
        return package_;
    }

    public void setPackage(QualifiedName package_) {
        this.package_ = package_;
    }

    public List<Statement> getStatements() {
        return statements;
    }

    public Modifier[] getModifiers() {
        return modifiers;
    }

    public QualifiedName getName() {
        return name;
    }

    public QualifiedName getSuper() {
        if (sourceTree != null) {
            for (QualifiedName qualifiedName : sourceTree.getImports()) {
                if (qualifiedName.toString().endsWith(super_.toString())) {
                    return qualifiedName;
                }
            }
        }
        return super_;
    }

    public List<Statement> getFields() {
        return statements.stream().filter(stmt -> (stmt instanceof VarDecl)).collect(Collectors.toList());
    }

    public VarDecl findVar(String name) {
        for (Statement statement : getFields()) {
            VarDecl decl = (VarDecl) statement;
            if (decl.getName().toString().equals(name))
                return decl;
        }
        return null;
    }

    public List<Fun> getMethods() {
        List<Fun> methods = new ArrayList<>();
        for (Statement statement : statements) {
            if (statement instanceof Fun) {
                Fun fun = (Fun) statement;
                if (!fun.getName().toString().equals(name.toString()))
                    methods.add(fun);
            }
        }
        return methods;
    }

    public String getFullName() {
        if (package_ == null)
            return name.toString();
        return (package_.toString() + "." + name).replace(".", "/");
    }

    public String getSignature() {
        return "L" + getFullName() + ";";
    }

    public QualifiedName[] getInterfaces() {
        return interfaces;
    }

    public List<Constructor> getConstructors() {
        if (constructors != null)
            return constructors;

        constructors = new ArrayList<>();
        for (Statement stmt : statements) {
            if (stmt instanceof Constructor) {
                constructors.add((Constructor) stmt);
            }
        }
        statements.removeAll(constructors);
        // create a new constructor for the class parameters
        // check if the super class has a default constructor
        // otherwise we need explicit definition of constructor
        boolean autoGenerate = hasVarParams();
        for (Constructor constructor : constructors) {
            if (constructor.getParams().length == varParams.size()) {
                autoGenerate = false;
                break;
            }
        }
        if (autoGenerate && varParams.size() > 0) {
            Constructor con = createConstructor();
            System.out.println(getName() + " : create constructor");
            constructors.add(con);
        }
        return constructors;
    }

    private Constructor createConstructor() {
        List<Statement> fields = getFields();

        VarDecl[] params_ = new VarDecl[fields.size()];
        Block body = new Block();

        for (int i = 0; i < params_.length; i++) {
            QualifiedName funParamName = new QualifiedName(((VarDecl) fields.get(i)).getName().toString() + "_");
            params_[i] = new VarDecl(funParamName, null);
            body.append(new BinOp(((VarDecl) fields.get(i)).getName(), Operator.ASSIGNMENT, funParamName));
        }
        return new Constructor(new Modifier[] {Modifier.PUBLIC}, body, params_);
    }
}