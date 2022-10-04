package net.infstudio.nepio.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Used to find the MEX.
 */
public class Trie01 {

    private TrieNode root;

    public Trie01() {
        root = new TrieNode(null);
    }

    public void clear() {
        root = new TrieNode(null);
    }

    public void insert(int x) {
        TrieNode p = root;
        for (int i = 30; i >= 0; --i) {
            int k = (x >> i) & 1;
            if (p.child[k] == null) p.child[k] = new TrieNode(p);
            p = p.child[k];
        }
        while (p.parent != null) {
            p.size++;
            p = p.parent;
        }
    }

    public boolean contain(int x) {
        TrieNode p = root;
        for (int i = 30; i >= 0; --i) {
            int k = x >> i;
            if (p.child[k] == null) return false;
            p = p.child[k];
        }
        return true;
    }

    public int mex() {
        TrieNode p = root;
        int result = 0;
        for (int i = 30; i >= 0; --i) {
            if (p == null) break;
            int sum = 1 << i;
            if (p.child[0] == null) break;
            if (p.child[0].size < sum) {
                p = p.child[0];
            } else {
                result += sum;
                p = p.child[1];
            }
        }
        return result;
    }

    public int mexAndInsert() {
        int x = mex();
        insert(x);
        return x;
    }

    public void remove(int x) {
        TrieNode p = root;
        for (int i = 30; i >= 0; --i) {
            int k = (x >> i) & 1;
            if (p.child[k] == null) return;
            p = p.child[k];
        }
        while (p.parent != null) {
            p.size--;
            p = p.parent;
        }
    }

    private void getNumbers(TrieNode cur, List<Integer> list, int depth, int sum) {
        if (depth == -1) {
            list.add(sum);
            return;
        }
        if (cur.child[0] != null)
            if (cur.child[0].size > 0)
                getNumbers(cur.child[0], list, depth-1, sum);
        if (cur.child[1] != null)
            if (cur.child[1].size > 0)
                getNumbers(cur.child[1], list, depth-1, sum+(1 << depth));
    }

    public List<Integer> numbers() {
        List<Integer> result = new ArrayList<>();
        getNumbers(root, result, 30, 0);
        return result;
    }

    private class TrieNode {

        TrieNode parent;
        TrieNode[] child;
        int size;

        public TrieNode(TrieNode parent) {
            this.parent = parent;
            this.child = new TrieNode[]{null, null};
            this.size = 0;
        }

    }

}
