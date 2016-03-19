package parohyapp.navigationtracker.Locations.dummy;

import android.util.Log;

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

    public List<ListItem> getItems(int beaconId) {
        if (beaconId == 0) {
            return items;
        } else {
            loadItems(beaconId);
            return items;
        }
    }

    public Map<String, ListItem> getItemMap(int beaconId) {
        if (beaconId == 0) {
            return itemMap;
        } else {
            loadItems(beaconId);
            return itemMap;
        }
    }

    private void loadItems(int beaconId) {
        /*
         * Do some Volley magic
         */

        //TODO: temporary dummy data
        for (int i = 1; i <= beaconId; i++) {
            addItem(createDummyItem(i));
        }
    }

    private void addItem(ListItem item) {
        items.add(item);
        itemMap.put(item.id, item);
    }

    private ListItem createDummyItem(int position) {
        return new ListItem(String.valueOf(position), "Short description " + position, "Some bullshit.");
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
