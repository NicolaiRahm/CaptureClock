package tensorflow.alarm_clock.nicolai.com.roomup.objects;

import androidx.room.TypeConverter;

public class DaysConverter {

    @TypeConverter
    public static Days toDays(String values){
        boolean monday = values.charAt(0) == 'y';
        boolean tuesday = values.charAt(1) == 'y';
        boolean wednesday = values.charAt(2) == 'y';
        boolean thursday = values.charAt(3) == 'y';
        boolean friday = values.charAt(4) == 'y';
        boolean saturday = values.charAt(5) == 'y';
        boolean sunday = values.charAt(6) == 'y';

        return new Days(monday, tuesday, wednesday, thursday, friday, saturday, sunday);
    }

    @TypeConverter
    public static String fromDays(Days days){
        StringBuilder values = new StringBuilder("nnnnnnn");

        values.setCharAt(0, booleanToChar(days.isMonday()));
        values.setCharAt(1, booleanToChar(days.isTuesday()));
        values.setCharAt(2, booleanToChar(days.isWednesday()));
        values.setCharAt(3, booleanToChar(days.isThursday()));
        values.setCharAt(4, booleanToChar(days.isFriday()));
        values.setCharAt(5, booleanToChar(days.isSaturday()));
        values.setCharAt(6, booleanToChar(days.isSunday()));

        return values.toString();
    }

    private static char booleanToChar(boolean b){
       return b ? 'y' : 'n';
    }
}
