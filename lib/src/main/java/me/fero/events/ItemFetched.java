package me.fero.events;

import me.fero.objects.Item;

import javax.annotation.Nullable;

public interface ItemFetched<T extends Item> {
   void handle(@Nullable T item);
}
