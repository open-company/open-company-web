(ns open-company-web.macros)

(defmacro reify-bool
  [statement]
  `(if ~statement 1 0))