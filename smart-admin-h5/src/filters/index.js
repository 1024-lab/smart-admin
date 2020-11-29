import Vue from 'vue'

import * as filter from './filter'

Object.keys(filter).forEach(key => Vue.filter(key, filter[key]))
