//package com.garrisonthomas.junkapp;
//
//import java.io.BufferedReader;
//import java.io.InputStreamReader;
//
//public class TestClass {
//
//    public static void main(String a[]) throws Exception {
//        String strn;
//        int flag=0;
//        System.out.println("Enter the string:");
//        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
//        strn=br.readLine();
//        char[] charStrn = strn.toCharArray();
//        System.out.println("Result string is:");
//        //write your logic here
//        int i1 = 0;
//        int i2 = charStrn.length-1;
//
//        while (i2 > i1) {
//            if (charStrn[i1] != charStrn[i2]){
//                flag = 0;
//            } else {
//                i1++;
//                i2--;
//            }
//        }
//
//
//
//
//
//
//        //end
//        if(flag==1)
//            System.out.print("palindrome");
//        else
//            System.out.print("not a palindrome2");
//
//    }
//}
