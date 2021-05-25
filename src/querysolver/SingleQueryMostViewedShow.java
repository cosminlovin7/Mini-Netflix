package querysolver;

import common.Constants;
import fileio.SerialInputData;
import fileio.ActionInputData;
import fileio.Writer;
import fileio.UserInputData;
import fileio.OrderedList;
import sortclasses.SortByViews;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class SingleQueryMostViewedShow {

    private static final SingleQueryMostViewedShow SQMVS_INSTANCE
            = new SingleQueryMostViewedShow();
    private SingleQueryMostViewedShow() { }
    public static SingleQueryMostViewedShow getSQMVSInstance() {
        return SQMVS_INSTANCE;
    }

    /**
     * Clasa care implementeaza un singleton prin intermediul caruia
     * se poate apela metoda doSingleQueryMostViewedShow.
     * @param serials
     * @param action
     * @param users
     * @param fileWriter
     * @return
     * @throws IOException
     */

    public Object doSingleQueryMostViewedShow(final List<SerialInputData> serials,
                                                   final ActionInputData action,
                                                   final List<UserInputData> users,
                                                   final Writer fileWriter) throws IOException {

        Object jsonWriter = null;
        List<String> yearFilter
                = new ArrayList<>(action.getFilters().get(Constants.YEAR_FILTER));
        List<String> genreFilter
                = new ArrayList<>(action.getFilters().get(Constants.GENRE_FILTER));
        List<OrderedList> orderMostViewedSerials = new ArrayList<>();

        for (SerialInputData serial : serials) {
            if ((yearFilter.contains(String.valueOf(serial.getYear()))
                    || yearFilter.get(Constants.FIRST_ELEMENT) == null)
                    && (serial.getGenres().contains(genreFilter.get(Constants.FIRST_ELEMENT))
                    || genreFilter.get(Constants.FIRST_ELEMENT) == null)) {
                orderMostViewedSerials.add(new OrderedList(serial.getTitle(),
                    0,
                    0,
                    0,
                    0,
                    0,
                        0));
          }
        }

        for (UserInputData user : users) {
            for (OrderedList serial : orderMostViewedSerials) {
                if (user.getHistory().containsKey(serial.getName())) {
                    serial.addNumberOfViews(user.getHistory().get(serial.getName()));
                }
            }
        }
        orderMostViewedSerials.sort(new SortByViews());
        List<OrderedList> finalOrderedList = new ArrayList<>();
        int index = 0;

        if (action.getSortType().equalsIgnoreCase(Constants.ASC)) {
            for (OrderedList ser : orderMostViewedSerials) {
                if (ser.getNumberOfViews() != 0 && index < action.getNumber()) {
                    finalOrderedList.add(ser);
                    index++;
                }
            }
        } else if (action.getSortType().equalsIgnoreCase(Constants.DESC)) {
            for (int i = orderMostViewedSerials.size() - 1; i >= 0; i--) {
                if (orderMostViewedSerials.get(i).getNumberOfViews() != 0
                        && index < action.getNumber()) {
                    finalOrderedList.add(orderMostViewedSerials.get(i));
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
