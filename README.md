# CodeExplainer

An IntelliJ IDEA plugin that explains code snippets using a local LLM via [Ollama](https://ollama.com). Select any code in the editor, right-click, and get an explanation rendered directly in a tool window.

![Java](https://img.shields.io/badge/Java-21-blue) ![IntelliJ Platform](https://img.shields.io/badge/IntelliJ%20Platform-2025.1-orange) ![Build](https://img.shields.io/badge/build-Gradle-green)

---

## Features

- **Right-click → Explain Code** — explains any selected code snippet
- **Tool window panel** — explanation rendered as HTML in a dedicated side panel
- **Configurable** — set your own Ollama endpoint and model under *File → Settings → Tools → CodeExplainer Settings*

## Requirements

- IntelliJ IDEA 2025.1 or later
- Java 21
- [Ollama](https://ollama.com) running locally with at least one model pulled

## Setup

**1. Install Ollama**

```bash
# macOS / Linux
curl -fsSL https://ollama.com/install.sh | sh

# Windows: download the installer from https://ollama.com
```

**2. Pull a model**

```bash
ollama pull qwen2.5-coder:7b
```

Any model listed by `ollama list` will work. `qwen2.5-coder:7b` is the default.

**3. Run the plugin**

Clone the repo and run the plugin in a sandboxed IDE instance:

```bash
git clone https://github.com/oibaf7/CodeExplainer.git
cd CodeExplainer
./gradlew runIde
```

## Usage

1. Open any file in the editor
2. Select a code snippet
3. Right-click → **Explain Code**
4. The explanation appears in the **CodeExplainer** tool window (right side by default)

To change the model or endpoint: *File → Settings → Tools → CodeExplainer Settings*

## Architecture

```
src/main/java/com/codeexplainer/
├── core/
│   ├── OllamaClient.java          # Async HTTP client for /api/generate
│   └── AppSettings.java           # PersistentStateComponent (endpoint, model)
└── ui/
    ├── CodeExplainerWindowFactory.java    # Registers the tool window
    ├── CodeExplainerPanel.java            # Tool window UI (JEditorPane, HTML rendering)
    ├── ExplainAction.java                 # Reads the currently selected text and uses the OllamaClient to make the HTTP request 
    ├── AppSettingsComponent.java          # Settings form
    └── AppSettingsConfigurable.java       # Wires settings form into IDE settings
```

`OllamaClient` sends a non-streaming POST to `/api/generate` using Java's built-in `HttpClient`. The response is parsed from Ollama's newline-delimited JSON format. The result is handed back via `CompletableFuture` and rendered in the tool window.

## Configuration

| Setting  | Default                                  | Description                        |
|----------|------------------------------------------|------------------------------------|
| Endpoint | `http://localhost:11434/api/generate`    | Ollama HTTP API URL                |
| Model    | `qwen2.5-coder:7b`                       | Any model available via `ollama list` |


## Known Limitations

- `parseJson` targets Ollama's specific JSON format (`response` field, `done` flag) and other backends are not supported
- HTML formatting quality depends on the model's instruction-following ability, smaller models may produce inconsistent output
