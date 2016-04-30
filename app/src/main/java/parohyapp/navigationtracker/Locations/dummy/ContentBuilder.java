package parohyapp.navigationtracker.Locations.dummy;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p/>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class ContentBuilder {

    private JSONObject beacons;
    private List<ListItem> items;
    private Map<String, ListItem> itemMap;

    private static final ContentBuilder INSTANCE = new ContentBuilder();

    private ContentBuilder() {
        items = new ArrayList<>();
        itemMap = new HashMap<>();
    }

    public static ContentBuilder getInstance() {
        return INSTANCE;
    }

    public void setBeacons(JSONObject beacons) {
        this.beacons = beacons;
    }

    public List<ListItem> getItems() {
        if (beacons != null) {
            loadItems();
        }
        return items;
    }

    public Map<String, ListItem> getItemMap() {
        if (beacons != null) {
            loadItems();
        }
        return itemMap;
    }

    private void loadItems() {
        items.clear();
        itemMap.clear();
        try {
            for (int i = 0; i < beacons.length(); i++) {
                addItem(createDummyItem(beacons.getJSONObject("location" + i)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void addItem(ListItem item) {
        items.add(item);
        itemMap.put(item.id, item);
    }

    private ListItem createDummyItem(JSONObject location) throws JSONException {
        return new ListItem(location.getString("name"), location.getString("shortDescription"), location.getString("description"));
    }

    public class ListItem {
        public final String id;
        public final String content;
        public final String details;

        public ListItem(String id, String content, String details) {
            this.id = id;
            this.content = content;
            this.details = details;
        }

        @Override
        public String toString() {
            return content;
        }
    }
}
