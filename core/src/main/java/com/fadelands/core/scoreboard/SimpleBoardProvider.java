package com.fadelands.core.scoreboard;

import com.fadelands.array.Array;
import com.fadelands.core.scoreboard.provider.BoardProvider;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class SimpleBoardProvider implements BoardProvider {

    @Override
    public String getTitle(Player player) {
        return "§f§lFade§b§lLands";
    }

    @Override
    public List<String> getBoardLines(Player player) {
        return null;
    }

    @Override
    public String getNameTag(Player player, Player other) {
        try(Connection connection = Array.getConnection()){
            try(PreparedStatement statement = connection.prepareStatement("SELECT * FROM luckperms_players WHERE uuid = ?")) {
                statement.setString(1, other.getUniqueId().toString());
                try (ResultSet rs = statement.executeQuery()) {
                    if (!rs.next()) {
                        Bukkit.getLogger().warning("TABLIST: Couldn't select from luckperms_players.");
                    } else {
                        switch (rs.getString("primary_group").toLowerCase()) {

                            case ("owner"): {
                                return "&4[Owner] {player}" ;
                            }

                            case ("admin"): {
                                return "&c[Admin] {player}";
                            }

                            case ("developer"): {
                                return "&c[Dev] {player}";
                            }

                            case ("senior"): {
                                return "&6[Senior] {player}";
                            }

                            case ("mod"): {
                                return "&6[Mod] {player}";
                            }

                            case ("trainee"): {
                                return "&e[Trainee] {player}";
                            }

                            case ("builder"): {
                                return "&9[Builder] {player}";
                            }

                            case ("partner"): {
                                return "&b[Partner] {player}";
                            }

                            case ("media"): {
                                return "&d[Media] {player}";
                            }

                            case ("contributor"): {
                                return "&3[Retired] {player}";
                            }

                            case ("platinum"): {
                                return "&5[Platinum] {player}";
                            }

                            case ("premium"): {
                                return "&3[Premium] {player}";
                            }

                            case ("donator"): {
                                return "&a[Donator] {player}";
                            }

                            case ("default"): {
                                return "&7{player}";
                            }
                            // den kommer bli cuttad ifall du går över 32 characters samma gäller vid scoreboard linjerna
                            // om den inte hittar någon rank så går den till denna

                                // I
                                // I   en pil neråt
                                // I
                                // V
                            default: {
                                return "&7{player}";
                            }

                        }
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        // If sql error occurs
        return "&7{player}";
    }
}
