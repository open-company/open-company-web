import * as React from "react";
import * as cn from "classnames";

import { IGifObject, IGifImage } from "../types";
const defaultStyle = require("./SearchResult.css");

export interface ISearchResultProps {
  gifObject: IGifObject;
  onSelected: (gifObject: IGifObject) => void;
  searchResultClassName?: string;
  searchResultStyle: object;
}

export class SearchResult extends React.Component<ISearchResultProps, {}> {
  constructor(props: ISearchResultProps) {
    super(props);

    this.onClick = this.onClick.bind(this);
  }

  public onClick(event: any): void {
    event.preventDefault();

    this.props.onSelected(this.props.gifObject);
  }

  public render(): JSX.Element {
    const { gifObject, searchResultClassName, searchResultStyle } = this.props;

    const fixedWidth: IGifImage = gifObject.images.fixed_width,
          sourceImageUrl: string = fixedWidth.url || fixedWidth.gif_url;

    return (
      <li>
        <a
          style={searchResultStyle}
          // href="javascript:void(0)"
          onClick={this.onClick}
          className={cn(defaultStyle.searchResult, searchResultClassName)}
        >
          <img src={sourceImageUrl} />
        </a>
      </li>
    );
  }
}
