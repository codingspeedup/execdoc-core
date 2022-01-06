package io.github.codingspeedup.execdoc.toolbox.documents.json;

import io.github.codingspeedup.execdoc.toolbox.documents.TextFileWrapper;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

/**
 * https://stleary.github.io/JSON-java/index.html?org/json/JSONObject.html
 * https://www.baeldung.com/java-org-json
 */
@NoArgsConstructor
public class JsonDocument extends TextFileWrapper {

    @Getter
    @Setter
    private int indentFactor = 2;
    private JSONArray jsonArray;
    private JSONObject jsonObject;

    public JsonDocument(File file) {
        super(file);
    }

    @Override
    protected void loadFromWrappedFile() {
        String content = getTextFile().readContentAsString();
        try {
            jsonObject = new JSONObject(content);
        } catch (JSONException e) {
            jsonArray = new JSONArray(content);
        }
    }

    public JSONArray getJSONArray() {
        if (jsonArray == null) {
            jsonArray = new JSONArray();
            jsonObject = null;
        }
        return jsonArray;
    }

    public JSONObject getJSONObject() {
        if (jsonObject == null) {
            jsonObject = new JSONObject();
            jsonArray = null;
        }
        return jsonObject;
    }

    @Override
    public String toString() {
        if (jsonObject != null) {
            return jsonObject.toString(indentFactor);
        }
        if (jsonArray != null) {
            return jsonArray.toString(indentFactor);
        }
        return null;
    }

}
