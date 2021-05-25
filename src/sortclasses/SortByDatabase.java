package sortclasses;

import fileio.OrderedList;

import java.util.Comparator;

public class SortByDatabase implements Comparator<OrderedList> {
    /**
     * Sorteaza o lista dupa numarul de vizionari.
     * Daca se intampla ca doua videoclipuri sa aiba
     * acelasi numar de vizionari, se sorteaza dupa
     * tipul videoclipului(filmele primele, apoi serialele).
     * @param o1
     * @param o2
     * @return
     */
    @Override
    public int compare(final OrderedList o1, final OrderedList o2) {
        if (o1.getNumberOfViews() > o2.getNumberOfViews()) {
            return -1;
        } else if (o1.getNumberOfViews() < o2.getNumberOfViews()) {
            return 1;
        }

        return o1.getType() - o2.getType();
    }
}
