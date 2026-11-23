# Annual Objectives Evidence — Test Automation Infrastructure

**Author:** Sebastian Valencia
**Project:** AutomationCICD — Selenium Test Automation Framework
**Repository:** https://github.com/Sebas190381/AutomationCICD (PR #6)
**Date:** November 18, 2026

---

## Objective

Modernize the Selenium test automation framework to support **cross-browser execution** and **distributed execution via Selenium Grid**, integrated into the CI/CD pipeline.

---

## Actions Taken

### 1. Cross-Browser Support (4 browsers, 8 execution modes)

| Browser | Headed | Headless |
|---|---|---|
| Chrome | `chrome` | `chromeheadless` |
| Firefox | `firefox` | `firefoxheadless` |
| Edge | `edge` / `edgeheadless` (repaired — was broken) | |
| Opera | `opera` / `operaheadless` (new — Selenium 4 removed OperaDriver; implemented via ChromeDriver + Opera binary) | |

- Browser selection via TestNG suite parameter with CLI override (`-Dbrowser=...`).
- Resolution order: `-Dbrowser` CLI → TestNG suite parameter → `GlobalData.properties`.

### 2. Cross-Browser Stability Fixes

- **Firefox headless add-to-cart failure:** the "Product Added To Cart" toast renders with empty text in Firefox headless, causing false failures. Replaced the toast-text wait with a **cart-badge state-change wait** — a browser-agnostic signal that works everywhere.
- **Click-swallow race:** the full-screen loading spinner can overlay the add-to-cart button right after login. Added a **click-retry loop (3 attempts)** with spinner-wait fallback.
- **Search filter test disabled** with a documented app defect note (search form is decorative — not wired to the product grid).

### 3. Selenium Grid Execution Mode

- New `-Dbrowser=grid` mode in `BaseTest` using `RemoteWebDriver`.
- Target browser selectable via `-DbrowserName=chrome|firefox|edge`; Grid endpoint overridable with `-DgridUrl`.
- Dedicated `testSuites/Grid.xml` regression suite.
- **Full suite through the Grid: 42/42 tests green (BUILD SUCCESS).**

### 4. Parallel Execution Through the Grid

- `testSuites/GridParallelDemo.xml`: two test blocks (Chrome + Firefox) running **simultaneously** through the Grid.
- **5/5 tests green, BUILD SUCCESS** — login and error-validation flows parallelize safely.
- True multi-browser parallelism verified via the Grid GraphQL API: **Chrome 157 and Firefox 151 sessions running simultaneously** on the same node.
- Purchase flows intentionally run sequentially: both browsers share the same backend account, so parallel cart operations race server-side (documented design decision).

### 5. CI/CD Integration

- GitHub Actions workflow now:
  1. Downloads and starts a **standalone Selenium Grid** (4.35.0)
  2. Waits for Grid readiness (health-check polling)
  3. Runs the Grid suite (Chrome via Grid)
  4. Uploads ExtentReports + Grid logs as artifacts on failure

---

## Metrics

| Metric | Result |
|---|---|
| Total automated tests | 42 |
| Cross-browser pass rate | **100%** (42/42 on Chrome, Firefox, Opera headless) |
| Grid full-suite pass rate | **100%** (42/42 through RemoteWebDriver) |
| Parallel Grid demo | **5/5 green** (Chrome 157 + Firefox 151 simultaneously, verified via Grid GraphQL) |
| Browsers supported | 4 (Chrome, Firefox, Edge, Opera) — 8 execution modes |
| Grid node capacity | 25 slots (Chrome 8, Firefox 8, Edge 8, Safari 1) |
| Framework defects fixed | 3 (Edge path, Firefox toast race, click interception) |
| CI pipeline | Grid-based execution with log artifacts on failure |

---

## Visual Evidence

Screenshots captured during live Grid execution (see `grid_ui_*.png`):

1. **`grid_ui_console.png`** — Grid console showing the macOS node with 25 browser slots (Chrome 8, Firefox 8, Edge 8, Safari 1), Selenium Grid 4.35.0.
2. **`grid_ui_sessions_live.png`** — Sessions view during a live parallel run: running Chrome 157 session with session ID, start time, duration and node URL.
3. **`grid_ui_overview_live.png`** — Overview with live concurrency usage (1/8 slots, 13%).

---

## Impact

- **Before:** tests ran on a single browser (Chrome), locally, with flaky headless failures and a broken Edge path.
- **After:** the same suite runs on 4 browsers, through a Grid, in CI, with self-healing waits (badge-state waits, click retries) that eliminated the headless race conditions.
- **Scalability:** the Grid node exposes 25 concurrent slots — the framework can scale to parallel execution on demand (purchase flows sequential by design due to shared test account).

---

## Artifacts

| Artifact | Location |
|---|---|
| Cross-browser suite | `testSuites/CrossBrowser.xml` |
| Grid suite | `testSuites/Grid.xml` |
| Parallel demo suite | `testSuites/GridParallelDemo.xml` |
| Grid execution mode | `BaseTest.initializeDriver()` — `grid` branch |
| CI workflow | `.github/workflows/ci.yml` |
| PR | https://github.com/Sebas190381/AutomationCICD/pull/6 |
