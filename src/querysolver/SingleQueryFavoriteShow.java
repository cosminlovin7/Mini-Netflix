package querysolver;

import common.Constants;
import fileio.SerialInputData;
import fileio.ActionInputData;
import fileio.UserInputData;
import fileio.OrderedList;
import fileio.Writer;
import sortclasses.SortByViews;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class SingleQueryFavoriteShow {

    private static final SingleQueryFavoriteShow SQFS_INSTANCE = new SingleQueryFavoriteShow();
    private SingleQueryFavoriteShow() { }
    public static SingleQueryFavoriteShow getSQFSInstance() {
        return SQFS_INSTANCE;
    }

    /**
     * Clasa implementeaza un singleton prin intermediul caruia
     * se apeleaza metoda doSingleQueryFavoriteShow
     * @param serials
     * @param action
     * @param users
     * @param fileWriter
     * @return
     * @throws IOException
     */


    public Object doSingleQueryFavoriteShow(final List<SerialInputData> serials,
                                            final ActionInputData action,
                                            final List<UserInputData> users,
                                            final Writer fileWriter) throws IOException {

        Object jsonWriter;
        List<String> yearFilter = new ArrayList<>(action.getFilters().get(Constants.YEAR_FILTER));
        List<String> genreFilter = new ArrayList<>(action.getFilters().get(Constants.GENRE_FILTER));
        List<OrderedList> orderFavouriteMovies = new ArrayList<>();

        for (SerialInputData serial : serials) {
            if ((yearFilter.contains(String.valueOf(serial.getYear()))
                    || yearFilter.get(Constants.FIRST_ELEMENT) == null)
                    && (serial.getGenres().contains(genreFilter.get(Constants.FIRST_ELEMENT))
                    || genreFilter.get(Constants.FIRST_ELEMENT) == null)) {
                orderFavouriteMovies.add(new OrderedList(serial.getTitle(),
                        0,
                        0,
                        0,
                        0,
                        0,
                        0));
            }
        }

        for (UserInputData user : users) {
            for (OrderedList serial : orderFavouriteMovies) {
                if (user.getFavoriteMovies().contains(serial.getName())) {
                    serial.addNumberOfViews(1);
                }
            }
        }

        orderFavouriteMovies.sort(new SortByViews());
        List<OrderedList> finalOrderedList = new ArrayList<>();
        int index = 0;

        if (action.getSortType().equalsIgnoreCase(Constants.ASC)) {
            for (OrderedList fmov : orderFavouriteMovies) {
                if (fmov.getNumberOfViews() != 0 && index < action.getNumber()) {
                    finalOrderedList.add(fmov);
                    index++;
                }
            }
        } else if (action.getSortType().equalsIgnoreCase(Constants.DESC)) {
            for (int i = orderFavouriteMovies.size() - 1; i >= 0; i--) {
                if (orderFavouriteMovies.get(i).getNumberOfViews() != 0
                        && index < action.getNumber()) {
                    finalOrderedList.add(orderFavouriteMovies.get(i));
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
