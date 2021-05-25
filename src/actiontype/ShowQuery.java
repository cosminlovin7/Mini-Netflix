package actiontype;

import fileio.ActionInputData;
import fileio.SerialInputData;
import fileio.UserInputData;
import fileio.Writer;

import org.json.simple.JSONArray;
import querysolver.SingleQueryFavoriteShow;
import querysolver.SingleQueryMostViewedShow;
import querysolver.SingleQueryRatingShow;
import querysolver.SingleQueryLongestShow;

import java.io.IOException;
import java.util.List;

public final class ShowQuery {
    private static final ShowQuery INSTANCE = new ShowQuery();
    private ShowQuery() { }
    public static ShowQuery getSQInstance() {
        return INSTANCE;
    }
    /**
     *Rezolva toate query-urile de tip show.
     */
    public JSONArray doShowQuery(final List<SerialInputData> serials,
                                 final List<UserInputData> users,
                                 final ActionInputData action,
                                 final Writer fileWriter) throws IOException {
        JSONArray arrayResult = new JSONArray();
        switch (action.getCriteria()) {
            case "ratings" -> {
                SingleQueryRatingShow sqrsInstance
                        = SingleQueryRatingShow.getSQRSInstance();
                Object jsonWriter
                        = sqrsInstance.doSingleQueryRatingShow(
                        serials,
                        action,
                        fileWriter);
                arrayResult.add(jsonWriter);
            }
            case "longest" -> {
                SingleQueryLongestShow sqlsInstance
                        = SingleQueryLongestShow.getSQLSInstance();
                Object jsonWriter
                        = sqlsInstance.doSingleQueryLongestShow(
                        serials,
                        action,
                        fileWriter);
                arrayResult.add(jsonWriter);
            }
            case "most_viewed" -> {
                SingleQueryMostViewedShow sqmvsInstance
                        = SingleQueryMostViewedShow.getSQMVSInstance();
                Object jsonWriter
                        = sqmvsInstance.doSingleQueryMostViewedShow(
                        serials,
                        action,
                        users,
                        fileWriter);
                arrayResult.add(jsonWriter);
            }
            case "favorite" -> {
                SingleQueryFavoriteShow sqfsInstance
                        = SingleQueryFavoriteShow.getSQFSInstance();
                Object jsonWriter
                        = sqfsInstance.doSingleQueryFavoriteShow(
                        serials,
                        action,
                        users,
                        fileWriter);
                arrayResult.add(jsonWriter);
            }
            default -> System.out.println("Show Query not found.");
        }

        return arrayResult;
    }
}
