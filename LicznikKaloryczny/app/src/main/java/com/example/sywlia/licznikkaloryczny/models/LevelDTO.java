package com.example.sywlia.licznikkaloryczny.models;


public class LevelDTO {

        public static final String POZIOM_TABLE="POZIOM";
        public static final String POZIOM_ID="POZIOM_ID";
        public static final String POZIOM_NAZWA="POZIOM_NAZWA";
        public static final String POZIOM_ODZNAKI_MAX="POZIOM_ODZNAKI_MAX";
        public static final String POZIOM_ODZNAKI_MIN="POZIOM_ODZNAKI_MIN";
        public static final String POZIOM_IMAGE_SRC="POZIOM_IMAGE_SRC";

        private long id;
        private String imie;
        private int medaleMin;
        private int medaleMax;
        private int imageSrc;

        public int getImageSrc() {
            return imageSrc;
        }

        public void setImageSrc(int imageSrc) {
            this.imageSrc = imageSrc;
        }

        public long getId() {
            return id;
        }



        public void setId(long id) {
            this.id = id;
        }

        public String getImie() {
            return imie;
        }

        public void setImie(String imie) {
            this.imie = imie;
        }


    }

