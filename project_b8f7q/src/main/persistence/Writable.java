package persistence;

import org.json.JSONObject;

public interface Writable {
    // Code from Professor Paul Carter https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo/tree/master/lib
    // EFFECTS: returns this as JSON object
    JSONObject toJson();
}
