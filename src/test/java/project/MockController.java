package project;

public class MockController extends GOLController{
    @Override
    public void drawBoard() {
        // Overstyrer drawBoard for å unngå GUI-kall under testing
        // Dette gjør at Board kan kalle drawBoard() uten at noe GUI blir forsøkt oppdatert
    }
}

