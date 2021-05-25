package commandsolver;

import fileio.ActionInputData;
import fileio.UserInputData;
import fileio.Writer;

import java.io.IOException;

public final class SingleCommandFavourite {

    private static final SingleCommandFavourite SCF_INSTANCE
            = new SingleCommandFavourite();
    private SingleCommandFavourite() { }
    public static SingleCommandFavourite getSCFInstance() {
        return SCF_INSTANCE;
    }

    /**
     * Creeaza un singleton pentru clasa SingleCommandFavourite
     * Prin acesta se apeleaza metoda doSingleCommandFavorite.
     */

    public Object doSingleCommandFavorite(final UserInputData currentUser,
                                          final ActionInputData action,
                                          final Writer fileWriter) throws IOException {
        if (currentUser == null) {
            return null;
        }

        Object jsonWriter = null;
        boolean favourite = false;
        boolean seen = false;

        for (String movie : currentUser.getFavoriteMovies()) {
            if (movie.equalsIgnoreCase(action.getTitle())) {
                favourite = true;
                break;
            }
        }

        if (currentUser.getHistory().containsKey(action.getTitle())) {
          seen = true;
        }

        if (!favourite && seen) {
          jsonWriter = fileWriter.writeFile(
                  action.getActionId(),
                  "message",
                  "success -> "
                          + action.getTitle()
                          + " was added as favourite");
          currentUser.addFavoriteMovie(action.getTitle());
        } else if (!seen) {
          jsonWriter = fileWriter.writeFile(
                  action.getActionId(),
                  "message",
                  "error -> "
                          + action.getTitle()
                          + " is not seen");
        } else if (favourite) {
          jsonWriter = fileWriter.writeFile(
                  action.getActionId(),
                  "message",
                  "error -> "
                          + action.getTitle()
                          + " is already in favourite list");
        }

        return jsonWriter;
    }
}
