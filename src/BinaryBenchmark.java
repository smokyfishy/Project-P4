import java.util.*;

public class BinaryBenchmark {
	private static String[] ourStrings = {
        "apple", "banana", "cherry", "lemon",
        "mango", "orange", "papaya", "quince",
        "strawberry", "watermelon"
    };
    private static Random ourRandom = new Random();

    public ArrayList<String> createList(int n) {
        ArrayList<String> list = new ArrayList<>();
        for(String str : ourStrings) {
            for(int j=0; j < n; j++){
                list.add(str);
            }
        }
        return list;
    }

	public int standard(List<String> list, String target,Comparator<String> comp){
        int index = Collections.binarySearch(list, target,comp);
        return index;
    }

	public List<String> naiveFirstK(List<String> list, int k){
		Collections.sort(list);
		return new ArrayList<String>(list.subList(0, k));
	}
	public List<String> pqFirstK(List<String> list, int k) {
		PriorityQueue<String> pq = new PriorityQueue<>();
		for(String s : list) {
		    pq.add(s);
			if (pq.size() > k) {
				pq.remove();
			}
		}
		LinkedList<String> ret = new LinkedList<>();
		for(int j=0; j < k; j++) {
			ret.addFirst(pq.remove());
		}
		return ret;
	}

	public int bsearch(List<String> list, String target, Comparator<String> comp){
        int low = 0;
        int high = list.size() - 1;
        while (low <= high) {
            int mid = (high-low)/2 + low; // (low+high)/2
            int value = comp.compare(target,list.get(mid));
            if (value < 0) {
                high = mid-1;
            }
            else if (value > 0) {
                low = mid + 1;
            }
            else {
                return mid;
            }
        }
        return -1;
    }

	public int firstMatch(List<String> list, String target, Comparator<String> comp) {
        int low = -1;
        int high = list.size()-1;
        while (low+1 != high) {
            int mid = (high-low)/2 + low;
            int value = comp.compare(target,list.get(mid));
            if (value <= 0) {
                high = mid;
            }
            else {
                low = mid;
            }
        }
        if (high < 0 || high >= list.size()){
            return -1;
        }
        if (list.get(high).compareTo(target) == 0){
            return high;
        }
        return -1;
	}
	public void firstResults() {
		String target = "apple";
		CountedComparator<String> comp = new CountedComparator<>();
        System.out.printf("\tsize\tsdex\tscomps\tfdex\tfcomps\n");
        for(int k = 10; k <= 20; k++) {
			int size = 1 << k;						
			ArrayList<String> list = createList(size);
			

            int slowdex = BinarySearchLibrary.firstIndexSlow(list,target,comp);
            int slowcount = comp.getCount();
			comp.reset();
			int fdex = BinarySearchLibrary.firstIndex(list, target, comp);
			int fcomp = comp.getCount();
			System.out.printf("%8d\t%d\t%d\t%d\t%d\n",
							  size,slowdex,slowcount,fdex,fcomp);
		}
	}

    public void lastResults() {
		String target = "apple";
		CountedComparator<String> comp = new CountedComparator<>();
        System.out.printf("\tsize\tsdex\tscomps\tfdex\tfcomps\n");
        for(int k = 10; k <= 20; k++) {
			int size = 1 << k;						
			ArrayList<String> list = createList(size);
			
            int slowdex = BinarySearchLibrary.lastIndexSlow(list,target,comp);
            int slowcount = comp.getCount();
			comp.reset();
			int fdex = BinarySearchLibrary.lastIndex(list, target, comp);
			int fcomp = comp.getCount();
			System.out.printf("%8d\t%d\t%d\t%d\t%d\n",
							  size,slowdex,slowcount,fdex,fcomp);
		}
	}


	public static void main(String[] args) {
		BinaryBenchmark bbm = new BinaryBenchmark();
		bbm.firstResults();
        bbm.lastResults();
	}
}
