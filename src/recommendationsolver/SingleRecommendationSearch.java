package recommendationsolver;

import common.Constants;
import fileio.MovieInputData;
import fileio.SerialInputData;
import fileio.Writer;
import fileio.ActionInputData;
import fileio.UserInputData;
import fileio.OrderedList;
import main.Main;
import sortclasses.SortByRating;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class SingleRecommendationSearch {
      private static final SingleRecommendationSearch SRS_INSTANCE
              = new SingleRecommendationSearch();
      private SingleRecommendationSearch() { }
      public static SingleRecommendationSearch getSRSInstance() {
          return SRS_INSTANCE;
      }

    /**
     * Clasa implementeaza un singleton prin intermediul caruia
     * se poate apela metoda doSingleRecomendationSearch.
     * @param movies
     * @param serials
     * @param users
     * @param action
     * @param fileWriter
     * @return
     * @throws IOException
     */

      public Object doSingleRecommendationSearch(final List<MovieInputData> movies,
                                                final List<SerialInputData> serials,
                                                final List<UserInputData> users,
                                                final ActionInputData action,
                                                final Writer fileWriter) throws IOException {
          Object jsonWriter;
          UserInputData currentUser = Main.getUser(action.getUsername(), users);
          List<OrderedList> newMoviesAndSerials = new ArrayList<>();
          if (currentUser == null
                  || currentUser.getSubscriptionType().equalsIgnoreCase(Constants.BASIC)) {
              jsonWriter = fileWriter.writeFile(action.getActionId(),
                      "message",
                      "SearchRecommendation cannot be applied!");
              return jsonWriter;
          }
          boolean foundMovie = false;
          boolean foundSerial = false;

          for (MovieInputData movie : movies) {
              if (movie.getGenres().contains(action.getGenre())
              && !currentUser.getHistory().containsKey(movie.getTitle())) {
                  newMoviesAndSerials.add(new OrderedList(movie.getTitle(),
                          movie.getRating(),
                          0,
                          0,
                          0,
                          0,
                          0));
                  foundMovie = true;
              }
          }

          for (SerialInputData serial : serials) {
              if (serial.getGenres().contains(action.getGenre())
              && !currentUser.getHistory().containsKey(serial.getTitle())) {
                  double serialRating = 0;
                  for (int i = 0; i < serial.getNumberSeason(); i++) {
                      if (serial.getRatingPerSeason().get(i) != null) {
                          serialRating += serial.getRatingPerSeason().get(i);
                      }
                  }
                  serialRating = serialRating / serial.getNumberSeason();
                  newMoviesAndSerials.add(new OrderedList(serial.getTitle(),
                          serialRating,
                          0,
                          0,
                          0,
                          0,
                          0));
                  foundSerial = true;
              }
          }
          newMoviesAndSerials.sort(new SortByRating());
          if (!foundSerial && !foundMovie) {
              jsonWriter = fileWriter.writeFile(action.getActionId(),
                      "message",
                      "SearchRecommendation cannot be applied!");
          } else {
              jsonWriter = fileWriter.writeFile(action.getActionId(),
                      "message",
                      "SearchRecommendation result: "
                              + newMoviesAndSerials.toString());
          }
          return jsonWriter;
      }
}
