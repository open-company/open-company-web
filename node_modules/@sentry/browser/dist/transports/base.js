Object.defineProperty(exports, "__esModule", { value: true });
var core_1 = require("@sentry/core");
var utils_1 = require("@sentry/utils");
/** Base Transport class implementation */
var BaseTransport = /** @class */ (function () {
    function BaseTransport(options) {
        this.options = options;
        /** A simple buffer holding all requests. */
        this._buffer = new utils_1.PromiseBuffer(30);
        this._api = new core_1.API(this.options.dsn);
        // eslint-disable-next-line deprecation/deprecation
        this.url = this._api.getStoreEndpointWithUrlEncodedAuth();
    }
    /**
     * @inheritDoc
     */
    BaseTransport.prototype.sendEvent = function (_) {
        throw new utils_1.SentryError('Transport Class has to implement `sendEvent` method');
    };
    /**
     * @inheritDoc
     */
    BaseTransport.prototype.close = function (timeout) {
        return this._buffer.drain(timeout);
    };
    return BaseTransport;
}());
exports.BaseTransport = BaseTransport;
//# sourceMappingURL=base.js.map