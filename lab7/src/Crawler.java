import java.io.*;
import java.net.*;
import java.util.*;
import java.util.regex.*;

public class Crawler {

    //2 списка для ссылок и общий пулл
    private HashMap<String, URLDepthPair> links = new HashMap<>();
    private LinkedList<URLDepthPair> pool = new LinkedList<>();

    //глубина
    private int depth = 0;

    //Констурктор, инициализация
    public Crawler(String url, int depth_) {
        depth = depth_;
        pool.add(new URLDepthPair(url, 0));
    }

    //Метод объединяет другие и парсит ссылку, выводит результат
    public void run() {
        while (pool.size() > 0)
            parseLink(pool.pop());

        for (URLDepthPair link : links.values())
            System.out.println(link);

        System.out.println();
        System.out.printf("Найдено %d URLS\n", links.size());
    }

    //Паттерн для ссылок
    public static Pattern LINK_REGEX = Pattern.compile("<a\\s+(?:[^>]*?\\s+)?href=([\"'])(.*?)\\1");

    //Метод проверяет есть ли URL в ссылках, и если есть, то увелмичивает кол-во посещений для ссылки
    private void parseLink(URLDepthPair link) {
        if (links.containsKey(link.getURL())) {
            URLDepthPair knownLink = links.get(link.getURL());
            knownLink.incrementVisited();
            return;
        }
        //Добавляем
        links.put(link.getURL(), link);

        //Проверка на глубину ссылки
        if (link.getDepth() >= depth)
            return;

        //Сам запрос
        try {
            URL url = new URL(link.getURL());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");


            Scanner s = new Scanner(conn.getInputStream());
            //Ищем с данным паттерном и добавляем
            while (s.findWithinHorizon(LINK_REGEX, 0) != null) {
                String newURL = s.match().group(2);
                if (newURL.startsWith("/"))
                    newURL = link.getURL() + newURL;
                else if (!newURL.startsWith("http"))
                    continue;
                URLDepthPair newLink = new URLDepthPair(newURL, link.getDepth() + 1);
                pool.add(newLink);
            }
        } catch (Exception e) {
        }
    }
    //Ошибка
    public static void showHelp() {
        System.out.println("Нужно: <URL> <depth>");
        System.exit(1);
    }

    public static void main(String[] args) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String url = "";
        int depth = 0;

        try {
            url = reader.readLine();
            depth = Integer.parseInt(reader.readLine());
            reader.close();
        } catch (Exception e) {
            showHelp();
        }

        Crawler crawler = new Crawler(url, depth);
        crawler.run();
    }
}
