package VisionEyes.scripts.iSmithing.resources;

import java.util.LinkedHashMap;

public class Bar {
    /**
     * Hashmap to get ID(value) by name (key)
     **/
    private LinkedHashMap<String, Integer> bars = new LinkedHashMap<>();
    private LinkedHashMap<String, Integer> barsComponent = new LinkedHashMap<>();
    private String selectedBar;

    public Bar() {
        //Bars <Bar Name, Bar Id>
        bars.put("Bronze", 2349);
        bars.put("Iron", 2351);
        bars.put("Silver", 2355);
        bars.put("Steel", 2353);
        bars.put("Gold", 2357);
        bars.put("Mithril", 2359);
        bars.put("Adamantite", 2361);
        bars.put("Runite", 2361);
        // Bar Widget Component
        barsComponent.put("Bronze", 14);
        barsComponent.put("Iron", 15);
        barsComponent.put("Silver", 16);
        barsComponent.put("Steel", 17);
        barsComponent.put("Gold", 18);
        barsComponent.put("Mithril", 19);
        barsComponent.put("Adamantite", 20);
        barsComponent.put("Runite", 21);
    }


    public LinkedHashMap<String, Integer> getBars() {
        return bars;
    }

    public Integer getParentComponent(){
        return barsComponent.get(this.getSelectedBar());
    }

    public Integer getBarId(){
        return bars.get(getSelectedBar());
    }

    public String getSelectedBar() {
        return selectedBar;
    }

    public void setSelectedBar(String selectedBar) {
        this.selectedBar = selectedBar;
    }
}
