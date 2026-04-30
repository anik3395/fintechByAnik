package org.example.fintect;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class demoStream2 {
    public static void main(String[] args) {

        List<String> list = Arrays.asList("Apple", "banana", "Jack fruit");
        Stream<String> streamedList = list.stream();
        streamedList.forEach(System.out::println);

        List<Integer> listOfInteger = Arrays.asList(18, 20,20, 5, 2, 8, 1, 11);

        List<Integer> filteredOfList =
                listOfInteger.stream()
                .filter(n -> n % 2 == 0)
                .collect(Collectors.toList()); //Collect data as a list.
        System.out.println(filteredOfList);

        List<Integer> mappedOfList =
                filteredOfList.stream()
                        .map(n -> n / 2) //process data on previous data.
                        .distinct() //Prevent duplicate value.
                        .sorted((a,b) -> (b - a)) // Sorted data as ASC by default. but in here use custom DESC.
                        .skip(1) // Skip the first data from list.
                        .collect(Collectors.toList());
        System.out.println(mappedOfList);


        long totalCount =
                mappedOfList.stream()
                        .map(n -> n / 2)
                        .distinct()
                        .count(); // count() is a terminal operation

        System.out.println(totalCount);

    }
}
