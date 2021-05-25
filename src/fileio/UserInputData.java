package fileio;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Information about an user, retrieved from parsing the input test files
 * <p>
 * DO NOT MODIFY
 */
public final class UserInputData {
    public class Rating {
        private double rating;
        private int seasonNumber;
        private String name;

        public Rating(final double rating,
                      final int seasonNumber,
                      final String name) {
            this.rating = rating;
            this.seasonNumber = seasonNumber;
            this.name = name;
        }

        /**
         * Returneaza rating-ul dat pentru un film.
         */

        public double getRating() {
      return rating;
    }

        /**
         * Returneaza sezonul filmului:
         * 0 - daca e film
         * >0 - daca e serial.
         */

        public int getSeasonNumber() {
      return seasonNumber;
    }

        /**
         * Returneaza numele videoclipului.
         */

        public String getName() {
      return name;
    }
    }

    /**
     * User's username
     */
    private final String username;
    /**
     * Subscription Type
     */
    private final String subscriptionType;
    /**
     * The history of the movies seen
     */
    private final Map<String, Integer> history;
    /**
     * Movies added to favorites
     */
    private final ArrayList<String> favoriteMovies;

    private List<Rating> ratingHistory = new ArrayList<>();

    public UserInputData(final String username, final String subscriptionType,
                         final Map<String, Integer> history,
                         final ArrayList<String> favoriteMovies) {
        this.username = username;
        this.subscriptionType = subscriptionType;
        this.favoriteMovies = favoriteMovies;
        this.history = history;
    }

    /**
     * Returneaza username-ul.
     */

    public String getUsername() {
        return username;
    }

    public Map<String, Integer> getHistory() {
        return history;
    }

    public String getSubscriptionType() {
        return subscriptionType;
    }

    public ArrayList<String> getFavoriteMovies() {
        return favoriteMovies;
    }

    public List<Rating> getActivity() {
        return ratingHistory;
    }

    /**
     * Adauga un videoclip in lista de favorite.
     */

    public void addFavoriteMovie(final String movie) {
        this.favoriteMovies.add(movie);
    }

    /**
     * Marcheaza un videoclip ca vizionat.
     */

    public void incrementView(final String movie) {
        int numberOfViews = this.history.get(movie) + 1;
        this.history.replace(movie, numberOfViews);
    }

    /**
     * Adauga un videoclip intr-o lista ce contine
     * toate videoclipurile care au primit rating
     * de la utilizatorul curent.
     */

    public void addRating(final String movieName,
                          final double rating,
                          final int seasonNumber) {
        ratingHistory.add(new Rating(rating, seasonNumber, movieName));
    }

    /**
     * Adauga un videoclip in istoricul de vizionari.
     */

    public void addVideoToHistory(final String movie) {
        this.history.put(movie, 1);
    }

    /**
     * Override pentru functia toString().
     */

    @Override
    public String toString() {
        return "UserInputData{" + "username='"
                + username + '\'' + ", subscriptionType='"
                + subscriptionType + '\'' + ", history="
                + history + ", favoriteMovies="
                + favoriteMovies + '}';
    }
}
