package actiontype;

import commandsolver.SingleCommandFavourite;
import commandsolver.SingleCommandRating;
import commandsolver.SingleCommandView;

import fileio.ActionInputData;
import fileio.MovieInputData;
import fileio.SerialInputData;
import fileio.UserInputData;
import fileio.Writer;

import main.Main;
import org.json.simple.JSONArray;

import java.io.IOException;
import java.util.List;

public final class Command {
    private static final Command INSTANCE = new Command();
    private Command() { }
    public static Command getCommInstance() {
        return INSTANCE;
    }

    /**
     * Creeaza un singleton.
     * Rezolva toate actiunile de tip command.
     */

    public JSONArray doCommand(final List<MovieInputData> movies,
                               final List<SerialInputData> serials,
                               final List<UserInputData> users,
                               final ActionInputData action,
                               final Writer fileWriter) throws IOException {
        JSONArray arrayResult = new JSONArray();
        switch (action.getType()) {
            case "favorite" -> {
                UserInputData currentUser
                        = Main.getUser(action.getUsername(), users);

                SingleCommandFavourite scfInstance
                        = SingleCommandFavourite.getSCFInstance();
                Object jsonWriter
                        = scfInstance.doSingleCommandFavorite(
                        currentUser,
                        action,
                        fileWriter);
                arrayResult.add(jsonWriter);
            }
            case "rating" -> {
                UserInputData currentUser
                        = Main.getUser(action.getUsername(), users);
                MovieInputData currentMovie
                        = Main.getMovie(action.getTitle(), movies);
                SerialInputData currentSerial
                        = Main.getSerial(action.getTitle(), serials);

                SingleCommandRating scrInstance
                        = SingleCommandRating.getSCRInstance();
                Object jsonWriter
                        = scrInstance.doSingleCommandRating(
                        currentUser,
                        action,
                        currentMovie,
                        currentSerial,
                        fileWriter);
                arrayResult.add(jsonWriter);
            }
            case "view" -> {
                UserInputData currentUser
                        = Main.getUser(action.getUsername(), users);

                SingleCommandView scvInstance
                        = SingleCommandView.getSCVInstance();
                Object jsonWriter
                        = scvInstance.doSingleCommandView(
                        currentUser,
                        action,
                        fileWriter);
                arrayResult.add(jsonWriter);
            }
            default -> System.out.println("The command is unknown!");
        }

        return arrayResult;
    }
}
