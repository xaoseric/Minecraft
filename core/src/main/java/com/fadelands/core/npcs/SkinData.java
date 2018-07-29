package com.fadelands.core.npcs;

public final class SkinData {

    public static final SkinData arrayofc = new SkinData("eyJ0aW1lc3RhbXAiOjE1Mjg4NDIxNzA3MzIsInByb2ZpbGVJZCI6IjA2NzhmMjY2ODg0NTQxNDBiYzgyMGU0YzE4YjYxYWYxIiwicHJvZmlsZU5hbWUiOiJhcnJheW9mYyIsInNpZ25hdHVyZVJlcXVpcmVkIjp0cnVlLCJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGQ3ZmRmNmRkYjRmYzkyOGIzNDU1MmQ3YzAzZjA0ZmU1NzRmNjgyMzBhYmM1ODk3MmZlOGY1Y2M1NGJjYTEzYyJ9fX0=", "lMx2BGJaGJgX/2UAr+PYfGRhwKm6hTFS5l/m6I5XseZPVXa2m5plbaOufEWzuIU9g0TcNwhMEaQ645qb4pQyfVaW3IdDWvsvohIeV0L0pIwLAlG2ymoCFgIKCm3+kUw//liimDp7gXRfdCYtGu7xAt458K8ApQmLY8QD1QBKRh6M3lLhjr5eQpY3ATw1A6reVpFHSrndqf25IiXmcrBxO7GH7bT48ymNrxCjB/Tbct+DRiiebfAMQgwNGLUya+MEqqF+N4gi8TavcGS8ja2dMttSnHFV3LCkwuYsIRlCZg0SO90n01vVbOGBPDmopaLvNVmWdnneOsLMCnetJzarTgAxD80/WOe5yTm6TZVKRhe5uREPxe2TZn07e/kv2cwYBxheDGp/Cib85teASct/ZC+cpzqDpJzWoF6WYAje0Vg5GzPYXeyJtI2T687XtEMz6egq6AkM+tkZJyf8h26I/wgH8ZY/GemBMtHoM7pWm2lgHKpudsC8mmDJFWvTCKkt6peHMcT4npRitpA9f2+lggM/cmAhFvE2rRuFYGgVG1wNu2kBHBGev412I8n9Yu15Wv9BZ+fyAKjQA4mi1QfirBojIylFfImnj5hLaHq5sEaZ2KMei1fh0h6NsNvYqav929oDst9ebU03HM+e3L1M0+rsMtjj5HX/TqSvzwzp15U=");

    private final String textures, signature;

    protected SkinData(String textures, String signature) {
        this.textures = textures;
        this.signature = signature;
    }

    public String getTextures() {
        return this.textures;
    }

    public String getSignature() {
        return this.signature;
    }
}
