public class URLDepthPair {
    private String url;
    private int depth;
    private int visited;
    public URLDepthPair(String url_, int depth_) {
        url = url_;
        depth = depth_;
        visited = 1;
    }

    //Возврат URL
    public String getURL() {
        return url;
    }

    //Возврат depth
    public int getDepth() {
        return depth;
    }

    //Увеличиваем кол-во посещенных ссылок
    public void incrementVisited() {
        visited++;
    }

    //Вывод
    public String toString() {
        return "<URL href=\"" + url + "\" visited=\"" + visited + "\" depth=\"" + depth + "\" \\>";
    }
}