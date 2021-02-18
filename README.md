# Rank My Bits League Games

Just a fun ranking exercise.

This simple command line application read league results from either a file, or from `stdin` to produce ranking based on a simple scoring rule:

- A win scores 3 points
- A tie scores 1 point
- A loss scores 0 points

For a more in depth description please consult the assessment PDF via this [link](BE%20Coding%20Test%20-%20Candidate.pdf).

## Client Requirements

- Any if the latest Windows, macOS, or Linux operating systems
- OpenJdk 8, or Oracle JDK 8 (also runs against JDK 11)

## How to Build

1. Clone this project from this repo.
2. Open a terminal.
3. Navigate to the where you cloned the repo.
3. Execute the following commands: 
    ```shell
   ./gradlew installDist
    ```
## Installation

From the build directory, copy the `build\install\rankmybits` folder to your application folder.

## How to execute

Execute the application with the following command: 

```shell
.\rankmybits\bin\rankmybits 
```


