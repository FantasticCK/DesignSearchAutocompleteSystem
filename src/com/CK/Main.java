package com.CK;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;

public class Main {

    public static void main(String[] args) {
//        AutocompleteSystem auto = new AutocompleteSystem(new String[]{"i love you", "island", "ironman", "i love leetcode"}, new int[]{5, 3, 2, 2});
//        System.out.println(auto.input('i'));
//        System.out.println(auto.input(' '));
//        System.out.println(auto.input('a'));
//        System.out.println(auto.input('#'));
//        System.out.println(auto.input('i'));
//        System.out.println(auto.input(' '));
//        System.out.println(auto.input('a'));
//        System.out.println(auto.input('#'));
//        System.out.println(auto.input('i'));
//        System.out.println(auto.input(' '));
//        System.out.println(auto.input('a'));
//        System.out.println(auto.input('#'));

        AutocompleteSystem auto = new AutocompleteSystem(new String[]{"abc","abbc","a"}, new int[]{3,3,3});
        System.out.println(auto.input('b'));
        System.out.println(auto.input('c'));
        System.out.println(auto.input('#'));
        System.out.println(auto.input('b'));
        System.out.println(auto.input('c'));
        System.out.println(auto.input('#'));
        System.out.println(auto.input('a'));
        System.out.println(auto.input('b'));
        System.out.println(auto.input('c'));
        System.out.println(auto.input('#'));
        System.out.println(auto.input('a'));
        System.out.println(auto.input('b'));
        System.out.println(auto.input('c'));
        System.out.println(auto.input('#'));
        System.out.println("");
    }
}

class AutocompleteSystem {
    TrieNode root;
    StringBuilder input;
    List<String> inputRes;
    PriorityQueue<String[]> heap;

    public AutocompleteSystem(String[] sentences, int[] times) {
        root = new TrieNode();
        input = new StringBuilder();
        if (sentences.length != 0 && sentences.length == times.length) {
            for (int i = 0; i < sentences.length; i++) {
                insert(root, sentences[i], times[i], 0);
            }
        }
    }

    public List<String> input(char c) {
        heap = new PriorityQueue<>((o1, o2) -> {
            int o1Time = Integer.parseInt(o1[1]), o2Time = Integer.parseInt(o2[1]);
            if (o1Time != o2Time) return o2Time - o1Time;
            else {
                return o1[0].compareTo(o2[0]);
            }
        });
        inputRes = new ArrayList<>();
        if (c == '#') {
            insert(root, input.toString(), 1, 0);
            input = new StringBuilder();
            inputRes.clear();
            return inputRes;
        }
        input.append(c);
        search(root, input.toString(), 0);
        for (int i = 0; i < 3; i++) {
            if (!heap.isEmpty()) inputRes.add(heap.poll()[0]);
        }
        return inputRes;
    }

    private void insert(TrieNode root, String s, int time, int index) {
        if (index >= s.length()) {
            root.time += time;
            return;
        }
        TrieNode curr = null;
        if (!root.children.isEmpty()) {
            for (TrieNode neighbor : root.children) {
                if (neighbor.val == s.charAt(index)) {
                    curr = neighbor;
                    break;
                }
            }
        }
        if (root.children.isEmpty() || curr == null) {
            curr = root;
            TrieNode child = new TrieNode(s.charAt(index), new ArrayList<>());
            root.children.add(child);
            curr = child;
            insert(curr, s, time, index + 1);
        } else insert(curr, s, time, index + 1);
    }

    private void search(TrieNode root, String s, int index) {
        if (index >= s.length()) {
            StringBuilder sb = new StringBuilder(input);
            dfsCurrNode(root, sb);
            return;
        }
        if (root.children.isEmpty()) return;
        TrieNode curr = null;
        for (TrieNode child : root.children) {
            if (child.val == s.charAt(index)) {
                curr = child;
                break;
            }
        }
        if (curr == null) return;
        search(curr, s, index + 1);
    }

    private void dfsCurrNode(TrieNode root, StringBuilder sb) {
        if (root.time > 0) heap.offer(new String[]{sb.toString(), String.valueOf(root.time)});
        if (root.children.isEmpty()) return;
        TrieNode curr = root;
        for (TrieNode child : root.children) {
            sb.append(child.val);
            curr = child;
            dfsCurrNode(curr, sb);
            sb.deleteCharAt(sb.length() - 1);
        }
    }

    class TrieNode {
        char val;
        int time;
        List<TrieNode> children;

        TrieNode() {
            this.time = 0;
            this.children = new ArrayList<>();
        }

        TrieNode(char val, List<TrieNode> children) {
            this.val = val;
            this.time = 0;
            this.children = children;
        }

    }
}