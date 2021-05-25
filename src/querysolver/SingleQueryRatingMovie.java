package querysolver;

import common.Constants;
import fileio.ActionInputData;
import fileio.MovieInputData;
import fileio.OrderedList;
import fileio.Writer;
import sortclasses.SortByRating;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class SingleQueryRatingMovie {

    private static final SingleQueryRatingMovie SQRM_INSTANCE
            = new SingleQueryRatingMovie();
    private SingleQueryRatingMovie() { }
    public static SingleQueryRatingMovie getSQRMInstance() {
        return SQRM_INSTANCE;
    }

    /**
     * Clasa care implementeaza un singleton prin intermediul caruia
     * se poate apela metoda doSingleQueryRatingMovie.
     * @param movies
     * @param action
     * @param fileWriter
     * @return
     * @throws IOException
     */

    public Object doSingleQueryRatingMovie(final List<MovieInputData> movies,
                                           final ActionInputData action,
                                           final Writer fileWriter) throws IOException {
        Object jsonWriter;
        List<OrderedList> orderedMovies = new ArrayList<>();
        List<String> yearFilter
                = new ArrayList<>(action.getFilters().get(Constants.YEAR_FILTER));
        List<String> genreFilter
                = new ArrayList<>(action.getFilters().get(Constants.GENRE_FILTER));
        for (MovieInputData movie : movies) {
            if (movie.getRating() != 0
                    && (yearFilter.contains(String.valueOf(movie.getYear()))
                    || yearFilter.get(Constants.FIRST_ELEMENT) == null)
                    && (movie.getGenres().contains(genreFilter.get(Constants.FIRST_ELEMENT))
                    || genreFilter.get(Constants.FIRST_ELEMENT) == null)) {
                orderedMovies.add(new OrderedList(movie.getTitle(),
                        movie.getRating(),
                        0,
                        0,
                        0,
                        0,
                        0));
            }
        }
        orderedMovies.sort(new SortByRating());
        List<OrderedList> finalOrderedList = new ArrayList<>();
        int index = 0;

        if (action.getSortType().equalsIgnoreCase(Constants.ASC)) {
            for (OrderedList mov : orderedMovies) {
                if (index < action.getNumber()) {
                    finalOrderedList.add(mov);
                    index++;
                }
            }
        } else if (action.getSortType().equalsIgnoreCase(Constants.DESC)) {
            for (int i = orderedMovies.size() - 1; i >= 0; i--) {
                if (index < action.getNumber()) {
                    finalOrderedList.add(orderedMovies.get(i));
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
