![Maintenance](https://img.shields.io/maintenance/yes/2023?logo=github&style=flat)

# OmdbJava
OmdbJava is an Easy to use and Light weight Omdb API wrapper for Java

### Version : 1.0.2

## Include library in your projects
You can use Gradle or maven

### Gradle
- Add it in your root build.gradle at the end of repositories:
```gradle
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
- Add the dependency
```gradle
dependencies {
	        implementation 'com.github.fero1xd:OmdbJava:1.0.2'
	}
```

### Maven
- Add the repository in pom.xml file
```maven
    <repositories>
		<repository>
		    <id>jitpack.io</id>
		    <url>https://jitpack.io</url>
		</repository>
	</repositories>

```
- Add the dependency
```maven 

	<dependency>
	    <groupId>com.github.fero1xd</groupId>
	    <artifactId>OmdbJava</artifactId>
	    <version>1.0.1</version>
	</dependency>


```

### Jar
```
Use the Release Section
```

# Quick Start
- Search a Movie with Title
```java
import me.fero.OmdbJava;
import me.fero.exceptions.ResponseError;
import me.fero.objects.movie.Movie;

public class App {
    public static void main(String[] args) {
        // Get your API key from https://omdbapi.com/apikey.aspx
        OmdbJava omdbJava = new OmdbJava("YOUR_API_KEY");
        
        // Find movie with title (Synchronous)
        try {
            Movie movie = omdbJava.getMovieByTitle("Turning Red");
            
            System.out.println(movie.getTitle());
            System.out.println(movie.getPlot());
            System.out.println(movie.getActors());
        } catch (ResponseError e) {
            e.printStackTrace();
        }
    }
}


```
- Find movie with Title (Asynchronous)
```java
import me.fero.OmdbJava;
import me.fero.objects.Item;
import me.fero.objects.movie.Movie;

public class App {
    public static void main(String[] args) {
        // Get your API key from https://omdbapi.com/apikey.aspx
        OmdbJava omdbJava = new OmdbJava("YOUR_API_KEY");
        
        // Find movie with title (Asynchronous)
        omdbJava.getMovieByTitle("Turning Red", (Movie movie) -> {
            if(movie == null) return;

            System.out.println(movie.getTitle());
            System.out.println(movie.getPlot());
            System.out.println(movie.getActors());
        });
    }
}


```

## Initializing 
- With Api Key
- Get your API key from https://omdbapi.com/apikey.aspx
```java 
import me.fero.OmdbJava;

OmdbJava client = new OmdbJava("YOUR_API_KEY");
```
- Without API Key
```java 
import me.fero.OmdbJava;

OmdbJava client = new OmdbJava();
client.setApiKey("YOUR_API_KEY");
```

## Some Comman Methods/Functions of Question Class
- The Item class 

- Item class is the parent class for Movie And Series Class
```java
        Movie movie = client.getMovieByTitle("Turning Red");

        System.out.println(movie.getTitle()); // The title of the movie
        System.out.println(movie.getPlot()); // The Plot . Can be passed as a parameter . Eg Plot.SHORT
        System.out.println(movie.getActors()); // Gets a list of actors
        System.out.println(movie.getLanguages()); // Gets Available languages
        System.out.println(movie.getDirector()); // Gets the director of the movie
        System.out.println(movie.getRatings()); // Gets a List of Rating Object
        // etc....
```

## Search Movies/Series

```java
        Iterator iterator = client.searchMovies("Happy new Year");

        List<PartialItem> partialItems = new ArrayList<>();

        while ((partialItems = iterator.getNextPage()) != null) {
            // Loop over partial items or etc ..
        }

        iterator.getPage(); // Gets current page
        iterator.getTotalResults(); // Total 
```

## Get Movie/Series by imdb Id
```java
Movie movie = client.getMovieById("tt2372222");
Series series = client.getSeriesById("id");
```

# Using Options

```java
Series series = client.getSeriesByTitle("Squid Game", Plot.SHORT);
Series series = client.getSeriesByTitle("Money Heist", Plot.FULL); // Default
```
### Types
```java
Type.MOVIE;
Type.SERIES; 
```

### Plot
```java
Plot.FULL;
Plot.SHORT;
```


### Official Omdb API docs
[Omdb docs](https://omdbapi.com/)


### Dependencies
[JSONSimple](https://github.com/fangyidong/json-simple)
