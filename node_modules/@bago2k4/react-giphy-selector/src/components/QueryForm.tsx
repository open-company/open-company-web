import * as React from "react";

export interface IQueryFormProps {
  queryFormAutoFocus?: boolean;
  queryFormClassName?: string;
  queryFormInputClassName?: string;
  queryFormSubmitClassName?: string;
  queryFormSubmitContent: any;
  onQueryChange: (q: string) => void;
  onQueryExecute: () => void;
  queryInputPlaceholder: string;
  queryValue: string;
}

export class QueryForm extends React.Component<IQueryFormProps, {}> {

  private searchInput: HTMLInputElement;
  private setSearchInputRef: (HTMLInputElement) => void;

  constructor(props: IQueryFormProps) {
    super(props);

    this.onValueChange = this.onValueChange.bind(this);
    this.onSubmit = this.onSubmit.bind(this);
    this.searchInput = null;
    this.setSearchInputRef = element => {
      this.searchInput = element;
    };
  }

  /**
   * Fires when the search input box has changed value
   * @param event
   */
  public onValueChange(event: any): void {
    this.props.onQueryChange(event.target.value || "");
  }

  /**
   * Fires when the form has been submitted (via "enter" or button)
   * @param event
   */
  public onSubmit(event: any): void {
    event.preventDefault();
    this.props.onQueryExecute();
  }

  public componentDidMount() {
    if (this.props.queryFormAutoFocus) {
      this.searchInput.focus();
    }
  }

  public render(): JSX.Element {
    const {
      queryValue,
      queryInputPlaceholder,
      queryFormAutoFocus,
      queryFormClassName,
      queryFormInputClassName,
      queryFormSubmitClassName,
      queryFormSubmitContent
    } = this.props;

    return (
      <div>
        <form
          className={queryFormClassName}
          onSubmit={this.onSubmit}
        >
          <input
            className={queryFormInputClassName}
            value={queryValue}
            type="text"
            onChange={this.onValueChange}
            placeholder={queryInputPlaceholder}
            ref={this.setSearchInputRef}
          />
          <button
            type="submit"
            className={queryFormSubmitClassName}
          >
            {queryFormSubmitContent}
          </button>
        </form>
      </div>
    );
  }
}
