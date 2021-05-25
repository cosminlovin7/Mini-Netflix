package querysolver;

import common.Constants;

import fileio.ActorInputData;
import fileio.MovieInputData;
import fileio.SerialInputData;
import fileio.Writer;
import fileio.ActionInputData;
import fileio.OrderedList;
import main.Main;
import sortclasses.SortByRating;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class SingleQueryAverageActors {

    private static final SingleQueryAverageActors SQAA_INSTANCE = new SingleQueryAverageActors();
    private SingleQueryAverageActors() { }
    public static SingleQueryAverageActors getSQAAInstance() {
        return SQAA_INSTANCE;
    }

    /**
     * Clasa implementeaza un singleton prin intermediul caruia
     * se apeleaza metoda doSingleQueryAverageActors, si care
     * rezolva task-ul de tip query.
     * @param actors
     * @param movies
     * @param serials
     * @param action
     * @param fileWriter
     * @return
     * @throws IOException
     */
    public Object doSingleQueryAverageActors(final List<ActorInputData> actors,
                                             final List<MovieInputData> movies,
                                             final List<SerialInputData> serials,
                                             final ActionInputData action,
                                             final Writer fileWriter) throws IOException {
        List<OrderedList> orderedActors = new ArrayList<>();

        for (ActorInputData actor : actors) {
            double rating = 0;
            for (String film : actor.getFilmography()) {
                MovieInputData currentMovie = Main.getMovie(film, movies);
                SerialInputData currentSerial = Main.getSerial(film, serials);

                if (currentMovie != null) {
                    double movieRating = currentMovie.getRating();

                    if (movieRating != 0) {
                        if (rating == 0) {
                            rating = movieRating;
                        } else {
                            rating = (rating + movieRating) / 2;
                        }
                    }
                } else if (currentSerial != null) {
                    double serialRating = 0;

                    for (int i = 1; i <=  currentSerial.getNumberSeason(); i++) {
                        if (currentSerial.getRatingPerSeason().get(i) != null) {
                            serialRating += currentSerial.getRatingPerSeason().get(i);
                        }
                    }
                    serialRating = serialRating / currentSerial.getNumberSeason();

                    if (rating == 0) {
                        rating = serialRating;
                    } else {
                        rating = (rating + serialRating) / 2;
                    }
                }
            }
            orderedActors.add(new OrderedList(actor.getName(),
                    rating,
                    -1,
                    0,
                    0,
                    0,
                    0));
        }

        orderedActors.sort(new SortByRating());
        List<OrderedList> finalOrderedActors = new ArrayList<>();
        int index = 0;

        if (action.getSortType().equalsIgnoreCase(Constants.ASC)) {
            for (OrderedList act : orderedActors) {
                if (act.getRating() != 0 && index < action.getNumber()) {
                    finalOrderedActors.add(act);
                    index++;
                }
            }
        } else if (action.getSortType().equalsIgnoreCase(Constants.DESC)) {
            for (int i = orderedActors.size() - 1; i >= 0; i--) {
                if (orderedActors.get(i).getRating() != 0 && index < action.getNumber()) {
                    finalOrderedActors.add(orderedActors.get(i));
                    index++;
                }
            }
        }

        Object jsonWriter = fileWriter.writeFile(action.getActionId(),
                "message",
                "Query result: "
                        + finalOrderedActors.toString());

        return jsonWriter;
    }
}
