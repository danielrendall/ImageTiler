package uk.co.danielrendall.imagetiler.annotations;

/**
 * @author Daniel Rendall
 */
public interface FieldVisitor {
    
    abstract void visit(BooleanField bField);
    abstract void visit(DoubleField dField);
    abstract void visit(IntegerField iField);
    abstract void visit(StringField sField);
}
