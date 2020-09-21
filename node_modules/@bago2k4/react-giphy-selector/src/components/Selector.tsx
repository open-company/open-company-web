import * as React from "react";

import { Rating, ResultSort, IGifObject } from "../types";
import { GiphyClient, ISearchResult } from "../lib/GiphyClient";
import { QueryForm } from "./QueryForm";
import { Suggestions } from "./Suggestions";
import { SearchResults } from "./SearchResults";

const footerImgSrc = "data:image/png;base64,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";

export interface ISelectorProps {
  // main props
  apiKey: string;
  rating?: Rating;
  sort?: ResultSort;
  limit?: number;
  suggestions?: string[];
  queryInputPlaceholder?: string;
  resultColumns?: number;
  onGifSelected: (gifObject: IGifObject) => void;
  showGiphyMark?: boolean;
  preloadTrending?: boolean,

  // main container style props
  containerClassName?: string;

  // query form style/content props
  queryFormAutoFocus?: boolean;
  queryFormClassName?: string;
  queryFormInputClassName?: string;
  queryFormSubmitClassName?: string;
  queryFormSubmitContent?: any;

  // search results style
  searchResultsClassName?: string;
  searchResultClassName?: string;

  suggestionsClassName?: string;
  suggestionClassName?: string;

  // loader style/content props
  loaderClassName?: string;
  loaderContent?: any;

  // error style/content props
  searchErrorClassName?: string;

  // footer style props
  footerClassName?: string;
}

export interface ISelectorState {
  query: string;
  isPending: boolean;
  searchError?: Error;
  searchResult?: ISearchResult;
}

export class Selector extends React.Component<ISelectorProps, ISelectorState> {
  public static defaultProps: Partial<ISelectorProps> = {
    rating: Rating.G,
    sort: ResultSort.Relevant,
    limit: 20,
    resultColumns: 3,
    showGiphyMark: true,
    preloadTrending: false,
    containerClassName: "",
    queryInputPlaceholder: 'Enter search text',
    suggestions: [],
    loaderContent: "Loading...",
    queryFormSubmitContent: "Search"
  };

  public client: GiphyClient;

  constructor(props: ISelectorProps) {
    super(props);

    // Setup a new giphy client
    this.client = new GiphyClient(props.apiKey);

    // Set initial state
    this.state = {
      query: "",
      isPending: false,
      searchError: null,
      searchResult: null
    };

    this.onQueryChange = this.onQueryChange.bind(this);
    this.onQueryExecute = this.onQueryExecute.bind(this);
    this.onSuggestionSelected = this.onSuggestionSelected.bind(this);
    this.onTrendingExecute = this.onTrendingExecute.bind(this);
  }

  /**
   * Fired when the query value changes for the
   * search
   * @param q string
   * @param cb func optional callback for when state is done updating
   */
  public onQueryChange(q: string, cb?: () => void): void {
    // Update the query
    this.setState({ query: q }, cb);
  }

  /**
   * Fired when the query should be executed
   */
  public onQueryExecute(): void {
    const { query } = this.state;
    const { rating, sort, limit } = this.props;

    this.setState({
      isPending: true,
      searchError: null
    });

    this.client
      .searchGifs({
        q: query,
        rating,
        limit,
        sort,
        offset: 0
      })
      .then((result: ISearchResult) => {
        this.setState({
          isPending: false,
          searchResult: result
        });
      })
      .catch((err: Error) => {
        this.setState({
          isPending: false,
          searchError: err
        });
      });
  }

  /**
   * Fired when the component is mounted if preloadTrending is set
   */
  public onTrendingExecute(): void {
    const { rating, limit } = this.props;

    this.setState({
      isPending: true,
      searchError: null
    });

    this.client
      .trendingGifs({
        rating,
        limit,
        offset: 0
      })
      .then((result: ISearchResult) => {
        this.setState({
          isPending: false,
          searchResult: result
        });
      })
      .catch((err: Error) => {
        this.setState({
          isPending: false,
          searchError: err
        });
      });
  }

  /**
   * Fired when a suggestion has been selected
   */
  public onSuggestionSelected(q: string): void {
    // Update query and wait for state change to finish
    // before executing query
    this.onQueryChange(q, () => {
      this.onQueryExecute();
    });
  }

  public componentDidMount() {
    if (this.props.preloadTrending) {
      this.onTrendingExecute();
    }
  }

  public render(): JSX.Element {
    const { query, searchResult, isPending, searchError } = this.state;
    const {
      suggestions,
      onGifSelected,
      queryInputPlaceholder,
      resultColumns,
      showGiphyMark,
      preloadTrending,

      containerClassName,

      queryFormAutoFocus,
      queryFormClassName,
      queryFormInputClassName,
      queryFormSubmitClassName,
      queryFormSubmitContent,

      searchResultClassName,
      searchResultsClassName,

      suggestionClassName,
      suggestionsClassName,

      loaderClassName,
      loaderContent,

      searchErrorClassName,

      footerClassName
    } = this.props;

    const showSuggestions =
      !!suggestions.length && !searchResult && !isPending && !searchError;

    return (
      <div
        className={containerClassName}>
        <QueryForm
          queryInputPlaceholder={queryInputPlaceholder}
          onQueryChange={this.onQueryChange}
          onQueryExecute={this.onQueryExecute}
          queryValue={query}
          queryFormAutoFocus={queryFormAutoFocus}
          queryFormClassName={queryFormClassName}
          queryFormInputClassName={queryFormInputClassName}
          queryFormSubmitClassName={queryFormSubmitClassName}
          queryFormSubmitContent={queryFormSubmitContent}
        />

        {showSuggestions && (
          <Suggestions
            suggestions={suggestions}
            onSuggestionSelected={this.onSuggestionSelected}
            suggestionClassName={suggestionClassName}
            suggestionsClassName={suggestionsClassName}
          />
        )}

        {isPending && (
          <div
            className={loaderClassName}
          >
            {loaderContent}
          </div>
        )}

        {!isPending &&
          !!searchError && (
            <div
              className={searchErrorClassName}
            >
              {searchError.message}
            </div>
          )}

        {!isPending &&
          !!searchResult && (
            <SearchResults
              searchResultClassName={searchResultClassName}
              searchResultsClassName={searchResultsClassName}
              columns={resultColumns}
              gifObjects={searchResult.gifObjects}
              onGifSelected={onGifSelected}
            />
          )}
        <footer
          className={footerClassName}
        >
          <img src={footerImgSrc} />
        </footer>
      </div>
    );
  }
}
