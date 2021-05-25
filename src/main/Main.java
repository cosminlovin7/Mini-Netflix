package main;

import actiontype.Command;
import actiontype.ActorQuery;
import actiontype.ShowQuery;
import actiontype.MovieQuery;
import actiontype.UserQuery;
import actiontype.Recommendation;

import checker.Checkstyle;
import checker.Checker;
import common.Constants;

import fileio.ActionInputData;
import fileio.ActorInputData;
import fileio.Input;
import fileio.InputLoader;
import fileio.MovieInputData;
import fileio.SerialInputData;
import fileio.UserInputData;
import fileio.Writer;

import org.json.simple.JSONArray;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.ArrayList;
import java.util.Objects;

/**
 * The entry point to this homework. It runs the checker that tests your implentation.
 */
public final class Main {
    /**
     * for coding style
     */
    private Main() {
    }
    /**
     * Cauta in lista de users, user-ul cu userName primit.
     * @param userName
     * @param users
     * @return
     */
    public static UserInputData getUser(final String userName,
                                        final List<UserInputData> users) {
        UserInputData currentUser = null;

        for (UserInputData user : users) {

            if (user.getUsername().equalsIgnoreCase(userName)) {
                currentUser = user;
                return currentUser;
            }
        }

        return currentUser;
    }

    /**
     * Cauta in lista de seriale, serialul cu numele primit.
     * @param serialName
     * @param serials
     * @return
     */
    public static SerialInputData getSerial(final String serialName,
                                            final List<SerialInputData> serials) {
        SerialInputData currentSerial = null;

        for (SerialInputData serial : serials) {

            if (serial.getTitle().equalsIgnoreCase(serialName)) {
                return serial;
            }
        }

        return currentSerial;
    }

    /**
     * Cauta in lista de filme filmul cu numele primit.
     * @param movieName
     * @param movies
     * @return
     */
    public static MovieInputData getMovie(final String movieName,
                                          final List<MovieInputData> movies) {
        MovieInputData currentMovie = null;

        for (MovieInputData movie : movies) {

            if (movie.getTitle().equalsIgnoreCase(movieName)) {
                return movie;
            }
        }

        return currentMovie;
    }

    /**
     * Call the main checker and the coding style checker
     * @param args from command line
     * @throws IOException in case of exceptions to reading / writing
     */


    public static void main(final String[] args) throws IOException {
        File directory = new File(Constants.TESTS_PATH);
        Path path = Paths.get(Constants.RESULT_PATH);
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }

        File outputDirectory = new File(Constants.RESULT_PATH);

        Checker checker = new Checker();
        checker.deleteFiles(outputDirectory.listFiles());

        for (File file : Objects.requireNonNull(directory.listFiles())) {

            String filepath = Constants.OUT_PATH + file.getName();
            File out = new File(filepath);
            boolean isCreated = out.createNewFile();
            if (isCreated) {
                action(file.getAbsolutePath(), filepath);
            }
        }

        checker.iterateFiles(Constants.RESULT_PATH, Constants.REF_PATH, Constants.TESTS_PATH);
        Checkstyle test = new Checkstyle();
        test.testCheckstyle();
    }

    /**
     * @param filePath1 for input file
     * @param filePath2 for output file
     * @throws IOException in case of exceptions to reading / writing
     */

    public static void action(final String filePath1,
                              final String filePath2) throws IOException {
        InputLoader inputLoader = new InputLoader(filePath1);
        Input input = inputLoader.readData();

        Writer fileWriter = new Writer(filePath2);
        JSONArray arrayResult = new JSONArray();

        List<ActionInputData> actions = new ArrayList<>(input.getCommands());
        List<ActorInputData> actors = new ArrayList<>(input.getActors());
        List<MovieInputData> movies = new ArrayList<>(input.getMovies());
        List<SerialInputData> serials = new ArrayList<>(input.getSerials());
        List<UserInputData> users = new ArrayList<>(input.getUsers());

        for (ActionInputData action : actions) {
            switch (action.getActionType()) {
                case "command":
                    Command commandInstance = Command.getCommInstance();
                    arrayResult.addAll(commandInstance.doCommand(
                            movies,
                            serials,
                            users,
                            action,
                            fileWriter));
                    break;
                case "query":
                    switch (action.getObjectType()) {
                        case "actors" -> {
                            ActorQuery aqueryInstance = ActorQuery.getAcQInstance();
                            arrayResult.addAll(aqueryInstance.doActorQuery(
                                    movies,
                                    serials,
                                    actors,
                                    action,
                                    fileWriter));
                        }
                        case "movies" -> {
                            MovieQuery mqueryInstance = MovieQuery.getMQInstance();
                            arrayResult.addAll(mqueryInstance.doMovieQuery(
                                    movies,
                                    users,
                                    action,
                                    fileWriter));
                        }
                        case "shows" -> {
                            ShowQuery squeryInstance = ShowQuery.getSQInstance();
                            arrayResult.addAll(squeryInstance.doShowQuery(
                                    serials,
                                    users,
                                    action,
                                    fileWriter));
                        }
                        case "users" -> {
                            UserQuery uqueryInstance = UserQuery.getUQInstance();
                            arrayResult.addAll(uqueryInstance.doUserQuery(
                                    users,
                                    action,
                                    fileWriter));
                        }
                        default -> System.out.println("Not correct objectType");
                    }
                    break;
                case "recommendation":
                    Recommendation recommendationInstance = Recommendation.getRECInstance();
                    arrayResult.addAll(recommendationInstance.doRecommendation(
                            movies,
                            serials,
                            users,
                            action,
                            fileWriter));
                    break;
                default:
                    System.out.println("The command type is unknown!");
            }
        }

        fileWriter.closeJSON(arrayResult);
    }

}
