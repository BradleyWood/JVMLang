package org.raven.core.wrappers;

import org.raven.core.Hidden;

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
    public int compareTo(final TObject o) {
        if (!(o instanceof TNull))
            return 0;
        throw new RuntimeException("Cannot compare null with " + o.getClass().getName());
    }

    @Override
    public Object coerce(final Class clazz) {
        return toObject();
    }

    @Override
    public int coerceRating(final Class clazz) {
        if (clazz.isPrimitive())
            return COERCE_IMPOSSIBLE;
        return COERCE_IDEAL;
    }

    @Override
    public TObject EQ(final TObject obj) {
        return ((obj == null) || (obj instanceof TNull)) ? TBoolean.TRUE : TBoolean.FALSE;
    }

    @Override
    public TObject NE(final TObject obj) {
        return EQ(obj).not();
    }

    @Override
    public String toString() {
        return "null";
    }

    @Hidden
    @Override
    public boolean equals(final Object o) {
        return o == NULL;
    }
}
