# Refactor Pull Request

## Summary
Explain why the refactor is necessary.
- Improve readability?
- Apply DDD / Hexagonal conventions?
- Remove technical debt?
- Enhance testability or performance?

## Scope of Refactor
List what was changed without altering the system’s behaviour.
- Folder restructuring / module boundaries
- Breaking up large classes or methods
- Renaming classes for better domain clarity
- Cleaning unused code, annotations, or dependencies
- Improving adapter/port separation
- Updating configurations / annotations for consistency

## Behavioural Guarantee
Confirm that the refactor introduces **no functional changes**.
- [ ] The public API remains the same
- [ ] Domain behaviour unchanged
- [ ] Kafka event contracts unchanged
- [ ] Database structure unchanged (unless explicitly noted)

## Testing
Explain how you validated that behaviour is unchanged.
- Existing tests still pass
- Added safety tests if necessary
- Verified key user flows manually

## Risks
Highlight any risks or follow-up tasks.
- Potential backward-compatibility concerns
- Components that might need further clean-up
- Deprecated code paths to be removed later
