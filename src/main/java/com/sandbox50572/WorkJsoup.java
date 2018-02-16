package com.sandbox50572;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

public class WorkJsoup implements Runnable{
    //Конструктор
    public WorkJsoup() {

    }

    public void run() {

        try {
            //for(int i=10; i>0; i--) {
            //System.out.println("this 2 thread");
            choiceYorN();

            Thread.sleep(5000);
            //}
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void choiceYorN() throws IOException {
        System.out.println("This 2 tread");
        System.out.println("to search in yandex hit: S");
        System.out.println("if you need a new analysis hit: P");
        System.out.println("Do you want to download the file? Y / N (Quit)");//Хотите скачать файл?
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String s = br.readLine();
        if (s.equals("Y")) {
            String strUrl;
            String file;
            System.out.println("Enter the URL for the download.");//введите url загружаемого файла
            strUrl = br.readLine();
            System.out.println("Enter the file name.");//введите имя файла
            file = br.readLine();
            int buffSize = 512;//TODO Размер буфера байт
            downloadUsingStream(strUrl, file, buffSize);
        } else if (s.equals("N")) {
            System.out.println("Never give up!");

        } else if (s.equals("P")) {
            writeTheUrl();
        } else if (s.equals("S")) {
            System.out.println("Введите запрос :");
            String request = br.readLine();
            searchYandex(request);
        } else {
            System.out.println("You have entered an invalid command.\nYou can try again.");
            tryAgain();
        }
    }
    private void tryAgain() throws IOException {//переключатель
        choiceYorN();
    }
    private void writeTheUrl() {
        String url = null;
        String whatToFind = null;
        System.out.println("write the url where to look?");//напишите url где искать
        try {
            BufferedReader br2 = new BufferedReader(new InputStreamReader(System.in));
            url = br2.readLine();
            // br2.close();

            System.out.println("what do you want to find?");//что вы хотите найти

            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            whatToFind = br.readLine();

        } catch (IOException e) {
            e.printStackTrace();
        }
        urlRes(url, whatToFind);

    }
    private void urlRes(String url, String whatToFind) {

        ArrayList<String> listLinks = new ArrayList<String>();
        // String whatToFind = whatToFind();//вызов метода записи параметров поиска
        int a = 0;//счетчик совпадений
        long time = System.currentTimeMillis();
        String fileName = time + "logParse.htm";
        String file2 = time + "logLinks.htm";
        File file = new File(fileName);//файл создается в рабочем каталоге
        File f2 = new File(file2);

        try {
            /*
            Connection.Response res = (Connection.Response) Jsoup.connect("http://echo.msk.ru").data("loginField", "ivanlykov702@mail.ru", "1234cani").method(Connection.Method.POST).execute();
            // получаем куки
            Map<String, String> loginCookies = res.cookies();
            org.jsoup.nodes.Document doc = res.parse();
            String sessionId = res.cookie("SESSIONID");
            */

            org.jsoup.nodes.Document doc1 = Jsoup.connect(url).get();
            String name = doc1.title();//извлекаем title страницы
            System.out.println("Name of page HTML: " + name);
            Element mBody = doc1.body();
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            bw.write(String.valueOf(doc1));
            bw.close();



                BufferedWriter bw2 = new BufferedWriter(new OutputStreamWriter(//TODO Пишет в файл кириллицу неадекватно!!!
                        new FileOutputStream(f2), "UTF8"));
                bw2.write("<!DOCTYPE html><HTML><HEAD><meta charset=\"utf-8\"><TITLE>" + name + " Links</TITLE></HEAD><BODY>");
                Elements links = mBody.getElementsByTag("a");//получение ссылок страницы

                for (Element found1 : links) {
                    if (found1.toString().contains(whatToFind)) {//поиск объекта
                        listLinks.add(String.valueOf(found1));//добавление найденых объектов в ArrayList
                        System.out.println(found1);
                        bw2.write("<P>");
                        bw2.write(String.valueOf(found1));//запись в файл + пробел перевод строки
                        bw2.write("\r\n");
                        a++;
                    }

                }

                if (a == 0) {
                    System.out.println("No matches found.");//совпадения не найдены
                    System.out.println("You can change search parameters: C, or print all links: A.");//попробуйте изменить параметры поиска или вывести все ссылки
                    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                    String s = br.readLine();
                    if (s.equals("C")) {
                        tryAgain();
                    } else if (s.equals("A")) {
                        for (Element found1 : links) {
                            System.out.println(found1);
                        }
                        tryAgain();
                    } else {
                        tryAgain();
                    }

                }

                System.out.println("Found " + a + " matches");//найдено
                Date date = new Date();
                System.out.println(date);//вывод текущих даты и времени
                bw2.write(date + "</BODY></HTML>");
                bw2.close();

                System.out.println("Will You need see all links? Y/N:");//просмотр всех ссылок
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                String s = br.readLine();
                if (s.equals("Y")) {
                    for (Element link : links) {
                        System.out.println(link.text());
                    }

                } else if (s.equals("N")) {
                    tryAgain();//возврат к началу
                } else {
                    System.out.println("Invalid command!");
                    tryAgain();
                }
                tryAgain();//возврат к началу
                // bw2.flush();

        } catch (IOException ex) {
            // Logger.getLogger(UnicodeWriteExample.class.getName())
            //         .log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }

    }
    /*
   TODO МЕТОД СКАЧИВАНИЯ ФАЙЛА
    */
    private void downloadUsingStream(String strUrl, String file, int buffSize) throws IOException {
        URL url = new URL(strUrl);
        BufferedInputStream bis = new BufferedInputStream(url.openStream());
        FileOutputStream fos = new FileOutputStream(file);
        int count = 0;
        byte buffer[] = new byte[buffSize];
        while((count = bis.read(buffer, 0, buffSize)) !=1) {
            fos.write(buffer, 0, count);
        }
        fos.close();
        bis.close();
    }
    //TODO Поиск в Яндекс

    public void searchYandex(String request) throws IOException {

        //int count = 0;
        ArrayList<String> listLinks = new ArrayList<String>();
        String line = null;

        org.jsoup.nodes.Document doc1 = Jsoup.connect("http://yandex.ru/search?text=" + request).userAgent
                ("Mozilla/5.0 (Windows; U; Windows NT 5.1; ru; rv:1.9.2) Gecko/20100115 Firefox/3.6").get();
        //Elements yandexind = doc1.select("div[class=serp-adv__found]");
        org.jsoup.nodes.Element mBody = doc1.body();
        Elements links = mBody.getElementsByTag("a");
        for(org.jsoup.nodes.Element link : links) {
            line = link.toString();
            listLinks.add(line);
            System.out.println(link.text());
        }
        System.out.println(" * * * * * * * * * *");
        System.out.println(listLinks.size());
        System.out.println(" * * * * * * * * * *");
        for(int i=0; i<listLinks.size(); i++) {
            System.out.println(listLinks.get(i));
        }
        tryAgain();
    }
}
