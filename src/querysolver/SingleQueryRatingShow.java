package querysolver;

import common.Constants;
import fileio.ActionInputData;
import fileio.OrderedList;
import fileio.SerialInputData;
import fileio.Writer;
import sortclasses.SortByRating;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class SingleQueryRatingShow {

    private static final SingleQueryRatingShow SQRS_INSTACE = new SingleQueryRatingShow();
    private SingleQueryRatingShow() { }
    public static SingleQueryRatingShow getSQRSInstance() {
        return SQRS_INSTACE;
    }

    /**
     * Clasa implementeaza un singleton prin intermediul caruia se poate
     * apela metoda doSingleQueryRatingShow.
     * @param serials
     * @param action
     * @param fileWriter
     * @return
     * @throws IOException
     */

    public Object doSingleQueryRatingShow(final List<SerialInputData> serials,
                                          final ActionInputData action,
                                          final Writer fileWriter) throws IOException {
        Object jsonWriter;
        List<OrderedList> orderedSerials = new ArrayList<>();
        List<String> yearFilter = new ArrayList<>(action.getFilters().get(Constants.YEAR_FILTER));
        List<String> genreFilter = new ArrayList<>(action.getFilters().get(Constants.GENRE_FILTER));

        for (SerialInputData serial : serials) {
            if ((yearFilter.contains(String.valueOf(serial.getYear()))
                    || yearFilter.get(Constants.FIRST_ELEMENT) == null)
                    && (serial.getGenres().contains(genreFilter.get(Constants.FIRST_ELEMENT))
                    || genreFilter.get(Constants.FIRST_ELEMENT) == null)) {
                  boolean isRated = false;
                  double rating = 0;

                  for (int i = 1; i <= serial.getNumberSeason(); i++) {
                      if (serial.getRatingPerSeason().get(i) != null) {
                          rating += serial.getRatingPerSeason().get(i);
                          isRated = true;
                      }
                  }
                  if (isRated) {
                      rating = rating / serial.getNumberSeason();
                      orderedSerials.add(new OrderedList(serial.getTitle(),
                              rating,
                              0,
                              0,
                              0,
                              0,
                              0));
                  }
            }
        }
        orderedSerials.sort(new SortByRating());
        List<OrderedList> finalOrderedSerials = new ArrayList<>();
        int index = 0;

        if (action.getSortType().equalsIgnoreCase(Constants.ASC)) {
            for (OrderedList srl : orderedSerials) {
                if (index < action.getNumber()) {
                    finalOrderedSerials.add(srl);
                    index++;
                }
            }
        } else if (action.getSortType().equalsIgnoreCase(Constants.DESC)) {
            for (int i = orderedSerials.size() - 1; i >= 0; i--) {
                if (index < action.getNumber()) {
                    finalOrderedSerials.add(orderedSerials.get(i));
                    index++;
                }
            }
        }

        jsonWriter = fileWriter.writeFile(action.getActionId(),
                "message",
                "Query result: "
                        + finalOrderedSerials.toString());
        return jsonWriter;
    }
}
