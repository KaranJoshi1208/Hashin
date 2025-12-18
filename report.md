# Hashin: Secure Passkey Vault — Comprehensive Report

## Table of Contents
- Introduction
- Product Vision & Goals
- User Personas & Use Cases
- Core Features
- Non-Functional Requirements
- Tech Stack
- Architecture Overview
- System Design (Textual Diagrams)
- Security Model
- Data Model
- Offline & Sync Strategy
- Theming & UI/UX
- Navigation Flow
- Authentication & Session Management
- Biometric/PIN Protection Flows
- Encryption Pipeline
- Room (Local Cache) Integration
- Firestore (Remote) Integration
- Error Handling & Resilience
- Testing Strategy
- CI/CD & Tooling (future work)
- Performance Considerations
- Observability & Logging
- Known Gaps / Future Enhancements
- Setup & Run
- Conclusion

## Introduction
Hashin is a mobile-first Android application that stores and manages user passkeys. It encrypts secrets locally, syncs encrypted blobs to Firestore, and enforces biometric/PIN confirmation before showing sensitive data or executing destructive actions. The project uses Jetpack Compose for UI, Room for local persistence, and Firebase for auth + cloud storage. The guiding principle is to never expose plaintext secrets outside the device while keeping the experience fast and intuitive. Hashin follows an offline-first, privacy-first mindset: user data stays usable without network, and when the network returns, sync brings the cloud copy up to date. The design emphasizes minimal friction for common tasks while requiring explicit user presence for sensitive operations.

## Product Vision & Goals
- Provide a secure, privacy-first passkey vault that feels delightful to use, not just safe.
- Keep UX fast with offline-first local cache and opportunistic cloud sync so users are never blocked by network outages.
- Enforce strong identity verification (Firebase auth plus biometric/PIN gates) at sensitive moments to reduce risk of shoulder-surfing or device theft.
- Offer a clean, modern UI with light/dark themes and professional aesthetics, reducing cognitive load during frequent daily use.
- Be pragmatic: avoid overengineering; focus on a minimal but solid stack that can evolve (Room + Firestore + Compose).
- Ensure per-user data isolation across sessions and devices with consistent encryption policies.

## User Personas & Use Cases
- Security-conscious individual: stores personal credentials; expects biometrics before revealing passwords; wants quick search and copy.
- Frequent traveler: often offline; still needs to view/add/update passkeys with a guarantee that changes will sync later.
- Power user / admin: edits, deletes, or rotates secrets routinely; wants friction only at high-risk operations, not at every tap.
- New user: needs simple onboarding, predictable navigation, and a safe default theme with obvious call-to-actions.

## Core Features
- Sign up / sign in with email/password and Google; Firestore profile creation on first entry with metadata defaults.
- Add, edit, delete passkeys with encrypted password fields; metadata (service/user/label/notes) stays unencrypted for UX, but secrets never do.
- Local encryption/decryption via `CryptoManager` before anything touches disk or network; decrypt only on-demand in the detail screen.
- Local cache via Room with reactive Flows powering Compose recomposition; remote sync via Firestore keeps per-user data consistent.
- Biometric/PIN prompts on app unlock (when already signed in), on password reveal/copy, and on destructive actions (delete passkey, delete account).
- Theming (dark/light) with persisted preference and status/nav bar adjustments for immersion.
- Search with focus handling and empty states to guide the user.

## Non-Functional Requirements
- Minimum API 24; status/navigation bar handling respects API 24–33 differences, using InsetsController and fallbacks.
- Data confidentiality: only encrypted secrets leave the device; Room also stores ciphertext, not plaintext.
- Availability: offline read/write with eventual consistency to cloud; non-blocking UI even when remote sync fails.
- Usability: focus handling, empty states, consistent typography and spacing, professional styling; avoid intrusive prompts except at sensitive actions.
- Maintainability: clear layering (UI/ViewModel/Repo/Data/Util) with minimal global state and centralized navigation definitions.
- Reliability: defensive error handling for Firestore and biometric failures; local cache as source of truth for UI.

## Tech Stack
- Language: Kotlin, coroutines, Flow.
- UI: Jetpack Compose (Material3), Navigation-Compose, previews for key screens.
- Architecture: MVVM with repositories; CompositionLocals for theme and biometric helper.
- Local data: Room (DAO, entities, database singleton) with Flow.
- Remote: Firebase Auth (email/pass, Google) + Firestore (users collection, vault subcollection).
- Security: AndroidX BiometricPrompt with device credential fallback; custom `CryptoManager` for AES encryption; SharedPreferences for theme prefs.
- Build: Gradle (KTS), version catalogs in `libs.versions.toml`.

