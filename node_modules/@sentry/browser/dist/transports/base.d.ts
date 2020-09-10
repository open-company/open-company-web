import { API } from '@sentry/core';
import { Event, Response, Transport, TransportOptions } from '@sentry/types';
import { PromiseBuffer } from '@sentry/utils';
/** Base Transport class implementation */
export declare abstract class BaseTransport implements Transport {
    options: TransportOptions;
    /**
     * @deprecated
     */
    url: string;
    /** Helper to get Sentry API endpoints. */
    protected readonly _api: API;
    /** A simple buffer holding all requests. */
    protected readonly _buffer: PromiseBuffer<Response>;
    constructor(options: TransportOptions);
    /**
     * @inheritDoc
     */
    sendEvent(_: Event): PromiseLike<Response>;
    /**
     * @inheritDoc
     */
    close(timeout?: number): PromiseLike<boolean>;
}
//# sourceMappingURL=base.d.ts.map