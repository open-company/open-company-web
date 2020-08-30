import * as React from "react";
import * as cn from "classnames";

const defaultStyle = require("./Suggestion.css");

export interface ISuggestionProps {
  suggestion: string;
  onSelected: (q: string) => void;
  suggestionClassName?: string;
  suggestionStyle: object;
}

export class Suggestion extends React.Component<ISuggestionProps, {}> {
  constructor(props: ISuggestionProps) {
    super(props);

    this.onClick = this.onClick.bind(this);
  }

  public onClick(event: any): void {
    event.preventDefault();

    this.props.onSelected(this.props.suggestion);
  }

  public render(): JSX.Element {
    const { suggestion, suggestionClassName, suggestionStyle } = this.props;

    return (
      <a
        style={suggestionStyle}
        className={cn(defaultStyle.suggestion, suggestionClassName)}
        href="javascript:void(0)"
        onClick={this.onClick}
      >
        {suggestion}
      </a>
    );
  }
}
