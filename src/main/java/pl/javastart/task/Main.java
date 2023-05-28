package pl.javastart.task;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAccessor;
import java.util.*;

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
        Set<Character> keys = new LinkedHashSet<>(Arrays.asList('y', 'M', 'd', 'h', 'm', 's'));
        Map<Character, Integer> map = new LinkedHashMap<>();

        int indexStart = 1;
        for (Character key : keys) {
            if (userInput.contains(String.valueOf(key))) {
                String stringValue = userInput.substring(indexStart, userInput.indexOf(String.valueOf(key)));
                map.put(key, Integer.valueOf(stringValue));
                indexStart = userInput.indexOf(String.valueOf(key)) + 1;
            } else {
                map.put(key, 0);
            }
        }

        LocalDateTime modified;
        int yearsToAdd = map.get('y');
        int monthToAdd = map.get('M');
        int daysToAdd = map.get('d');
        int hoursToAdd = map.get('h');
        int minutesToAdd = map.get('m');
        int secondsToAdd = map.get('s');
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