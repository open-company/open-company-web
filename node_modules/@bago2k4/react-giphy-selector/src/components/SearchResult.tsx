import * as React from "react";

import { IGifObject, IGifImage } from "../types";

export interface ISearchResultProps {
  gifObject: IGifObject;
  onSelected: (gifObject: IGifObject) => void;
  searchResultClassName?: string;
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
    const { gifObject, searchResultClassName } = this.props;

    const fixedWidth: IGifImage = gifObject.images.fixed_width,
          sourceImageUrl: string = fixedWidth.url || fixedWidth.gif_url;

    return (
      <li>
        <a
          // href="javascript:void(0)"
          onClick={this.onClick}
          className={searchResultClassName}
        >
          <img src={sourceImageUrl} />
        </a>
      </li>
    );
  }
}
