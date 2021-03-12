import Vuetify from 'vuetify';
import 'vuetify/dist/vuetify.min.css';

const opts = {};

export function initVuetify(vue) {
  vue.use(Vuetify);
  return new Vuetify({ opts });
}
