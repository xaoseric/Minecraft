package com.fadelands.bungeecore.discordsync;

import com.fadelands.bungeecore.*;
import com.fadelands.bungeecore.utils.DM;
import com.fadelands.bungeecore.utils.Utils;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.managers.GuildController;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LinkDiscordID extends Command {

    public LinkDiscordID() {
        super("discord", null, "dc");
    }

    DM dm = new DM();

    @Override
    public void execute(CommandSender sender, String[] args) {

        if (!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(new ComponentBuilder("You can not use this command from the console.").create());
            return;
        }

        ProxiedPlayer player = (ProxiedPlayer) sender;

        if (args.length == 0) {
            try (Connection connection = Main.getConnection()) {
                try (PreparedStatement st = connection.prepareStatement("SELECT * FROM fadelands_discord_regs WHERE player_uuid='" + player.getUniqueId().toString() + "'")) {
                    try (ResultSet rs = st.executeQuery()) {

                        if (!rs.next()) {
                            player.sendMessage(new ComponentBuilder("" +
                                    "§8§l┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓" + "\n" +
                                    " §c§lYour account isn't linked yet!" + "\n" +
                                    " §7Use /discord link <ID> to link your account." + "\n" +
                                    " §7The FadeLands bot will message you " + "\n" +
                                    " §7your ID when you join the Discord!" + "\n" +
                                    "§8§l┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛"
                            ).color(ChatColor.DARK_AQUA).create());
                        } else if (!rs.getBoolean("registered")) {
                            player.sendMessage(new ComponentBuilder("" +
                                    "§8§l┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓" + "\n" +
                                    " §6§lYour account must be verified!" + "\n" +
                                    " §7We sent a message with a verification " + "\n" +
                                    " §7key to " + rs.getString("reg_to_discordname") + "#" + rs.getString("reg_to_discord_disc") + "\n" +
                                    " §3Use §b/discord verify <KEY> §3to verify the account!" + "\n" +
                                    "§8§l┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛"
                            ).color(ChatColor.DARK_AQUA).create());
                        } else {
                            player.sendMessage(new ComponentBuilder("" +
                                    "§8§l┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓" + "\n" +
                                    " §a§lYour account is linked!" + "\n" +
                                    " §3Minecraft account linked to: §b" + rs.getString("reg_to_discordname") + "#" + rs.getString("reg_to_discord_disc") + "\n" +
                                    " §cUse §l/discord unlink §cto unlink your account!" + "\n" +
                                    "§8§l┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛"
                            ).color(ChatColor.AQUA).create());
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return;
        }

        if (args.length == 1) {

            if (args[0].equalsIgnoreCase("link")) {
                player.sendMessage(new ComponentBuilder(Utils.Prefix + "§cYou have to enter an ID!").color(ChatColor.RED).create());
                return;
            }
            if (args[0].equalsIgnoreCase("verify")) {
                player.sendMessage(new ComponentBuilder(Utils.Prefix + "§cYou have to enter the security key sent to you from the FadeLands Bot!").color(ChatColor.RED).create());
                return;
            }

            if (args[0].equalsIgnoreCase("unlink"))
                try (Connection connection = Main.getConnection()) {
                    try (PreparedStatement st = connection.prepareStatement("SELECT * FROM fadelands_discord_regs WHERE player_uuid='" + player.getUniqueId().toString() + "'")) {
                        try (ResultSet rs = st.executeQuery()) {

                            if (!rs.next()) {
                                player.sendMessage(new ComponentBuilder(Utils.Prefix_Red + "§cYour Minecraft account isn't linked to a Discord account.").color(ChatColor.RED).create());
                            } else {
                                //Removing EVERY role
                                String userID = rs.getString("reg_to_discordid");
                                Guild guild = BuildBot.jda.getGuildById("402096765018570752");
                                GuildController gc = guild.getController();
                                Member member = guild.getMemberById(userID);


                                for (int i = 0; i < Group.values().length; i++) {
                                    Group g = Group.values()[i];
                                    if (g.getID() != null)
                                        gc.removeSingleRoleFromMember(member, guild.getRoleById(g.getID())).queue();
                                }

                                player.sendMessage(new ComponentBuilder(Utils.Prefix + "§aUnlinked your Discord account!").color(ChatColor.GREEN).create());
                                dm.log("`" + player.getName() + " (" + player.getUniqueId().toString() + ")` unlinked their Minecraft account from their Discord account `" + member.getUser().getName() + "#" + member.getUser().getDiscriminator() + "`.");
                                MySQL.deleteFromTable(player, "fadelands_discord_regs");

                            }
                        }

                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

        }

        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("link")) {
                String id = args[1];

                if (BuildBot.jda.getUserById(id) == null) {
                    player.sendMessage(new ComponentBuilder(Utils.Prefix_Red + "§cThe given ID is invalid. Copy the command the bot sent to you and try again.").color(ChatColor.RED).create());
                    return;
                }

                User user = BuildBot.jda.getUserById(id);
                Member member = BuildBot.jda.getGuildById("402096765018570752").getMember(user);
                Guild guild = BuildBot.jda.getGuildById("402096765018570752");
                GuildController gc = guild.getController();
                try(Connection connection = Main.getConnection()){
                    try(PreparedStatement st = connection.prepareStatement("SELECT * FROM fadelands_discord_regs WHERE player_uuid='" + player.getUniqueId() + "'")) {
                        try (ResultSet rs = st.executeQuery()) {

                            if (rs.next()) {
                                player.sendMessage(new ComponentBuilder(Utils.Prefix_Red + "§cYour Minecraft account is already linked to Discord!").color(ChatColor.RED).create());
                            } else {

                                String let = RandomKeys.digits + "ACEFGHJKLMNPQRUVWXYabcdefhijkprstuvwx";
                                RandomKeys output = new RandomKeys(9, new SecureRandom(), let);

                                String key = output.nextString();

                                try (PreparedStatement insert = connection.prepareStatement("INSERT INTO fadelands_discord_regs " +
                                        "(player_uuid," +
                                        "player_username," +
                                        "registered," +
                                        "reg_to_discordid," +
                                        "reg_to_discordname," +
                                        "reg_to_discord_disc," +
                                        "security_key)" +
                                        " VALUE (?,?,?,?,?,?,?)")) {

                                    insert.setString(1, player.getUniqueId().toString());
                                    insert.setString(2, player.getName());
                                    insert.setBoolean(3, false);
                                    insert.setString(4, user.getId());
                                    insert.setString(5, user.getName());
                                    insert.setString(6, user.getDiscriminator());
                                    insert.setString(7, key);
                                    insert.executeUpdate();

                                    player.sendMessage(new ComponentBuilder(Utils.Prefix_Green + "§aPlease verify your account with the key you received in a private message sent from the FadeLands Bot.").color(ChatColor.GREEN).create());

                                    user.openPrivateChannel().queue(privateChannel -> privateChannel.sendMessage("You're almost done! To verify that you are the owner of this account, please enter the command below in-game: \n" + "\n" +
                                            "`/discord verify " + key + "` \n" + "\n" +
                                            "If you weren't the one who tried to register your account, please contact a staff member." + "\n" + "\n" + "Thanks for linking your account!").queue());
                                }
                            }
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (args[0].equalsIgnoreCase("verify")) {
                try(Connection connection = Main.getConnection()){
                    try(PreparedStatement st = connection.prepareStatement("SELECT * FROM fadelands_discord_regs WHERE security_key='" + args[1] + "'")){
                        try(ResultSet rs = st.executeQuery()) {

                            if (!rs.next()) {
                                player.sendMessage(new ComponentBuilder(Utils.Prefix_Red + "§cThat's not a valid key!").color(ChatColor.RED).create());
                            } else {
                                try (PreparedStatement st2 = connection.prepareStatement("SELECT * FROM fadelands_discord_regs WHERE player_uuid='" + player.getUniqueId().toString() + "'")) {
                                    try (ResultSet rs2 = st2.executeQuery()) {

                                        if (rs2.next()) {
                                            if (rs2.getBoolean("registered")) {
                                                player.sendMessage(new ComponentBuilder(Utils.Prefix_Red + "§cYou have already verified this account.").color(ChatColor.RED).create());
                                            } else {

                                                String userID = rs.getString("reg_to_discordid");

                                                User user = BuildBot.jda.getUserById(userID);
                                                Member member = BuildBot.jda.getGuildById("402096765018570752").getMember(user);
                                                Guild guild = BuildBot.jda.getGuildById("402096765018570752");
                                                GuildController gc = guild.getController();
                                                try (PreparedStatement luckperms = connection.prepareStatement("SELECT * FROM luckperms_players WHERE uuid='" + player.getUniqueId().toString() + "'")) {
                                                    try (ResultSet lpRs = luckperms.executeQuery()) {
                                                        if (lpRs.next()) {

                                                            String luckyGroup = lpRs.getString("primary_group");
                                                            Group group = Group.fromString(luckyGroup);
                                                            String ID = group.getID();

                                                            if (ID != null)
                                                                gc.addSingleRoleToMember(member, guild.getRoleById(ID)).queue();

                                                            gc.addSingleRoleToMember(member, guild.getRoleById(Group.VERIFIED.getID())).queue();
                                                            gc.setNickname(member, player.getName()).queue();
                                                            MySQL.updateTable(player, "fadelands_discord_regs", "registered", true);

                                                            player.sendMessage(new ComponentBuilder(Utils.Prefix + "§2Your account has been linked to " + user.getName() + "#" + user.getDiscriminator() + ".").color(ChatColor.DARK_GREEN).create());
                                                            dm.log("`" + player.getName() + " (" + player.getUniqueId().toString() + ")` linked their Minecraft account to Discord account `" + user.getName() + "#" + user.getDiscriminator() + "` with permission group: `" + luckyGroup + "`");

                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }

                    }

                } catch (SQLException e) {
                }
            }


            }
        }

        public static enum Group {

            VERIFIED("430121745647796225", true),

            DEFAULT(null, true),

            DONATOR("402098414483144705", true),
            PREMIUM("406620113702486019", true),
            PLATINUM("428566070136143882", true),
            CONTRIBUTOR("402099925472772098", true),
            MEDIA("402101807142404117", true),
            PARTNER("402101870039924746", true),
            BUILDER("402099275326291968", true),
            TRAINEE("460801186279325727", true),
            MOD("402098399635308546", true),
            SENIOR("402098365183033356", true),
            DEVELOPER("402098341581815820", true),
            ADMIN("402098325160984577", true),
            OWNER("402098299374403584", true);

            private final String ID;

            private Group(String ID, boolean auto) {
                this.ID = ID;
            }

            public String getID() {
                return this.ID;
            }

            public static Group fromString(String s) {
                for (int i = 0; i < values().length; i++) {
                    Group g = values()[i];
                    if (s.equalsIgnoreCase(g.name())) return g;
                }
                return DEFAULT;
            }
        }
    }


