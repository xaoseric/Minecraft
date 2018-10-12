package com.fadelands.core.manager;

import com.fadelands.core.Core;
import com.fadelands.core.punishments.PunishmentMenu;

public class GUIManager {

    private Core core;

    private PunishmentMenu punishmentMenu;

    public GUIManager(Core core){
        this.core = core;
        this.punishmentMenu = new PunishmentMenu(core);
    }

    public PunishmentMenu getPunishmentMenu() {
        return this.punishmentMenu;
    }
}
