import { __assign } from "tslib";
import { addGlobalEventProcessor, getCurrentHub } from '@sentry/core';
import { getGlobalObject } from '@sentry/utils';
var global = getGlobalObject();
/** UserAgent */
var UserAgent = /** @class */ (function () {
    function UserAgent() {
        /**
         * @inheritDoc
         */
        this.name = UserAgent.id;
    }
    /**
     * @inheritDoc
     */
    UserAgent.prototype.setupOnce = function () {
        addGlobalEventProcessor(function (event) {
            if (getCurrentHub().getIntegration(UserAgent)) {
                if (!global.navigator || !global.location) {
                    return event;
                }
                var request = event.request || {};
                request.url = request.url || global.location.href;
                request.headers = request.headers || {};
                request.headers['User-Agent'] = global.navigator.userAgent;
                return __assign(__assign({}, event), { request: request });
            }
            return event;
        });
    };
    /**
     * @inheritDoc
     */
    UserAgent.id = 'UserAgent';
    return UserAgent;
}());
export { UserAgent };
//# sourceMappingURL=useragent.js.map