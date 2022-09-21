package net.infstudio.nepio.util;

import org.junit.Test;

import static org.junit.Assert.*;

public class Trie01Test {

    @Test
    public void test1() {
        Trie01 trie = new Trie01();
        assertEquals(0, trie.mexAndInsert());
        assertEquals(1, trie.mexAndInsert());
        assertEquals(2, trie.mexAndInsert());
        assertEquals(3, trie.mexAndInsert());
        trie.insert(5);
        assertEquals(4, trie.mexAndInsert());
        assertEquals(6, trie.mexAndInsert());
        trie.remove(2);
        assertEquals(2, trie.mex());
        assertEquals(2, trie.mex());
        for (int x : trie.numbers()) {
            System.out.println(x);
        }
    }

}