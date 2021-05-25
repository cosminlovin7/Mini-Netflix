package sortclasses;

import fileio.OrderedList;

import java.util.Comparator;

public class SortByName implements Comparator<OrderedList> {
    /**
     * Se sorteaza dupa nume.
     * @param o1
     * @param o2
     * @return
     */
    @Override
    public int compare(final OrderedList o1, final OrderedList o2) {
        int o1Len = o1.getName().length();
        int o2Len = o2.getName().length();
        int minimumLen = Math.min(o1Len, o2Len);

        for (int i = 0; i < minimumLen; i++) {
            int o1Fragment = o1.getName().charAt(i);
            int o2Fragment = o2.getName().charAt(i);
            if (o1Fragment != o2Fragment) {
                return o1Fragment - o2Fragment;
            }
        }
        return 0;
    }

}
