# GitScape

GitScape is an Android application for browsing, searching, and understanding GitHub repositories from a single mobile interface. It combines GitHub API integration, local persistence, configurable Material 3 UI, and an AI-powered repository assistant.

## Setup

The following credentials are required to run GitScape:

- GitHub account credentials
- AI API key

### GitHub Authentication

GitScape uses a **GitHub OAuth App** and the **Device Flow** to authenticate users securely. This allows you to sign in with your GitHub account without manually generating or configuring a Personal Access Token (PAT).

Once authenticated, GitScape uses the GitHub REST API to retrieve information about your user profile and repositories.

### Sign in with GitHub

1. Launch GitScape and start the GitHub sign-in process.
2. GitScape displays a device code and a button to copy it to your clipboard and open the browser.
3. Press the button to open GitHub.
4. Sign in to GitHub, if prompted, paste the code and authorize GitScape.
5. Return to GitScape. The app completes the authentication process automatically.

Once authentication is complete, GitScape can access the repositories permitted by your GitHub account and the authorization granted to the app.

### Token Management

GitScape manages OAuth tokens automatically:

- **Access token:** Used to authenticate GitHub API requests.
- **Refresh token:** Used to obtain a new access token when the current one is approaching expiration.
- **Automatic session restoration:** GitScape attempts to restore your session when you reopen the app, without requiring you to sign in again as long as your saved tokens remain valid.

If your refresh token expires or becomes invalid, you may need to authenticate with GitHub again.

### Repository Access

GitScape can access repositories that your GitHub account is authorized to access, subject to the permissions granted to the GitHub App and any applicable organization policies.

For private repositories, you must authorize the app with an account that has access to those repositories. Organization policies or approval requirements may apply.

### Configure the AI Agent

An API key for the configured LLM provider is required. GitScape uses Google Gemini as its default provider, with the model configured through `LLM_MODEL` in `Constants.kt`.

### Alternative LLM Provider

GitScape uses **Google Gemini** by default, but Koog integrations are also available for the following LLM providers:

- **Google Gemini** (enabled by default)
- **OpenAI**
- **Anthropic (Claude)**
- **DeepSeek**
- **OpenRouter**
- **Mistral AI**
- **Alibaba DashScope (Qwen)**
- **Ollama** (for models hosted by an accessible Ollama server)

Only the Gemini integration is enabled by default. The other provider integrations are included in the project configuration as commented-out options.

#### Switch to another provider:

1. Uncomment the corresponding dependency in `libs.versions.toml`.
2. Uncomment the corresponding dependency in `build.gradle.kts`.
3. Uncomment and configure the desired LLM client in `DomainModule`.
4. Update the `LLM_MODEL` constant in `Constants` to select a model supported by the chosen provider.
5. Configure the appropriate AI_KEY in `local.properties`, if required by the provider. For Ollama, configure the server endpoint instead.

Each cloud provider requires its own valid API credentials. Adding a provider does not automatically enable fallback: the selected model and its corresponding client configuration determine which provider handles requests.

### Configure local properties

In your `local.properties` file add the following lines:

AI_KEY=YOUR_AI_API_KEY

Do not commit your AI key to version control.

### Configure release keys

If you want to build the app in release mode, create a file named `keys.properties` in the project root add the following lines:


KEYSTORE_FILE=Path of your keystore file
KEYSTORE_PASSWORD=Password of your keystore file
KEY_ALIAS=Alias of your key in the keystore file
KEY_PASSWORD=Password of your key in the keystore file


Do not commit your Play Store keys to version control.

### Security

GitScape requires read-only permissions for the features described above. For the safest configuration, restrict the token to only the repositories needed.

**Important:** A token embedded in an APK can be extracted through reverse engineering. Injecting a token at build time or storing it in `local.properties` prevents accidental commitment to version control, but does not protect the token once it is included in a distributed APK. Avoid distributing builds containing a personal access token.

## Overview

GitScape connects to GitHub to load the authenticated user's repositories and profile information. Repositories are displayed in a two-column grid with useful metadata such as:

- Repository name
- Star count
- Primary programming language

The application is built with Jetpack Compose and Material 3. It supports light and dark system themes, multiple color styles, multiple languages, local settings persistence, and an interactive AI assistant.

## Features

### GitHub repository browsing

- Loads repositories belonging to the authenticated GitHub user.
- Displays repositories in a responsive Compose grid.
- Supports refreshing the remote repository list.
- Opens a repository's GitHub page from its card.
- Shows the authenticated user's GitHub profile information.
- Loads up to the first 50 repositories from GitHub.
- Provides loading, empty-list, and error states.
- Offers a fallback to locally saved repositories when remote loading fails.

