package autocomplete;

import java.util.Objects;

/**
 * This is currently the only implementation of the {@link Term} interface, which is why it's named
 * "default." (Having an interface with a single implementation is a little redundant, but we need
 * it to keep you from accidentally renaming things.)
 * <p>
 * Make sure to check out the interface for method specifications.
 *
 * @see Term
 */
public class DefaultTerm implements Term {

    String query;
    long weight;

    /**
     * Initializes a term with the given query string and weight.
     *
     * @throws IllegalArgumentException if query is null or weight is negative
     */
    public DefaultTerm(String query, long weight) {
        if (query == null || weight < 0) {
            throw new IllegalArgumentException();
        }
        this.query = query;
        this.weight = weight;
    }

    @Override
    public String query() {
        return query;

    }

    @Override
    public String toString() {
        return "DefaultTerm{" +
            "query='" + query + '\'' +
            ", weight=" + weight +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DefaultTerm that = (DefaultTerm) o;
        return weight == that.weight &&
            Objects.equals(query, that.query);
    }

    @Override
    public int hashCode() {
        return Objects.hash(query, weight);
    }

    @Override
    public long weight() {
        return weight;

    }

    @Override
    public int queryOrder(Term that) {
        if (that == null) {
            throw new NullPointerException();
        }
        return this.query.compareTo(that.query());
    }

    @Override
    public int reverseWeightOrder(Term that) {
        if (that == null) {
            throw new NullPointerException();
        }
        return (int) (-1 * (this.weight - that.weight()));
    }

    @Override
    public int matchesPrefix(String prefix) {
        if (this.query.startsWith(prefix)) {
            return 0;
        } else {
            return this.query.compareTo(prefix);
        }
    }
}
