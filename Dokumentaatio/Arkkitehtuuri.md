
# Ruutu

Jokainen ruutu tai näkymä koostuu yhdestä tai useammasta `@Composable`-funktiosta, joiden ui-tilat ovat `ViewModel`-luokissa. Muutokset ViewModeleissa oleviin ui-tiloihin aiheuttavat uudelleenkokoonpanon, mikä mahdollistaa sovelluksen reaaliaikaisen päivittymisen käyttäjän toimien mukaan.


```mermaid
classDiagram
    Screen "*" -- "*" ViewModel
    ViewModel -- SokerihiiriRepository
    ViewModel -- DataStoreManager
    SokerihiiriRepository -- SokerihiiriRoomDatabase
    class Screen{
        @Composable
    }
    class ViewModel{
        @HiltViewModel
    }
    class SokerihiiriRepository{
    }
    class SokerihiiriRoomDatabase {
        +BloodSugarMeasurementDao
        +InsulinInjectionDao
        +MealDao
        +OtherDao
    }
    class DataStoreManager{
    }
```
