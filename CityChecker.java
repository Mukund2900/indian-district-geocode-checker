import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;

public class CityChecker {

    private JSONObject geoJson;

    public CityChecker(String filePath) {
        try {
            JSONParser parser = new JSONParser();
            FileReader reader = new FileReader(filePath);
            this.geoJson = (JSONObject) parser.parse(reader);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isCoordinateInCity(String cityName, double latitude, double longitude) {
        JSONArray features = (JSONArray) geoJson.get("features");

        for (Object featureObj : features) {
            JSONObject feature = (JSONObject) featureObj;
            JSONObject properties = (JSONObject) feature.get("properties");
            String dtname = (String) properties.get("dtname");

            if (dtname.equalsIgnoreCase(cityName)) {
                JSONObject geometry = (JSONObject) feature.get("geometry");
                JSONArray coordinates = (JSONArray) geometry.get("coordinates");
                JSONArray firstCoordinates = (JSONArray) coordinates.get(0);

                if (isPointInPolygon(latitude, longitude, firstCoordinates)) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean isPointInPolygon(double latitude, double longitude, JSONArray polygonCoordinates) {
        int i;
        int j;
        boolean isInside = false;
        double x = longitude;
        double y = latitude;

        int verticesCount = polygonCoordinates.size();
        for (i = 0, j = verticesCount - 1; i < verticesCount; j = i++) {
            double xi = ((Number) ((JSONArray) polygonCoordinates.get(i)).get(0)).doubleValue();
            double yi = ((Number) ((JSONArray) polygonCoordinates.get(i)).get(1)).doubleValue();
            double xj = ((Number) ((JSONArray) polygonCoordinates.get(j)).get(0)).doubleValue();
            double yj = ((Number) ((JSONArray) polygonCoordinates.get(j)).get(1)).doubleValue();

            boolean intersect = ((yi > y) != (yj > y)) &&
                    (x < (xj - xi) * (y - yi) / (yj - yi) + xi);
            if (intersect) isInside = !isInside;
        }

        return isInside;
    }


    public static void main(String[] args) {
        CityChecker cityChecker = new CityChecker("cities.geojson"); //refer according to your path
        boolean isWithinCity = cityChecker.isCoordinateInCity("Bangalore", 12.9716, 77.5946);
        System.out.println("Coordinate is within city: " + isWithinCity);
    }
}
