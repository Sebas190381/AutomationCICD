# Grid Parallel Demo — Headed & Headless Cycles (Evidence)

**Date:** 2026-11-23
**Repo:** Sebas190381/AutomationCICD (branch `feature/login-test`)
**Suite:** `testSuites/GridParallelDemo.xml` — 6 blocks, `parallel="tests"`, `thread-count=6`
**Grid:** Selenium Server 4.35.0 standalone, `http://localhost:4444`

## Suite composition (6 blocks)

| Block | Browser | Mode |
|---|---|---|
| Chrome via Grid | Chrome 158 | headless or headed |
| Chrome via Grid (2nd cycle) | Chrome 158 | headless or headed |
| Firefox via Grid | Firefox 160 | headless or headed |
| Firefox via Grid (2nd cycle) | Firefox 160 | headless or headed |
| Edge via Grid | Edge 158 | headless or headed |
| Safari via Grid | Safari 27 | **always headed** (safaridriver limitation) |

## Cycle 1 — Visible browsers (`-DgridHeaded=true`)

- **Result: 12/12 green, BUILD SUCCESS (6:00)**
- Live sessions mid-run (Grid GraphQL): **8 sessions — 4 Chrome 158, 2 Firefox 160, Edge 158, Safari 27**, all visible on screen.

## Cycle 2 — Hidden browsers (headless, default)

- **Result: 12/12 green, BUILD SUCCESS (10:45)** (first attempt: 1 flake — `successfulLogin` timeout under parallel load; passed on retry)
- Live sessions mid-run (Grid GraphQL): **8 sessions — 4 Firefox 160, 2 Chrome 158, Edge 158, Safari 27**; Chrome/Firefox/Edge hidden, Safari headed.

## Evidence screenshots (`.coda/docs/evidence/`)

- `grid_ui_sessions_live.png` — Grid sessions view with live parallel sessions
- `grid_ui_overview_live.png` — Grid overview UI
- `grid_ui_console.png` — Grid console

## How to reproduce

```bash
# Headed (visible browsers; Safari always headed)
mvn test -Dsurefire.suiteXmlFiles=testSuites/GridParallelDemo.xml -Dbrowser=grid -DgridHeaded=true

# Headless (hidden browsers)
mvn test -Dsurefire.suiteXmlFiles=testSuites/GridParallelDemo.xml -Dbrowser=grid
```

## Notes

- Purchase flows (`SubmitOrderTest`, `ProductErrorValidation`) are excluded from the parallel suite: both browsers share the same backend account, so parallel cart operations race server-side.
- Session-creation retry (3 attempts with backoff) in `BaseTest` keeps Grid runs stable under slot contention.
- Safari cannot run headless; it is macOS-only and always headed.
