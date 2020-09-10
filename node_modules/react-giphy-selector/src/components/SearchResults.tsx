import * as React from "react";
import * as cn from "classnames";

import { IGifObject } from "../types";
import { SearchResult } from "./SearchResult";
const defaultStyle = require("./SearchResults.css");

export interface ISearchResultsProps {
  columns: number;
  gifObjects: IGifObject[];
  onGifSelected: (gifObject: IGifObject) => void;
  searchResultsClassName?: string;
  searchResultsStyle: object;
  searchResultClassName?: string;
  searchResultStyle: object;
}

export class SearchResults extends React.Component<ISearchResultsProps, {}> {
  constructor(props: ISearchResultsProps) {
    super(props);

    this.getColumnGifs = this.getColumnGifs.bind(this);
  }

  /**
   * Given an array of gifs, compose them into an array of arrays, each top-level
   * array representing a column and each second-level array representing the gifs
   * in that column
   * @param gifObjects
   */
  public getColumnGifs(gifObjects: IGifObject[]): Array<Array<IGifObject>> {
    const numColumns = this.props.columns;

    const columnsGifs: Array<Array<IGifObject>> = [];

    let c = 0;

    // fill column array with arrays representing column contents
    while (c < numColumns) {
      columnsGifs.push([]);
      c++;
    }

    let j = 0;

    // sort gifs into columns
    gifObjects.forEach((gifObject: IGifObject) => {
      columnsGifs[j].push(gifObject);

      j++;

      if (j === numColumns) {
        j = 0;
      }
    });

    return columnsGifs;
  }

  public render(): JSX.Element {
    const {
      gifObjects,
      onGifSelected,
      searchResultsClassName,
      searchResultsStyle,
      searchResultClassName,
      searchResultStyle
    } = this.props;

    const columnGifs = this.getColumnGifs(gifObjects);

    return (
      <ul
        style={searchResultsStyle}
        className={cn(defaultStyle.searchResults, searchResultsClassName)}
      >
        {columnGifs.map((column: IGifObject[], c: number) => (
          <li key={`column-${c}`}>
            <ul>
              {column.map((gifObject: IGifObject) => (
                <SearchResult
                  searchResultClassName={searchResultClassName}
                  searchResultStyle={searchResultStyle}
                  key={gifObject.id}
                  gifObject={gifObject}
                  onSelected={onGifSelected}
                />
              ))}
            </ul>
          </li>
        ))}
      </ul>
    );
  }
}
