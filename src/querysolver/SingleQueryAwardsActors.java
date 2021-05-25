package querysolver;

import common.Constants;
import fileio.ActionInputData;
import fileio.ActorInputData;
import fileio.OrderedList;
import fileio.Writer;
import sortclasses.SortByAwards;
import actor.ActorsAwards;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static utils.Utils.stringToAwards;

public final class SingleQueryAwardsActors {

    private static final SingleQueryAwardsActors SQAWA_INSTANCE = new SingleQueryAwardsActors();
    private SingleQueryAwardsActors() { }
    public static SingleQueryAwardsActors getSQAWAInstance() {
        return SQAWA_INSTANCE;
    }

    /**
     * Clasa implementeaza un singleton prin intermediul caruia
     * se apeleaza metoda doSingleQueryAwardsActors.
     * @param actors
     * @param action
     * @param fileWriter
     * @return
     * @throws IOException
     */

    public Object doSingleQueryAwardsActors(final List<ActorInputData> actors,
                                            final ActionInputData action,
                                            final Writer fileWriter) throws IOException {

        Object jsonWriter;
        List<String> awardFilters
                = new ArrayList<>(action.getFilters().get(Constants.AWARDS_FILTER));
        List<OrderedList> orderActors = new ArrayList<>();

        for (ActorInputData actor : actors) {
            boolean ok = true;
            for (String award : awardFilters) {
                if (!actor.getAwards().containsKey(stringToAwards(award))) {
                    ok = false;
                    break;
                }
            }

            if (ok) {
                int totalAwards = 0;
                for (Map.Entry<ActorsAwards, Integer> entry : actor.getAwards().entrySet()) {
                    totalAwards += entry.getValue();
                }
                orderActors.add(new OrderedList(actor.getName(),
                        0,
                        totalAwards,
                        0,
                        0,
                        0,
                        0));
            }
        }

        orderActors.sort(new SortByAwards());
        List<OrderedList> finalOrderedList = new ArrayList<>();
        int index = 0;

        if (action.getSortType().equalsIgnoreCase(Constants.ASC)) {
            for (OrderedList act : orderActors) {
                if (act.getNumberOfAwards() != 0 && index < action.getNumber()) {
                    finalOrderedList.add(act);
                    index++;
                }
            }
        } else if (action.getSortType().equalsIgnoreCase(Constants.DESC)) {
            for (int i = orderActors.size() - 1; i >= 0; i--) {
                if (orderActors.get(i).getNumberOfAwards() != 0 && index < action.getNumber()) {
                    finalOrderedList.add(orderActors.get(i));
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
