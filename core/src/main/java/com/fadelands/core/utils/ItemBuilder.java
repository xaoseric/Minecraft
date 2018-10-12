package com.fadelands.core.utils;

import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
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

    public ItemBuilder setSkullOwner(String name) {
        SkullMeta meta = ((SkullMeta)this.itemstack.getItemMeta());
        meta.setOwner(name);
        this.itemstack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder addUnsafeEnchantment(Enchantment ench, int level){
        this.itemstack.addUnsafeEnchantment(ench, level);
        return this;
    }

    public ItemBuilder addEnchant(Enchantment ench, int level){
        ItemMeta im = this.itemstack.getItemMeta();
        im.addEnchant(ench, level, true);
        this.itemstack.setItemMeta(im);
        return this;
    }

    @SuppressWarnings("deprecation")
    public ItemBuilder setDyeColor(DyeColor color){
        this.itemstack.setDurability(color.getData());
        return this;
    }

    @Deprecated
    public ItemBuilder setWoolColor(DyeColor color){
        if(!this.itemstack.getType().equals(Material.WOOL))return this;
        this.itemstack.setDurability(color.getData());
        return this;
    }

    public ItemBuilder setLeatherArmorColor(Color color){
        try{
            LeatherArmorMeta im = (LeatherArmorMeta)this.itemstack.getItemMeta();
            im.setColor(color);
            this.itemstack.setItemMeta(im);
        }catch(ClassCastException ignored){}
        return this;
    }

    public ItemStack toItemStack() {
        return this.itemstack;
    }
}
