package recommendationsolver;

import common.Constants;
import fileio.MovieInputData;
import fileio.OrderedList;
import fileio.UserInputData;
import fileio.Writer;
import fileio.ActionInputData;
import fileio.SerialInputData;
import main.Main;
import sortclasses.SortByRatingAndDb;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class SingleRecommendationBestUnseen {
    private static final SingleRecommendationBestUnseen SRBU_INSTANCE
            = new SingleRecommendationBestUnseen();
    private SingleRecommendationBestUnseen() { }
    public static SingleRecommendationBestUnseen getSRBUInstance() {
        return SRBU_INSTANCE;
    }
    /**
     * Clasa implementeaza un singleton prin intermediul caruia se
     * poate apela metoda doSingleRecommendationBestUnseen.
     * @param movies
     * @param serials
     * @param users
     * @param action
     * @param fileWriter
     * @return
     * @throws IOException
     */
    public Object doSingleRecommendationBestUnseen(final List<MovieInputData> movies,
                                                   final List<SerialInputData> serials,
                                                   final List<UserInputData> users,
                                                   final ActionInputData action,
                                                   final Writer fileWriter) throws IOException {
        Object jsonWriter;
        UserInputData currentUser = Main.getUser(action.getUsername(), users);
        if (currentUser == null) {
            jsonWriter = fileWriter.writeFile(action.getActionId(),
                                              "message",
                                                "BestRatedUnseenRecommendation cannot be applied!");
            return jsonWriter;
        }
        List<OrderedList> brMvsAndSrl = new ArrayList<>();
        boolean canReccAMovie = false;
        boolean canReccASerial = false;
        for (MovieInputData movie : movies) {
            boolean isSeen = false;
            if (currentUser.getHistory().containsKey(movie.getTitle())) {
                isSeen = true;
            }
            if (!isSeen) {
                brMvsAndSrl.add(new OrderedList(movie.getTitle(),
                        movie.getRating(),
                        0,
                        0,
                        0,
                        0,
                        1));
                canReccAMovie = true;
            }
        }
        for (SerialInputData serial : serials) {
            boolean isSeen = false;
            if (currentUser.getHistory().containsKey(serial.getTitle())) {
                isSeen = true;
            }
            if (!isSeen) {
                double serialRating = 0;
                for (int i = 0; i < serial.getNumberSeason(); i++) {
                    if (serial.getRatingPerSeason().get(i) != null) {
                        serialRating += serial.getRatingPerSeason().get(i);
                    }
                }
                serialRating = serialRating / serial.getNumberSeason();
                brMvsAndSrl.add(new OrderedList(serial.getTitle(),
                        serialRating,
                        0,
                        0,
                        0,
                        0,
                        2));
                canReccASerial = true;
            }
        }
        brMvsAndSrl.sort(new SortByRatingAndDb());
        if (canReccAMovie || canReccASerial) {
            jsonWriter = fileWriter.writeFile(action.getActionId(),
                                         "message",
                                      "BestRatedUnseenRecommendation result: "
                                              + brMvsAndSrl.get(Constants.FIRST_ELEMENT));
            return jsonWriter;
        }
        jsonWriter = fileWriter.writeFile(action.getActionId(),
                "message",
                "BestRatedUnseenRecommendation cannot be applied!");

        return jsonWriter;
    }
}
