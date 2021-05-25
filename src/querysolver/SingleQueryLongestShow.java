package querysolver;

import common.Constants;
import entertainment.Season;
import fileio.SerialInputData;
import fileio.ActionInputData;
import fileio.Writer;
import fileio.OrderedList;
import sortclasses.SortByDuration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class SingleQueryLongestShow {

    private static final SingleQueryLongestShow SQLS_INSTANCE = new SingleQueryLongestShow();
    private SingleQueryLongestShow() { }
    public static SingleQueryLongestShow getSQLSInstance() {
        return SQLS_INSTANCE;
    }

    /**
     * Clasa implementeaza un singleton prin intermediul caruia
     * se poate apela metoda do SingleQueryLongestShow.
     * @param serials
     * @param action
     * @param fileWriter
     * @return
     * @throws IOException
     */

    public Object doSingleQueryLongestShow(final List<SerialInputData> serials,
                                           final ActionInputData action,
                                           final Writer fileWriter) throws IOException {

        Object jsonWriter;
        List<String> yearFilter = new ArrayList<>(action.getFilters().get(Constants.YEAR_FILTER));
        List<String> genreFilter = new ArrayList<>(action.getFilters().get(Constants.GENRE_FILTER));
        List<OrderedList> orderLongestSerials = new ArrayList<>();

        for (SerialInputData serial : serials) {
            if ((yearFilter.contains(String.valueOf(serial.getYear()))
                    || yearFilter.get(Constants.FIRST_ELEMENT) == null)
                    && (serial.getGenres().contains(genreFilter.get(Constants.FIRST_ELEMENT))
                    || genreFilter.get(Constants.FIRST_ELEMENT) == null)) {
                int totalDuration = 0;
                for (Season season : serial.getSeasons()) {
                    totalDuration += season.getDuration();
                }
                orderLongestSerials.add(new OrderedList(serial.getTitle(),
                        0,
                        0,
                        0,
                        totalDuration,
                        0,
                        0));
            }
        }
        orderLongestSerials.sort(new SortByDuration());
        List<OrderedList> finalOrderedList = new ArrayList<>();
        int index = 0;

        if (action.getSortType().equalsIgnoreCase(Constants.ASC)) {
            for (OrderedList srl : orderLongestSerials) {
                if (index < action.getNumber()) {
                    finalOrderedList.add(srl);
                    index++;
                }
            }
        } else if (action.getSortType().equalsIgnoreCase(Constants.DESC)) {
            for (int i = orderLongestSerials.size() - 1; i >= 0; i--) {
                if (index < action.getNumber()) {
                    finalOrderedList.add(orderLongestSerials.get(i));
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
