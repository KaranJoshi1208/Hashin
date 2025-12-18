# Hashin: Secure Passkey Vault — Comprehensive Report

## Table of Contents
- Introduction
- Product Vision & Goals
- User Personas & Use Cases
- Core Features
- Non-Functional Requirements
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
Hashin is a mobile-first Android application that stores and manages user passkeys. It encrypts secrets locally, syncs encrypted blobs to Firestore, and enforces biometric/PIN confirmation before showing sensitive data or executing destructive actions. The project uses Jetpack Compose for UI, Room for local persistence, and Firebase for auth + cloud storage.

## Product Vision & Goals
- Provide a secure, privacy-first passkey vault.
- Keep UX fast with offline-first local cache and opportunistic cloud sync.
- Enforce strong identity verification (Firebase auth + biometric/PIN gates).
- Offer a clean, modern UI with light/dark themes and professional aesthetics.

## User Personas & Use Cases
- Security-conscious individual: store passwords, require biometrics to reveal.
- Frequent traveler: works offline, syncs when online.
- Power user: edits, deletes passkeys; expects quick search and responsive UI.

## Core Features
- Sign up / sign in (email/pass + Google).
- Store passkeys with encrypted secrets.
- Local encryption/decryption using `CryptoManager` before persistence.
- Local cache via Room; remote sync via Firestore.
- Biometric/PIN prompts on app unlock (when already signed in), on password reveal/copy, and on destructive actions (delete passkey, delete account).
- Theming (dark/light) and settings with profile data from Firestore.

## Non-Functional Requirements
- Minimum API 24, supports changes for nav/status bar handling across API levels.
- Data confidentiality: secrets never stored in plaintext remotely.
- Availability: offline read/write with later sync.
- Usability: smooth Compose UI, focus handling, professional styling.

## Architecture Overview
Layered approach:
- **UI (Compose screens & components):** `Auth`, `Splash`, `Home`, `Vault`, `Passkey`, `ViewKey`, `Settings`, `BottomAppBar`, `Element`, etc.
- **ViewModels:** `AuthViewModel`, `HomeViewModel`, `SplashViewModel` — hold state, orchestrate repo calls, manage encryption/decryption and sync.
- **Repositories:** `AuthRepo`, `HomeRepo` — abstract Firebase/Room.
- **Data:** Room entities/DAO (`PassKey`, `DAO`, `Database`), Firestore remote (`FireStoreDB`).
- **Utilities:** `CryptoManager` for encryption, `BiometricAuth` for prompts, `AppContextHolder`, theming locals.

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
- **Encryption:** `CryptoManager` encrypts plaintext passwords into cipher + IV; only encrypted data is stored locally and remotely.
- **Identity:** FirebaseAuth (email/pass + Google). User doc created on signup/sign-in if missing.
- **Access control:** Biometric/PIN prompts on:
  - App open for signed-in users (Splash).
  - Viewing/copying password in `ViewKey`.
  - Deleting passkey (in `ViewKey`).
  - Deleting account (in `Settings`).
- **Data isolation:** Room cache cleared on sign-out/sign-in change; remote sync repopulates for the new user.

## Data Model
- `PassKey` (Room entity & Firestore document): id, service, userName, desc, label, passwordCipher, passwordIv, updatedAt.
- User profile (Firestore `users/{uid}`): name, email, uid, dateTime, bio, phone.

## Offline & Sync Strategy
- Observe Room Flow (`DAO.getAll`) to drive UI reactively.
- `refreshFromRemote` clears local then hydrates from Firestore for current user.
- All writes: upsert local first, then remote add/update/delete; Room flow updates UI.
- Sign-out / user-change: clear Room to avoid data leakage across accounts.

## Theming & UI/UX
- Material3 Compose; dark/light via `LocalDarkTheme` and preference storage.
- Professional vault/search cards, bottom app bar, refined settings, modern passkey/view screens.
- Focus handling on search; empty states with guidance.
- Status/navigation bar appearance adjusted per theme.

