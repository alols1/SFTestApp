package se.sfbio.mobile.android.Test;

import java.util.ArrayList;

interface SFBioUI {

    public void changeCity(SFBioTestData.City city);

    public void changeCityRandom();

    public void goToStatTab();

    public void goToTicketsTab();

    public void goToGPSTab();

    public void goToNyheterTab();

    public void goToKampanjerTab();

    public void goToUnderTabBiografer();

    public void waitForApplicationToStart();

    public void checkCarouselItem(int i);

    public void checkItemsInListFromArray(ArrayList<String> lista);

}