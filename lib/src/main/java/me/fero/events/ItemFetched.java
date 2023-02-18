package me.fero.events;

import javax.annotation.Nullable;

public interface ItemFetched<T> {
   void handle(@Nullable T item);
}