## Architecture Overview
Layered, Compose-first:
- **UI (Compose screens & components):** `Auth`, `Splash`, `Home`, `Vault`, `Passkey`, `ViewKey`, `Settings`, `BottomAppBar`, `Element`, etc. They are stateless except for view-local UI state and rely on ViewModels for data.
- **ViewModels:** `AuthViewModel`, `HomeViewModel`, `SplashViewModel` orchestrate flows, interact with repos, and expose state to Compose. `HomeViewModel` owns passkey list, crypto, and sync orchestration.
- **Repositories:** `AuthRepo`, `HomeRepo` abstract persistence and remote calls; hide Room/Firestore details.
- **Data:** Room entities/DAO (`PassKey`, `DAO`, `Database`), Firestore remote (`FireStoreDB`). `PassKey` stores only encrypted password fields.
- **Utilities:** `CryptoManager` for AES encryption, `BiometricAuth` for BiometricPrompt, `AppContextHolder`, theme locals, navigation helpers.

## System Design (Textual Diagrams)
High-level data flow:
```
[Compose UI] -> [ViewModel] -> [Repo] -> [Local Room] <-> [Firestore]
                                  \-> [CryptoManager]
                                  \-> [BiometricAuth]
```

Request handling (passkey add/edit):
```
User Action -> ViewModel.add/update
    -> CryptoManager.encrypt(password)
    -> Repo.upsertLocal(encrypted)
    -> Repo.add/updateRemote(encrypted)
    -> UI observes Room Flow -> recomposes list
```

Splash/auth gating:
```
App start -> Splash
   if user null -> Auth
   else -> BiometricAuth (prompt)
       success -> Home
       failure -> Auth
```

ViewKey password reveal:
```
Eye icon tap -> BiometricAuth
   success -> show decrypted password (from ViewModel.decrypt)
   failure -> show inline error chip
```

Account delete:
```
Settings delete -> BiometricAuth -> Repo.deleteUserData (Firestore) -> FirebaseAuth.delete -> clear Room
```

## Security Model
- **Encryption:** `CryptoManager` encrypts plaintext passwords into cipher + IV using a symmetric key kept on-device (not shared); only ciphertext is stored locally/remotely.
- **Identity:** FirebaseAuth (email/pass + Google). User doc is created on signup/sign-in if missing to keep profile metadata consistent.
- **Access control (biometric/PIN):**
  - App open when already signed in (Splash) to prevent casual access if device is unlocked.
  - In `ViewKey`: eye icon, copy-all, delete passkey each prompt.
  - In `Settings`: delete account prompts before irreversible cloud wipe.
- **Data isolation:** On sign-out or account switch, Room cache is cleared; next user reloads only their own data from Firestore. No plaintext persists beyond process memory needed for immediate UI.
- **Least exposure:** Decryption happens on demand in `ViewKey` and is not cached beyond composable state; copy actions still require auth.
- **Transport security:** Firebase uses HTTPS; no custom network stack needed.

## Data Model
- `PassKey` (Room + Firestore):
  - `id` (String), `service`, `userName`, `desc`, `label`, `passwordCipher`, `passwordIv`, `updatedAt`.
- User profile (`users/{uid}` Firestore): `name`, `email`, `uid`, `dateTime`, `bio`, `phone`.
- Room DAO: `upsert`, `getAll` (Flow), `deleteById`, `clearAll`.
- Remote vault path: `users/{uid}/vault/{passkeyId}`; user doc at `users/{uid}`.

## Offline & Sync Strategy
- UI observes Room Flow for instant updates. `HomeViewModel.observeLocal` collects Flow and repopulates Compose state.
- `refreshFromRemote` clears local then writes remote snapshots, ensuring no stale cross-account data; invoked on init and user change.
- Writes: upsert local first (responsive), then remote add/update/delete. Remote failures do not block UI but should be surfaced (future: retry queue/backoff with WorkManager).
- Sign-out / user-change triggers `clearLocalData` to prevent data leakage.
- Conflict handling: last-write-wins based on updatedAt; future improvement could add merge or versioning.

## Theming & UI/UX
- Material3 with `HashinTheme`, dark/light toggle persisted in SharedPreferences.
- Status/navigation bar colors adapt to theme; edge-to-edge enabled for immersion; insets controller used for light/dark icon appearance.
- UI patterns: cards with rounded corners, spaced typography, consistent iconography, empty states with guidance, focused search bar, bottom app bar for primary navigation.
- Settings polished with profile card, preferences, actionable list items, bottom sheet editing, and destructive actions called out.
- Compose previews for fast iteration (where available).

## Navigation Flow
- Root `NavGraph`: `Splash` (start) -> `Auth` -> `Home` (inner NavHost).
- Inner `Home` graph: `Vault` (list/search), `Passkey` (add/edit), `ViewKey`/`Detail`, `Settings`.
- Back handling: custom back in Vault; top app bars provide navigation icons.
- `Screens` sealed class centralizes routes; navigation uses `NavController` with `launchSingleTop` to avoid duplicate destinations.

## Authentication & Session Management
- FirebaseAuth for primary login; Google credential support via Credential Manager.
- Firestore user doc created when missing (`AuthRepo.createUserDocument`); ensures profile metadata exists for Settings.
- `HomeViewModel.onUserChanged` clears Room and reloads remote for the active user; used after login/sign-up flows.
- Sign-out in Settings clears cache and navigates to Auth; Splash redirects based on auth state (with biometric gate if signed in).
- Session persistence relies on FirebaseAuth internal tokens; no custom session layer.

