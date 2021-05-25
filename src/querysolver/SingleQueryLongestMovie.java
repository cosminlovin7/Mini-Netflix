package querysolver;

import common.Constants;
import fileio.ActionInputData;
import fileio.MovieInputData;
import fileio.OrderedList;
import fileio.Writer;
import sortclasses.SortByDuration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class SingleQueryLongestMovie {

    private static final SingleQueryLongestMovie SQLM_INSTANCE = new SingleQueryLongestMovie();
    private SingleQueryLongestMovie() { }
    public static SingleQueryLongestMovie getSQLMInstance() {
        return SQLM_INSTANCE;
    }

    /**
     *Clasa ce genereaza un singleton prin intermediul caruia se
     * apeleaza metoda doSingleQueryLongestMovies
     * @param movies
     * @param action
     * @param fileWriter
     * @return
     * @throws IOException
     */
    public Object doSingleQueryLongestMovie(final List<MovieInputData> movies,
                                            final ActionInputData action,
                                            final Writer fileWriter) throws IOException {

        Object jsonWriter;
        List<String> yearFilter = new ArrayList<>(action.getFilters().get(Constants.YEAR_FILTER));
        List<String> genreFilter = new ArrayList<>(action.getFilters().get(Constants.GENRE_FILTER));
        List<OrderedList> orderLongestMovies = new ArrayList<>();


        for (MovieInputData movie : movies) {
            if ((yearFilter.contains(String.valueOf(movie.getYear()))
                    || yearFilter.get(Constants.FIRST_ELEMENT) == null)
                    && (movie.getGenres().contains(genreFilter.get(Constants.FIRST_ELEMENT))
                    || genreFilter.get(Constants.FIRST_ELEMENT) == null)) {
                orderLongestMovies.add(new OrderedList(movie.getTitle(),
                        0,
                        0,
                        0,
                        movie.getDuration(),
                        0,
                        0));
            }
        }
        orderLongestMovies.sort(new SortByDuration());
        List<OrderedList> finalOrderedList = new ArrayList<>();
        int index = 0;

        if (action.getSortType().equalsIgnoreCase(Constants.ASC)) {
            for (OrderedList mov : orderLongestMovies) {
                if (index < action.getNumber()) {
                    finalOrderedList.add(mov);
                    index++;
                }
            }
        } else if (action.getSortType().equalsIgnoreCase(Constants.DESC)) {
            for (int i = orderLongestMovies.size() - 1; i >= 0; i--) {
                if (index < action.getNumber()) {
                    finalOrderedList.add(orderLongestMovies.get(i));
                    index++;
                }
            }
        }

        jsonWriter = fileWriter.writeFile(action.getActionId(),
                "message",
                "Query result: "
                        + finalOrderedList.toString());

        return jsonWriter;
    }
}
