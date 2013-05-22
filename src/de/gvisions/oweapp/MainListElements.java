package de.gvisions.oweapp;

/**
 * ListElements
 * @author agruessu
 *
 */

public class MainListElements {


        /**
         * Der Name des Jenigen
         */
        public String name;

        /**
         * Kontakt f�r ContactBadge
         */
        public String kontakt;

        /**
         * Anzahl "Ich habe mir ausgeliehen"
         */
        public int anzahlAusgeliehen;

        /**
         * Anzahl "Ich leihe dir"
         */
        public int anzahlVerliehen;

        /**
         * Konstruktor
         *
         * @param int anzahlAusgeliehen
         * @param int anzahlVerliehen
         * @param String name
         * @param String kontakt
         */
        public MainListElements(int a, int v, String name, String kontakt)
        {
            this.anzahlAusgeliehen = a;
            this.anzahlVerliehen = v;
            this.name = name;
            this.kontakt = kontakt;
        }



}
