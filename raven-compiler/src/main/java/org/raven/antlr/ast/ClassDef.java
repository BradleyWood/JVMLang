package org.raven.antlr.ast;

import org.raven.antlr.Modifier;
import org.raven.antlr.Operator;
import org.raven.antlr.RavenTree;
import org.raven.core.TLFile;

import java.util.*;

public class ClassDef extends ModifiableStatement {

    private final QualifiedName[] interfaces;
    private final List<Statement> statements;
    private final QualifiedName name;
    private QualifiedName super_;
    private QualifiedName package_;
    private Expression[] superParams = null;
    private List<Constructor> constructors = null;
    private List<VarDecl> varParams = new LinkedList<>();
    private RavenTree sourceTree = null;

    private List<Fun> methods = null;

    public ClassDef(final Modifier[] modifiers, final String name, final Inheritance inh, final List<Statement> statements) {
        super(modifiers);
        this.statements = statements;
        this.superParams = inh.getSuperParams();
        this.name = new QualifiedName(name);
        this.superParams = inh.getSuperParams();
        this.super_ = inh.getSuperClass();
        this.interfaces = inh.getInterfaces();
    }

    public ClassDef(final Modifier[] modifiers, final QualifiedName package_, final String name, final QualifiedName super_, final QualifiedName[] interfaces, final List<Statement> statements) {
        this(modifiers, name, new Inheritance(super_, null, interfaces), statements);
        this.package_ = package_;
    }

    public boolean hasVarParams() {
        return varParams.size() > 0;
    }

    public void addVarParam(final VarDecl decl) {
        varParams.add(decl);
    }

    public void setVarParams(final List<VarDecl> varParams) {
        this.varParams = varParams;
    }

    public List<VarDecl> getVarParams() {
        return varParams;
    }

    public RavenTree getSourceTree() {
        return sourceTree;
    }

    public void setSourceTree(final RavenTree sourceTree) {
        this.sourceTree = sourceTree;
        this.super_ = getName(super_);
        for (int i = 0; i < this.interfaces.length; i++) {
            this.interfaces[i] = getName(this.interfaces[i]);
        }
    }

    public boolean isInterface() {
        return this instanceof Interface;
    }

    /**
     * Checks if the class def contains a method with a given name and number of params
     *
     * @param name       The method name
     * @param paramCount The the number of params
     * @return True if the def contains the method
     */
    public boolean containsMethod(final String name, final int paramCount) {
        for (Fun fun : getMethods()) {
            if (fun.getName().toString().equals(name) && fun.getParams().length == paramCount)
                return true;
        }
        return false;
    }

    /**
     * Checks if this def contains the method with a given name and descriptor
     *
     * @param name The method name
     * @param desc The method descriptor
     * @return True if this class def contains a method with the given name and descriptor
     */
    public boolean containsExact(final String name, final String desc) {
        for (Fun fun : getMethods()) {
            if (fun.getName().toString().equals(name) && fun.getDesc().equals(desc))
                return true;
        }
        return false;
    }

    @Override
    public void accept(final TreeVisitor visitor) {
        visitor.visitClassDef(this);
    }

    public QualifiedName getPackage() {
        return package_;
    }

    public void setPackage(final QualifiedName package_) {
        this.package_ = package_;
    }

    public List<Statement> getStatements() {
        return statements;
    }

    public QualifiedName getName() {
        return name;
    }

    public QualifiedName getSuper() {
        return super_;
    }

    private QualifiedName getName(final QualifiedName name) {
        if (sourceTree != null) {
            for (QualifiedName qualifiedName : sourceTree.getImports()) {
                if (qualifiedName.toString().endsWith(name.toString())) {
                    return qualifiedName;
                }
            }
        }
        return name;
    }

    public boolean hasTlSuper() {
        try {
            Class c = Class.forName(getSuper().toString());
            return c.getAnnotation(TLFile.class) != null;
        } catch (ClassNotFoundException e) {
            return true;
        }
    }

    public List<VarDecl> getFields() {
        List<VarDecl> fields = new LinkedList<>();
        for (Statement statement : statements) {
            if (statement instanceof VarDecl) {
                fields.add((VarDecl) statement);
            }
        }
        fields.addAll(varParams);
        return fields;
    }

    public VarDecl findVar(final String name) {
        for (Statement statement : getFields()) {
            VarDecl decl = (VarDecl) statement;
            if (decl.getName().toString().equals(name))
                return decl;
        }
        return null;
    }

