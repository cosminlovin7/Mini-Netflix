package querysolver;

import common.Constants;
import fileio.ActionInputData;
import fileio.ActorInputData;
import fileio.OrderedList;
import fileio.Writer;
import sortclasses.SortByName;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class SingleQueryFilterDescriptionActors {

    private static final SingleQueryFilterDescriptionActors SQFDA_INSTANCE
            = new SingleQueryFilterDescriptionActors();
    private SingleQueryFilterDescriptionActors() { }
    public static SingleQueryFilterDescriptionActors getSQFDAInstance() {
        return SQFDA_INSTANCE;
    }

    /**
     * Clasa implementeaza un singleton prin intermediul caruia se poate
     * apela metoda doSingleQueryFilterDescriptionActors
     * @param actors
     * @param action
     * @param fileWriter
     * @return
     * @throws IOException
     */

    public Object doSingleQueryFilterDescriptionActors(final List<ActorInputData> actors,
                                                       final ActionInputData action,
                                                       final Writer fileWriter) throws IOException {
        Object jsonWriter;
        List<String> wordFilters = new ArrayList<>(action.getFilters().get(Constants.WORDS_FILTER));
        List<OrderedList> orderedActors = new ArrayList<>();

        for (ActorInputData actor : actors) {
            boolean contains = true;
            for (String word : wordFilters) {
                String description = actor.getCareerDescription().toLowerCase();
                String[] descriptionWords = description.split(" |,|[.]|\"|-|:|\n");
                boolean containsWord = false;
                for (String w : descriptionWords) {
                    if (w.equalsIgnoreCase(word)) {
                        containsWord = true;
                        break;
                    }
                }
                if (!containsWord) {
                    contains = false;
                    break;
                }
            }
            if (contains) {
                orderedActors.add(new OrderedList(actor.getName(),
                        0,
                        0,
                        0,
                        0,
                        0,
                        0));
            }
        }

        orderedActors.sort(new SortByName());
        List<OrderedList> finalOrderedList = new ArrayList<>();
        int index = 0;

        if (action.getSortType().equalsIgnoreCase(Constants.ASC)) {
            for (OrderedList act : orderedActors) {
                if (index < action.getNumber()) {
                    finalOrderedList.add(act);
                    index++;
                }
            }
        } else if (action.getSortType().equalsIgnoreCase(Constants.DESC)) {
            for (int i = orderedActors.size() - 1; i >= 0; i--) {
                if (index < action.getNumber()) {
                    finalOrderedList.add(orderedActors.get(i));
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
