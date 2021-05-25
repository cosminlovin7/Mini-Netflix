package entertainment;

import fileio.OrderedList;

import java.util.ArrayList;
import java.util.List;

public class MostPopularGenre {
    private final String genreName;
    private int totalViews = 0;
    private List<OrderedList> moviesOrSerials = new ArrayList<>();

    public MostPopularGenre(final String genreName) {
        this.genreName = genreName;
    }

    /**
     * Returneaza numele genului.
     */

    public final String getGenreName() {
        return genreName;
    }

    /**
     * Incrementeaza numarul total de views pentru acest gen.
     */

    public void addTotalViews(final int views) {
        this.totalViews += views;
    }

    /**
     * Adauga un videoclip nou (film sau serial) la lista de
     * videoclipuri de acest gen.
     */

    public void addMoviesOrSerials(final String serialName, final int type) {
        moviesOrSerials.add(new OrderedList(serialName,
                0,
                0,
                0,
                0,
                0,
                type));
    }

    /**
     * Returneaza numarul total de views.
     */

    public int getTotalViews() {
        return totalViews;
    }

    /**
     * Adauga o noua lista pentru acest gen.
     */

    public void setNewList(final List<OrderedList> list) {
        this.moviesOrSerials = list;
    }

    /**
     * Returneaza o lista de seriale sau videoclipuri
     * de acest gen.
     */

    public List<OrderedList> getMoviesOrSerialsList() {
        return moviesOrSerials;
    }

    /**
     * Override pentru functia toString().
     */

    @Override
    public String toString() {
        return "MostPopularGenre{"
                + "genreName='" + genreName + '\''
                + ", totalViews=" + totalViews
                + ", movies and serials: " + moviesOrSerials
                + '}';
    }
}
