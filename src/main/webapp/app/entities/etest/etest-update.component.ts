import { Component, Vue, Inject } from 'vue-property-decorator';

import { IEtest, Etest } from '@/shared/model/etest.model';
import EtestService from './etest.service';

const validations: any = {
  etest: {
    testname: {},
    testnum: {},
    testaddress: {},
    testphone: {},
  },
};

@Component({
  validations,
})
export default class EtestUpdate extends Vue {
  @Inject('etestService') private etestService: () => EtestService;
  public etest: IEtest = new Etest();
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.etestId) {
        vm.retrieveEtest(to.params.etestId);
      }
    });
  }

  created(): void {
    this.currentLanguage = this.$store.getters.currentLanguage;
    this.$store.watch(
      () => this.$store.getters.currentLanguage,
      () => {
        this.currentLanguage = this.$store.getters.currentLanguage;
      }
    );
  }

  public save(): void {
    this.isSaving = true;
    if (this.etest.id) {
      this.etestService()
        .update(this.etest)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = 'A Etest is updated with identifier ' + param.id;
          return this.$root.$bvToast.toast(message.toString(), {
            toaster: 'b-toaster-top-center',
            title: 'Info',
            variant: 'info',
            solid: true,
            autoHideDelay: 5000,
          });
        });
    } else {
      this.etestService()
        .create(this.etest)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = 'A Etest is created with identifier ' + param.id;
          this.$root.$bvToast.toast(message.toString(), {
            toaster: 'b-toaster-top-center',
            title: 'Success',
            variant: 'success',
            solid: true,
            autoHideDelay: 5000,
          });
        });
    }
  }

  public retrieveEtest(etestId): void {
    this.etestService()
      .find(etestId)
      .then(res => {
        this.etest = res;
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {}
}