## Navigation Flow
- Top-level `NavGraph`: `Splash` → `Auth` → `Home` (with inner nav: Vault, Passkey, Setting, Detail).
- `Home` hosts inner NavHost and bottom bar navigation.
- `ViewKey` navigated from vault item tap (no biometric there per latest requirement).

## Authentication & Session Management
- FirebaseAuth for primary identity.
- `AuthRepo.createUserDocument` ensures Firestore profile created on sign-up/Google sign-in.
- `HomeViewModel.onUserChanged` clears cache and reloads per account.
- Sign-out clears cache and navigates to Auth.

## Biometric/PIN Protection Flows
- Helper: `BiometricAuth` (BiometricPrompt with device credential fallback), provided via `LocalBiometricAuth` from `MainActivity` (now `FragmentActivity`).
- Gated actions:
  - App launch when already signed in (Splash).
  - In `ViewKey`: eye icon toggle, "Copy all", delete passkey.
  - In `Settings`: delete account (prompted).
- Vault item tap no longer prompts; prompt happens inside `ViewKey` at the eye icon/copy/delete interactions.

## Encryption Pipeline
- `CryptoManager.encrypt` -> returns cipher/IV.
- `PassKey.fromPlain` builds entity with encrypted fields.
- `decryptPassword` uses `CryptoManager.decrypt` with stored cipher/IV.

## Room (Local Cache) Integration
- `DAO`: upsert, getAll (Flow), deleteById, clearAll.
- `HomeRepo`: wrap DAO; expose clearLocal, deleteRemote, syncRemote.
- `HomeViewModel`: observes Flow, clears on user change, refreshes from remote.

## Firestore (Remote) Integration
- `FireStoreDB`: CRUD for vault subcollection and user doc deletion; fetch passkeys; add/update/delete passkey; deleteUserData wipes vault and user doc.
- `AuthRepo`: creates user doc on signup/sign-in if missing.

## Error Handling & Resilience
- Biometric errors surfaced inline (error chip in ViewKey; text message in Splash fallback leads to Auth).
- Firestore fetch guarded with try/catch; returns empty on failure.
- Edit/save sheets show inline errors; delete dialogs confirm.

## Testing Strategy (current/manual)
- Manual flows: signup/login, add/edit/delete passkey, biometric prompt on eye/copy/delete, account delete, theme toggle, sign-out cache clear, remote sync.
- Future: add instrumented tests for ViewModels (Room + fake Firestore), UI tests for biometric-gated flows using test authenticators, unit tests for CryptoManager.

## CI/CD & Tooling (future work)
- Add Gradle CI pipeline (lint, unit tests, detekt/ktlint).
- Instrumented tests on Firebase Test Lab.

## Performance Considerations
- Room Flow keeps UI reactive without heavy recomposition; list uses LazyColumn with keys.
- Biometric prompt only on sensitive actions to reduce friction.
- Local-first writes keep UI snappy; remote sync off main thread.

## Observability & Logging
- Targeted logging in AuthRepo/HomeViewModel; errors logged on Firestore failures.
- Future: structured logging and crash reporting integration (Crashlytics) with PII-safe policies.

## Known Gaps / Future Enhancements
- Persist user notification/security preferences (currently UI only).
- Add settings toggle to enable/disable biometric gate per action, with policy defaults.
- Add password strength meter and breach checking (k-anonymity API).
- Export/import encrypted backups.
- Add proper re-auth flows when Firebase delete requires recent login.
- More polished nav/status bar handling across API 24–29 (some work already done).

## Setup & Run
Prereqs: Android Studio + SDK 24+, Firebase project (google-services.json present).

Build/Run:
```bash
./gradlew assembleDebug
```
Install/run on device/emulator via Android Studio or:
```bash
./gradlew installDebug
```

## Conclusion
Hashin delivers a secure, biometric-gated passkey manager with offline-first behavior, local encryption, and cloud sync. The architecture separates UI, state, data, and crypto concerns; biometric prompts guard sensitive actions; Room + Firestore keep data consistent per user while preventing cross-account leakage. This report documents the design, implementation choices, and next steps for evolving the app.

