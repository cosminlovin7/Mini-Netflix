package actiontype;

import fileio.ActionInputData;
import fileio.UserInputData;
import fileio.Writer;

import org.json.simple.JSONArray;
import querysolver.SingleQueryUsers;

import java.io.IOException;
import java.util.List;

public final class UserQuery {
    private static final UserQuery INSTANCE = new UserQuery();
    private UserQuery() { }
    public static UserQuery getUQInstance() {
        return INSTANCE;
    }
    /**
     *Rezolva toate user query-urile.
     */

    public JSONArray doUserQuery(final List<UserInputData> users,
                                 final ActionInputData action,
                                 final Writer fileWriter) throws IOException {
        JSONArray arrayResult = new JSONArray();
        switch (action.getCriteria()) {
            case "num_ratings" -> {
                SingleQueryUsers squInstance
                        = SingleQueryUsers.getSQUInstance();
                Object jsonWriter
                        = squInstance.doSingleQueryUsers(
                        users,
                        action,
                        fileWriter);
                arrayResult.add(jsonWriter);
            }
            default -> System.out.println("Users Query not found.");
        }

        return arrayResult;
    }
}
