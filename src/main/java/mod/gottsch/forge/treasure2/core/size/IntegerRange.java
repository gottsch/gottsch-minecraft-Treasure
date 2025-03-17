package mod.gottsch.forge.treasure2.core.size;

import java.util.Objects;

/**
 *
 * @author Mark Gottschling on Aug 16, 2024
 * @deprecated use GottschCore 3.0.0+ version.
 *
 */
@Deprecated
public class IntegerRange {
    private int min;
    private int max;

    public IntegerRange() {
    }

    public IntegerRange(IntegerRange q) {
        this.setMin(q.getMin());
        this.setMax(q.getMax());
    }

    public IntegerRange(int min, int max) {
        this.min = min;
        this.max = max;
    }

    public IntegerRange copy() {
        return new IntegerRange(this);
    }

    public double getMinDouble() {
        return (double)this.getMin();
    }

    public double getMaxDouble() {
        return (double)this.getMax();
    }

    public int getMin() {
        return this.min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return this.max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public String toString() {
        return "IntegerRange [min=" + this.min + ", max=" + this.max + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IntegerRange that = (IntegerRange) o;
        return min == that.min && max == that.max;
    }

    @Override
    public int hashCode() {
        return Objects.hash(min, max);
    }
}
