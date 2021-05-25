package sortclasses;

import entertainment.MostPopularGenre;

import java.util.Comparator;


public class SortByTotalViews implements Comparator<MostPopularGenre> {
    /**
     *Sorteaza in functie de numarul total de vizionari. Daca
     * acesta coincide, atunci se sorteaza in functie de nume.
     * @param o1
     * @param o2
     * @return
     */
    @Override
    public int compare(final MostPopularGenre o1, final MostPopularGenre o2) {
        if (o1.getTotalViews() > o2.getTotalViews()) {
            return -1;
        } else if (o1.getTotalViews() < o2.getTotalViews()) {
            return 1;
        }

        int o1Len = o1.getGenreName().length();
        int o2Len = o2.getGenreName().length();
        int minimumLen = Math.min(o1Len, o2Len);

        for (int i = 0; i < minimumLen; i++) {
            int o1Fragment = o1.getGenreName().charAt(i);
            int o2Fragment = o2.getGenreName().charAt(i);

            if (o1Fragment != o2Fragment) {
                return o1Fragment - o2Fragment;
            }
        }
        return 0;
    }
}
