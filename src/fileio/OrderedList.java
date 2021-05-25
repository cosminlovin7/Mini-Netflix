package fileio;

public class OrderedList {
        private final String name;
        private double rating;
        private int numberOfAwards;
        private int numberOfRatings;
        private final int duration;
        private int numberOfViews; //Am folosit si pt a calcula numarul de aparitii in favorite.
        private int type; // 1 - isMovie ; 2 - isSerial

        /**
        * Clasa este folosita pentru a sorta actorii/filmele
         * dupa diferite criterii.
        */

        public OrderedList(final String name,
                           final double rating,
                           final int numberOfAwards,
                           final int numberOfRatings,
                           final int duration,
                           final int numberOfViews,
                           final int type) {
            this.name = name;
            this.rating = rating;
            this.numberOfAwards = numberOfAwards;
            this.numberOfRatings = numberOfRatings;
            this.duration = duration;
            this.numberOfViews = numberOfViews;
            this.type = type;
        }

        /**
        * Returneaza numele filmului/actorului.
        */

        public String getName() {
            return name;
        }

        /**
         * Incrementeaza numarul de vizionari.
        */

        public void addNumberOfViews(final int noOfViews) {
            this.numberOfViews += noOfViews;
        }

        /**
        * Returneaza rating-ul pentru o instanta OrderedList.
        */

        public double getRating() {
            return rating;
        }

        /**
        * Returneaza numarul de awards pentru un actor.
        */

        public int getNumberOfAwards() {
            return numberOfAwards;
        }

        /**
        * Returneaza numarul de rating-uri date de un utilizator.
        */

        public int getNumberOfRatings() {
            return numberOfRatings;
        }

        /**
        * Returneaza durata unui film.
        */

        public int getDuration() {
            return duration;
        }

        /**
        * Returneaza numarul de vizionari pentru un film/serial.
         */

        public int getNumberOfViews() {
            return numberOfViews;
        }

        /**
        * Returneaza tipul videoclipului
         * 1 - film
         * 2 - serial
        */

        public int getType() {
            return type;
        }

        /**
        * Override pentru functia de toString().
        */

        @Override
        public String toString() {
            return name;
        }
}

