Use this function, specify your district and lat lng ->

cities.geojson is a file with all districts and their multipolygon ( ref -> https://www.kaggle.com/datasets/adityaradha007/indian-districts-geojson?resource=download )

```    
public static void main(String[] args) {
    CityChecker cityChecker = new CityChecker("cities.geojson"); //refer according to your path
    boolean isWithinCity = cityChecker.isCoordinateInCity("Bangalore", 12.9716, 77.5946);
    System.out.println("Coordinate is within city: " + isWithinCity);
}
```
