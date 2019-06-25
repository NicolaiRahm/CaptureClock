package tensorflow.alarm_clock.nicolai.com.roomup.objects;

import tensorflow.alarm_clock.nicolai.com.roomup.R;

public enum Room_Object {

    BATHROOM(R.string.enum_bathroom, new int[]{R.string.enum_bathroom_toilet, R.string.enum_bathroom_shower, R.string.enum_bathroom_toothbrush, R.string.enum_bathroom_waterbowl}),
    KITCHEN(R.string.enum_kitchen, new int[]{R.string.enum_kitchen_hotplate, R.string.enum_kitchen_fridge}),
    LIVINGROOM(R.string.enum_livingroom, new int[]{R.string.enum_livingroom_couch, R.string.enum_livingroom_armchair});

    private int room_name;
    private int[] objects;

    Room_Object(int room_name, int[] objects){
        this.room_name = room_name;
        this.objects = objects;
    }

    public int[] getObjects(){
        return objects;
    }

    public int getRoom_name(){
        return room_name;
    }
}
