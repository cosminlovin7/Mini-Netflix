package commandsolver;

import fileio.UserInputData;
import fileio.Writer;
import fileio.ActionInputData;
import fileio.SerialInputData;
import fileio.MovieInputData;

import java.io.IOException;

public final class SingleCommandRating {

    private static final SingleCommandRating SCR_INSTANCE = new SingleCommandRating();
    private SingleCommandRating() { }
    public static SingleCommandRating getSCRInstance() {
        return SCR_INSTANCE;
    }

    /**
     * Creeaza un singleton pentru clasa SingleCommandRating
     * Prin acesta se apeleaza metoda doSingleCommandRating.
     */

    public Object doSingleCommandRating(final UserInputData currentUser,
                                        final ActionInputData action,
                                        final MovieInputData currentMovie,
                                        final SerialInputData currentSerial,
                                        final Writer fileWriter) throws IOException {

        if (currentUser == null) {
            return null;
        }

        Object jsonWriter = null;
        boolean seen = false;
        boolean isRated = false;

        if (currentUser.getHistory().containsKey(action.getTitle())) {
            seen = true;
        }

        if (action.getSeasonNumber() == 0) { // e film
            if (seen) {
                for (UserInputData.Rating iterator : currentUser.getActivity()) {
                    if (iterator.getName().equalsIgnoreCase(action.getTitle())) {
                        isRated = true;
                        break;
                    }
                }
                if (!isRated) {
                    currentMovie.modifyRating(action.getGrade());
                    currentUser.addRating(action.getTitle(),
                            action.getGrade(),
                            0);
                    jsonWriter = fileWriter.writeFile(action.getActionId(),
                            "message",
                            "success -> "
                                    + action.getTitle()
                                    + " was rated with "
                                    + action.getGrade()
                                    + " by " + action.getUsername());
                } else {
                    jsonWriter = fileWriter.writeFile(action.getActionId(),
                            "message",
                            "error -> "
                                    + action.getTitle()
                                    + " has been already rated");
                }
            } else {
                jsonWriter = fileWriter.writeFile(action.getActionId(),
                        "message",
                        "error -> "
                                + action.getTitle()
                                + " is not seen");
            }
        } else {
            if (seen) {
                for (UserInputData.Rating iterator : currentUser.getActivity()) {
                    if (iterator.getName().equalsIgnoreCase(action.getTitle())
                            && iterator.getSeasonNumber() == action.getSeasonNumber()) {
                        isRated = true;
                        break;
                    }
                }
                if (!isRated) {
                    currentSerial.addRating(action.getSeasonNumber(), action.getGrade());
                    currentUser.addRating(action.getTitle(),
                            action.getGrade(),
                            action.getSeasonNumber());
                    jsonWriter = fileWriter.writeFile(action.getActionId(),
                            "message",
                            "success -> "
                                    + action.getTitle()
                                    + " was rated with "
                                    + action.getGrade()
                                    + " by " + action.getUsername());
                } else {
                    jsonWriter = fileWriter.writeFile(action.getActionId(),
                            "message",
                            "error -> "
                                    + action.getTitle()
                                    + " has been already rated");
                }
            } else {
                jsonWriter = fileWriter.writeFile(action.getActionId(),
                        "message",
                        "error -> "
                                + action.getTitle()
                                + " is not seen");
            }
        }
        return jsonWriter;
    }
}
