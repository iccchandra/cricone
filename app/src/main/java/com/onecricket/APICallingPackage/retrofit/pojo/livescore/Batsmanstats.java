
package com.onecricket.APICallingPackage.retrofit.pojo.livescore;

import java.util.List;
import com.google.gson.annotations.Expose;

@SuppressWarnings("unused")
public class Batsmanstats {

    @Expose
    private List<Player> player;

    public List<Player> getPlayer() {
        return player;
    }

    public void setPlayer(List<Player> player) {
        this.player = player;
    }

}
