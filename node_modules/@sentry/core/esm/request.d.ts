import { Event } from '@sentry/types';
import { API } from './api';
/** A generic client request. */
export interface SentryRequest {
    body: string;
    url: string;
}
/** Creates a SentryRequest from an event. */
export declare function eventToSentryRequest(event: Event, api: API): SentryRequest;
//# sourceMappingURL=request.d.ts.map