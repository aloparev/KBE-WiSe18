package de.htw.ai.kbe.storage;

import de.htw.ai.kbe.data.Song;
import org.junit.*;

import static org.junit.Assert.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import java.util.List;
import java.util.NoSuchElementException;

import static de.htw.ai.kbe.utils.Constants.*;
import static de.htw.ai.kbe.utils.Utils.jsonToSongsList;

public class SongsDaoTest {
    static SongsHandler testHandler;
    static ISongsHandler handler;
    static EntityManagerFactory emf;
    static EntityManager em;

    @BeforeClass
    public static void setUp() {
        testHandler = new SongsHandler(jsonToSongsList("songs10.json"));
        emf = Persistence.createEntityManagerFactory(TEST_PERSISTENCE_UNIT_NAME);
        em = emf.createEntityManager();
        handler = new SongsDaoEm(em);
    }

    @After
    public void clear(){
        em.clear();
    }

    @AfterClass
    public static void close() {
        em.close();
        emf.close();
    }

    @Test
    public void init() {
        System.out.println("SongsDaoEmfTest.init.all=" + handler.getAllSongs());
        System.out.println("SongsDaoEmfTest.init.all.size=" + handler.getAllSongs().size());
        assert (testHandler.getStorage().size() == handler.getAllSongs().size());
    }

    @Test
    public void getSong() {
        int id = 2;
        Song testSong = testHandler.getSong(id);
        Song song = handler.getSong(id);

        System.out.println("SongsDaoEmfTest.getSong.test=" + testSong);
        System.out.println("SongsDaoEmfTest.getSong.real=" + song);

        assertEquals(testSong.toString(), song.toString());
    }

    @Test(expected = NoSuchElementException.class)
    public void getNonExistingSong() {
        int id = 2222;
        Song song = handler.getSong(id);
        System.out.println("SongsDaoEmfTest.getSong.real=" + song);
        assertNull(song);
    }

    @Test
    public void addUpdateDelete() {
        Song newSong = new Song("christmas", "who s the singer??", "end of the year", 1980);
        handler.addSong(newSong);
        assertEquals(11, handler.getAllSongs().size());

        int newSongId = newSong.getId();
        System.out.println("SongsDaoTest.addUpdateDelete.newSong="+handler.getSong(newSongId));

        //update
        handler.updateSong(newSongId, new Song("CHRISTMAS", "who s the singer??", "end of the year", 1980));
//        newSongId++;
        System.out.println("SongsDaoTest.addUpdateDelete.updatedSong="+handler.getSong(newSongId));

        //delete
//        handler.deleteSong(newSongId);

    }
}