### Local repository persistence

GitScape can save the latest repository list locally using Room. Local persistence makes it possible to:

- Reuse the most recently loaded repositories when the network is unavailable.
- Detect whether the remote repository list has changed using a stored hash.
- Recover from temporary GitHub or network errors.
- Clear locally stored repositories from Settings by disabling local repositories.

The **Local Repositories** setting controls whether repository data and its synchronization hash are retained. When disabled, saved repository data is cleared.

## User Interface and Interactions

GitScape provides an interactive repository dashboard built with Jetpack Compose and Material 3.

### Repository Dashboard

- **GitHub profile avatar:** Tapping the user's avatar opens the homepage of their GitHub account in the browser.
- **AI Assistant button:** The AI icon in the toolbar opens the conversational repository assistant.
- **Refresh button:** Tapping the circular refresh icon triggers a new repository loading operation, refreshing the list with the latest data from GitHub.
- **Settings button:** The gear icon in the toolbar opens the Settings screen, where users can configure the application's preferences.
- **Repository cards:** Each card is tappable and opens the corresponding repository's GitHub page in the browser.

#### Repository cards and visual feedback

Repository cards provide animated visual feedback while they are visible, selected, or opened. The animated border uses the currently selected color style.

The **Scroll glow effect** setting controls this animation. It can be disabled from Settings. The animation applies to card visibility, programmatic scrolling, and card-click interactions.

### AI Assistant

The conversational assistant supports both text and voice input:

- **Text input:** Users can type their questions into the message field and submit them to the assistant.
- **Voice input:** Tapping the microphone icon starts Android speech recognition, allowing users to dictate their questions instead of typing. Speech recognition uses the currently selected application language.

The assistant can answer questions about loaded repositories, retrieve additional information from README files when needed, and interact with the dashboard through its available tools. For example, it can point to a specific repository card when requested by the user.

### Settings

GitScape provides a dedicated Settings screen where users can customize the application according to their preferences.

The following options are available:

- **Language:** Switches the application language between English and Italian.
- **Local Repositories:** Enables or disables local repository persistence. When enabled, GitScape stores the latest repository list and its synchronization hash in the Room database, allowing the application to reuse cached data when the network is unavailable or remote loading fails. When disabled, saved repository data and the synchronization hash are cleared.
- **Color Style:** Allows users to choose the application's accent color from five available styles: Azure, Green, Red, Pink, and Yellow. The selected style affects the color scheme and repository card glow effects.
- **Scroll Glow Effect:** Enables or disables animated glow effects on repository cards. The animation applies to card visibility, programmatic scrolling, and card-click interactions.

Settings are persisted in the Room database and restored when the application starts, ensuring that user preferences are retained between sessions.

#### Color Styles and Theming

GitScape uses Material 3 theming and supports multiple color styles, including:

- Azure
- Green
- Red
- Pink
- Yellow

The selected style affects the application's color scheme and repository-card glow colors. The app also follows the device's light or dark theme preference.

## AI Repository Assistant

GitScape includes an AI-powered assistant available from the repository list screen. The assistant can answer questions about the repositories currently loaded in the application and can inspect repository README files when metadata is not sufficient.

The default model is configured through LLM_MODEL in Constants.kt. The agent is implemented using Koog, with support for multiple LLM providers through a common abstraction.

The assistant is implemented as a tool-enabled agent.

It is designed to ground its answers in the repository data supplied by the app rather than inventing repository facts.

The assistant can select and highlight a specific repository.

### What the assistant can do

The assistant can:

- List all currently loaded repositories.
- Find a repository by name.
- Find a repository by ID.
- Return repository metadata in response to user questions.
- Retrieve README content for one or more repositories.
- Compare or summarize repositories using their loaded metadata and README content.
- Answer follow-up questions using the conversation history.
- Infer the repository referred to by a follow-up question when the context is unambiguous.
- Ask for clarification when the intended repository cannot be determined safely.
- Point to a specific repository in the grid when the user asks to show, select, or locate it.
- Reply in the same language used in the user's latest question.

### Assistant tools

The agent has access to the following tools:

#### `list_repos`

Returns the repositories currently available in the application, including their serialized repository metadata. The agent is instructed to call this tool before answering a conversation so that it starts from the current repository list.

#### `get_repo_by_name`

Looks up a repository by name. Matching is case-insensitive and ignores surrounding whitespace.

#### `get_repo_by_id`

Looks up a repository by its GitHub ID.

#### `get_repos_readme`

Loads README content for selected repositories. The tool:

