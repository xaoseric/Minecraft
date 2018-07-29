package com.fadelands.bungeecore.discordsync;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

import java.security.SecureRandom;
import java.util.concurrent.ThreadLocalRandom;

public class RandomKeyCommand extends Command {

    public RandomKeyCommand() {
        super("rkey", null);

    }


    @Override
    public void execute(CommandSender commandSender, String[] strings) {

        RandomKeys gen = new RandomKeys(9, ThreadLocalRandom.current());
        RandomKeys session = new RandomKeys();

        String let = RandomKeys.digits + "ACEFGHJKLMNPQRUVWXYabcdefhijkprstuvwx";
        RandomKeys output = new RandomKeys(9, new SecureRandom(), let);

        commandSender.sendMessage(output.nextString());
    }
}
