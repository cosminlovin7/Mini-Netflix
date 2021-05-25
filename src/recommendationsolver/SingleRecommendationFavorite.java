package recommendationsolver;

import common.Constants;
import fileio.MovieInputData;
import fileio.SerialInputData;
import fileio.UserInputData;
import fileio.ActionInputData;
import fileio.Writer;
import fileio.OrderedList;
import main.Main;
import sortclasses.SortByDatabase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class SingleRecommendationFavorite {
    private static final SingleRecommendationFavorite SRF_INSTANCE
            = new SingleRecommendationFavorite();
    private SingleRecommendationFavorite() { }
    public static SingleRecommendationFavorite getSRFInstance() {
        return SRF_INSTANCE;
    }
    /**
     * Clasa implementeaza un singleton prin intermediul caruia se poate
     * apela metoda doSingleRecommendationFavorite.
     * @param movies
     * @param serials
     * @param users
     * @param action
     * @param fileWriter
     * @return
     * @throws IOException
     */
    public Object doSingleRecommendationFavorite(final List<MovieInputData> movies,
                                                 final List<SerialInputData> serials,
                                                 final List<UserInputData> users,
                                                 final ActionInputData action,
                                                 final Writer fileWriter) throws IOException {
        Object jsonWriter = null;
        UserInputData currentUser = Main.getUser(action.getUsername(), users);
        List<OrderedList> favMoviesAndSerials = new ArrayList<>();

        if (currentUser == null
                || currentUser.getSubscriptionType().equalsIgnoreCase(Constants.BASIC)) {
            jsonWriter = fileWriter.writeFile(action.getActionId(),
                    "message",
                    "FavoriteRecommendation cannot be applied!");
            return jsonWriter;
        }

        for (MovieInputData movie : movies) {
            for (UserInputData user : users) {
                if (user.getFavoriteMovies().contains(movie.getTitle())) {
                    boolean isInFavMoviesList = false;
                    for (OrderedList currFavMovie : favMoviesAndSerials) {
                        if (currFavMovie.getName().equalsIgnoreCase(movie.getTitle())) {
                            currFavMovie.addNumberOfViews(1);
                            isInFavMoviesList = true;
                            break;
                        }
                    }
                    if (!isInFavMoviesList) {
                        favMoviesAndSerials.add(new OrderedList(movie.getTitle(),
                                0,
                                0,
                                0,
                                0,
                                1,
                                Constants.MOVIE));
                    }
                }
            }
        }
        for (SerialInputData serial : serials) {
            for (UserInputData user : users) {
                if (user.getFavoriteMovies().contains(serial.getTitle())) {
                    boolean isInFavSerialList = false;
                    for (OrderedList currFavSerial : favMoviesAndSerials) {
                        if (currFavSerial.getName().equalsIgnoreCase(serial.getTitle())) {
                            currFavSerial.addNumberOfViews(1);
                            isInFavSerialList = true;
                            break;
                        }
                    }
                    if (!isInFavSerialList) {
                        favMoviesAndSerials.add(new OrderedList(serial.getTitle(),
                                0,
                                0,
                                0,
                                0,
                                1,
                                Constants.SERIAL));
                    }
                }
            }
        }
        favMoviesAndSerials.sort(new SortByDatabase());
        boolean foundRec = false;
        for (OrderedList movieOrSerial : favMoviesAndSerials) {
            if (!currentUser.getHistory().containsKey(movieOrSerial.getName())
            && !currentUser.getFavoriteMovies().contains(movieOrSerial.getName())) {
                jsonWriter = fileWriter.writeFile(action.getActionId(),
                        "message",
                        "FavoriteRecommendation result: "
                        + movieOrSerial.getName());
                foundRec = true;
                break;
            }
        }
        if (!foundRec) {
            jsonWriter = fileWriter.writeFile(action.getActionId(),
                    "message",
                    "FavoriteRecommendation cannot be applied!");
        }

        return jsonWriter;
    }
}
