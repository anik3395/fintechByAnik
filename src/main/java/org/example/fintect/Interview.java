package org.example.fintect;

import java.util.Scanner;

public class Interview {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        int count = 0;

        while (count < 3) {

            System.out.print("Enter Password: ");
            String password = sc.nextLine();

            if (password.equals("12345")) {
                System.out.println("Password is correct");
                return; // Program ends
            } else {
                count++;
                System.out.println("Wrong Password");
            }
        }

        System.out.println("User Blocked");
    }
}
