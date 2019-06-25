package tensorflow.alarm_clock.nicolai.com.roomup.objects;

public class Days {
    private boolean monday, tuesday, wednesday, thursday, friday, saturday, sunday;


    //Für converter
    public Days(boolean monday, boolean tuesday, boolean wednesday, boolean thursday, boolean friday, boolean saturday, boolean sunday) {
        this.monday = monday;
        this.tuesday = tuesday;
        this.wednesday = wednesday;
        this.thursday = thursday;
        this.friday = friday;
        this.saturday = saturday;
        this.sunday = sunday;
    }

    //Default initialisation
    public Days(){}

    //Getter
    public boolean isMonday() {
        return monday;
    }

    public boolean isTuesday() {
        return tuesday;
    }

    public boolean isWednesday() {
        return wednesday;
    }

    public boolean isThursday() {
        return thursday;
    }

    public boolean isFriday() {
        return friday;
    }

    public boolean isSaturday() {
        return saturday;
    }

    public boolean isSunday() {
        return sunday;
    }

    //Changer
    public void changeMonday(){
        monday = !monday;
    }

    public void changeTuesday(){
        tuesday = !tuesday;
    }

    public void changeWednesday(){
        wednesday = !wednesday;
    }

    public void changeThursday(){
        thursday = !thursday;
    }

    public void changeFriday(){
        friday = !friday;
    }

    public void changeSaturday(){
        saturday = !saturday;
    }

    public void changeSunday(){
        sunday = !sunday;
    }

    //At least one day
    public boolean atLeastOne(){
        if(monday || tuesday || wednesday || thursday || friday || saturday || sunday){
            return true;
        }

        return false;
    }

    //UnCheck if !weekly after the alarm has gone of
    public void unCheck(String thisDay){
        switch (thisDay){
            case "Montag":  changeMonday(); break;
            case "Dienstag": changeTuesday(); break;
            case "Mittwoch": changeWednesday(); break;
            case "Donnerstag": changeThursday(); break;
            case "Freitag": changeFriday(); break;
            case "Samstag": changeSaturday(); break;
            case "Sonntag": changeSunday(); break;
        }
    }
}
