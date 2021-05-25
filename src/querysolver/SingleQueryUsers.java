package querysolver;

import common.Constants;
import fileio.ActionInputData;
import fileio.OrderedList;
import fileio.UserInputData;
import fileio.Writer;
import sortclasses.SortByNumberOfRatings;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class SingleQueryUsers {

    private static final SingleQueryUsers SQU_INSTANCE = new SingleQueryUsers();
    private SingleQueryUsers() { }
    public static SingleQueryUsers getSQUInstance() {
        return SQU_INSTANCE;
    }

    /**
     * Clasa implementeaza un singleton prin intermediul caruia
     * se poate apela metoda doSingleQueryUsers.
     * @param users
     * @param action
     * @param fileWriter
     * @return
     * @throws IOException
     */

    public Object doSingleQueryUsers(final List<UserInputData> users,
                                     final ActionInputData action,
                                     final Writer fileWriter) throws IOException {
        Object jsonWriter;
        List<OrderedList> orderedUsers = new ArrayList<>();

        for (UserInputData user : users) {
            if (user.getActivity().size() != 0) {
                orderedUsers.add(new OrderedList(user.getUsername(),
                        0,
                        0,
                        user.getActivity().size(),
                        0,
                        0,
                        0));
            }
        }
        orderedUsers.sort(new SortByNumberOfRatings());
        List<OrderedList> finalOrderedList = new ArrayList<>();
        int index = 0;

        if (action.getSortType().equalsIgnoreCase(Constants.ASC)) {
            for (OrderedList usr : orderedUsers) {
                if (index < action.getNumber()) {
                    finalOrderedList.add(usr);
                    index++;
                }
            }
        } else if (action.getSortType().equalsIgnoreCase(Constants.DESC)) {
            for (int i = orderedUsers.size() - 1; i >= 0; i--) {
                if (index < action.getNumber()) {
                    finalOrderedList.add(orderedUsers.get(i));
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
