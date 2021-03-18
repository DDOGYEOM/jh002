import { Component, Vue, Inject } from 'vue-property-decorator';

import { IEtest } from '@/shared/model/etest.model';
import EtestService from './etest.service';

@Component
export default class EtestDetails extends Vue {
  @Inject('etestService') private etestService: () => EtestService;
  public etest: IEtest = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.etestId) {
        vm.retrieveEtest(to.params.etestId);
      }
    });
  }

  public retrieveEtest(etestId) {
    this.etestService()
      .find(etestId)
      .then(res => {
        this.etest = res;
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
