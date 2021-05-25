package actiontype;

import fileio.ActionInputData;
import fileio.MovieInputData;
import fileio.SerialInputData;
import fileio.UserInputData;;
import fileio.Writer;

import org.json.simple.JSONArray;
import recommendationsolver.SingleRecommendationStandard;
import recommendationsolver.SingleRecommendationSearch;
import recommendationsolver.SingleRecommendationPopular;
import recommendationsolver.SingleRecommendationFavorite;
import recommendationsolver.SingleRecommendationBestUnseen;

import java.io.IOException;
import java.util.List;

public final class Recommendation {
    private static final Recommendation INSTANCE = new Recommendation();
    private Recommendation() { }
    public static Recommendation getRECInstance() {
        return INSTANCE;
    }
    /**
     *Rezolva toate recommend-urile.
     */

    public JSONArray doRecommendation(final List<MovieInputData> movies,
                                      final List<SerialInputData> serials,
                                      final List<UserInputData> users,
                                      final ActionInputData action,
                                      final Writer fileWriter) throws IOException {
        JSONArray arrayResult = new JSONArray();
        switch (action.getType()) {
            case "standard" -> {
                SingleRecommendationStandard srsInstance
                        = SingleRecommendationStandard.getSRSInstance();
                Object jsonWriter
                        = srsInstance.doSingleRecommendationStandard(
                        movies,
                        serials,
                        users,
                        action,
                        fileWriter);
                arrayResult.add(jsonWriter);
            }
            case "search" -> {
                SingleRecommendationSearch srssInstance
                        = SingleRecommendationSearch.getSRSInstance();
                Object jsonWriter
                        = srssInstance.doSingleRecommendationSearch(
                        movies,
                        serials,
                        users,
                        action,
                        fileWriter);
                arrayResult.add(jsonWriter);
            }
            case "popular" -> {
                SingleRecommendationPopular srpInstance
                        = SingleRecommendationPopular.getSRPInstance();
                Object jsonWriter
                        = srpInstance.doSingleRecommendationPopular(
                        movies,
                        serials,
                        users,
                        action,
                        fileWriter);
                arrayResult.add(jsonWriter);
            }
            case "favorite" -> {
                SingleRecommendationFavorite srfInstance
                        = SingleRecommendationFavorite.getSRFInstance();
                Object jsonWriter
                        = srfInstance.doSingleRecommendationFavorite(
                        movies,
                        serials,
                        users,
                        action,
                        fileWriter);
                arrayResult.add(jsonWriter);
            }
            case "best_unseen" -> {
                SingleRecommendationBestUnseen srbuInstance
                        = SingleRecommendationBestUnseen.getSRBUInstance();
                Object jsonWriter
                        = srbuInstance.doSingleRecommendationBestUnseen(
                        movies,
                        serials,
                        users,
                        action,
                        fileWriter);
                arrayResult.add(jsonWriter);
            }
            default -> System.out.println("Recommendation type not found.");
        }

        return arrayResult;
    }
}
