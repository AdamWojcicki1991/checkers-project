package com.checkers.UIX;

import javafx.scene.image.Image;

public interface UIXContent {
    Image BACKGROUND_MAIN_VIEW = new Image("com/checkers/UIX/image/background.jpg");
    Image BACKGROUND_MENU_BAR = new Image("com/checkers/UIX/image/backgroundMenuBar.jpg");
    Image BP = new Image("com/checkers/UIX/figures/BP.png");
    Image BQ = new Image("com/checkers/UIX/figures/BQ.png");
    Image WP = new Image("com/checkers/UIX/figures/WP.png");
    Image WQ = new Image("com/checkers/UIX/figures/WQ.png");

    static String printManual() {
        return "1. You only use a mouse to interact with Checkers Game.\n" +
                "2. To start a game please click on File -> New Game on Top Menu Bar.\n" +
                "3. White figure player always starts as first.\n" +
                "4. Below Checkers Board you will find help window.\n     Just follow the tips in this window to make correct move.\n" +
                "5. If you want to give up you can click on File -> Give Up\n     on Top Menu Bar. After this your opponent will automaticly win the game.\n" +
                "6. If you want to make one move back please click on Edit -> Undo Move\n     on Top Menu Bar.\n" +
                "7. If you want to make one move forward please click on Edit -> Next Move\n      on Top Menu Bar.\n" +
                "8. If you want to change game preferences just click \"Preferences\"\n     on Top Menu Bar.\n" +
                "9. If you want to change game options just click \"Options\" on Top Menu Bar.\n" +
                "10. If you are interested in the game and want to know more about it\n     pleaase click on Help -> About on Top Menu Bar.";
    }

    static String printAbout() {
        return "Hi, my name is Adam Wójcicki and I am a developer who creates this\n Checkers Game.\n" +
                "\nIf You want to contact with me about the game, I leave my email here\n adamwojcicki1991@gmail.com.\n" +
                "\nI want to introduce to You some informations about game development below:\n" +
                "\n1. Game is a variation of checkers, which is called Polish Checkers or\n     International Checkers.\n" +
                "2. Game has implemented logic that accurately reproduces the real\n     tournament game of checkers." +
                "More information about game you will find on\n     \"https://en.wikipedia.org/wiki/International_draughts\" that was my" +
                "\n     inspiration during development process.\n" +
                "3. Total time spend on development in hour: 225 hours.\n" +
                "4. Project starts from 01.04.2020 and lasted until end of April.\n" +
                "\n Good luck and well play!";
    }
}
