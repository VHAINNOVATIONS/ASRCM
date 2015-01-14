package gov.va.med.srcalc.util;

import java.util.*;

/**
 * <p>A Set decorator that does not permit null elements.</p>
 * 
 * <p>After wrapping a Set, do not use the original reference as it provides a
 * back door for adding nulls.</p>
 * 
 * <p>Thanks to <a href="http://stackoverflow.com/a/591322/123205">Stack
 * Overflow</a> for the inspiration.
 */
public class NoNullSet<E> implements Set<E>
{
    private final Set<E> fWrappedSet;
    
    /**
     * Constructs an instance that decorates the given set.
     * @param wrappedSet
     * @throws IllegalArgumentException if the given set contains null.
     */
    protected NoNullSet(final Set<E> wrappedSet)
    {
        if (wrappedSet.contains(null))
        {
            throw new IllegalArgumentException("Null is not permitted in a NoNullSet");
        }

        fWrappedSet = wrappedSet;
    }
    
    /**
     * Constructs an instance that decorates the given set.
     * @param wrappedSet
     * @throws IllegalArgumentException if the given set contains null.
     */
    public static <E> NoNullSet<E> fromSet(final Set<E> wrappedSet)
    {
        return new NoNullSet<>(wrappedSet);
    }

    /**
     * Like {@link Set#add(Object)} but throws {@link IllegalArgumentException}
     * if the argument is null.
     * @throws IllegalArgumentException if the argument is null
     */
    @Override
    public boolean add(final E e)
    {
        if (e == null)
        {
            throw new IllegalArgumentException("Null is not permitted in a NoNullSet");
        }

        return fWrappedSet.add(e);
    }

    /**
     * Like {@link Set#addAll(Collection)} but throws {@link IllegalArgumentException}
     * if the argument contains null.
     * @throws IllegalArgumentException if the argument contains null
     */
    @Override
    public boolean addAll(final Collection<? extends E> c)
    {
        if (c.contains(null))
        {
            throw new IllegalArgumentException("Null is not permitted in a NoNullSet");
        }
        
        return fWrappedSet.addAll(c);
    }

    @Override
    public void clear()
    {
        fWrappedSet.clear();
    }

    @Override
    public boolean contains(final Object o)
    {
        return fWrappedSet.contains(o);
    }

    @Override
    public boolean containsAll(final Collection<?> c)
    {
        return fWrappedSet.containsAll(c);
    }

    @Override
    public boolean isEmpty()
    {
        return fWrappedSet.isEmpty();
    }

    @Override
    public Iterator<E> iterator()
    {
        return fWrappedSet.iterator();
    }

    @Override
    public boolean remove(final Object o)
    {
        return fWrappedSet.remove(o);
    }

    @Override
    public boolean removeAll(final Collection<?> c)
    {
        return fWrappedSet.removeAll(c);
    }

    @Override
    public boolean retainAll(final Collection<?> c)
    {
        return fWrappedSet.retainAll(c);
    }

    @Override
    public int size()
    {
        return fWrappedSet.size();
    }

    @Override
    public Object[] toArray()
    {
        return fWrappedSet.toArray();
    }

    @Override
    public <T> T[] toArray(final T[] a)
    {
        return fWrappedSet.toArray(a);
    }
    
    /**
     * Delegates to the decorated set.
     */
    @Override
    public boolean equals(Object obj)
    {
        return fWrappedSet.equals(obj);
    }
    
    /**
     * Delegates to the decorated set.
     */
    @Override
    public int hashCode()
    {
        return fWrappedSet.hashCode();
    }
    
    /**
     * Delegates to the decorated set.
     */
    @Override
    public String toString()
    {
        return fWrappedSet.toString();
    }
}
