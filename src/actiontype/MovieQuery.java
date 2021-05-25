package actiontype;

import fileio.ActionInputData;
import fileio.MovieInputData;
import fileio.UserInputData;
import fileio.Writer;

import org.json.simple.JSONArray;
import querysolver.SingleQueryFavoriteMovie;
import querysolver.SingleQueryMostViewedMovie;
import querysolver.SingleQueryRatingMovie;
import querysolver.SingleQueryLongestMovie;

import java.io.IOException;
import java.util.List;

public final class MovieQuery {
    private static final MovieQuery INSTANCE = new MovieQuery();
    private MovieQuery() { }
    public static MovieQuery getMQInstance() {
        return INSTANCE;
    }
    /**
     *Rezolva toate query-urile de tip movie.
     */

    public JSONArray doMovieQuery(final List<MovieInputData> movies,
                                  final List<UserInputData> users,
                                  final ActionInputData action,
                                  final Writer fileWriter) throws IOException {
        JSONArray arrayResult = new JSONArray();
        switch (action.getCriteria()) {
            case "ratings" -> {
                SingleQueryRatingMovie sqrmInstance
                        = SingleQueryRatingMovie.getSQRMInstance();
                Object jsonWriter = sqrmInstance.doSingleQueryRatingMovie(
                        movies,
                        action,
                        fileWriter);
                arrayResult.add(jsonWriter);
            }
            case "longest" -> {
                SingleQueryLongestMovie sqlmInstance
                        = SingleQueryLongestMovie.getSQLMInstance();
                Object jsonWriter = sqlmInstance.doSingleQueryLongestMovie(
                        movies,
                        action,
                        fileWriter);
                arrayResult.add(jsonWriter);
            }
            case "most_viewed" -> {
                SingleQueryMostViewedMovie sqmvmInstance
                        = SingleQueryMostViewedMovie.getSQMVMInstance();
                Object jsonWriter
                        = sqmvmInstance.doSingleQueryMostViewedMovie(
                        movies,
                        users,
                        action,
                        fileWriter);
                arrayResult.add(jsonWriter);
            }
            case "favorite" -> {
                SingleQueryFavoriteMovie sqfmInstance =
                        SingleQueryFavoriteMovie.getSQFMInstance();
                Object jsonWriter
                        = sqfmInstance.doSingleQueryFavoriteMovie(
                        movies,
                        action,
                        users,
                        fileWriter);
                arrayResult.add(jsonWriter);
            }
            default -> System.out.println("Movie Query not found.");
        }

        return arrayResult;
    }
}
