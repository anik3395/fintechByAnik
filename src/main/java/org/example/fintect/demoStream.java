package org.example.fintect;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class demoStream {
    public static void main(String[] args) {
        List<Integer> nums = Arrays.asList(3,2,1,5,6,4,7,9,8);

        Stream<Integer> data = nums.stream();
        data.forEach(System.out::println);
        System.out.println("++++++++");

       nums.stream()
               .sorted()
               .filter(n -> n % 2 == 0)
               .map(n -> n * 2)
               .forEach(System.out::println);

    }
}
