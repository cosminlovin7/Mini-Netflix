package commandsolver;

import fileio.ActionInputData;
import fileio.UserInputData;
import fileio.Writer;

import java.io.IOException;

public final class SingleCommandView {

    private static final SingleCommandView SCV_INSTANCE = new SingleCommandView();
    private SingleCommandView() { }
    public static SingleCommandView getSCVInstance() {
        return SCV_INSTANCE;
    }

    /**
     * Creeaza un singleton pentru clasa SingleCommandView
     * Prin acesta se apeleaza metoda doSingleCommandView.
     */

    public Object doSingleCommandView(final UserInputData currentUser,
                                      final ActionInputData action,
                                      final Writer fileWriter) throws IOException {
        if (currentUser == null) {
            return null;
        }

        boolean seen = false;

        if (currentUser.getHistory().containsKey(action.getTitle())) {
            seen = true;
        }

        if (seen) {
            currentUser.incrementView(action.getTitle());
        } else {
            currentUser.addVideoToHistory(action.getTitle());
        }

        Object jsonWriter = fileWriter.writeFile(action.getActionId(),
                "message",
                "success -> "
                        + action.getTitle()
                        + " was viewed with total views of "
                        + currentUser.getHistory().get(action.getTitle()));

        return jsonWriter;
    }
}
