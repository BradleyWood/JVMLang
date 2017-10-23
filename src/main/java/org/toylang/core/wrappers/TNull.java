package org.toylang.core.wrappers;

import org.toylang.core.Hidden;

public class TNull extends TObject {

    @Hidden
    public static final TType TYPE = new TType(TNull.class);

    public static final TNull NULL = new TNull();

    @Hidden
    private TNull() {
    }
    @Override
    public TObject getType() {
        return TYPE;
    }
    @Override
    public boolean isTrue() {
        return super.isTrue();
    }
    @Hidden
    @Override
    public Object toObject() {
        return null;
    }

    @Override
    public int compareTo(TObject o) {
        if(!(o instanceof TNull))
            return 0;
        throw new RuntimeException("Cannot compare null with "+o.getClass().getName());
    }

    @Override
    public TObject EQ(TObject obj) {
        return ((obj == null) || (obj instanceof TNull)) ? TBoolean.TRUE : TBoolean.FALSE;
    }
    @Override
    public TObject NE(TObject obj) {
        return EQ(obj).not();
    }
    @Override
    public String toString() {
        return "null";
    }
    @Hidden
    @Override
    public boolean equals(Object o) {
        return o instanceof TNull;
    }
}