# Smelly Cat - Breeds Images Gallery

## Architectural choices
The project is structured using a simple implementation of the `MVI` architecture, 
which transforms Intents (interactions) into a view state which will be rendered via `Jetpack Compose`, 
toolkit for building native UI in Android and `Kotlin Asynchronous Flow`.

I have chosen MVI architectural pattern for a few reasons:
1. It offers single source of truth (`SSOT`) (the view state) unlike `MVVM` architecture, where ViewModel (`VM`) can have multiple streams (`LiveData`, `StateFlow`, others.)
2. It's easier to debug, since it only deals with a `SSOT`.

`MVI` actors:
- `Reducer`: responsible for holding and processing state.
- `VM`: fetches data from the data sources, presentation layer business-logic, and handles events from the UI layer transforming them into Intentions passed to the `Reducer`.

This project also uses `Clean architecture` (layered) for better separation of concerns and easier refactoring.

This approach introduces a little bit more of boilerplate compared to other architectural options (read MVVM), but it gives a lot of flexibility and agility at scale.

## The app

The app is an implementation of the [thecatapi.com](https://thecatapi.com).
Single screen app, displays a Breed selector (drop down UI component).
| Breed selector | Breeds list |
| --- | --- |
|![Breed selector](/screen_1.png)|![Breed selector](/screen_2.png)|

The app displays an indicator placed on toolbar, to indicate online/offline status.
The app works in 2 modalities: 
- `online `
- `offline`

### Online
Upon selection, data are fetched from remote data source.
Once selected breed images are retrieved, they are laid out in a pager UI component. 
More information about the selected breed are also displayed.

<figure>
    <img src="/screen_3.png" width="300"/>
    <figcaption>Bengal breed images.</figcaption>
</figure>

### Offline
1. If the app does not have data stored on database, we simply display an empty state.
2. Otherwise, local data will be loaded from the database.
   Note: The UI component to load remote images caches recent data, so you will be able to display recently selected breed images while offline.

<figure>
    <img src="/screen_4.png" width="300"/>
    <figcaption>Empty state.</figcaption>
</figure>

## Next steps
1. Add unit tests
2. Run data analysis to assess whether an image can be associated to multiple breeds. This might influence persistence strategies.
3. Update UI layer to dynamically determine `page`/`limit`/`order` parameters in `image/search` request.
4. Integrate Crash reporting (`Crashlytics`, `Sentry`, others).
5. Integrate custom Theme.
6. Integrate more features 
   1. Details page to show info about cat's breed preferred food, live in environment, etc. (Will need addition data sources).
   2. Search bar to search up cross-breed criteria of interest (cats with specific temperament, etc.).