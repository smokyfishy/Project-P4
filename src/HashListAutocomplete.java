import java.util.*;

public class HashListAutocomplete implements Autocompletor {

    private static final int MAX_PREFIX = 10;
    private Map<String, List<Term>> myMap;
    private int mySize;

    public HashListAutocomplete(String[] terms, double[] weights) {
        if (terms == null || weights == null) {
            throw new NullPointerException("One or more arguments null");
        }
        if (terms.length != weights.length) {
            throw new IllegalArgumentException("terms and weights are not the same length");
        }
        myMap = new HashMap<>();
        initialize(terms, weights);
    }

    @Override
    public List<Term> topMatches(String prefix, int k) {
        if (k < 0) {
            throw new IllegalArgumentException("Illegal value of k:" + k);
        }
        if (prefix.length() > MAX_PREFIX) {
            prefix = prefix.substring(0, MAX_PREFIX);
        }
        List<Term> all = myMap.get(prefix);
        if (all == null) {
            return new ArrayList<>();
        }
        return all.subList(0, Math.min(k, all.size()));
    }

    @Override
    public void initialize(String[] terms, double[] weights) {
        myMap = new HashMap<>();
        mySize = 0;
        for (int i = 0; i < terms.length; i++) {
            if (weights[i] < 0) {
                throw new IllegalArgumentException("Negative weight " + weights[i]);
            }
            String word = terms[i];
            Term t = new Term(word, weights[i]);
            int maxLen = Math.min(word.length(), MAX_PREFIX);
            for (int len = 0; len <= maxLen; len++) {
                String prefix = word.substring(0, len);
                if (!myMap.containsKey(prefix)) {
                    myMap.put(prefix, new ArrayList<>());
                    mySize += BYTES_PER_CHAR * prefix.length();
                }
                myMap.get(prefix).add(t);
                mySize += BYTES_PER_CHAR * word.length() + BYTES_PER_DOUBLE;
            }
        }
        Comparator<Term> byWeight = Comparator.comparing(Term::getWeight).reversed();
        for (List<Term> list : myMap.values()) {
            Collections.sort(list, byWeight);
        }
    }

    @Override
    public int sizeInBytes() {
        if (mySize == 0) {
            for (String key : myMap.keySet()) {
                mySize += BYTES_PER_CHAR * key.length();
                for (Term t : myMap.get(key)) {
                    mySize += BYTES_PER_CHAR * t.getWord().length() + BYTES_PER_DOUBLE;
                }
            }
        }
        return mySize;
    }
}
