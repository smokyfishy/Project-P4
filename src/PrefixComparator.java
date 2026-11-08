import java.util.Comparator;

/**
 * Factory pattern for obtaining PrefixComparator objects
 * without calling new. Users simply use
 *
 *     Comparator<Term> comp = PrefixComparator.getComparator(size)
 *
 * @author owen astrachan
 * @date October 8, 2020
 * @date October 2025
 */
public class    PrefixComparator implements Comparator<Term> {

    private int myPrefixSize; // size of prefix

    /**
     * private constructor, called by getComparator
     * @param prefix is prefix used in compare method
     */
    private PrefixComparator(int prefix) {
        myPrefixSize = prefix;
    }


    /**
     * Factory method to return a PrefixComparator object
     * @param prefix is the size of the prefix to compare with
     * @return PrefixComparator that uses prefix
     */
    public static PrefixComparator getComparator(int prefix) {
        return new PrefixComparator(prefix);
    }


    @Override
    /**
     * Use at most myPrefixSize characters from each of v and w
     * to return a value comparing v and w by words (.getWord()). 
     * Comparisons should be made based on the first 
     * (at most) myPrefixSize chars in v and w.
     * @return < 0 if v < w, == 0 if v == w, and > 0 if v > w
     */
    public int compare(Term v, Term w) {
        String a = v.getWord();
        String b = w.getWord();
        int limit = Math.min(myPrefixSize, Math.min(a.length(), b.length()));
        for (int i = 0; i < limit; i++) {
            char ca = a.charAt(i);
            char cb = b.charAt(i);
            if (ca != cb) {
                return ca - cb;
            }
        }
        // all compared chars equal: shorter word (below prefix size) is smaller
        int la = Math.min(a.length(), myPrefixSize);
        int lb = Math.min(b.length(), myPrefixSize);
        return la - lb;
    }
}
