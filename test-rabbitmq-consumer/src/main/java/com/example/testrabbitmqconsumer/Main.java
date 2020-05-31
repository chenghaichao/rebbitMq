package com.example.testrabbitmqconsumer;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Main {
     public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
                 RPCClient fibonacciRpc = new RPCClient();

                 System.out.println(" [x] Requesting fib(30)");
         for(int i = 0; i < 100; i++){
             String response = fibonacciRpc.call(i+"---");
             fibonacciRpc.close();
                 }

             //    System.out.println(" [.] Got '" + response + "'");


             }

       }
