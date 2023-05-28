package pl.javastart.task;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAccessor;
import java.util.*;

import static java.lang.Integer.sum;

public class Main {

    public static void main(String[] args) {
        Main main = new Main();
        main.run(new Scanner(System.in));
    }

    public void run(Scanner scanner) {
        // uzupełnij rozwiązanie. Korzystaj z przekazanego w parametrze scannera

        System.out.println("Podaj datę:");
        String userInput = scanner.nextLine();

        LocalDateTime localDateTime = null;
        try {
            localDateTime = createLocalDateTimeFromDate(userInput);
        } catch (DateTimeParseException e) {
            //
        }
        if (localDateTime == null) {
            localDateTime = createLocalDateTimeFromPattern(userInput);
        }
        if (localDateTime == null) {
            localDateTime = createModifiedLocalDateTime(userInput);
        }
        if (localDateTime == null) {
            System.out.println("Nieprawidlowy format daty.");
        } else {
            printAllTimeInZones(localDateTime);
        }
    }

    private LocalDateTime createModifiedLocalDateTime(String userInput) {
        LocalDateTime t = LocalDateTime.now();

        List<Character> keys = Arrays.asList('y', 'M', 'd', 'h', 'm', 's');
        Map<Character, Integer> mapOfAllParameters = new HashMap<>();
        for (Character key : keys) {
            mapOfAllParameters.put(key, 0);
        }

        Map<Integer, Character> mapOfUsedParameters = new HashMap<>();
        for (int i = 1; i < userInput.length(); i++) {
            for (Character key : keys) {
                if (userInput.charAt(i) == key) {
                    mapOfUsedParameters.put(i, key);
                }
            }
        }

        List<Integer> list = new ArrayList<>(mapOfUsedParameters.keySet().stream().toList());
        Collections.sort(list);

        int indexStart = 1;
        for (Integer element : list) {
            String stringValue = userInput.substring(indexStart, element);
            mapOfAllParameters.get(mapOfUsedParameters.get(element));
            Integer newValue = sum(mapOfAllParameters.get(mapOfUsedParameters.get(element)), Integer.parseInt(stringValue));
            mapOfAllParameters.put(mapOfUsedParameters.get(element), newValue);
            indexStart = element + 1;

        }

        LocalDateTime modified;
        int yearsToAdd = mapOfAllParameters.get('y');
        int monthToAdd = mapOfAllParameters.get('M');
        int daysToAdd = mapOfAllParameters.get('d');
        int hoursToAdd = mapOfAllParameters.get('h');
        int minutesToAdd = mapOfAllParameters.get('m');
        int secondsToAdd = mapOfAllParameters.get('s');
        modified = t.plusYears(yearsToAdd)
                .plusMonths(monthToAdd)
                .plusDays(daysToAdd)
                .plusHours(hoursToAdd)
                .plusMinutes(minutesToAdd)
                .plusSeconds(secondsToAdd);
        return modified;
    }

    private LocalDateTime createLocalDateTimeFromDate(String userInput) {
        DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        TemporalAccessor temporalAccessor = pattern.parse(userInput);
        LocalDate localDate = LocalDate.from(temporalAccessor);

        return localDate.atTime(0, 0, 0);
    }

    private LocalDateTime createLocalDateTimeFromPattern(String userInput) {
        List<String> listOfPatterns = Arrays.asList("yyyy-MM-dd HH:mm:ss", "dd.MM.yyyy HH:mm:ss");
        TemporalAccessor temporalAccessor;
        LocalDateTime localDateTime = null;
        for (String pat : listOfPatterns) {
            try {
                DateTimeFormatter pattern = DateTimeFormatter.ofPattern(pat);
                temporalAccessor = pattern.parse(userInput);
                localDateTime = LocalDateTime.from(temporalAccessor);

            } catch (DateTimeParseException e) {
                //
            }
        }
        return localDateTime;
    }

    private void printAllTimeInZones(LocalDateTime localDateTime) {
        TimeZone localTimeZone = TimeZone.getDefault();
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        System.out.print("Czas lokalny: ");
        System.out.println(localDateTime.format(formatter));
        ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.of(localTimeZone.getID()));
        printTimeInZone(zonedDateTime, "UTC", "UTC: ", formatter);
        printTimeInZone(zonedDateTime, "Europe/London", "Londyn: ", formatter);
        printTimeInZone(zonedDateTime, "America/Los_Angeles", "Los Angeles: ", formatter);
        printTimeInZone(zonedDateTime, "Australia/Sydney", "Sydney: ", formatter);
    }

    private void printTimeInZone(ZonedDateTime zonedDateTime, String zoneId, String zoneName, DateTimeFormatter formatter) {
        ZonedDateTime inZone = zonedDateTime.withZoneSameInstant(ZoneId.of(zoneId));
        System.out.print(zoneName);
        System.out.println(inZone.format(formatter));
    }
}