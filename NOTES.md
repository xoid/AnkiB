# Project Context & Implementation Details

## SRS Algorithm: Leitner System
- **Mechanism**: Words are placed in "boxes" (0-5).
- **Promotion**: When a word is learned (Headset Short Press), it moves to a higher box (e.g., Box 1 -> Box 2).
- **Demotion**: If a word is forgotten or needs review, it can be moved back to Box 0 (or reduced).
- **Sorting**: `Folder.words` is sorted using `SRS.cmp`, which prioritizes lower boxes (more frequent review) or uses a weighted random selection based on Leitner principles.

## Playback Flow
- **Structure**: `Folder` -> `Word` -> `AFile`.
- **Pauses**: `Config.pause` is the duration of silence *before* playing the translation/answer file within a `Word`, giving the user time to recall.
- **Speed**: `AudioPlayer` must apply `Config.speed` (Float) to each `AFile.play()`.

## Background Execution & Control
- **Headset Support**: `MediaSession` is used to capture headset events (`Play/Pause`, `Next`) even when the screen is off.
- **Focus**: The app must maintain execution focus and prevent the system from killing it during background playback.
- **Paused State**: A `paused` flag is needed in `AudioPlayer` or a central controller to stop the sequence progression.

## Data Handling
- **Grouping**: Files with the same `F.base` are grouped into one `Word`.
- **AFile Sorting**: Alphabetical by `F.suffix` (reversed if `Config.sortReverse` is true).
- **Storage**: In-memory sorting of lists is sufficient for now; persistence is handled by `Store` (SharedPreferences).
