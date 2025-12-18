# Hashin

## Overview
Hashin is an Android application designed to provide users with a simple, secure, and efficient tool for generating and verifying cryptographic hashes. It caters to developers, security professionals, and everyday users who need to ensure data integrity, verify file authenticity, or perform basic cryptographic operations on mobile devices. The app supports multiple hashing algorithms and handles both text inputs and file selections, making it versatile for various use cases such as checking download integrity, password hashing, or digital forensics.

## Purpose
The primary goal of Hashin is to democratize access to cryptographic hashing functions on Android devices. In an era where data breaches and tampering are prevalent, users often need to verify the integrity of files or strings without relying on desktop tools. Hashin achieves this by offering a user-friendly interface that abstracts complex cryptographic operations, ensuring that even non-experts can perform secure hashing tasks. It promotes security best practices by encouraging the use of strong algorithms over deprecated ones and providing clear, actionable results.

## Key Features
- **Multiple Algorithm Support**: Includes popular hashing algorithms such as MD5, SHA-1, SHA-256, SHA-384, SHA-512, and more, allowing users to choose based on their security needs.
- **Input Flexibility**: Supports hashing of plain text strings or files selected from the device's storage.
- **Verification Mode**: Enables users to input an expected hash and compare it against the computed hash for verification purposes.
- **User-Friendly Interface**: Built with Android's Material Design for intuitive navigation, including tabs for hashing and verification.
- **Security Focus**: Warns users about weak algorithms (e.g., MD5) and recommends stronger alternatives.
- **Offline Operation**: All computations are performed locally on the device, ensuring privacy and no reliance on internet connectivity.
- **Export Options**: Allows copying hashes to clipboard or sharing them via Android's share intent.

## How It Achieves Its Goals
Hashin leverages Android's native cryptographic libraries to deliver reliable and performant hashing. Here's a detailed breakdown of the technical implementation:

### Core Architecture
- **Platform**: Developed using Java and Kotlin in Android Studio, targeting API level 21+ for broad compatibility.
- **UI Framework**: Utilizes Android Jetpack components, including ViewModel for state management, LiveData for reactive UI updates, and RecyclerView for displaying algorithm options.
- **Cryptographic Backend**: Relies on Java's `java.security.MessageDigest` class, which provides a standardized interface for hashing. This ensures compliance with industry standards and leverages optimized native implementations for speed.

### Hashing Process
1. **Input Handling**: Users can enter text directly or select a file via Android's file picker (using `Intent.ACTION_OPEN_DOCUMENT`). For files, the app reads the content in chunks to handle large files efficiently without loading everything into memory.
2. **Algorithm Selection**: The app initializes a `MessageDigest` instance based on the chosen algorithm (e.g., `MessageDigest.getInstance("SHA-256")`).
3. **Computation**: The input data is fed into the digest via `update()` methods. For text, it's encoded as UTF-8 bytes. For files, a buffered input stream processes data in 8KB chunks to minimize memory usage.
4. **Output Generation**: The final hash is computed using `digest()` and formatted as a hexadecimal string for readability.
5. **Performance Optimization**: Asynchronous processing with Kotlin Coroutines ensures the UI remains responsive during computations, especially for large files.

### Verification Process
1. **Expected Hash Input**: Users provide the expected hash value alongside the input data.
2. **Comparison**: The app computes the hash as above and performs a case-insensitive string comparison with the provided hash.
3. **Feedback**: Displays a clear success/failure message, highlighting any mismatches for debugging.

### Security Considerations
- **No Key Storage**: The app does not store sensitive data; all operations are ephemeral.
- **Algorithm Recommendations**: Implements logic to flag insecure algorithms (e.g., MD5/SHA-1) with warnings, guiding users toward SHA-256 or higher.
- **Permissions**: Requests minimal permissions (e.g., `READ_EXTERNAL_STORAGE` for file access), adhering to Android's least-privilege principle.
- **Error Handling**: Robust exception handling for invalid inputs, unsupported algorithms, or file access issues, preventing crashes and providing user-friendly error messages.

### Installation and Usage
1. Clone or download the project from the repository.
2. Open in Android Studio and build the APK.
3. Install on an Android device (API 21+).
4. Launch the app, select an algorithm, input text or choose a file, and generate/verify hashes.

## Contributing
Contributions are welcome! Please fork the repository, make changes, and submit a pull request. Ensure code follows Android development best practices and includes unit tests for cryptographic functions.

## License
This project is licensed under the MIT License. See LICENSE file for details.

## Contact
For questions or feedback, reach out via [your contact method].

