import { Component, Vue, Inject } from 'vue-property-decorator';

import { ICovid } from '@/shared/model/covid.model';
import CovidService from './covid.service';

@Component
export default class CovidDetails extends Vue {
  @Inject('covidService') private covidService: () => CovidService;
  public covid: ICovid = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.covidId) {
        vm.retrieveCovid(to.params.covidId);
      }
    });
  }

  public retrieveCovid(covidId) {
    this.covidService()
      .find(covidId)
      .then(res => {
        this.covid = res;
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
