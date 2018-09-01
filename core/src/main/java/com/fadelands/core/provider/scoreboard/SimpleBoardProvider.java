package com.fadelands.core.provider.scoreboard;

import com.fadelands.array.Array;
import com.fadelands.core.provider.scoreboard.provider.BoardProvider;
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
                                return "&c&lADMIN &7{player}" ;
                            }

                            case ("admin"): {
                                return "&c&lADMIN &7{player}";
                            }

                            case ("developer"): {
                                return "&c&lDEV &7{player}";
                            }

                            case ("senior"): {
                                return "&6&lSENIOR &7{player}";
                            }

                            case ("mod"): {
                                return "&6&lMOD &7{player}";
                            }

                            case ("trainee"): {
                                return "&e&lTRAINEE &7{player}";
                            }

                            case ("builder"): {
                                return "&9&lBUILDER &7{player}";
                            }

                            case ("partner"): {
                                return "&b&lPARTNER &7{player}";
                            }

                            case ("media"): {
                                return "&d&lMEDIA &7{layer}";
                            }

                            case ("contributor"): {
                                return "&3&lRETIRED &7{player}";
                            }

                            case ("platinum"): {
                                return "&5&lPLATINUM &7{player}";
                            }

                            case ("premium"): {
                                return "&3&lPREMIUM &7{player}";
                            }

                            case ("donator"): {
                                return "&a&lDONATOR &7{player}";
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
