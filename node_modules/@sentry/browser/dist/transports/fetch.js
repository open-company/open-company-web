Object.defineProperty(exports, "__esModule", { value: true });
var tslib_1 = require("tslib");
var core_1 = require("@sentry/core");
var types_1 = require("@sentry/types");
var utils_1 = require("@sentry/utils");
var base_1 = require("./base");
var global = utils_1.getGlobalObject();
/** `fetch` based transport */
var FetchTransport = /** @class */ (function (_super) {
    tslib_1.__extends(FetchTransport, _super);
    function FetchTransport() {
        var _this = _super !== null && _super.apply(this, arguments) || this;
        /** Locks transport after receiving 429 response */
        _this._disabledUntil = new Date(Date.now());
        return _this;
    }
    /**
     * @inheritDoc
     */
    FetchTransport.prototype.sendEvent = function (event) {
        var _this = this;
        if (new Date(Date.now()) < this._disabledUntil) {
            return Promise.reject({
                event: event,
                reason: "Transport locked till " + this._disabledUntil + " due to too many requests.",
                status: 429,
            });
        }
        var sentryReq = core_1.eventToSentryRequest(event, this._api);
        var options = {
            body: sentryReq.body,
            method: 'POST',
            // Despite all stars in the sky saying that Edge supports old draft syntax, aka 'never', 'always', 'origin' and 'default
            // https://caniuse.com/#feat=referrer-policy
            // It doesn't. And it throw exception instead of ignoring this parameter...
            // REF: https://github.com/getsentry/raven-js/issues/1233
            referrerPolicy: (utils_1.supportsReferrerPolicy() ? 'origin' : ''),
        };
        if (this.options.fetchParameters !== undefined) {
            Object.assign(options, this.options.fetchParameters);
        }
        if (this.options.headers !== undefined) {
            options.headers = this.options.headers;
        }
        return this._buffer.add(new utils_1.SyncPromise(function (resolve, reject) {
            global
                .fetch(sentryReq.url, options)
                .then(function (response) {
                var status = types_1.Status.fromHttpCode(response.status);
                if (status === types_1.Status.Success) {
                    resolve({ status: status });
                    return;
                }
                if (status === types_1.Status.RateLimit) {
                    var now = Date.now();
                    /**
                     * "The name is case-insensitive."
                     * https://developer.mozilla.org/en-US/docs/Web/API/Headers/get
                     */
                    var retryAfterHeader = response.headers.get('Retry-After');
                    _this._disabledUntil = new Date(now + utils_1.parseRetryAfterHeader(now, retryAfterHeader));
                    utils_1.logger.warn("Too many requests, backing off till: " + _this._disabledUntil);
                }
                reject(response);
            })
                .catch(reject);
        }));
    };
    return FetchTransport;
}(base_1.BaseTransport));
exports.FetchTransport = FetchTransport;
//# sourceMappingURL=fetch.js.map