    public Fun findFun(final String name, final int paramCount) {
        for (Fun fun : getMethods()) {
            if (fun.getName().toString().equals(name) && fun.getParams().length == paramCount) {
                return fun;
            }
        }
        return null;
    }

    public List<Fun> getMethods() {
        if (methods != null)
            return methods;
        List<Fun> methods = new ArrayList<>();
        for (Statement statement : statements) {
            if (statement instanceof Fun) {
                Fun fun = (Fun) statement;
                if (!fun.getName().toString().equals(name.toString()))
                    methods.add(fun);
            }
        }
        methods.addAll(createGetters());
        methods.addAll(createSetters());
        return this.methods = methods;
    }

    public String getFullName() {
        if (package_ == null || package_.equals(new QualifiedName()))
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
                initFieldsInConstructor((Constructor) stmt);
                constructors.add((Constructor) stmt);
            }
        }
        statements.removeAll(constructors);

        if (superParams != null && superParams.length > 0) {
            Constructor con = new Constructor(superParams);
            initFieldsInConstructor(con);
            con.setParent(this);
            constructors.add(con);
            varParams.clear();
            return constructors;
        }
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
            con.setParent(this);
            initFieldsInConstructor(con);
            constructors.add(con);
        }

        if (constructors.isEmpty()) {
            Constructor defaultConstructor = new Constructor(new Modifier[]{Modifier.PUBLIC}, new Block());
            defaultConstructor.setParent(this);
            initFieldsInConstructor(defaultConstructor);
            constructors.add(defaultConstructor);
        }

        return constructors;
    }

    private void initFieldsInConstructor(final Constructor c) {
        for (Statement statement : statements) {
            if (statement instanceof VarDecl) {
                VarDecl decl = (VarDecl) statement;
                if (!decl.hasModifier(Modifier.STATIC))
                    c.initializeVar((VarDecl) statement);
            }
        }
    }

    private Constructor createConstructor() {
        List<VarDecl> fields = getVarParams();

        VarDecl[] params_ = new VarDecl[fields.size()];
        Block body = new Block();

        for (int i = 0; i < params_.length; i++) {
            QualifiedName funParamName = new QualifiedName(fields.get(i).getName().toString() + "_");
            params_[i] = new VarDecl(funParamName, null);
            body.append(new BinOp(fields.get(i).getName(), Operator.ASSIGNMENT, funParamName));
        }
        return new Constructor(new Modifier[]{Modifier.PUBLIC}, body, params_);
    }

    private List<Fun> createGetters() {
        LinkedList<Fun> getters = new LinkedList<>();
        for (VarDecl decl : getVarParams()) {
            Block block = new Block();
            block.append(new Return(decl.getName()));
            Fun f = new Fun(new QualifiedName("get" + decl.getName()), block, new Modifier[]{Modifier.PUBLIC}, null);
            getters.add(f);
        }
        return getters;
    }

    private List<Fun> createSetters() {
        LinkedList<Fun> setters = new LinkedList<>();
        for (VarDecl decl : getVarParams()) {
            Block block = new Block();
            VarDecl param = new VarDecl(new QualifiedName(decl.getName().toString() + "_"), null);
            block.append(new BinOp(decl.getName(), Operator.ASSIGNMENT, param.getName()));
            Fun f = new Fun(new QualifiedName("set" + decl.getName()), block, new Modifier[]{Modifier.PUBLIC}, null, param);
            setters.add(f);
        }
        return setters;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClassDef classDef = (ClassDef) o;
        return Objects.equals(getModifiers(), classDef.getModifiers()) &&
                Objects.equals(name, classDef.name) &&
                Objects.equals(super_, classDef.super_) &&
                Arrays.equals(interfaces, classDef.interfaces) &&
                Objects.equals(package_, classDef.package_) &&
                Objects.equals(statements, classDef.statements) &&
                Arrays.equals(superParams, classDef.superParams) &&
                Objects.equals(constructors, classDef.constructors) &&
                Objects.equals(varParams, classDef.varParams) &&
                Objects.equals(sourceTree, classDef.sourceTree) &&
                Objects.equals(methods, classDef.methods) &&
                Objects.equals(getAnnotations(), classDef.getAnnotations());
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(name, super_, package_, statements, constructors, varParams, sourceTree, methods);
        result = 31 * result + Objects.hashCode(getModifiers());
        result = 31 * result + Arrays.hashCode(interfaces);
        result = 31 * result + Arrays.hashCode(superParams);
        return result;
    }
}