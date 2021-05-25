package querysolver;

import common.Constants;
import fileio.MovieInputData;
import fileio.ActionInputData;
import fileio.Writer;
import fileio.OrderedList;
import fileio.UserInputData;
import sortclasses.SortByViews;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class SingleQueryFavoriteMovie {

    private static final SingleQueryFavoriteMovie SQFM_INSTANCE = new SingleQueryFavoriteMovie();
    private SingleQueryFavoriteMovie() { }
    public static SingleQueryFavoriteMovie getSQFMInstance() {
        return SQFM_INSTANCE;
    }

    /**
     * Clasa implementeaza un singleton prin intermediul caruia se poate
     * apela metoda doSingleQueryFavoriteMovie.
     * @param movies
     * @param action
     * @param users
     * @param fileWriter
     * @return
     * @throws IOException
     */

    public Object doSingleQueryFavoriteMovie(final List<MovieInputData> movies,
                                             final ActionInputData action,
                                             final List<UserInputData> users,
                                             final Writer fileWriter) throws IOException {

        Object jsonWriter;
        List<String> yearFilter = new ArrayList<>(action.getFilters().get(Constants.YEAR_FILTER));
        List<String> genreFilter = new ArrayList<>(action.getFilters().get(Constants.GENRE_FILTER));

        List<OrderedList> orderFavouriteMovies = new ArrayList<>();

        for (MovieInputData movie : movies) {
            if ((yearFilter.contains(String.valueOf(movie.getYear()))
                || yearFilter.get(Constants.FIRST_ELEMENT) == null)
                && (movie.getGenres().contains(genreFilter.get(Constants.FIRST_ELEMENT))
                || genreFilter.get(Constants.FIRST_ELEMENT) == null)) {
                orderFavouriteMovies.add(new OrderedList(movie.getTitle(),
                        0,
                        0,
                        0,
                        0,
                        0,
                        0));
            }
        }

        for (UserInputData user : users) {
            for (OrderedList movie : orderFavouriteMovies) {
                if (user.getFavoriteMovies().contains(movie.getName())) {
                    movie.addNumberOfViews(1);
                }
            }
        }

        orderFavouriteMovies.sort(new SortByViews());
        List<OrderedList> finalOrderedList = new ArrayList<>();
        int index = 0;

        if (action.getSortType().equalsIgnoreCase(Constants.ASC)) {
            for (OrderedList mov : orderFavouriteMovies) {
                if (mov.getNumberOfViews() != 0 && index < action.getNumber()) {
                    finalOrderedList.add(mov);
                    index++;
                }
            }
        } else if (action.getSortType().equalsIgnoreCase(Constants.DESC)) {
            for (int i = orderFavouriteMovies.size() - 1; i >= 0; i--) {
                if (orderFavouriteMovies.get(i).getNumberOfViews() != 0
                        && index < action.getNumber()) {
                    finalOrderedList.add(orderFavouriteMovies.get(i));
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
