import * as React from "react";

import { Suggestion } from "./Suggestion";

export interface ISuggestionsProps {
  suggestions: string[];
  onSuggestionSelected: (q: string) => void;
  suggestionsClassName?: string;
  suggestionClassName?: string;
}

export class Suggestions extends React.Component<ISuggestionsProps, {}> {
  constructor(props: ISuggestionsProps) {
    super(props);
  }

  public render(): JSX.Element {
    const {
      suggestions,
      onSuggestionSelected,
      suggestionClassName,
      suggestionsClassName
    } = this.props;

    return (
      <div
        className={suggestionsClassName}
      >
        {suggestions.map((s: string) => (
          <Suggestion
            key={`suggestion-${s}`}
            suggestion={s}
            onSelected={onSuggestionSelected}
            suggestionClassName={suggestionClassName}
          />
        ))}
      </div>
    );
  }
}
