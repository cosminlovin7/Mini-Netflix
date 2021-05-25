package querysolver;

import common.Constants;
import fileio.MovieInputData;
import fileio.UserInputData;
import fileio.ActionInputData;
import fileio.Writer;
import fileio.OrderedList;
import sortclasses.SortByViews;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class SingleQueryMostViewedMovie {

    private static final SingleQueryMostViewedMovie SQMVM_INSTANCE
            = new SingleQueryMostViewedMovie();
    private SingleQueryMostViewedMovie() { }
    public static SingleQueryMostViewedMovie getSQMVMInstance() {
        return SQMVM_INSTANCE;
    }

    /**
     * Clasa implementeaza un singleton prin intermediul caruia
     * se apeleaza metoda doSingleQueryMostViewedMovie.
     * @param movies
     * @param users
     * @param action
     * @param fileWriter
     * @return
     * @throws IOException
     */

    public Object doSingleQueryMostViewedMovie(final List<MovieInputData> movies,
                                               final List<UserInputData> users,
                                               final ActionInputData action,
                                               final Writer fileWriter) throws IOException {
        Object jsonWriter;
        List<String> yearFilter
                = new ArrayList<>(action.getFilters().get(Constants.YEAR_FILTER));
        List<String> genreFilter
                = new ArrayList<>(action.getFilters().get(Constants.GENRE_FILTER));
        List<OrderedList> filteredMovies = new ArrayList<>();

        for (MovieInputData movie : movies) {
            if ((yearFilter.contains(String.valueOf(movie.getYear()))
                    || yearFilter.get(Constants.FIRST_ELEMENT) == null)
                    && (movie.getGenres().contains(genreFilter.get(Constants.FIRST_ELEMENT))
                    || genreFilter.get(Constants.FIRST_ELEMENT) == null)) {
                filteredMovies.add(new OrderedList(movie.getTitle(),
                        0,
                        0,
                        0,
                        0,
                        0,
                        0));
            }
        }

        for (UserInputData user : users) {
            for (OrderedList movie : filteredMovies) {
                if (user.getHistory().containsKey(movie.getName())) {
                    movie.addNumberOfViews(user.getHistory().get(movie.getName()));
                }
            }
        }

        filteredMovies.sort(new SortByViews());
        List<OrderedList> finalOrderedList = new ArrayList<>();
        int index = 0;

        if (action.getSortType().equalsIgnoreCase(Constants.ASC)) {
            for (OrderedList mov : filteredMovies) {
                if (mov.getNumberOfViews() != 0 && index < action.getNumber()) {
                    finalOrderedList.add(mov);
                    index++;
                }
            }
        } else if (action.getSortType().equalsIgnoreCase(Constants.DESC)) {
            for (int i = filteredMovies.size() - 1; i >= 0; i--) {
                if (filteredMovies.get(i).getNumberOfViews() != 0
                        && index < action.getNumber()) {
                    finalOrderedList.add(filteredMovies.get(i));
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
