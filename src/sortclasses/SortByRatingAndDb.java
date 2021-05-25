package sortclasses;

import fileio.OrderedList;

import java.util.Comparator;

public class SortByRatingAndDb implements Comparator<OrderedList> {
    /**
     *Sorteaza in functie de rating si de pozitia in database.
     * @param o1
     * @param o2
     * @return
     */
    @Override
    public int compare(final OrderedList o1, final OrderedList o2) {
        if (o1.getRating() > o2.getRating()) {
            return -1;
        } else if (o1.getRating() < o2.getRating()) {
            return 1;
        }

        return o1.getType() - o2.getType();
    }
}
