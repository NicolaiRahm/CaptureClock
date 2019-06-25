package tensorflow.alarm_clock.nicolai.com.roomup.utils;

import android.content.Context;
import tensorflow.alarm_clock.nicolai.com.roomup.R;
import tensorflow.alarm_clock.nicolai.com.roomup.objects.Room_Object;
import java.util.Random;

public class RoomUtil {

    public static String[] randomRoomWithObject(Context context){

        //Random room
        Room_Object[] roomArray = Room_Object.values();
        Room_Object rdmRoom = roomArray[new Random().nextInt(roomArray.length)];
        String room = context.getString(rdmRoom.getRoom_name());

        //Random object (int id for the string)
        int[] objectsArray = rdmRoom.getObjects();
        int rdmObject = objectsArray[new Random().nextInt(objectsArray.length)];
        String object = context.getString(rdmObject);

        String[] generated = {object, context.getString(R.string.room_with_object, room, object)};
        return generated;
    }

    public static String getLabelTranslation(Context context, String lable){

        String result = "";
        switch (lable){
            case "toilet": result = context.getString(R.string.enum_bathroom_toilet); break;
            case "shower": result = context.getString(R.string.enum_bathroom_shower); break;
            case "toothbrush": result = context.getString(R.string.enum_bathroom_toothbrush); break;
            case "washbowl": result = context.getString(R.string.enum_bathroom_waterbowl); break;
            case "hotplate": result = context.getString(R.string.enum_kitchen_hotplate); break;
            case "fridge": result = context.getString(R.string.enum_kitchen_fridge); break;
            case "couch": result = context.getString(R.string.enum_livingroom_couch); break;
            case "armchair": result = context.getString(R.string.enum_livingroom_armchair); break;
            case "waschs dasch": result = context.getString(R.string.enum_room_nothing); break;
        }

        return result;
    }

    public static String displayedImageClassification(Context context, String label, float value){

        String object = RoomUtil.getLabelTranslation(context, label);

        /*if(object.equals(context.getString(R.string.enum_room_nothing))){
            return String.format("%s\n", object);
        }else{
            String confidencePercentage = String.format("%4.0f", value*100) + "%";
            return String.format("%s: %s\n", object, confidencePercentage);
        }*/

        String confidencePercentage = String.format("%4.0f", value*100) + "%";
        return String.format("%s: %s\n", object, confidencePercentage);
    }
}
