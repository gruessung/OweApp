package de.gvisions.oweapp;

/**
 * ListElements
 * @author agruessu
 *
 */

public class ListElements {


        /**
         * ID in DB
         */
        public int id;

        /**
         * Beschreibung
         */
        public String beschreibung;

        /**
         * Der Name des Jenigen
         */
        public String name;

        /**
         * Das Objekt (zB Buch), um das es geht
         */
        public String objekt;

        /**
         * Datum der R�ckgabe
         */
        public String datum;

        /**
         * Richtung des Leihens
         * "Ich leihe dir..." <-> "Ich habe mir ausgeliehen..."
         */
        public String leihRichtung;

        /**
         * Kontakt f�r ContactBadge
         */
        public String kontakt;

        /**
         * Konstruktor
         */
        public ListElements(int id, String b, String name, String objekt, String datum, String leihRichtung, String kontakt)
        {
            this.beschreibung = b;
            this.id = id;
            this.name = name;
            this.objekt = objekt;
            this.datum = datum;
            this.leihRichtung = leihRichtung;
            this.kontakt = kontakt;
        }



}
