package recommendationsolver;

import fileio.MovieInputData;
import fileio.SerialInputData;
import fileio.UserInputData;
import fileio.ActionInputData;
import fileio.Writer;
import main.Main;

import java.io.IOException;
import java.util.List;

public final class SingleRecommendationStandard {

    private static final SingleRecommendationStandard SRS_INSTANCE
            = new SingleRecommendationStandard();
    private SingleRecommendationStandard() { }
    public static SingleRecommendationStandard getSRSInstance() {
        return SRS_INSTANCE;
    }
    /**
     * Clasa implementeaza un singleton prin intermediul caruia se poate
     * apela metoda doSingleRecommendationStandard.
     * @param movies
     * @param serials
     * @param users
     * @param action
     * @param fileWriter
     * @return
     * @throws IOException
     */
    public Object doSingleRecommendationStandard(final List<MovieInputData> movies,
                                                 final List<SerialInputData> serials,
                                                 final List<UserInputData> users,
                                                 final ActionInputData action,
                                                 final Writer fileWriter) throws IOException {
        Object jsonWriter = null;
        UserInputData currentUser = Main.getUser(action.getUsername(), users);
        if (currentUser == null) {
            jsonWriter = fileWriter.writeFile(action.getActionId(),
                    "message",
                    "StandardRecommendation cannot be applied!");
            return jsonWriter;
        }
        boolean foundMovie = false;
        boolean foundSerial = false;
        for (MovieInputData movie : movies) {
            if (!currentUser.getHistory().containsKey(movie.getTitle())) {
                jsonWriter = fileWriter.writeFile(action.getActionId(),
                        "message",
                        "StandardRecommendation result: "
                        + movie.getTitle());
                foundMovie = true;
                break;
            }
        }
        if (!foundMovie) {
            for (SerialInputData serial : serials) {
                if (!currentUser.getHistory().containsKey(serial.getTitle())) {
                    jsonWriter = fileWriter.writeFile(action.getActionId(),
                            "message",
                            "StandardRecommendation result: "
                                    + serial.getTitle());
                    foundSerial = true;
                    break;
                }
            }
        }
        if (!foundMovie && !foundSerial) {
            jsonWriter = fileWriter.writeFile(action.getActionId(),
                    "message",
                    "StandardRecommendation cannot be applied!");
        }
        return jsonWriter;
    }
}
