package com.fadelands.lobby.utils;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ItemBuilder {
    protected ItemStack itemstack;

    public ItemBuilder(ItemStack itemstack) {
        this.itemstack = itemstack;
    }

    public ItemBuilder(Material material) {
        this(new ItemStack(material, 1));
    }

    public ItemBuilder setMaterial(Material material) {
        this.itemstack.setType(material);
        return this;
    }

    public ItemBuilder setName(String s) {
        ItemMeta meta = this.itemstack.getItemMeta();
        meta.setDisplayName(s);

        this.itemstack.setItemMeta(meta);
        return this;
    }
    public ItemBuilder setGlowing() {

        ItemMeta meta = this.itemstack.getItemMeta();
        this.itemstack.addUnsafeEnchantment(Enchantment.ARROW_INFINITE, 0);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        this.itemstack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder setAmount(int i) {
        this.itemstack.setAmount(i);
        return this;
    }

    public ItemBuilder setData(int i) {
        this.itemstack.setDurability((short)i);
        return this;
    }

    public ItemBuilder setLore(String... string) {
        setLore(new ArrayList<>(Arrays.asList(string)));
        return this;
    }

    public ItemBuilder setLore(List<String> list) {
        ItemMeta meta = this.itemstack.getItemMeta();
        meta.setLore(list);
        this.itemstack.setItemMeta(meta);
        return this;
    }
    @Deprecated
    public ItemBuilder setSkullOwner(String name) {
        SkullMeta meta = ((SkullMeta)this.itemstack.getItemMeta());
        meta.setOwner(name);
        this.itemstack.setItemMeta(meta);
        return this;
    }

    public ItemStack toItemStack() {
        return this.itemstack;
    }
}

