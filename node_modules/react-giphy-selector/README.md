# react-giphy-selector
A search and select React.JS component for picking the perfect giphy.

![Example selector](./docs/example_1.gif)

> This component is highly-customizable and only provides basic styling out-of-box. The example above includes simple customization to a few elements. You can view this example in `/example/src`.

## Table of Contents

- [Installation](#installation)
- [Usage](#usage)

## Installation

You just install `react-giphy-selector` the good ole' fashion way through NPM:

```
npm install --save react-giphy-selector
```

## Usage

This package exports the `Selector` React component and then two helper `enums`:

```js
import {Selector, ResultSort, Rating} from "react-giphy-selector";

```

- [Selector](#selector)
- [Rating](#rating)
- [ResultSort](#resultSort)

### Selector

The selector component contains all of the search, display, and selection logic. The only required properties are `apiKey` and `onGifSelected`.

```jsx
<Selector
	apiKey={'myKey'}
	onGifSelected={this.saveGif} />
```

That said, there are a bunch of props that allow you to make this component your own. Note: the `?` included at the end of a property name denotes it as optional.

- `apiKey: string`: [Your Giphy Project API Key](https://developers.giphy.com/).
- `onGifSelected?: (gifObject: IGifObject) => void`: The function to fire when a gif search result has been selected. The `IGifObject` represents the full [GIF Object](https://developers.giphy.com/docs/#gif-object) returned via the Giphy API.
- `rating?: Rating`: The maximum rating you want to allow in your search results. Use the exported [Rating](#rating) enum for help. Default: `Rating.G`.
- `sort?: ResultSort`: The sort order of the search results. Use the helper enum [ResultSort](#resultsort). Default: `ResultSort.Relevant`.
- `limit?: number`: The number of results to return. Default: `20`.
- `suggestions?: string[]`: An array containing one-click searches to make it easy for your user. Will not show suggestions section if none are passed. Default: `[]`.
- `queryInputPlaceholder?: string`: The placeholder text for the search bar text input. Default `'Enter search text'`.
- `resultColumns?: number`: The number of columns to divide the search results into. Default: `3`.
- `showGiphyMark?: boolean`: Indicates whether to show the "powered by Giphy" mark in the selector. This is required when [using a Giphy Production API Key](https://developers.giphy.com/docs/#production-key). Default: `true`.

#### Styling your Selector

There are a bunch of `props` to help you customize the style of the the selector. Both the `className` and the `style` methods are available. `react-giphy-selector` is very intentionally unopinionated about how exactly each section of the selector should look. Instead, the package offers a lot of customization and flexibility through the props below.

The images below will help you understand the nomenclature of the components:

![Diagram of component nomenclature for query form, suggestions, and footer](./docs/components_1.png)
![Diagram of component nomenclature for search results](./docs/components_2.png)

Here are all the props available for styling the component:

- `queryFormClassName?: string`: Additional `className` for the query form section of the component. You can find the default style applied in `src/components/QueryForm.css`.
- `queryFormInputClassName?: string`: Additional `className` for the text input in the query form. You can find the default style applied in `src/components/QueryForm.css`.
- `queryFormSubmitClassName?: string`: Additional `className` for the submit button in the query form. You can find the default style applied in `src/components/QueryForm.css`.
- `queryFormStyle?: object`: A style object to add to the query form style. You can find the default style applied in `src/components/QueryForm.css`.
- `queryFormInputStyle?: object`: A style object to add to the text input in the query form. You can find the default style applied in `src/components/QueryForm.css`.
- `queryFormSubmitStyle?: object`: A style object to add to the submit button in the query form. You can find the default style applied in `src/components/QueryForm.css`.
- `queryFormSubmitContent?: string or Component`: You can pass in a `string` or your own component to render inside the submit button in the query form. This allows you to pass in things like custom icons. Default: `'Search'`.
- `searchResultsClassName?: string`: Additional `className` for the search results component. You can find the default style in `src/components/SearchResults.css`.
- `searchResultsStyle?: object`: A style object to the add to the search results container. You can find the default style in `src/components/SearchResults.css`.
- `searchResultClassName?: string`: Additional `className` to add to a search result. Search results are `a` elements. You can find the default style in `src/components/SearchResult.css`.
- `searchResultStyle?: object`: A style object to add to a search result. Search results are `a` elements. You can find the default style in `src/components/SearchResult.css`.
- `suggestionsClassName?: string`: Additional `className` to add to the suggestions container. You can find the default style in `src/components/Suggestions.css`.
- `suggestionsStyle?: object`: A style object to add to the suggestions container. You can find the default style in `src/components/Suggestions.css`.
- `suggestionClassName?: string`: Additional `className` to add to a suggestion. This is an `a` element. You can find the default style in `src/components/Suggestion.css`.
- `suggestionStyle?: object`: A style object to add to a suggestion. This is an `a` element. You can find the default style in `src/components/Suggestion.css`.
- `loaderClassName?: string`: Additional `className` to add to the loader container. You can find the default style in `src/components/Selector.css`.
- `loaderStyle?: object`: A style object to add to the loader container. You can find the default style in `src/components/Selector.css`.
- `loaderContent?: string or Component`: You can pass in a `string` or customer component to display when results are loading. Default `'Loading'...`.
- `searchErrorClassName?: string`: Additional `className` to add to the error message shown on broken searches. You can find the default style in `src/components/Selector.css`.
- `searchErrorStyle?: object`: A style object to add to the error message shown on broken searches. You can find the default style in `src/components/Selector.css`.
- `footerClassName?: string`: Additional `className` to add to footer of selector.  You can find the default style in `src/components/Selector.css`.
- `footerStyle?: object`: A style object to add to footer of selector.  You can find the default style in `src/components/Selector.css`.

If you have a cool style you'd like to share, please [make an issue](https://github.com/tshaddix/react-giphy-selector/issues).

### Rating

The `Rating` enum contains all the possible ratings you can limit searches to:

```js
Rating.Y
Rating.G
Rating.PG
Rating.PG13
Rating.R
```

### ResultSort

The `ResultSort` enum contains the different sort methods supported by the Giphy API.

```js
ResultSort.Recent // ordered by most recent
ResultSort.Relevant // ordered by relevance
```