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
