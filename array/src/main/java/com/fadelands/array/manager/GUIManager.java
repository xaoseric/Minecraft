package com.fadelands.array.manager;

import com.fadelands.array.Array;
import com.fadelands.array.punishments.PunishmentMenu;

public class GUIManager {

    private Array array;

    private PunishmentMenu punishmentMenu;

    public GUIManager(Array array){
        this.array = array;
        this.punishmentMenu = new PunishmentMenu(array);
    }

    public PunishmentMenu getPunishmentMenu() {
        return this.punishmentMenu;
    }
}
