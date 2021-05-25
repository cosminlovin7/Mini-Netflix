package recommendationsolver;

import common.Constants;
import entertainment.Genre;
import entertainment.MostPopularGenre;
import fileio.MovieInputData;
import fileio.SerialInputData;
import fileio.UserInputData;
import fileio.Writer;
import fileio.ActionInputData;
import fileio.OrderedList;
import main.Main;
import sortclasses.SortByDatabase;
import sortclasses.SortByTotalViews;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class SingleRecommendationPopular {
    private static final SingleRecommendationPopular SRP_INSTANCE
            = new SingleRecommendationPopular();
    private SingleRecommendationPopular() { }
    public static SingleRecommendationPopular getSRPInstance() {
        return SRP_INSTANCE;
    }
    /**
     * Clasa implementeaza un singleton prin intermediul caruia se
     * poate apela metoda doSingleRecommendationPopular.
     * @param movies
     * @param serials
     * @param users
     * @param action
     * @param fileWriter
     * @return
     * @throws IOException
     */

    public Object doSingleRecommendationPopular(final List<MovieInputData> movies,
                                                final List<SerialInputData> serials,
                                                final List<UserInputData> users,
                                                final ActionInputData action,
                                                final Writer fileWriter) throws IOException {
        Object jsonWriter;
        List<MostPopularGenre> genresList = new ArrayList<>();
        UserInputData currentUser = Main.getUser(action.getUsername(), users);

        if (currentUser == null
            || currentUser.getSubscriptionType().equalsIgnoreCase(Constants.BASIC)) {
            jsonWriter = fileWriter.writeFile(action.getActionId(),
                    "message",
                    "PopularRecommendation cannot be applied!");
            return jsonWriter;
        }
        for (Genre genre : Genre.values()) {
            for (MovieInputData movie : movies) {
                for (String movieGenre : movie.getGenres()) {
                    if (movieGenre.equalsIgnoreCase(String.valueOf(genre))) {
                        boolean contains = false;
                        for (MostPopularGenre genreElement : genresList) {
                            if (genreElement.getGenreName().equalsIgnoreCase(movieGenre)) {
                                contains = true;
                                genreElement.addMoviesOrSerials(movie.getTitle(), 1);
                                int numberOfViews = 0;
                                for (UserInputData user : users) {
                                    if (user.getHistory().containsKey(movie.getTitle())) {
                                        numberOfViews +=
                                                user.getHistory().get(movie.getTitle());
                                    }
                                }
                                genreElement.addTotalViews(numberOfViews);
                            }
                        }
                        if (!contains) {
                            genresList.add(new MostPopularGenre(movieGenre));
                            for (MostPopularGenre genreElement : genresList) {
                                if (genreElement.getGenreName().equalsIgnoreCase(movieGenre)) {
                                    genreElement.addMoviesOrSerials(movie.getTitle(), 1);
                                    int numberOfViews = 0;
                                    for (UserInputData user : users) {
                                        if (user.getHistory().containsKey(movie.getTitle())) {
                                            numberOfViews +=
                                                    user.getHistory().get(movie.getTitle());
                                        }
                                    }
                                    genreElement.addTotalViews(numberOfViews);
                                }
                            }
                        }
                    }
                }
            }
            for (SerialInputData serial : serials) {
                for (String serialGenre : serial.getGenres()) {
                    if (serialGenre.equalsIgnoreCase(String.valueOf(genre))) {
                        boolean contains = false;
                        for (MostPopularGenre genreElement : genresList) {
                            if (genreElement.getGenreName().equalsIgnoreCase(serialGenre)) {
                                contains = true;
                                genreElement.addMoviesOrSerials(serial.getTitle(), 2);
                                int numberOfViews = 0;
                                for (UserInputData user : users) {
                                    if (user.getHistory().containsKey(serial.getTitle())) {
                                        numberOfViews
                                                += user.getHistory().get(serial.getTitle());
                                    }
                                }
                                genreElement.addTotalViews(numberOfViews);
                            }
                        }
                        if (!contains) {
                            genresList.add(new MostPopularGenre(serialGenre));
                            for (MostPopularGenre genreElement : genresList) {
                                if (genreElement.getGenreName().equalsIgnoreCase(serialGenre)) {
                                    genreElement.addMoviesOrSerials(serial.getTitle(), 2);
                                    int numberOfViews = 0;
                                    for (UserInputData user : users) {
                                        if (user.getHistory().containsKey(serial.getTitle())) {
                                            numberOfViews
                                                    += user.getHistory().get(serial.getTitle());
                                        }
                                    }
                                    genreElement.addTotalViews(numberOfViews);
                                }
                            }
                        }
                    }
                }
            }
        }

        genresList.sort(new SortByTotalViews());
        for (MostPopularGenre genreElement : genresList) {
            List<OrderedList> listToOrder = genreElement.getMoviesOrSerialsList();
            listToOrder.sort(new SortByDatabase());
            genreElement.setNewList(listToOrder);
        }
        for (MostPopularGenre genreElement : genresList) {
            List<OrderedList> currentList = genreElement.getMoviesOrSerialsList();
            for (OrderedList movieOrSerial : currentList) {
                if (!currentUser.getHistory().containsKey(movieOrSerial.getName())) {
                    jsonWriter = fileWriter.writeFile(action.getActionId(),
                            "message",
                            "PopularRecommendation result: " + movieOrSerial.getName());
                    return jsonWriter;
                }
            }
        }

        jsonWriter = fileWriter.writeFile(action.getActionId(),
                    "message",
                    "PopularRecommendation cannot be applied!");

        return jsonWriter;
    }
}
