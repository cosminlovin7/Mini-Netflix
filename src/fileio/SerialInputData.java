package fileio;

import entertainment.Season;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Map;

/**
 * Information about a tv show, retrieved from parsing the input test files
 * <p>
 * DO NOT MODIFY
 */
public final class SerialInputData extends ShowInput {
    /**
     * Number of seasons
     */
    private final int numberOfSeasons;
    /**
     * Season list
     */
    private final ArrayList<Season> seasons;

    private Map<Integer, Double> ratingPerSeason = new HashMap<>();

    public SerialInputData(final String title, final ArrayList<String> cast,
                           final ArrayList<String> genres,
                           final int numberOfSeasons, final ArrayList<Season> seasons,
                           final int year) {
        super(title, year, cast, genres);
        this.numberOfSeasons = numberOfSeasons;
        this.seasons = seasons;
    }


    public int getNumberSeason() {
        return numberOfSeasons;
    }

    public Map<Integer, Double> getRatingPerSeason() {
        return ratingPerSeason;
    }

    public ArrayList<Season> getSeasons() {
        return seasons;
    }

    /**
     * Functia adauga un rating pentru un serial in functie
     * de sezon. La fel ca si pentru filme, face media intre
     * rating-ul vechi si cel nou. Daca nu a primit rating
     * pana acum, pur si simplu il adauga pe cel primit
     * ca parametru.
     */

    public void addRating(final Integer seasonNumber, final Double rating) {
        Double oldRating = ratingPerSeason.get(seasonNumber);

        if (!ratingPerSeason.containsKey(seasonNumber)) {
            ratingPerSeason.put(seasonNumber, rating);
        } else {
            if (oldRating == 0) {
                ratingPerSeason.replace(seasonNumber, rating);
            } else {
                ratingPerSeason.replace(seasonNumber, (rating + oldRating) / 2);
            }
        }
    }

    @Override
    public String toString() {
        return "SerialInputData{" + " title= "
                + super.getTitle() + " " + " year= "
                + super.getYear() + " cast {"
                + super.getCast() + " }\n" + " genres {"
                + super.getGenres() + " }\n "
                + " numberSeason= " + numberOfSeasons
                + ", seasons=" + seasons + "\n\n" + '}';
    }
}