## Biometric/PIN Protection Flows
- Helper `BiometricAuth` wraps `BiometricPrompt` with device-credential fallback; provided via `LocalBiometricAuth` (MainActivity as FragmentActivity).
- Prompts:
  - Splash when already signed in.
  - ViewKey eye icon, copy-all, delete passkey.
  - Settings delete-account dialog.
- Vault item taps are not gated to keep navigation smooth; sensitive actions inside the detail are gated.
- Error handling: inline messages (chip/text) allow retry; failures do not crash.

## Encryption Pipeline
- `PassKey.fromPlain` encrypts password via `CryptoManager.encrypt`, storing `cipherText` and `iv`.
- Decryption: `HomeViewModel.decryptPassword` uses stored cipher/IV to return plaintext on demand.
- Only encrypted blobs are persisted to Room and Firestore; decryption happens transiently in memory for the requesting composable; no plaintext persisted.
- AES mode/parameters are encapsulated in `CryptoManager` (implementation detail retained in codebase).

## Room (Local Cache) Integration
- DAO exposes Flow for `getAll`, enabling reactive UI.
- `clearAll` used on sign-out/user change and before hydrating remote snapshots.
- `HomeRepo` mediates DAO calls; `HomeViewModel` collects and mirrors into `SnapshotStateList` for Compose.
- Upserts are conflict-replace; ordering by updatedAt desc in DAO query.

## Firestore (Remote) Integration
- `FireStoreDB` handles vault subcollection CRUD and user doc deletion.
- `syncRemote` fetches all passkeys for the user; errors are caught/logged and return empty list.
- `deleteUserData` wipes vault documents then deletes the user doc; called before FirebaseAuth.delete for account deletion.
- Writes use document IDs supplied by app; if empty, Firestore-generated ID is used.

## Error Handling & Resilience
- Biometric failures show inline messages (chip/text) and do not crash flows; user can retry.
- Firestore operations are wrapped in try/catch; UI falls back to local data or empty state with Retry.
- Edit/save in Settings bottom sheet shows inline errors; destructive actions require confirmation dialogs.
- Navigation guarded to avoid null user crashes (current user checks before remote ops).

## Testing Strategy
- Current: manual verification of auth, add/edit/delete passkey, biometric gating on eye/copy/delete/account delete, theme toggle, sign-out isolation, remote sync.
- Planned: unit tests for `CryptoManager` and `HomeViewModel` with fake DAO/Repo; UI tests for biometric-gated flows using test authenticators; snapshot tests for Compose screens; integration tests for Firestore with emulator suite.

## CI/CD & Tooling (future work)
- Add GitHub Actions/CI: `./gradlew lint ktlint detekt test assembleDebug`.
- Instrumented tests on Firebase Test Lab matrix (API 24–34).
- Dependency vulnerability scanning; version catalogs already present (`libs.versions.toml`).
- Static analysis (detekt/ktlint) and binary size checks as gates.

## Performance Considerations
- Room Flow minimizes recomposition overhead; LazyColumn keyed items avoid jank.
- Biometric prompts limited to sensitive actions to reduce friction.
- Local-first writes keep UI responsive; heavy work dispatched to IO dispatcher in ViewModels.
- Splash animation is lightweight; navigation deferred until auth/biometric decision completes.
- Avoided unnecessary LiveData/Flow conversions; Compose observes state directly.

## Observability & Logging
- Targeted logging in AuthRepo and Firestore DB helpers; errors logged with tags.
- Future: structured logging, Crashlytics with PII-safe policies; add in-app toasts/snackbars for user-facing errors.
- Consider event tracing for sync cycles and biometric failures for supportability.

## Known Gaps / Future Enhancements
- Persist notification/security preferences (UI-only toggles now).
- User-selectable biometric gating policy (always, per-action, off) with policy enforcement.
- Password strength meter and breach-check API integration.
- Export/import encrypted backups.
- Re-authentication flow for Firebase delete when recent sign-in is required.
- Additional polish for nav/status bar handling on API 24–29.
- Retry/backoff queue for remote writes when offline; WorkManager integration.

## Setup & Run
Prerequisites:
- Android Studio (Hedgehog or newer recommended).
- Android SDK 24+.
- Firebase project with `google-services.json` already placed in `app/` (present in repo).
- JDK 17 (per modern AGP default).

Build commands:
```bash
./gradlew clean assembleDebug
```

Install on a connected device/emulator:
```bash
./gradlew installDebug
```

Run instrumentation tests (if added):
```bash
./gradlew connectedAndroidTest
```

Optional: run lint/static checks (once configured):
```bash
./gradlew lint ktlint detekt
```

## Conclusion
Hashin delivers a secure, biometric-gated passkey manager with offline-first behavior, local encryption, and cloud sync. The architecture separates UI, state, data, and crypto concerns; biometric prompts guard sensitive actions; Room + Firestore keep data consistent per user while preventing cross-account leakage. This expanded report details the design rationale, technical stack, implementation choices, and clear next steps so contributors can extend the app confidently.
