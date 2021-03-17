import { Component, Vue, Inject } from 'vue-property-decorator';

import { ICovid, Covid } from '@/shared/model/covid.model';
import CovidService from './covid.service';

const validations: any = {
  covid: {
    cid: {},
    ctype: {},
    centername: {},
    coi: {},
    facilityname: {},
    zipcode: {},
    address: {},
  },
};

@Component({
  validations,
})
export default class CovidUpdate extends Vue {
  @Inject('covidService') private covidService: () => CovidService;
  public covid: ICovid = new Covid();
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.covidId) {
        vm.retrieveCovid(to.params.covidId);
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
    if (this.covid.id) {
      this.covidService()
        .update(this.covid)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = 'A Covid is updated with identifier ' + param.id;
          return this.$root.$bvToast.toast(message.toString(), {
            toaster: 'b-toaster-top-center',
            title: 'Info',
            variant: 'info',
            solid: true,
            autoHideDelay: 5000,
          });
        });
    } else {
      this.covidService()
        .create(this.covid)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = 'A Covid is created with identifier ' + param.id;
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

  public retrieveCovid(covidId): void {
    this.covidService()
      .find(covidId)
      .then(res => {
        this.covid = res;
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {}
}
