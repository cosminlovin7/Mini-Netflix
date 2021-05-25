package actiontype;

import fileio.ActionInputData;
import fileio.MovieInputData;
import fileio.SerialInputData;
import fileio.ActorInputData;
import fileio.Writer;

import org.json.simple.JSONArray;
import querysolver.SingleQueryAverageActors;
import querysolver.SingleQueryAwardsActors;
import querysolver.SingleQueryFilterDescriptionActors;

import java.io.IOException;
import java.util.List;

public final class ActorQuery {
    private static final ActorQuery INSTANCE = new ActorQuery();
    private ActorQuery() { }
    public static ActorQuery getAcQInstance() {
        return INSTANCE;
    }
    /**
     *Rezolva toate query-urile de tip actor.
     */

    public JSONArray doActorQuery(final List<MovieInputData> movies,
                                  final List<SerialInputData> serials,
                                  final List<ActorInputData> actors,
                                  final ActionInputData action,
                                  final Writer fileWriter) throws IOException {
        JSONArray arrayResult = new JSONArray();
        switch (action.getCriteria()) {
            case "average" -> {
                SingleQueryAverageActors sqaaInstance
                        = SingleQueryAverageActors.getSQAAInstance();
                Object jsonWriter
                        = sqaaInstance.doSingleQueryAverageActors(
                        actors,
                        movies,
                        serials,
                        action,
                        fileWriter);
                arrayResult.add(jsonWriter);
            }
            case "awards" -> {
                SingleQueryAwardsActors sqawaInstance
                        = SingleQueryAwardsActors.getSQAWAInstance();
                Object jsonWriter
                        = sqawaInstance.doSingleQueryAwardsActors(
                        actors,
                        action,
                        fileWriter);
                arrayResult.add(jsonWriter);
            }
            case "filter_description" -> {
                SingleQueryFilterDescriptionActors sqfdaInstance
                        = SingleQueryFilterDescriptionActors.getSQFDAInstance();
                Object jsonWriter
                        = sqfdaInstance.doSingleQueryFilterDescriptionActors(
                        actors,
                        action,
                        fileWriter);
                arrayResult.add(jsonWriter);
            }
            default -> System.out.println("Actors Query not found.");
        }

        return arrayResult;
    }
}
