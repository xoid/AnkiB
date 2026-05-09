# AnkiB Architecture Specification

## 1. Core Data Structures

### F (File Metadata)
- **Properties**: `filename`, `path`, `base`, `suffix`, `ext`.
- **Logic**: Parses full paths (e.g., `dir/horse_en.mp3` -> base:`horse`, suffix:`en`).

### AFile (Audio File Wrapper)
- **Properties**: `f: F`.
- **Methods**: `play(player, speed)`.
- **Sorting**: Alphabetical by `f.suffix`.

### Word (The Learning Unit)
- **Properties**: `base: String`, `box: Int` (Leitner box 0-5), `files: List<AFile>`.
- **Logic**: Groups `AFiles` by `base`. 
- **Playback**: Plays files in order with `Config.pause` between them.

### Folder (The Collection)
- **Properties**: `path: String`, `words: List<Word>`.
- **Methods**: `scanFiles()`, `play(player, store, config)`.
- **Sorting**: Sorted by `SRS.cmp` (Leitner priority).

## 2. Systems

### SRS (Spaced Repetition System)
- **Algorithm**: Leitner System with 6 boxes.
- **Methods**: 
    - `learned(word)`: Returns the next box index.
    - `cmp(word1, word2)`: Comparator for sorting words by review urgency.

### Store (Persistence)
- **Responsibilities**: 
    - Save/Load `word.box` by `word.base`.
    - Save/Load `Config` settings.

### AudioPlayer (Playback Engine)
- **Properties**: `speed: Float`, `paused: Boolean`, `currentFolder: Folder?`, `wordIndex: Int`.
- **Logic**: Handles sequential playback and pauses. Supports background playback.

### MediaSessionManager
- **Responsibilities**: Manages `MediaSession` to capture headset events (`Play`, `Pause`, `Skip`) and keeps the app alive in the background.

## 3. Interaction Rules
- **Short Press**: Mark current word as learned -> `SRS.learned` -> Update `Store`.
- **Long Press**: Toggle global `paused` state.
