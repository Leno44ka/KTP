package com.company;

//Класс сущности  который содержит путь и глубину
public class URLDepthPair {
    private String URL;
    private int depth;

    public int getDepth()  { return depth; }
    public String getURL() { return URL; }

    public URLDepthPair(String URL, int depth){
        this.URL = URL;
        this.depth = depth;
    }

    @Override
    public String toString() {
        return "depth: " + depth + " URL: ["+ URL + "]";
    }
}