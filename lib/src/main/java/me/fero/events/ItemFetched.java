package me.fero.events;

import me.fero.objects.Item;

import javax.annotation.Nullable;

public interface ItemFetched {
    void handle(@Nullable Item item);
}
