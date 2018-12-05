package de.htw.ai.kbe.storage;

import de.htw.ai.kbe.data.Song;

import static de.htw.ai.kbe.utils.Utils.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class SongsHandler implements iSongsHandler {
    private Map<Integer, Song> storage;
    private AtomicInteger counter;

    public SongsHandler() {
        this.storage = new ConcurrentHashMap<Integer, Song>();
        this.counter = new AtomicInteger();
        init();
    }

    void init() {
        List<Song> songs = loadSongs();

        for (Song song : songs) {
            storage.put(counter.getAndIncrement(), song);
        }
    }

//    void init() {
//        List<Song> songs;
//
//        try {
//            songs = readJSONToSongs("songs.json");
//
//            for(Song song : songs) {
//                storage.put(counter.getAndIncrement(), song);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }

    public Song getSong(int id) {
        return storage.get(id);
    }

    public Collection<Song> getAllSongs() {
        return storage.values();
    }

    public void addSong(Song song) {
        storage.put(counter.incrementAndGet(), song);
    }

    public boolean updateSong(Song newSong) {
        Song song = storage.remove(newSong.getId());

        if (song != null) {
            storage.put(newSong.getId(), newSong);
            return true;
        } else {
            return false;
        }
    }

    public boolean deleteSong(int id) {
        Song song = storage.remove(id);

        if (song != null) {
            return true;
        } else {
            return false;
        }
    }
}