/**
 * Paginated collection — aligned with backend PageResponse.
 */
export interface PageResponse<T> {
  content: T[];
  page: number;
  size: number;
  totalElements: number;
  totalPages: number;
  first: boolean;
  last: boolean;
}

export interface PageRequestParams {
  page?: number;
  size?: number;
  sort?: string;
}
