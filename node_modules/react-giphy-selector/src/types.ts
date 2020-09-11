export enum Rating {
  Y = "y",
  G = "g",
  PG = "pg",
  PG13 = "pg-13",
  R = "r"
}

export enum ResultSort {
  Relevant = "relevant",
  Recent = "recent"
}

export interface IGifImage {
  gif_url: string;
  url: string;
  height: string;
  width: string;
}

export interface IGifObject {
  id: string;
  slug: string;
  url: string;
  embed_url: string;
  source: string;
  rating: Rating;
  title: string;
  images: { [index: string]: IGifImage };
}