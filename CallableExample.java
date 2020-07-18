package com.ambrin.workspace;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CallableExample {

    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(30);


        String[] hostList = {"http://github.com", "http://yahoo.com", "http://www.ebay.com", "http://google.com",
                "http://spring.io", "https://paypal.com", "http://bing.com/", "http://www.udacity.com/", "http://www.coursera.com/",
                "http://www.w3schools.com/", "http://wordpress.com/", "http://wordpress.org/", "http://www.codeacademy.com/", "http://www.udemy.com/",
                "http://www.youtube.com/", "http://www.hackerrank.com/", "http://wikipedia.org/", "http://en.wikipedia.org"};

        List<Callable<Integer>> callables = Stream.of(hostList)
                .map(CallableExample::createCallable)
                .collect(Collectors.toList());



        try {
            List<Future<Integer>> futures = executor.invokeAll(callables);
            System.out.println("After invoke all");



            futures.forEach(future -> {
                try {
                    int status = future.get();
                    System.out.println("HTTP code is:" + status);

                } catch (InterruptedException e) {
                    e.printStackTrace();

                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            });

        } catch (InterruptedException ex) {
            System.out.println(ex);
        }

        System.out.println("done");
    }

    private static Callable<Integer> createCallable(String url) {
        return () -> {
            try {
                Connection.Response response = Jsoup.connect(url)
                        .method(Connection.Method.GET)
                        .timeout(3000)
                        .execute();

                System.out.println("Finished calling url: " + url);
                return response.statusCode();
            } catch (Exception e) {
                String result = "Error\t" + "Exception is: " + e.getMessage();
                System.out.println(result);

                return 500;
            }
        };
    }
}
