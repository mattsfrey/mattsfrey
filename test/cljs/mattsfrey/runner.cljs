(ns mattsfrey.runner
    (:require [doo.runner :refer-macros [doo-tests]]
              [mattsfrey.core-test]))

(doo-tests 'mattsfrey.core-test)