- Accepts repository IDs.
- Removes blank and duplicate IDs.
- Processes at most ten repositories per request.
- Returns repository descriptions and README content.
- Returns an error entry when a repository cannot be found or its README cannot be loaded.
- Uses a memory-sensitive cache to avoid repeatedly downloading the same README during the session.

The README tool should only be used when the repository metadata already available to the agent is not enough to answer the question.

#### `point_to_repo_by_id`

Requests a UI action that points to a repository in the repository grid. The assistant can use this when the user asks to show, select, or locate a particular repository. The command is sent from the agent to the Android UI through a ViewModel event and triggers an animated programmatic scroll to the matching card.

### Agent execution flow

For each user request, the assistant follows this general flow:

1. Adds the user's question to the conversation history.
2. Loads the current repository list through `list_repos`.
3. Executes additional tools only when the question requires them.
4. Sends tool results back to the language model.
5. Repeats the tool cycle if more information is required.
6. Produces a final answer when the available information is sufficient.

The agent is limited to a maximum number of iterations to avoid unbounded tool execution. Agent lifecycle, strategy, node, tool, and failure events are logged for diagnostics.

### Conversation behavior and grounding

The assistant follows these rules:

- It answers only from loaded repository data and retrieved README content.
- It does not invent repository facts.
- It explicitly reports when the loaded repositories do not contain the requested information.
- It uses conversation context for follow-up questions.
- It asks for clarification when a reference is ambiguous.
- It preserves the language of the user's latest question.

### AI errors

If the AI API key is missing, the application reports that it is not configured. Authentication failures and other provider errors are surfaced through the assistant state and displayed in the chat interface.

## Architecture

GitScape follows a layered Android architecture:

- **Jetpack Compose** for the UI.
- **MVVM with unidirectional data flow** for UI state management.
- **ViewModels** for UI state and user actions.
- **Navigation Compose** for in-app navigation.
- **Use cases** for application operations.
- **Repositories** for GitHub, local database, settings, and AI access.
- **Retrofit and OkHttp** for GitHub API communication.
- **Room** for local repository and settings persistence.
- **Hilt** for dependency injection.
- **Koog** for the tool-enabled AI agent.
- **Moshi** for JSON serialization.
- **Coroutines and Flow** for asynchronous work and reactive state.

### GitHub Authentication Interceptor

GitScape uses a custom OkHttp `GitHubAuthInterceptor` to centralize GitHub API authentication and request configuration.

The interceptor checks the host of each outgoing request before adding authentication headers. **The GitHub Personal Access Token (PAT) is attached only when the request host matches `GITHUB_API_BASE_PATH`.** Requests targeting other hosts are forwarded unchanged, without the GitHub authentication headers.

This host-based check helps prevent accidental exposure of the PAT to unrelated third-party services, particularly if additional API integrations are introduced in the future.

For requests targeting GitHub, the interceptor adds the following headers:

- `Authorization: Bearer <PAT>` — authenticates the request using the configured Personal Access Token.
- `Accept: application/vnd.github+json` — specifies the expected GitHub API media type.
- `X-GitHub-Api-Version: 2026-03-10` — explicitly sets the GitHub REST API version.

Centralizing this logic in an OkHttp interceptor avoids duplicating authentication headers across individual API methods and makes the networking layer easier to maintain.

**Security consideration:** The interceptor limits where the token is sent, but it does not protect the token from extraction if it is embedded in a distributed APK.

### AI Integration

 **Koog** is used to implement the AI agent framework. This choice allowed me to implement the agent and its tool-calling logic independently of a specific LLM provider, while supporting different providers through a common abstraction.

This approach also improves maintainability and extensibility: adding or replacing an LLM provider in the future can be done with limited impact on the rest of the application, keeping the agent's domain tools and application logic largely unchanged.


## Data and privacy

GitScape communicates with GitHub to retrieve the authenticated user's profile and repositories. The AI assistant receives the repository information required to answer the user's questions and may retrieve README content for selected repositories. Avoid sending sensitive repository information through the assistant unless the configured AI provider and project policies allow it.

GitHub and AI credentials are read from local Gradle properties and exposed to the application through `BuildConfig`. They should not be committed to source control.

## Testing

The project includes unit tests written with JUnit and MockK, covering:

- GitHub API and authentication behavior.
- Repository mapping and use cases.
- Local Room-backed repositories.
- Settings persistence and migrations.
- Settings ViewModel behavior.
- AI tool dispatching logic, agent strategy, repository behavior, and README caching.

Run the unit test suite with:

```bash
./gradlew test
```

On Windows, use:

```bat
gradlew.bat test
```

## License

No license has been specified for this project yet.
