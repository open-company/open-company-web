import * as React from "react";
import * as cn from "classnames";

import { Suggestion } from "./Suggestion";
const defaultStyle = require("./Suggestions.css");

export interface ISuggestionsProps {
  suggestions: string[];
  onSuggestionSelected: (q: string) => void;
  suggestionsClassName?: string;
  suggestionsStyle: object;
  suggestionClassName?: string;
  suggestionStyle: object;
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
      suggestionStyle,
      suggestionsClassName,
      suggestionsStyle
    } = this.props;

    return (
      <div
        style={suggestionsStyle}
        className={cn(defaultStyle.suggestions, suggestionsClassName)}
      >
        {suggestions.map((s: string) => (
          <Suggestion
            key={`suggestion-${s}`}
            suggestion={s}
            onSelected={onSuggestionSelected}
            suggestionClassName={suggestionClassName}
            suggestionStyle={suggestionStyle}
          />
        ))}
      </div>
    );
  }
}
