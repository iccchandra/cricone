package com.onecricket.pojo;


import com.onecricket.adapter.expandablerecyclerview.ParentListItem;

import java.util.List;

public class OddsCategory implements ParentListItem {

    private String name;
    private List<MatchOdds> matchOddsList;

    public OddsCategory(String name, List<MatchOdds> matchOddsList) {
        this.name = name;
        this.matchOddsList = matchOddsList;
    }

    public String getName() {
        return name;
    }

    @Override
    public List<MatchOdds> getChildItemList() {
        return matchOddsList;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }

    @Override
    public String toString() {
        return "OddsCategory{" +
                "name='" + name + '\'' +
                ", matchOddsList=" + matchOddsList +
                '}';
    }
}
